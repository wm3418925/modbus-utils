package wangmin.modbus.entity;

import com.google.common.collect.Lists;
import wangmin.modbus.util.ContinuousAddressBlock;
import wangmin.modbus.entity.type.DataNodeRegisterType;

import java.util.List;

/**
 * Created by wm on 2017/1/3.
 * modbus 的一个slave的地址信息
 */
public class ModbusSlaveAddressInfo {
    public DataNodeRegisterType registerType;
    public ContinuousAddressBlock cab;
    public List<List<Boolean>> digitalDataList;
    public List<byte[]> registerDataList;

    public ModbusSlaveAddressInfo(DataNodeRegisterType registerType) {
        this.registerType = registerType;

        cab = new ContinuousAddressBlock();

        if (registerType.isDigital())
            digitalDataList = Lists.newArrayList();
        else if (registerType.isRegister())
            registerDataList = Lists.newArrayList();
    }
}
