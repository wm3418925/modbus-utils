package wangmin.modbus.util;

import wangmin.modbus.entity.RDataNode;
import wangmin.modbus.entity.type.DataNodeDataType;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wm on 2016/11/29.
 * 数据项数据操作类
 * 警告 !!!! : 数据类型不考虑uint64的最高位为1的情况
 */
public abstract class DataNodeDataUtils {
    private static Logger logger = LoggerFactory.getLogger(DataNodeDataUtils.class);

    /**
     * 将数据转化为字符串 (如果是二进制数据, 转为16进制大写字符串显示)
     */
    public static String convertDataToStr(DataNodeDataType type, byte[] data, boolean hexWithPrefix) {
        if (type == DataNodeDataType.DataNodeDataType_void) {
            if (null == data)
                return "";

            char[] hexChars = Hex.encodeHex(data, false);
            if (hexWithPrefix)
                return "0X"+String.valueOf(hexChars);
            else
                return String.valueOf(hexChars);
        }

        if (null == data || 0 == data.length)
            return "";

        if (type == DataNodeDataType.DataNodeDataType_bool) {
            if (data[0] != 0)
                return "TRUE";
            else
                return "FALSE";
        }

        if (type == DataNodeDataType.DataNodeDataType_float) {
            try {
                int intBits = BinaryUtils.bytesToInt(data);
                return String.valueOf(Float.intBitsToFloat(intBits));
            } catch (Exception e) {
                return "";
            }
        }
        if (type == DataNodeDataType.DataNodeDataType_double) {
            try {
                long longBits = BinaryUtils.bytesToLong(data);
                return String.valueOf(Double.longBitsToDouble(longBits));
            } catch (Exception e) {
                return "";
            }
        }

        switch (type) {
            case DataNodeDataType_int8:
                return String.valueOf(data[0]);
            case DataNodeDataType_uint8:
                return String.valueOf(data[0] & 0xff);
            case DataNodeDataType_int16:
                return String.valueOf(BinaryUtils.bytesToShort(data));
            case DataNodeDataType_uint16:
                return String.valueOf(BinaryUtils.bytesToShort(data) & 0xffff);
            case DataNodeDataType_int32:
                return String.valueOf(BinaryUtils.bytesToInt(data));
            case DataNodeDataType_uint32:
                Integer uint32Bits = BinaryUtils.bytesToInt(data);
                return String.valueOf(uint32Bits.longValue() & 0xffffffffL);
            case DataNodeDataType_int64:
            case DataNodeDataType_uint64:
                return String.valueOf(BinaryUtils.bytesToLong(data));
        }

        return "";
    }
    private static final byte[] emptyByte = new byte[0];
    private static final byte[] trueBytes = {(byte)1};
    private static final byte[] falseBytes = {(byte)0};
    /**
     * 将字符串转化为数据
     */
    public static byte[] convertStrToData(DataNodeDataType type, String str, boolean hexWithPrefix) {
        if (str != null)
            str = str.toUpperCase();

        try {
            if (type == DataNodeDataType.DataNodeDataType_void) {
                if (null == str)
                    return emptyByte;

                if (hexWithPrefix) {
                    if (str.length() <= 2)
                        return emptyByte;
                    else
                        Hex.decodeHex(str.substring(2).toCharArray());
                } else {
                    return Hex.decodeHex(str.toCharArray());
                }
            }

            if (null == str || 0 == str.length())
                return emptyByte;

            if (type == DataNodeDataType.DataNodeDataType_bool) {
                if ("TRUE".equals(str))
                    return trueBytes;
                else if ("FALSE".equals(str))
                    return falseBytes;
                else
                    return emptyByte;
            }

            if (type == DataNodeDataType.DataNodeDataType_float) {
                float f = Float.valueOf(str);
                int i = Float.floatToIntBits(f);
                return BinaryUtils.intToBytes(i);
            }
            if (type == DataNodeDataType.DataNodeDataType_double) {
                double d = Double.valueOf(str);
                long l = Double.doubleToLongBits(d);
                return BinaryUtils.longToBytes(l);
            }

            switch (type) {
                case DataNodeDataType_int8:
                    return BinaryUtils.intToOneByte(Byte.valueOf(str));
                case DataNodeDataType_uint8:
                    return BinaryUtils.intToOneByte(Integer.valueOf(str));
                case DataNodeDataType_int16:
                    return BinaryUtils.shortToBytes(Short.valueOf(str));
                case DataNodeDataType_uint16:
                    return BinaryUtils.shortToBytes((short)(int)Integer.valueOf(str));
                case DataNodeDataType_int32:
                    return BinaryUtils.intToBytes(Integer.valueOf(str));
                case DataNodeDataType_uint32:
                    return BinaryUtils.intToBytes((int)(long)Long.valueOf(str));
                case DataNodeDataType_int64:
                case DataNodeDataType_uint64:
                    return BinaryUtils.longToBytes(Long.valueOf(str));
            }

            return emptyByte;
        } catch (Exception e) {
            logger.warn("", e);
            return emptyByte;
        }
    }


