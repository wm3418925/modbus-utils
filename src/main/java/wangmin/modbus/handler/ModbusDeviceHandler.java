package wangmin.modbus.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import wangmin.modbus.util.ContinuousAddressBlock;
import wangmin.modbus.util.ModbusConnPool;
import wangmin.modbus.entity.ModbusSlaveAddressInfo;
import wangmin.modbus.entity.type.ModbusStatus;
import wangmin.modbus.util.ModbusDataUtils;
import wangmin.modbus.util.ModbusUtils;
import net.wimpi.modbus.net.TCPMasterConnection;
import wangmin.modbus.entity.*;

import java.util.*;

/**
 * Created by wm on 2017/1/3.
 */
public class ModbusDeviceHandler implements Runnable {
    private volatile Thread thread = null;
    private final ModbusDeviceInfo device;
    private final ModbusDeviceProtocolInfo devicePi;
    private final List<ModbusDataNodeInfo> dataNodeList;
    private ModbusConnPool modbusConnPool = null;

    public ModbusDeviceHandler(ModbusDeviceInfo device, ModbusDeviceProtocolInfo devicePi, List<ModbusDataNodeInfo> dataNodeList) {
        this.device = device;
        this.devicePi = devicePi;
        this.dataNodeList = dataNodeList;
    }

    public void start() {
        thread = new Thread(this, "ModbusDeviceHandler");
        thread.start();
    }

    public synchronized void stop() {
        thread = null;
    }



    /**
     * 轮询设备一圈, 睡眠一次
     * @param pi
     */
    private void sleepOneRound(ModbusDeviceProtocolInfo pi) {
        try {
            int sleepTime = pi.sleepTime;
            Thread.sleep(sleepTime * 1000);
        } catch (Exception e) {
        }
    }

    /**
     * 获取设备的所有地址, 将他们拼接为连续的
     */
    private Map<Integer, ModbusSlaveAddressInfo> generateAddressInfoMap() {
        Map<Integer, ModbusSlaveAddressInfo> res = Maps.newHashMap();

        for (ModbusDataNodeInfo dataNode : dataNodeList) {
            try {
                ModbusDataNodeProtocolInfo dnProtocolInfo = dataNode.getProtocolInfo();

                if (null == dnProtocolInfo.rType || null == dnProtocolInfo.status || ModbusStatus.ON != ModbusStatus.valueOf(dnProtocolInfo.status))
                    continue;
                Integer registerAddress = dnProtocolInfo.rAddr;
                if (registerAddress == null)
                    continue;

                Integer slaveId = dnProtocolInfo.slaveId;
                if (null == slaveId)
                    slaveId = devicePi.slaveId;
                if (null == slaveId)
                    continue;

                ModbusSlaveAddressInfo msai = res.get(slaveId);
                if (null == msai) {
                    msai = new ModbusSlaveAddressInfo(dnProtocolInfo.rType);
                    res.put(slaveId, msai);
                }

                int home = dnProtocolInfo.rAddr;
                int len;
                if (msai.registerType.isDigital())
                    len = 1;
                else if (msai.registerType.isRegister())
                    len = dataNode.getDataType().getWordCount();
                else
                    continue;

                msai.cab.addBlock(home, home+len);
            } catch (Exception e) {
                System.out.println("data node=" + dataNode + ", e="+e.getMessage());
                continue;
            }
        }

        return res;
    }

    /**
     * 一次性的 拉取连续的 数据块数据
     * */
    private int pullAllDataByBlock(
            TCPMasterConnection conn,
            Map<Integer, ModbusSlaveAddressInfo> aim) {
        int failCount = 0;

        for (Map.Entry<Integer, ModbusSlaveAddressInfo> aimEntry : aim.entrySet()) {
            Integer slaveId = aimEntry.getKey();
            ModbusSlaveAddressInfo msai = aimEntry.getValue();
            int blockCount = msai.cab.getBlockCount();
            List<ContinuousAddressBlock.AddressBlock> blockAddressList = msai.cab.getBlocks();

            List<Boolean> digitalData = null;
            byte[] registerData = null;

            try {
                for (int i=0; i<blockCount; ++i) {
                    ContinuousAddressBlock.AddressBlock blockAddress = blockAddressList.get(i);
                    switch (msai.registerType) {
                        case DI:
                            digitalData = ModbusUtils.readInputDiscretes(
                                    conn, slaveId, blockAddress.home, blockAddress.end-blockAddress.home);
                            break;
                        case DO:
                            digitalData = ModbusUtils.readCoils(
                                    conn, slaveId, blockAddress.home, blockAddress.end-blockAddress.home);
                            break;
                        case IR:
                            registerData = ModbusUtils.readInputRegisters(
                                    conn, slaveId, blockAddress.home,
                                    blockAddress.end-blockAddress.home);
                            break;
                        case RE:
                            registerData = ModbusUtils.readRegisters(
                                    conn, slaveId, blockAddress.home,
                                    blockAddress.end-blockAddress.home);
                            break;
                        default:
                            break;
                    }

                    if (msai.registerType.isDigital())
                        msai.digitalDataList.add(digitalData);
                    else if (msai.registerType.isRegister())
                        msai.registerDataList.add(registerData);
                }
            } catch (Exception e) {
                System.out.println("slaveId=" + slaveId + ", msai=" + msai + ", e="+e.getMessage());
            }

            if (null == digitalData && null == registerData) {
                if (msai.registerType.isDigital())
                    msai.digitalDataList.add(null);
                else if (msai.registerType.isRegister())
                    msai.registerDataList.add(null);

                ++failCount;
                // TODO
            } else {
                // TODO
            }
        }

        return failCount;
    }

