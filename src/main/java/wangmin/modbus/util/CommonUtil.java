package wangmin.modbus.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class CommonUtil {
    public static List<File> getFiles(String path) {
        List<File> files = new ArrayList<File>();

        File d = new File(path);

        File list[] = d.listFiles();

        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                files.add(list[i]);
            }
        }

        return files;
    }

    public static Integer getInteger(Object i) {
        try {
            if (i == null || StringUtils.isEmpty(i.toString()))
                return null;
            return Integer.parseInt(String.valueOf(i));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDouble(Object i) {
        try {
            if (i == null || StringUtils.isEmpty(i.toString()))
                return null;
            return Double.parseDouble(String.valueOf(i));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getProps(String fileName, String key) {
        try {
            InputStream configFileStream = CommonUtil.class.getClassLoader().getResourceAsStream(fileName);
            if (null == configFileStream)
                throw new RuntimeException();

            Properties config = new Properties();
            config.load(configFileStream);
            return config.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNormalDateStr(String yyyyMMddHHmmss) {
        char[] cs = yyyyMMddHHmmss.toCharArray();
        StringBuilder sb = new StringBuilder(20);

        sb.append(cs[0]);
        sb.append(cs[1]);
        sb.append(cs[2]);
        sb.append(cs[3]);
        sb.append("-");
        sb.append(cs[4]);
        sb.append(cs[5]);
        sb.append("-");
        sb.append(cs[6]);
        sb.append(cs[7]);
        sb.append(" ");
        sb.append(cs[8]);
        sb.append(cs[9]);
        sb.append(":");
        sb.append(cs[10]);
        sb.append(cs[11]);
        sb.append(":");
        sb.append(cs[12]);
        sb.append(cs[13]);

        return sb.toString();
    }

}
