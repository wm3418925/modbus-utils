package wangmin.modbus.entity;

/**
 * Created by wm on 2017/1/3.
 */
public class ModbusDataNodeProtocolInfo {
    public Integer rAddr;

    public boolean isValid() {
        return rAddr != null;
    }
}