    // 预处理数据配置字符串
    public static String preHandleDataStr(DataNodeDataType type, String a) {
        if (type == DataNodeDataType.DataNodeDataType_void)
            return a;
        if (a == null)
            return null;

        a = a.trim();
        if (a.length() == 0)
            return null;
        return a;
    }

    // 预处理之后 对数据验证
    public static boolean validateData(DataNodeDataType type, String a) {
        if (type == DataNodeDataType.DataNodeDataType_void)
            return true;

        if (null == a || 0 == a.length())
            return false;

        if (type == DataNodeDataType.DataNodeDataType_bool)
            return "true".compareToIgnoreCase(a) == 0 || "false".compareToIgnoreCase(a) == 0;

        if (type == DataNodeDataType.DataNodeDataType_float) {
            try {
                Float.valueOf(a);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        if (type == DataNodeDataType.DataNodeDataType_double) {
            try {
                Double.valueOf(a);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        long lv;
        try {
            lv = Long.valueOf(a);
        } catch (Exception e) {
            return false;
        }

        switch (type) {
            case DataNodeDataType_int8:
                return (lv >= Byte.MIN_VALUE && lv <= Byte.MAX_VALUE);
            case DataNodeDataType_uint8:
                return (lv >= 0l && lv <= 0x00ffl);
            case DataNodeDataType_int16:
                return (lv >= Short.MIN_VALUE && lv <= Short.MAX_VALUE);
            case DataNodeDataType_uint16:
                return (lv >= 0l && lv <= 0x00ffffl);
            case DataNodeDataType_int32:
                return (lv >= Integer.MIN_VALUE && lv <= Integer.MAX_VALUE);
            case DataNodeDataType_uint32:
                return (lv >= 0l && lv <= 0x00ffffffffl);
            case DataNodeDataType_uint64:
                return (a.charAt(0) != '-');
            case DataNodeDataType_int64:
                return true;
        }

        return false;
    }

    // 字节数组比较
    private static int bytesCompare(byte[] a, byte[] b) {
        if (null == a && null == b)
            return 0;
        if (null == a && null != b)
            return -1;
        if (null != a && null == b)
            return 1;

        int minLen = Math.min(a.length, b.length);
        for (int i=0; i<minLen; ++i) {
            int v = (a[i] & 0xff) - (b[i] & 0xff);
            if (v != 0)
                return v;
        }

        if (a.length < b.length)
            return -1;
        else if (a.length > b.length)
            return 1;
        else
            return 0;
    }

    // 字符串数据值 与 字符串数据值 比较
    public static int compareData(DataNodeDataType type, String a, String b) {
        int strCmp = a.compareTo(b);
        if (0 == strCmp) return 0;

        switch (type) {
            case DataNodeDataType_void:
                return strCmp;

            case DataNodeDataType_bool:
                return a.compareToIgnoreCase(b);// false true

            case DataNodeDataType_int8:
            case DataNodeDataType_int16:
            case DataNodeDataType_int32:
            case DataNodeDataType_int64:
            case DataNodeDataType_uint8:
            case DataNodeDataType_uint16:
            case DataNodeDataType_uint32:
            case DataNodeDataType_uint64:
                return Long.valueOf(a).compareTo(Long.valueOf(b));

            case DataNodeDataType_float:
            case DataNodeDataType_double:
                // TODO 是否考虑数值相差很小的情况
                return Double.valueOf(a).compareTo(Double.valueOf(b));
        }
        return 0;
    }
    // 字符串数据值 与 字节数组数据值 比较
    public static int compareData(DataNodeDataType type, String a, byte[] b) throws Exception {
        if (null == a && null == b)
            return 0;
        if (null == a && null != b)
            return -1;
        if (null != a && null == b)
            return 1;

        switch (type) {
            case DataNodeDataType_void:
                return bytesCompare(BinaryUtils.strToBytes(a), b);

            case DataNodeDataType_bool:
                boolean ba;
                if ("TRUE".equalsIgnoreCase(a))
                    ba = true;
                else if ("FALSE".equalsIgnoreCase(a))
                    ba = false;
                else
                    throw new Exception();
                return Boolean.compare(ba, b[0] != 0);

            case DataNodeDataType_int8:
                if (b.length != 1) throw new Exception();
                return Byte.valueOf(a).compareTo(b[0]);
            case DataNodeDataType_uint8:
                if (b.length != 1) throw new Exception();
                return Integer.valueOf(a).compareTo(b[0] & 0XFF);

            case DataNodeDataType_int16:
                if (b.length != 2) throw new Exception();
                return Short.valueOf(a).compareTo(BinaryUtils.bytesToShort(b));
            case DataNodeDataType_uint16:
                if (b.length != 2) throw new Exception();
                return Integer.valueOf(a).compareTo(BinaryUtils.bytesToShort(b) & 0XFFFF);

            case DataNodeDataType_int32:
                if (b.length != 4) throw new Exception();
                return Integer.valueOf(a).compareTo(BinaryUtils.bytesToInt(b));
            case DataNodeDataType_uint32:
                if (b.length != 4) throw new Exception();
                return Long.valueOf(a).compareTo(BinaryUtils.bytesToInt(b) & 0XFFFFFFFFL);

            case DataNodeDataType_int64:
            case DataNodeDataType_uint64:
                if (b.length != 8) throw new Exception();
                return Long.valueOf(a).compareTo(BinaryUtils.bytesToLong(b));

            case DataNodeDataType_float:
                if (b.length != 4) throw new Exception();

                // TODO 是否考虑数值相差很小的情况
                float af = Float.valueOf(a);

                int intBites = BinaryUtils.bytesToInt(b);
                float bf = Float.intBitsToFloat(intBites);

                return Float.compare(af, bf);

            case DataNodeDataType_double:
                if (b.length != 8) throw new Exception();

                // TODO 是否考虑数值相差很小的情况
                double ad = Double.valueOf(a);

                long longBites = BinaryUtils.bytesToLong(b);
                double bd = Double.longBitsToDouble(longBites);

                return Double.compare(ad, bd);
        }
        return 0;
    }

    // 字符串数据转化, 包括 量程转换 和 表达式转换
    public static String convertData(RDataNode dn, String value) throws Exception {
        if (dn.getIsRangeSwitch() == 0)
            return value;

        switch (dn.getDataType()) {
            case DataNodeDataType_void:
                return value;

            case DataNodeDataType_bool:
                //if (StringUtils.isNotEmpty(dn.getExpression())) {
                    // TODO
                //}
                return value;

            case DataNodeDataType_int8:
            case DataNodeDataType_int16:
            case DataNodeDataType_int32:
            case DataNodeDataType_int64:
            case DataNodeDataType_uint8:
            case DataNodeDataType_uint16:
            case DataNodeDataType_uint32:
            case DataNodeDataType_uint64: {
                long v = Long.valueOf(value);
                long orgLow = Long.valueOf(dn.getOrgLow());
                long orgHigh = Long.valueOf(dn.getOrgHigh());
                long rangeLow = Long.valueOf(dn.getRangeLow());
                long rangeHigh = Long.valueOf(dn.getRangeHigh());
                if (v < orgLow)
                    v = rangeLow;
                else if (v > orgHigh)
                    v = rangeHigh;
                else
                    v = (v - orgLow) * (rangeHigh - rangeLow) / (orgHigh - orgLow) + rangeLow;

                //if (StringUtils.isNotEmpty(dn.getExpression())) {
                // TODO
                //}
                return String.valueOf(v);
            }

            case DataNodeDataType_float:
            case DataNodeDataType_double: {
                double v = Double.valueOf(value);
                double orgLow = Double.valueOf(dn.getOrgLow());
                double orgHigh = Double.valueOf(dn.getOrgHigh());
                double rangeLow = Double.valueOf(dn.getRangeLow());
                double rangeHigh = Double.valueOf(dn.getRangeHigh());
                if (v < orgLow)
                    v = rangeLow;
                else if (v > orgHigh)
                    v = rangeHigh;
                else
                    v = (v - orgLow) * (rangeHigh - rangeLow) / (orgHigh - orgLow) + rangeLow;

                //if (StringUtils.isNotEmpty(dn.getExpression())) {
                // TODO
                //}
                return String.valueOf(v);
            }
        }
        return value;
    }
    // 字节数组数据转化, 包括 量程转换 和 表达式转换
    public static void convertData(RDataNode dn, byte[] value) throws Exception {
        if (dn.getIsRangeSwitch() == 0)
            return;

        switch (dn.getDataType()) {
            case DataNodeDataType_void:
                return;

            case DataNodeDataType_bool:
                //if (StringUtils.isNotEmpty(dn.getExpression())) {
                // TODO
                //}
                return;

            case DataNodeDataType_int8:
            case DataNodeDataType_int16:
            case DataNodeDataType_int32:
            case DataNodeDataType_int64:
            case DataNodeDataType_uint8:
            case DataNodeDataType_uint16:
            case DataNodeDataType_uint32:
            case DataNodeDataType_uint64: {
                long v = 0L;
                switch (dn.getDataType()) {
                    case DataNodeDataType_int8:
                        v = (long) value[0];
                        break;
                    case DataNodeDataType_int16:
                        v = BinaryUtils.bytesToShort(value);
                        break;
                    case DataNodeDataType_int32:
                        v = BinaryUtils.bytesToInt(value);
                        break;
                    case DataNodeDataType_uint8:
                        v = (value[0] & 0XFFL);
                        break;
                    case DataNodeDataType_uint16:
                        v = BinaryUtils.bytesToShort(value) & 0XFFFFL;
                        break;
                    case DataNodeDataType_uint32:
                        v = BinaryUtils.bytesToInt(value) & 0XFFFFFFFFL;
                        break;
                    case DataNodeDataType_int64:
                    case DataNodeDataType_uint64:
                        v = BinaryUtils.bytesToLong(value);
                        break;
                }

                long orgLow = Long.valueOf(dn.getOrgLow());
                long orgHigh = Long.valueOf(dn.getOrgHigh());
                long rangeLow = Long.valueOf(dn.getRangeLow());
                long rangeHigh = Long.valueOf(dn.getRangeHigh());
                if (v < orgLow)
                    v = rangeLow;
                else if (v > orgHigh)
                    v = rangeHigh;
                else
                    v = (v - orgLow) * (rangeHigh - rangeLow) / (orgHigh - orgLow) + rangeLow;

                //if (StringUtils.isNotEmpty(dn.getExpression())) {
                // TODO
                //}

                switch (dn.getDataType()) {
                    case DataNodeDataType_int8:
                        value[0] = (byte) v;
                        return;
                    case DataNodeDataType_int16:
                        BinaryUtils.shortToBytes((short) v, value);
                        return;
                    case DataNodeDataType_int32:
                        BinaryUtils.intToBytes((int) v, value);
                        return;
                    case DataNodeDataType_uint8:
                        value[0] = (byte) (v & 0xff);
                        return;
                    case DataNodeDataType_uint16:
                        BinaryUtils.shortToBytes((short) (v & 0xffff), value);
                        return;
                    case DataNodeDataType_uint32:
                        BinaryUtils.intToBytes((int) (v & 0xffffffff), value);
                        return;
                    case DataNodeDataType_int64:
                    case DataNodeDataType_uint64:
                        BinaryUtils.longToBytes(v, value);
                        return;
                }
                return;
            }

            case DataNodeDataType_float:
            case DataNodeDataType_double: {
                double v;
                if (DataNodeDataType.DataNodeDataType_float == dn.getDataType())
                    v = Float.intBitsToFloat(BinaryUtils.bytesToInt(value));
                else //if (DataNodeDataType.DataNodeDataType_double == dn.getDataType())
                    v = Double.longBitsToDouble(BinaryUtils.bytesToLong(value));

                double orgLow = Double.valueOf(dn.getOrgLow());
                double orgHigh = Double.valueOf(dn.getOrgHigh());
                double rangeLow = Double.valueOf(dn.getRangeLow());
                double rangeHigh = Double.valueOf(dn.getRangeHigh());
                if (v < orgLow)
                    v = rangeLow;
                else if (v > orgHigh)
                    v = rangeHigh;
                else
                    v = (v - orgLow) * (rangeHigh - rangeLow) / (orgHigh - orgLow) + rangeLow;

                //if (StringUtils.isNotEmpty(dn.getExpression())) {
                // TODO
                //}

                if (DataNodeDataType.DataNodeDataType_float == dn.getDataType())
                    BinaryUtils.intToBytes(Float.floatToIntBits((float)v), value);
                else //if (DataNodeDataType.DataNodeDataType_double == dn.getDataType())
                    BinaryUtils.longToBytes(Double.doubleToLongBits(v), value);
                return;
            }
        }
    }

}
