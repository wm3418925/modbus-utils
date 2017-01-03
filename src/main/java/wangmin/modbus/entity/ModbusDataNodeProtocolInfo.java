package wangmin.modbus.entity;

import wangmin.modbus.entity.type.ModbusRegisterType;
import wangmin.modbus.entity.type.ModbusByteOrderType;

/**
 * Created by wm on 2017/1/3.
 */
public class ModbusDataNodeProtocolInfo {
    public Integer status;
    public Integer slaveId;
    public Integer rAddr;
    public ModbusRegisterType rType;
    public ModbusByteOrderType bot;
}
