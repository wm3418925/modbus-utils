package wangmin.modbus.entity.type;

/**
 * Created by wm on 2016/11/24.
 * 数据项数据类型枚举
 */
public enum DataNodeDataType {
    DataNodeDataType_void(0, 0),
    DataNodeDataType_bool(1, 1),

    DataNodeDataType_int8(2, 1),
    DataNodeDataType_int16(3, 2),
    DataNodeDataType_int32(4, 4),
    DataNodeDataType_int64(5, 8),

    DataNodeDataType_uint8(6, 1),
    DataNodeDataType_uint16(7, 2),
    DataNodeDataType_uint32(8, 4),
    DataNodeDataType_uint64(9, 8),

    DataNodeDataType_float(10, 4),
    DataNodeDataType_double(11, 8);


    protected static final DataNodeDataType defaultEnum = DataNodeDataType_void;

    protected final int value;
    protected final int byteLength;
    private DataNodeDataType(int value, int byteLength) {
        this.value = value;
        this.byteLength = byteLength;
    }

    public int getValue() {
        return this.value;
    }
    public static int getValue(DataNodeDataType status) {
        if (status == null)
            return defaultEnum.value;
        else
            return status.value;
    }
    public static DataNodeDataType valueOf(int value) {
        DataNodeDataType[] list = DataNodeDataType.values();
        for (int i=0; i<list.length; ++i) {
            if (list[i].value == value)
                return list[i];
        }
        return defaultEnum;
    }


    public int getByteLength() {
        return byteLength;
    }
    public int getWordCount() {
        return (byteLength/2) + (byteLength&1);
    }
}
