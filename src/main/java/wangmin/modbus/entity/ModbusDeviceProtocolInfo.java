package wangmin.modbus.entity;

import org.apache.commons.lang3.StringUtils;
import wangmin.modbus.entity.type.ModbusByteOrderType;
import wangmin.modbus.entity.type.ModbusRegisterType;

/**
 * Created by wm on 2017/1/3.
 */
public class ModbusDeviceProtocolInfo {
    public String host;
    public Integer port;
    public Integer slaveId;
    public Integer sleepTime;
    public ModbusRegisterType rType;
    public ModbusByteOrderType bot;

    public boolean isValid() {
        return StringUtils.isNotEmpty(host)
                && port != null && port != 0
                && slaveId != null
                && sleepTime != null
                && rType != null
                && bot != null
                ;
    }
}
