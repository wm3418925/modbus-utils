package wangmin.modbus.entity.type;

/**
 * Created by wm on 2016/12/28.
 */
public enum ModbusStatus {
    ON(1),
    OFF(0);

    protected static final ModbusStatus defaultEnum = OFF;

    protected final int value;
    private ModbusStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
    public static int getValue(ModbusStatus status) {
        if (status == null)
            return defaultEnum.value;
        else
            return status.value;
    }
    public static ModbusStatus valueOf(int value) {
        ModbusStatus[] list = ModbusStatus.values();
        for (int i=0; i<list.length; ++i) {
            if (list[i].value == value)
                return list[i];
        }
        return defaultEnum;
    }
}
