package wangmin.modbus.entity;

import com.alibaba.fastjson.JSON;

import wangmin.modbus.entity.type.DataNodeRegisterType;
import wangmin.modbus.entity.type.HexByteOrderType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wm on 2016/12/26.
 */
public class ModbusDataNodeProtocolInfo {
    private static final Logger logger = LoggerFactory.getLogger(ModbusDataNodeProtocolInfo.class);

    public Integer status;
    public Integer slaveId;
    public Integer rAddr;
    public DataNodeRegisterType rType;
    public HexByteOrderType bot;

    public ModbusDataNodeProtocolInfo() {}
    public String generateJsonString() {
        return JSON.toJSONString(this);
    }

    public static ModbusDataNodeProtocolInfo fromJsonString(String json) {
        if (StringUtils.isEmpty(json))
            return new ModbusDataNodeProtocolInfo();
        try {
            return JSON.parseObject(json, ModbusDataNodeProtocolInfo.class);
        } catch (Exception e) {
            logger.error("", e);
            return new ModbusDataNodeProtocolInfo();
        }
    }

}
