package wangmin.modbus.entity;

import wangmin.modbus.entity.type.ModbusDataType;

/**
 * Created by wm on 2017/1/3.
 * 数据项信息
 */
public class ModbusDataNodeInfo {
    private long id;

    private int deviceId;
    private ModbusDataType dataType;
    private String name;
    private String unit;
    private ModbusDataNodeProtocolInfo protocolInfo;

    private String rangeLow;
    private String rangeHigh;
    private String orgLow;
    private String orgHigh;
    private int isRangeSwitch;

    private int status;



    @Override
    public String toString() {
        return "id="+id
                +",deviceId="+deviceId
                +",dataType="+dataType
                +",name="+name
                +",unit="+unit
                +",protocolInfo="+protocolInfo

                +",rangeLow="+rangeLow
                +",rangeHigh="+rangeHigh
                +",orgLow="+orgLow
                +",orgHigh="+orgHigh
                +",isRangeSwitch="+isRangeSwitch

                +",status="+status
                ;}
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModbusDataNodeInfo) {
            return ((ModbusDataNodeInfo) obj).id == this.id;
        }
        return false;
    }
    @Override
    public int hashCode() {return toString().hashCode();}



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public ModbusDataType getDataType() {
        return dataType;
    }

    public void setDataType(ModbusDataType dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ModbusDataNodeProtocolInfo getProtocolInfo() {
        return protocolInfo;
    }

    public void setProtocolInfo(ModbusDataNodeProtocolInfo protocolInfo) {
        this.protocolInfo = protocolInfo;
    }

    public String getRangeLow() {
        return rangeLow;
    }

    public void setRangeLow(String rangeLow) {
        this.rangeLow = rangeLow;
    }

    public String getRangeHigh() {
        return rangeHigh;
    }

    public void setRangeHigh(String rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public String getOrgLow() {
        return orgLow;
    }

    public void setOrgLow(String orgLow) {
        this.orgLow = orgLow;
    }

    public String getOrgHigh() {
        return orgHigh;
    }

    public void setOrgHigh(String orgHigh) {
        this.orgHigh = orgHigh;
    }

    public int getIsRangeSwitch() {
        return isRangeSwitch;
    }

    public void setIsRangeSwitch(int isRangeSwitch) {
        this.isRangeSwitch = isRangeSwitch;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
