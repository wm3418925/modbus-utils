package wangmin.modbus.entity;

import wangmin.modbus.entity.type.DataNodeDataType;
import wangmin.modbus.util.BinaryUtils;

/**
 * Created by wm on 2016/11/24.
 * 数据项信息
 */
public class RDataNode {
    private static final long serialVersionUID = 6584622335538089970L;

    public static byte[] objectToBytes(RDataNode d) {
        byte[] nameBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.name));
        byte[] unitBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.unit));
        byte[] protocolInfoBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.protocolInfo));
        byte[] expressionBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.expression));
        byte[] rangeLowBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.rangeLow));
        byte[] rangeHighBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.rangeHigh));
        byte[] orgLowBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.orgLow));
        byte[] orgHighBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.orgHigh));
        byte[] warningLowBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningLow));
        byte[] warningHighBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningHigh));
        byte[] warningLowNoteBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningLowNote));
        byte[] warningHighNoteBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningHighNote));
        byte[] warningExpBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningExp));
        byte[] warningExpNoteBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningExpNote));
        byte[] warningMapBytes = BinaryUtils.nullToEmptyByte(BinaryUtils.strToBytes(d.warningMap));

        int totalLen =
                8 + 4 + 1 + 1
                        + 2+nameBytes.length
                        + 2+unitBytes.length
                        + 2+protocolInfoBytes.length
                        + 2+expressionBytes.length

                        + 2+rangeLowBytes.length
                        + 2+rangeHighBytes.length
                        + 2+orgLowBytes.length
                        + 2+orgHighBytes.length
                        + 1

                        + 2+warningLowBytes.length
                        + 2+warningHighBytes.length
                        + 2+warningLowNoteBytes.length
                        + 2+warningHighNoteBytes.length
                        + 2+warningExpBytes.length
                        + 2+warningExpNoteBytes.length
                        + 2+warningMapBytes.length
                        + 1

                        + 4;


        byte[] b = new byte[totalLen];
        int idx = 0;


        BinaryUtils.longToBytes(d.id, b, idx); idx+=8;
        BinaryUtils.intToBytes(d.deviceId, b, idx); idx+=4;
        b[idx] = (byte) d.dataType.getValue(); idx+=1;
        b[idx] = (byte) d.gatherStatus; idx+=1;
        BinaryUtils.shortToBytes((short)nameBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, nameBytes);
        BinaryUtils.shortToBytes((short)unitBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, unitBytes);
        BinaryUtils.shortToBytes((short)protocolInfoBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, protocolInfoBytes);
        BinaryUtils.shortToBytes((short)expressionBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, expressionBytes);

        BinaryUtils.shortToBytes((short)rangeLowBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, rangeLowBytes);
        BinaryUtils.shortToBytes((short)rangeHighBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, rangeHighBytes);
        BinaryUtils.shortToBytes((short)orgLowBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, orgLowBytes);
        BinaryUtils.shortToBytes((short)orgHighBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, orgHighBytes);
        b[idx] = (byte) d.isRangeSwitch; idx+=1;

        BinaryUtils.shortToBytes((short)warningLowBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningLowBytes);
        BinaryUtils.shortToBytes((short)warningHighBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningHighBytes);
        BinaryUtils.shortToBytes((short)warningLowNoteBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningLowNoteBytes);
        BinaryUtils.shortToBytes((short)warningHighNoteBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningHighNoteBytes);
        BinaryUtils.shortToBytes((short)warningExpBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningExpBytes);
        BinaryUtils.shortToBytes((short)warningExpNoteBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningExpNoteBytes);
        BinaryUtils.shortToBytes((short)warningMapBytes.length, b, idx); idx+=2;  idx += BinaryUtils.myByteCopy(b, idx, warningMapBytes);
        b[idx] = (byte) d.warningNoticeManner; idx+=1;

        BinaryUtils.intToBytes(d.status, b, idx); //idx+=4;

        return b;
    }
    public static RDataNode bytesToObject(byte[] b) {
        int idx = 0;
        int[] nextIdx = new int[1];
        RDataNode d = new RDataNode();

        d.id = BinaryUtils.bytesToLong(b, idx); idx+=8;
        d.deviceId = BinaryUtils.bytesToInt(b, idx); idx+=4;
        d.dataType = DataNodeDataType.valueOf(0xff & b[idx]); idx+=1;
        d.gatherStatus = (0xff & b[idx]); idx+=1;
        d.name = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.unit = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.protocolInfo = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.expression = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];

        d.rangeLow = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.rangeHigh = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.orgLow = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.orgHigh = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.isRangeSwitch = (0xff & b[idx]); idx+=1;

        d.warningLow = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningHigh = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningLowNote = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningHighNote = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningExp = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningExpNote = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningMap = BinaryUtils.mySizedBytesToStr(b, idx, nextIdx); idx = nextIdx[0];
        d.warningNoticeManner = (0xff & b[idx]); idx+=1;

        d.status = BinaryUtils.bytesToInt(b, idx); //idx+=4;

        return d;
    }




    private long id;

    private int deviceId;
    private DataNodeDataType dataType;
    private int gatherStatus;
    private String name;
    private String unit;
    private String protocolInfo;
    private String expression;

    private String rangeLow;
    private String rangeHigh;
    private String orgLow;
    private String orgHigh;
    private int isRangeSwitch;

    private String warningLow;
    private String warningHigh;
    private String warningLowNote;
    private String warningHighNote;
    private String warningExp;
    private String warningExpNote;
    private String warningMap;
    private int warningNoticeManner;

    private int status;



    @Override
    public String toString() {
        return "id="+id
                +",deviceId="+deviceId
                +",dataType="+dataType
                +",gatherStatus="+gatherStatus
                +",name="+name
                +",unit="+unit
                +",protocolInfo="+protocolInfo
                +",expression="+expression

                +",rangeLow="+rangeLow
                +",rangeHigh="+rangeHigh
                +",orgLow="+orgLow
                +",orgHigh="+orgHigh
                +",isRangeSwitch="+isRangeSwitch

                +",warningLow="+warningLow
                +",warningHigh="+warningHigh
                +",warningLowNote="+warningLowNote
                +",warningHighNote="+warningHighNote
                +",warningExp="+warningExp
                +",warningExpNote="+warningExpNote
                +",warningMap="+warningMap
                +",warningNoticeManner="+warningNoticeManner

                +",status="+status
                ;}
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RDataNode) {
            return ((RDataNode) obj).id == this.id;
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

    public DataNodeDataType getDataType() {
        return dataType;
    }

    public void setDataType(DataNodeDataType dataType) {
        this.dataType = dataType;
    }

    public int getGatherStatus() {
        return gatherStatus;
    }

    public void setGatherStatus(int gatherStatus) {
        this.gatherStatus = gatherStatus;
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

    public String getProtocolInfo() {
        return protocolInfo;
    }

    public void setProtocolInfo(String protocolInfo) {
        this.protocolInfo = protocolInfo;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public String getWarningLow() {
        return warningLow;
    }

    public void setWarningLow(String warningLow) {
        this.warningLow = warningLow;
    }

    public String getWarningHigh() {
        return warningHigh;
    }

    public void setWarningHigh(String warningHigh) {
        this.warningHigh = warningHigh;
    }

    public String getWarningLowNote() {
        return warningLowNote;
    }

    public void setWarningLowNote(String warningLowNote) {
        this.warningLowNote = warningLowNote;
    }

    public String getWarningHighNote() {
        return warningHighNote;
    }

    public void setWarningHighNote(String warningHighNote) {
        this.warningHighNote = warningHighNote;
    }

    public String getWarningExp() {
        return warningExp;
    }

    public void setWarningExp(String warningExp) {
        this.warningExp = warningExp;
    }

    public String getWarningExpNote() {
        return warningExpNote;
    }

    public void setWarningExpNote(String warningExpNote) {
        this.warningExpNote = warningExpNote;
    }

    public String getWarningMap() {
        return warningMap;
    }

    public void setWarningMap(String warningMap) {
        this.warningMap = warningMap;
    }

    public int getWarningNoticeManner() {
        return warningNoticeManner;
    }

    public void setWarningNoticeManner(int warningNoticeManner) {
        this.warningNoticeManner = warningNoticeManner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
