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
     */
    private void sleepOneRound() {
        try {
            int sleepTime = devicePi.sleepTime;
            Thread.sleep(sleepTime * 1000);
        } catch (Exception e) {
        }
    }

    /**
     * 获取设备的所有地址, 将他们拼接为连续的
     */
    private ModbusSlaveAddressInfo generateAddressInfo() {
        ModbusSlaveAddressInfo res = new ModbusSlaveAddressInfo(devicePi.rType);

        for (ModbusDataNodeInfo dataNode : dataNodeList) {
            try {
                ModbusDataNodeProtocolInfo dnProtocolInfo = dataNode.getProtocolInfo();

                if (!dnProtocolInfo.isValid())
                    continue;

                int home = dnProtocolInfo.rAddr;
                int len;
                if (res.registerType.isDigital())
                    len = 1;
                else if (res.registerType.isRegister())
                    len = dataNode.getDataType().getWordCount();
                else
                    continue;

                res.cab.addBlock(home, home+len);
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
            Integer slaveId,
            ModbusSlaveAddressInfo msai) {
        int failCount = 0;

        int blockCount = msai.cab.getBlockCount();
        List<ContinuousAddressBlock.AddressBlock> blockAddressList = msai.cab.getBlocks();

        for (int i=0; i<blockCount; ++i) {
            List<Boolean> digitalData = null;
            byte[] registerData = null;

            try {
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
            } catch (Exception e) {
                System.out.println("slaveId=" + slaveId + ", msai=" + msai + ", e="+e.getMessage());
            }

            if (null == digitalData && null == registerData) {
                ++failCount;
                // TODO
            } else {
                // TODO
            }

            if (msai.registerType.isDigital())
                msai.digitalDataList.add(digitalData);
            else if (msai.registerType.isRegister())
                msai.registerDataList.add(registerData);
        }

        return failCount;
    }

    /**
     * 将获取到的数据, 根据数据项的地址, 映射到数据项上
     */
    private List<ModbusDataNodeData> mapDataToDataNodes(ModbusSlaveAddressInfo msai) {
        List<ModbusDataNodeData> dataList = Lists.newArrayListWithCapacity(dataNodeList.size());

        for (ModbusDataNodeInfo dataNode : dataNodeList) {
            try {
                ModbusDataNodeProtocolInfo dnProtocolInfo = dataNode.getProtocolInfo();

                if (!dnProtocolInfo.isValid())
                    continue;

                int home = dnProtocolInfo.rAddr;
                int len = dataNode.getDataType().getByteLength();
                int idx = msai.cab.getBlockIndexByStartAddress(home);
                if (idx < 0)
                    continue;
                ContinuousAddressBlock.AddressBlock block = msai.cab.getBlock(idx);

                if (msai.registerType.isDigital()) {
                    List<Boolean> dl = msai.digitalDataList.get(idx);
                    if (null == dl || dl.isEmpty())
                        continue;

                    int startIdx = home - block.home;
                    Boolean boolValue = dl.get(startIdx);
                    String dataStr = String.valueOf(boolValue);
                    dataList.add(new ModbusDataNodeData(dataNode.getId(), dataStr));
                } else if (msai.registerType.isRegister()) {
                    byte[] dl = msai.registerDataList.get(idx);
                    if (null == dl || 0 == dl.length)
                        continue;

                    int startIdx = (home - block.home)*2;   // 一个地址代表两个字节
                    byte[] bytes = ModbusUtils.transferModbusDataBytesOrder(dl, startIdx, len, devicePi.bot);
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
            if (null == modbusConnPool || !devicePi.host.equals(modbusConnPool.getHost()) || devicePi.port.equals(modbusConnPool.getPort())) {
                if (null != modbusConnPool) {
                    modbusConnPool.closeAllConn();
                    modbusConnPool = null;
                }

                try {
                    modbusConnPool = new ModbusConnPool(devicePi.host, devicePi.port, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    sleepOneRound();
                    continue;
                }
            } else {
                modbusConnPool.checkConnStatus();
            }

            // 获取一个连接
            TCPMasterConnection conn = modbusConnPool.getOneConn();
            if (null == conn) {
                sleepOneRound();
                continue;
            }

            // 拼接地址为连续的
            ModbusSlaveAddressInfo msai = generateAddressInfo();

            // 一次性的, 拉取连续的数据块数据
            int failCount = 0;
            try {
                failCount = pullAllDataByBlock(conn, devicePi.slaveId, msai);
            } catch (Exception e) {
                e.printStackTrace();
                sleepOneRound();
                continue;
            } finally {
                if (failCount == msai.cab.getBlockCount())
                    conn.close();
                else
                    modbusConnPool.returnOneConn(conn);
            }

            // 将拉取后的数据根据地址映射到具体的数据项
            List<ModbusDataNodeData> dataList;
            try {
                dataList = mapDataToDataNodes(msai);
            } catch (Exception e) {
                e.printStackTrace();
                sleepOneRound();
                continue;
            }

            // 处理拉取后的数据
            if (!dataList.isEmpty()) {
                try {
                    // TODO
                } catch (Exception e) {
                    e.printStackTrace();
                    sleepOneRound();
                    continue;
                }
            }

            // sleep之前清空对象
            msai = null;
            dataList = null;
            // sleep
            sleepOneRound();
        }
    }

}
