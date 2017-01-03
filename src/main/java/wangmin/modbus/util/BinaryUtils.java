package wangmin.modbus.util;

import com.google.common.collect.Lists;
//import lanchuang.rdms.mgr.core.common.entity.type.CommonResponseCode;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 封装 二进制 操作
 * */
public abstract class BinaryUtils {
	public enum MyObjectDataType {
		Type_int(0),
		Type_long(1),
		Type_date(2),
		Type_double(3),
		Type_string(4),
		Type_enum(5);

		protected static final MyObjectDataType defaultEnum = Type_int;

		protected final int value;
		private MyObjectDataType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
		public static int getValue(MyObjectDataType status) {
			if (status == null)
				return defaultEnum.value;
			else
				return status.value;
		}
		public static MyObjectDataType valueOf(int value) {
			MyObjectDataType[] list = MyObjectDataType.values();
			for (int i=0; i<list.length; ++i) {
				if (list[i].value == value)
					return list[i];
			}
			return defaultEnum;
		}
	}


	private static final Charset charset = Charset.forName("UTF-8");
	public static byte[] strToBytes(String str) {
		if (null == str)
			return null;
		else
			return str.getBytes(charset);
	}
	public static String bytesToStr(byte[] ba) {
		if (null == ba)
			return null;
		else
			return new String(ba, charset);
	}

	public static Map<byte[],byte[]> strToBytes(Map<String,String> src, Map<byte[],byte[]> dst) {
		for (Entry<String, String> entry : src.entrySet()) {
			byte[] key = entry.getKey().getBytes(charset);
			byte[] value = null;
			if (null != entry.getValue())
				value = dst.put(key, entry.getValue().getBytes(charset));
			dst.put(key, value);
		}
		return dst;
	}
	public static Map<String,String> bytesToStr(Map<byte[],byte[]> src, Map<String,String> dst) {
		for (Entry<byte[],byte[]> entry : src.entrySet()) {
			String key = new String(entry.getKey(), charset);
			String value = null;
			if (null == entry.getValue())
				value = new String(entry.getValue(), charset);
			dst.put(key, value);
		}
		return dst;
	}
	public static Collection<byte[]> strToBytes(Collection<String> src, Collection<byte[]> dst) {
		for (String entry : src) {
			if (null == entry)
				dst.add(null);
			else
				dst.add(entry.getBytes(charset));
		}
		return dst;
	}
	public static Collection<String> bytesToStr(Collection<byte[]> src, Collection<String>dst) {
		for (byte[] entry : src) {
			String value = null;
			if (null != entry)
				value = new String(entry, charset);
			dst.add(value);
		}
		return dst;
	}


