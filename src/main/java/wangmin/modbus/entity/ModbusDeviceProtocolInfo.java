package wangmin.modbus.entity;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wm on 2016/12/26.
 */
public class ModbusDeviceProtocolInfo {
    private static final Logger logger = LoggerFactory.getLogger(ModbusDeviceProtocolInfo.class);

    public Integer status;
    public String ip;
    public Integer port;
    public Integer slaveId;
    public Integer sleepTime;

    public ModbusDeviceProtocolInfo() {}
    public String generateJsonString() {
        return JSON.toJSONString(this);
    }

    public static ModbusDeviceProtocolInfo fromJsonString(String json) {
        if (StringUtils.isEmpty(json))
            return new ModbusDeviceProtocolInfo();
        try {
            return JSON.parseObject(json, ModbusDeviceProtocolInfo.class);
        } catch (Exception e) {
            logger.error("", e);
            return new ModbusDeviceProtocolInfo();
        }
    }

}
