package wangmin.modbus.entity;

/**
 * Created by wm on 2017/1/3.
 */
public class HDeviceDataNodeData {
    public long dataNodeId;
    public String value;

    public HDeviceDataNodeData() {}
    public HDeviceDataNodeData(long dataNodeId, String value) {
        this.dataNodeId = dataNodeId;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{\"dataNodeId\":"+dataNodeId+",\"value\":\""+value+"\"}";
    }
}