	public static long bytesToLong(byte[] buf) {
		return    ((0xffffffffffffffffl & (0xff&buf[0])) << 56)
				| ((0xffffffffffffffffl & (0xff&buf[1])) << 48)
				| ((0xffffffffffffffffl & (0xff&buf[2])) << 40)
				| ((0xffffffffffffffffl & (0xff&buf[3])) << 32)
				| ((0xffffffffffffffffl & (0xff&buf[4])) << 24)
				| ((0xffffffffffffffffl & (0xff&buf[5])) << 16)
				| ((0xffffffffffffffffl & (0xff&buf[6])) << 8)
				|  (0xffffffffffffffffl & (0xff&buf[7]));
	}
	public static long bytesToLong(byte[] buf, int startIdx) {
		return    ((0xffffffffffffffffl & (0xff&buf[startIdx])) << 56)
				| ((0xffffffffffffffffl & (0xff&buf[startIdx+1])) << 48)
				| ((0xffffffffffffffffl & (0xff&buf[startIdx+2])) << 40)
				| ((0xffffffffffffffffl & (0xff&buf[startIdx+3])) << 32)
				| ((0xffffffffffffffffl & (0xff&buf[startIdx+4])) << 24)
				| ((0xffffffffffffffffl & (0xff&buf[startIdx+5])) << 16)
				| ((0xffffffffffffffffl & (0xff&buf[startIdx+6])) << 8)
				|  (0xffffffffffffffffl & (0xff&buf[startIdx+7]));
	}
	public static byte[] longToBytes(long l) {
		byte[] b = new byte[8];
		b[0] = (byte) ((l >>> 56) & 0xff);
		b[1] = (byte) ((l >>> 48) & 0xff);
		b[2] = (byte) ((l >>> 40) & 0xff);
		b[3] = (byte) ((l >>> 32) & 0xff);
		b[4] = (byte) ((l >>> 24) & 0xff);
		b[5] = (byte) ((l >>> 16) & 0xff);
		b[6] = (byte) ((l >>> 8 ) & 0xff);
		b[7] = (byte) ( l         & 0xff);
		return b;
	}
	public static byte[] longToBytes(long l, byte[] b) {
		b[0] = (byte) ((l >>> 56) & 0xff);
		b[1] = (byte) ((l >>> 48) & 0xff);
		b[2] = (byte) ((l >>> 40) & 0xff);
		b[3] = (byte) ((l >>> 32) & 0xff);
		b[4] = (byte) ((l >>> 24) & 0xff);
		b[5] = (byte) ((l >>> 16) & 0xff);
		b[6] = (byte) ((l >>> 8 ) & 0xff);
		b[7] = (byte) ( l         & 0xff);
		return b;
	}
	public static byte[] longToBytes(long l, byte[] b, int startIdx) {
		b[startIdx  ] = (byte) ((l >>> 56) & 0xff);
		b[startIdx+1] = (byte) ((l >>> 48) & 0xff);
		b[startIdx+2] = (byte) ((l >>> 40) & 0xff);
		b[startIdx+3] = (byte) ((l >>> 32) & 0xff);
		b[startIdx+4] = (byte) ((l >>> 24) & 0xff);
		b[startIdx+5] = (byte) ((l >>> 16) & 0xff);
		b[startIdx+6] = (byte) ((l >>> 8 ) & 0xff);
		b[startIdx+7] = (byte) ( l         & 0xff);
		return b;
	}

	public static Collection<byte[]> longToBytes(Collection<Long> src, Collection<byte[]> dst) {
		for (Long entry : src) {
			if (null == entry)
				dst.add(null);
			else
				dst.add(longToBytes(entry));
		}
		return dst;
	}
	public static Collection<Long> bytesToLong(Collection<byte[]> src, Collection<Long> dst) {
		for (byte[] entry : src) {
			if (null == entry)
				dst.add(null);
			else
				dst.add(bytesToLong(entry));
		}
		return dst;
	}