    /**
     * 将获取到的数据, 根据数据项的地址, 映射到数据项上
     */
    private List<ModbusDataNodeData> mapDataToDataNodes(Map<Integer, ModbusSlaveAddressInfo> aim) {
        List<ModbusDataNodeData> dataList = Lists.newArrayListWithCapacity(dataNodeList.size());

        for (ModbusDataNodeInfo dataNode : dataNodeList) {
            try {
                ModbusDataNodeProtocolInfo dnProtocolInfo = dataNode.getProtocolInfo();

                if (null == dnProtocolInfo.rType || null == dnProtocolInfo.status || ModbusStatus.ON != ModbusStatus.valueOf(dnProtocolInfo.status))
                    continue;
                Integer registerAddress = dnProtocolInfo.rAddr;
                if (registerAddress == null)
                    continue;

                Integer slaveId = dnProtocolInfo.slaveId;
                if (null == slaveId)
                    slaveId = devicePi.slaveId;
                if (null == slaveId)
                    continue;

                ModbusSlaveAddressInfo msai = aim.get(slaveId);
                if (null == msai)
                    continue;

                int home = dnProtocolInfo.rAddr;
                int len = dataNode.getDataType().getByteLength();
                int idx = msai.cab.getBlockIndexByStartAddress(home);
                if (idx < 0)
                    continue;
                ContinuousAddressBlock.AddressBlock block = msai.cab.getBlock(idx);

                if (msai.registerType.isDigital()) {
                    int startIdx = home - block.home;
                    Boolean boolValue = msai.digitalDataList.get(idx).get(startIdx);
                    String dataStr = String.valueOf(boolValue);
                    dataList.add(new ModbusDataNodeData(dataNode.getId(), dataStr));
                } else if (msai.registerType.isRegister()) {
                    int startIdx = (home - block.home)*2;   // 一个地址代表两个字节
                    byte[] bytes = ModbusUtils.transferModbusDataBytesOrder(msai.registerDataList.get(idx), startIdx, len, dnProtocolInfo.bot);
                    String dataStr = ModbusDataUtils.convertDataToStr(dataNode.getDataType(), bytes, false);
                    dataList.add(new ModbusDataNodeData(dataNode.getId(), dataStr));
                }
            } catch (Exception e) {
                System.out.println("dataNode=" + dataNode + ", e="+e.getMessage());
                continue;
            }
        }

        return dataList;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        while (thread != null) {
            // 设备信息
            ModbusDeviceProtocolInfo devicePi = device.getProtocolInfo();

            // 没有连接池或者地址改变
            if (null == modbusConnPool || !devicePi.ip.equals(modbusConnPool.getIp()) || devicePi.port.equals(modbusConnPool.getPort())) {
                if (null != modbusConnPool) {
                    modbusConnPool.closeAllConn();
                    modbusConnPool = null;
                }

                try {
                    modbusConnPool = new ModbusConnPool(devicePi.ip, devicePi.port, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    sleepOneRound(devicePi);
                    continue;
                }
            } else {
                modbusConnPool.checkConnStatus();
            }

            // 获取一个连接
            TCPMasterConnection conn = modbusConnPool.getOneConn();
            if (null == conn) {
                sleepOneRound(devicePi);
                continue;
            }

            // 拼接地址为连续的
            Map<Integer, ModbusSlaveAddressInfo> aim = generateAddressInfoMap();

            // 一次性的, 拉取连续的数据块数据
            int failCount = 0;
            try {
                failCount = pullAllDataByBlock(conn, aim);
            } catch (Exception e) {
                e.printStackTrace();
                sleepOneRound(devicePi);
                continue;
            } finally {
                modbusConnPool.returnOneConn(conn);
            }

            // 将拉取后的数据根据地址映射到具体的数据项
            List<ModbusDataNodeData> dataList;
            try {
                dataList = mapDataToDataNodes(aim);
            } catch (Exception e) {
                e.printStackTrace();
                sleepOneRound(devicePi);
                continue;
            }

            // 处理拉取后的数据
            if (!dataList.isEmpty()) {
                try {
                    // TODO
                } catch (Exception e) {
                    e.printStackTrace();
                    sleepOneRound(devicePi);
                    continue;
                }
            }

            // sleep之前清空对象
            aim = null;
            dataList = null;
            // sleep
            sleepOneRound(devicePi);
        }
    }

}
