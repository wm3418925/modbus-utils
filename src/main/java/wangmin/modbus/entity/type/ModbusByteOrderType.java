package wangmin.modbus.entity.type;

/**
 * Created by wm on 2017/1/3.
 */
public enum ModbusByteOrderType {
    /**
     * 如有多个寄存器，则存储低字节的寄存器在前，每个寄存器内部大端排序,modbus模拟器默认 0 GRM503对应编码 3412
     */
    LowFirstBigEndian(0),

    /**
     * 如有多个寄存器，则存储高字节的寄存器在前，每个寄存器内部大端排序  GRM503对应编码 1234
     */
    HighFirstBigEndian(1),

    /**
     * 如有多个寄存器，则存储低字节的寄存器在前，每个寄存器内部小端排序  GRM503对应编码 4321
     */
    LowFirstLittleEndian(2),

    /**
     * 如有多个寄存器，则存储高字节的寄存器在前，每个寄存器内部小端排序   GRM503对应编码 2143
     */
    HighFirstLittleEndian(3);


    protected static final ModbusByteOrderType defaultEnum = HighFirstBigEndian;

    protected final int value;
    private ModbusByteOrderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
    public static int getValue(ModbusByteOrderType status) {
        if (status == null)
            return defaultEnum.value;
        else
            return status.value;
    }
    public static ModbusByteOrderType valueOf(int value) {
        ModbusByteOrderType[] list = ModbusByteOrderType.values();
        for (int i=0; i<list.length; ++i) {
            if (list[i].value == value)
                return list[i];
        }
        return defaultEnum;
    }
}