	public static int bytesToInt(byte[] buf) {
		return    ((0xff & buf[0]) << 24)
				| ((0xff & buf[1]) << 16)
				| ((0xff & buf[2]) << 8)
				| ( 0xff & buf[3]);
	}
	public static int bytesToInt(byte[] buf, int startIdx) {
		return    ((0xff & buf[startIdx  ]) << 24)
				| ((0xff & buf[startIdx+1]) << 16)
				| ((0xff & buf[startIdx+2]) << 8)
				| ( 0xff & buf[startIdx+3]);
	}
	public static byte[] intToBytes(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) ((i >>> 24) & 0xff);
		b[1] = (byte) ((i >>> 16) & 0xff);
		b[2] = (byte) ((i >>> 8 ) & 0xff);
		b[3] = (byte) ( i         & 0xff);
		return b;
	}
	public static byte[] intToBytes(int i, byte[] b) {
		b[0] = (byte) ((i >>> 24) & 0xff);
		b[1] = (byte) ((i >>> 16) & 0xff);
		b[2] = (byte) ((i >>> 8 ) & 0xff);
		b[3] = (byte) ( i         & 0xff);
		return b;
	}
	public static byte[] intToBytes(int i, byte[] b, int startIdx) {
		b[startIdx  ] = (byte) ((i >>> 24) & 0xff);
		b[startIdx+1] = (byte) ((i >>> 16) & 0xff);
		b[startIdx+2] = (byte) ((i >>> 8 ) & 0xff);
		b[startIdx+3] = (byte) ( i         & 0xff);
		return b;
	}

	public static int oneByteToInt(byte[] buf) {
		return (0xff & buf[0]);
	}
	public static byte[] intToOneByte(int i) {
		byte[] b = new byte[1];
		b[0] = (byte) (i & 0xff);
		return b;
	}

	public static Collection<byte[]> intToBytes(Collection<Integer> src, Collection<byte[]> dst) {
		for (Integer entry : src) {
			dst.add(intToBytes(entry));
		}
		return dst;
	}
	public static Collection<Integer> bytesToInteger(Collection<byte[]> src, Collection<Integer> dst) {
		for (byte[] entry : src) {
			if (null == entry)
				dst.add(null);
			else
				dst.add(bytesToInt(entry));
		}
		return dst;
	}


	public static short bytesToShort(byte[] buf) {
		return (short) (((0xff & buf[0]) << 8) | ( 0xff & buf[1]));
	}
	public static short bytesToShort(byte[] buf, int startIdx) {
		return (short) (((0xff & buf[startIdx]) << 8) | ( 0xff & buf[startIdx+1]));
	}
	public static byte[] shortToBytes(short i) {
		byte[] b = new byte[2];
		b[0] = (byte) ((i >>> 8) & 0xff);
		b[1] = (byte) ( i        & 0xff);
		return b;
	}
	public static byte[] shortToBytes(short i, byte[] b) {
		b[0] = (byte) ((i >>> 8) & 0xff);
		b[1] = (byte) ( i        & 0xff);
		return b;
	}
	public static byte[] shortToBytes(short i, byte[] b, int startIdx) {
		b[startIdx  ] = (byte) ((i >>> 8) & 0xff);
		b[startIdx+1] = (byte) ( i        & 0xff);
		return b;
	}



	public static byte[] intListToBytes(List<Integer> list) {
		byte[] bytes = new byte[list.size()*4];

		for (int i=0; i<list.size(); ++i) {
			BinaryUtils.intToBytes(list.get(i), bytes, 4*i);
		}

		return bytes;
	}
	public static List<Integer> bytesToIntList(byte[] bytes) {
		List<Integer> list = Lists.newArrayList();

		int count = bytes.length/4;
		for (int i=0; i<count; ++i) {
			list.add(BinaryUtils.bytesToInt(bytes, 4*i));
		}

		return list;
	}



	private static byte hexBytesTOneByte(byte[] hexBytes, int startIdx) {
		byte high = hexBytes[startIdx];
		if (high <= '9')
			high = (byte) (high - '0');
		else if (high <= 'F')
			high = (byte) (high - ('A'-10));
		else //if (high <= 'f')
			high = (byte) (high - ('a'-10));

		byte low = hexBytes[startIdx+1];
		if (low <= '9')
			low = (byte) (low - '0');
		else if (low <= 'F')
			low = (byte) (low - ('A'-10));
		else //if (low <= 'f')
			low = (byte) (low - ('a'-10));

		return (byte) (((high & 0xff) << 4) | low);
	}
	private static byte[] oneByteToHexBytes(byte b, byte[] hexBytes, int startIdx) {
		byte high = (byte) ((b & 0xf0) >>> 4);
		if (high <= 9)
			high += '0';
		else
			high += ('A'-10);
		hexBytes[startIdx] = high;

		byte low = (byte) (b & 0x0f);
		if (low <= 9)
			low += '0';
		else
			low += ('A'-10);
		hexBytes[startIdx+1] = low;

		return hexBytes;
	}

	public static byte[] hexBytesToBytes(byte[] hexBytes, int startIdx) {
		byte[] bytes = new byte[(hexBytes.length-startIdx)/2];
		for (int i=0; i<bytes.length; ++i) {
			bytes[i] = hexBytesTOneByte(hexBytes, startIdx+i*2);
		}
		return bytes;
	}

	public static int hexBytesToInt(byte[] buf) {
		return    ((0xff & hexBytesTOneByte(buf,0)) << 24)
				| ((0xff & hexBytesTOneByte(buf,2)) << 16)
				| ((0xff & hexBytesTOneByte(buf,4)) << 8)
				| ( 0xff & hexBytesTOneByte(buf,6));
	}
	public static int hexBytesToInt(byte[] buf, int startIdx) {
		return    ((0xff & hexBytesTOneByte(buf,startIdx  )) << 24)
				| ((0xff & hexBytesTOneByte(buf,startIdx+2)) << 16)
				| ((0xff & hexBytesTOneByte(buf,startIdx+4)) << 8)
				| ( 0xff & hexBytesTOneByte(buf,startIdx+6));
	}
	public static byte[] intToHexBytes(int i) {
		byte[] bytes = new byte[8];
		oneByteToHexBytes((byte) ((i >>> 24) & 0xff), bytes, 0);
		oneByteToHexBytes((byte) ((i >>> 16) & 0xff), bytes, 2);
		oneByteToHexBytes((byte) ((i >>> 8 ) & 0xff), bytes, 4);
		oneByteToHexBytes((byte) ( i         & 0xff), bytes, 6);
		return bytes;
	}
	public static byte[] intToHexBytes(int i, byte[] bytes) {
		oneByteToHexBytes((byte) ((i >>> 24) & 0xff), bytes, 0);
		oneByteToHexBytes((byte) ((i >>> 16) & 0xff), bytes, 2);
		oneByteToHexBytes((byte) ((i >>> 8 ) & 0xff), bytes, 4);
		oneByteToHexBytes((byte) ( i         & 0xff), bytes, 6);
		return bytes;
	}
	public static byte[] intToHexBytes(int i, byte[] bytes, int startIdx) {
		oneByteToHexBytes((byte) ((i >>> 24) & 0xff), bytes, startIdx  );
		oneByteToHexBytes((byte) ((i >>> 16) & 0xff), bytes, startIdx+2);
		oneByteToHexBytes((byte) ((i >>> 8 ) & 0xff), bytes, startIdx+4);
		oneByteToHexBytes((byte) ( i         & 0xff), bytes, startIdx+6);
		return bytes;
	}

	public static long hexBytesToLong(byte[] buf) {
		return    ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,0 ))) << 56)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,2 ))) << 48)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,4 ))) << 40)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,6 ))) << 32)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,8 ))) << 24)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,10))) << 16)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,12))) << 8 )
				| ( 0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,14))       );
	}
	public static long hexBytesToLong(byte[] buf, int startIdx) {
		return    ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx   ))) << 56)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+2 ))) << 48)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+4 ))) << 40)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+6 ))) << 32)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+8 ))) << 24)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+10))) << 16)
				| ((0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+12))) << 8 )
				| ( 0xffffffffffffffffl & (0xff&hexBytesTOneByte(buf,startIdx+14))       );
	}
	public static byte[] longToHexBytes(long i) {
		byte[] bytes = new byte[16];
		oneByteToHexBytes((byte) ((i >>> 56) & 0xff), bytes, 0);
		oneByteToHexBytes((byte) ((i >>> 48) & 0xff), bytes, 2);
		oneByteToHexBytes((byte) ((i >>> 40) & 0xff), bytes, 4);
		oneByteToHexBytes((byte) ((i >>> 32) & 0xff), bytes, 6);
		oneByteToHexBytes((byte) ((i >>> 24) & 0xff), bytes, 8);
		oneByteToHexBytes((byte) ((i >>> 16) & 0xff), bytes, 10);
		oneByteToHexBytes((byte) ((i >>> 8 ) & 0xff), bytes, 12);
		oneByteToHexBytes((byte) ( i         & 0xff), bytes, 14);
		return bytes;
	}
	public static byte[] longToHexBytes(long i, byte[] bytes) {
		oneByteToHexBytes((byte) ((i >>> 56) & 0xff), bytes, 0);
		oneByteToHexBytes((byte) ((i >>> 48) & 0xff), bytes, 2);
		oneByteToHexBytes((byte) ((i >>> 40) & 0xff), bytes, 4);
		oneByteToHexBytes((byte) ((i >>> 32) & 0xff), bytes, 6);
		oneByteToHexBytes((byte) ((i >>> 24) & 0xff), bytes, 8);
		oneByteToHexBytes((byte) ((i >>> 16) & 0xff), bytes, 10);
		oneByteToHexBytes((byte) ((i >>> 8 ) & 0xff), bytes, 12);
		oneByteToHexBytes((byte) ( i         & 0xff), bytes, 14);
		return bytes;
	}
	public static byte[] longToHexBytes(long i, byte[] bytes, int startIdx) {
		oneByteToHexBytes((byte) ((i >>> 56) & 0xff), bytes, startIdx);
		oneByteToHexBytes((byte) ((i >>> 48) & 0xff), bytes, startIdx+2);
		oneByteToHexBytes((byte) ((i >>> 40) & 0xff), bytes, startIdx+4);
		oneByteToHexBytes((byte) ((i >>> 32) & 0xff), bytes, startIdx+6);
		oneByteToHexBytes((byte) ((i >>> 24) & 0xff), bytes, startIdx+8);
		oneByteToHexBytes((byte) ((i >>> 16) & 0xff), bytes, startIdx+10);
		oneByteToHexBytes((byte) ((i >>> 8 ) & 0xff), bytes, startIdx+12);
		oneByteToHexBytes((byte) ( i         & 0xff), bytes, startIdx+14);
		return bytes;
	}



	public static byte[] mySimpleSerialization(Object o) throws Exception {
		if (null == o)
			return null;

		if (o instanceof Integer) {
			byte[] res = new byte[5];
			res[0] = (byte) MyObjectDataType.Type_int.getValue();
			intToBytes((Integer) o, res, 1);
			return res;
		} else if (o instanceof Long) {
			byte[] res = new byte[9];
			res[0] = (byte) MyObjectDataType.Type_long.getValue();
			longToBytes((Long) o, res, 1);
			return res;
		} else if (o instanceof Date) {
			long time = ((Date)o).getTime();
			byte[] res = new byte[9];
			res[0] = (byte) MyObjectDataType.Type_date.getValue();
			longToBytes(time, res, 1);
			return res;
		} else if (o instanceof Double) {
			long doubleLongBits = Double.doubleToLongBits((Double)o);
			byte[] res = new byte[9];
			res[0] = (byte) MyObjectDataType.Type_double.getValue();
			longToBytes(doubleLongBits, res, 1);
			return res;
		} else if (o instanceof String) {
			byte[] strChars = ((String)o).getBytes(charset);
			byte[] res = new byte[1+strChars.length];

			res[0] = (byte) MyObjectDataType.Type_string.getValue();
			for (int i=strChars.length-1; i>=0; --i)
				res[i+1] = strChars[i];
			return res;
		} else if (o instanceof Enum) {
			byte[] classNameBytes = o.getClass().getCanonicalName().getBytes(charset);
			byte[] enumValueNameBytes = ((Enum)o).name().getBytes(charset);
			byte[] res = new byte[1+1+classNameBytes.length+enumValueNameBytes.length];
			res[0] = (byte) MyObjectDataType.Type_enum.getValue();
			res[1] = (byte) (0xff & classNameBytes.length);
			for (int i=0; i<classNameBytes.length; ++i)
				res[i+2] = classNameBytes[i];
			for (int i=0; i<enumValueNameBytes.length; ++i)
				res[i+2+classNameBytes.length] = enumValueNameBytes[i];

			return res;
		} else {
			throw new Exception("invalid class " + o.getClass().getName());
		}
	}
	public static Object mySimpleDeserialization(byte[] b) throws Exception {
		if (null == b)
			return null;

		switch (MyObjectDataType.valueOf((int)b[0])) {
			case Type_int:
				return bytesToInt(b, 1);
			case Type_long:
				return bytesToLong(b, 1);
			case Type_date:
				long time = bytesToLong(b, 1);
				return new Date(time);
			case Type_double:
				long data = bytesToLong(b, 1);
				return Double.longBitsToDouble(data);
			case Type_string:
				byte[] bytes = new byte[b.length-1];
				for (int i=b.length-1; i>=1; --i)
					bytes[i-1] = b[i];
				return bytesToStr(bytes);
			case Type_enum:
				int classNameBytesLen = (0xff & b[1]);
				byte[] classNameBytes = new byte[classNameBytesLen];
				byte[] enumValueNameBytes = new byte[b.length-2-classNameBytesLen];

				for (int i=0; i<classNameBytesLen; ++i)
					classNameBytes[i] = b[i+2];
				for (int i=0; i<enumValueNameBytes.length; ++i)
					enumValueNameBytes[i] = b[i+2+classNameBytesLen];

				String className = bytesToStr(classNameBytes);
				String enumValueName = bytesToStr(enumValueNameBytes);

				Class<Enum> enumClass = (Class<Enum>) Class.forName(className);
				for (Enum enumValue : enumClass.getEnumConstants()) {
					if (enumValue.name().equals(enumValueName))
						return enumValue;
				}
				throw new Exception("invalid enum className=" + className + ", enumValueName" + enumValueName);

			default:
				throw new Exception("invalid type = " + ((int)b[0]));
		}
	}


	public static int myByteCopy(byte[]dst, int dstIdx, byte[] src) {
		for (int i=0; i<src.length; ++i) {
			dst[dstIdx+i] = src[i];
		}
		return src.length;
	}
	private static final byte[] emptyByteArray = new byte[0];
	public static byte[] nullToEmptyByte(byte[] bytes) {
		if (null == bytes)
			return emptyByteArray;
		else
			return bytes;
	}
	public static byte[] getMySizedBytes(byte[] src, int srcIdx, int[] nextIdx) {
		int byteLen = (int) bytesToShort(src, srcIdx);
		byte[] bytes = new byte[byteLen];

		srcIdx += 2;
		for (int i=0; i<byteLen; ++i) {
			bytes[i] = src[srcIdx+i];
		}

		nextIdx[0] = srcIdx+byteLen;
		return bytes;
	}
	public static String mySizedBytesToStr(byte[] src, int srcIdx, int[] nextIdx) {
		byte[] bytes = getMySizedBytes(src, srcIdx, nextIdx);
		return bytesToStr(bytes);
	}



	public static void main(String[] argvs) throws Exception{
		/*byte[] bs;
		Object result;

		String str = "ceshi wm测试赛2";
		bs = strToBytes(str);
		System.out.println(bytesToStr(bs));


		long l = 0x000001f2fbff4fffl;
		bs = longToBytes(l);
		System.out.printf("%x\n", bytesToLong(bs));
		l = 0x00000af2fbff4fffl;
		bs = new byte[100];
		bs = longToBytes(l, bs);
		System.out.printf("%x\n", bytesToLong(bs));
		bs = longToBytes(l, bs, 31);
		System.out.printf("%x\n", bytesToLong(bs, 31));


		int i = 0x0a1fbf2f;
		bs = intToBytes(i);
		System.out.printf("%x\n", bytesToInt(bs));
		i = 0x01bfcf68;
		bs = new byte[100];
		bs = intToBytes(i, bs);
		System.out.printf("%x\n", bytesToInt(bs));
		bs = intToBytes(i, bs, 71);
		System.out.printf("%x\n", bytesToInt(bs, 71));

		bs = intToOneByte(0xef);
		System.out.printf("%x\n", oneByteToInt(bs));
		bs = intToOneByte(0x16);
		System.out.printf("%x\n", oneByteToInt(bs));


		short s = (short)0xbf2f;
		bs = shortToBytes(s);
		System.out.printf("%x\n", bytesToShort(bs));


		bs = mySimpleSerialization(0x1abcdef);
		result = mySimpleDeserialization(bs);
		System.out.println("result.class="+result.getClass().getName() + ", result="+result);
		System.out.printf("%x\n", (Integer)result);

		bs = mySimpleSerialization(0xabcd12345678l);
		result = mySimpleDeserialization(bs);
		System.out.println("result.class="+result.getClass().getName() + ", result="+result);
		System.out.printf("%x\n", (Long)result);

		bs = mySimpleSerialization(new Date(0xabcd12345678l));
		result = mySimpleDeserialization(bs);
		System.out.println("result.class="+result.getClass().getName() + ", result="+result);
		System.out.printf("%x\n", ((Date)result).getTime());

		bs = mySimpleSerialization(242975834.23903);
		result = mySimpleDeserialization(bs);
		System.out.println("result.class="+result.getClass().getName() + ", result="+result);

		bs = mySimpleSerialization("dg34df大概地方4&%#<>?$");
		result = mySimpleDeserialization(bs);
		System.out.println("result.class="+result.getClass().getName() + ", result="+result);

		bs = mySimpleSerialization(CommonResponseCode.databaseError);
		result = mySimpleDeserialization(bs);
		System.out.println("result.class="+result.getClass().getName() + ", result="+result);*/



		/*byte[] bs = new byte[120];

		oneByteToHexBytes((byte) 0XB5, bs, 13);
		System.out.printf("%X\n", hexBytesTOneByte(bs, 13));

		intToHexBytes(0X9ABCF73E, bs, 41);
		System.out.printf("%X\n", hexBytesToInt(bs, 41));

		longToHexBytes(0X56789ABCDEF01234L, bs, 7);
		System.out.printf("%X\n", hexBytesToLong(bs, 7));*/
	}
}
