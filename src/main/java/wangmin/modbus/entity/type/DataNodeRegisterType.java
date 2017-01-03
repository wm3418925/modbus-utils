package wangmin.modbus.entity.type;

/**
 * Created by wm on 2017/1/3.
 */
public enum DataNodeRegisterType {
    /**
     * 线圈寄存器，按位操作，可读可写
     */
    DO,
    /**
     * 离散寄存器，按位操作，只读
     */
    DI,
    /**
     * 保持寄存器，按字操作，可读可写
     */
    RE,
    /**
     * 输入寄存器，按字操作，只读
     */
    IR;

    public boolean isDigital() {
        return this == DO || this == DI;
    }
    public boolean isWord() {
        return this == RE || this == IR;
    }
    public boolean isInput() {
        return this == DI || this == IR;
    }
    public boolean isOutput() {
        return this == DO || this == RE;
    }
}
