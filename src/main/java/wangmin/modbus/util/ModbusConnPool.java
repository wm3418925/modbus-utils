package wangmin.modbus.util;

import com.google.common.collect.Lists;
import net.wimpi.modbus.net.TCPMasterConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by wm on 2017/1/3.
 */
public class ModbusConnPool {
    private static final Logger logger = LoggerFactory.getLogger(ModbusConnPool.class);

    private volatile boolean flag = true;

    private final String ip;
    public String getIp() {return ip;}
    private final int port;
    public int getPort() {return port;}

    private final List<TCPMasterConnection> availConns = Lists.newArrayList();
    private final List<TCPMasterConnection> usingConns = Lists.newArrayList();

    private TCPMasterConnection connOneWithRetry() throws Exception {
        int retry = 0;
        while (true) {
            try {
                return ModbusUtil.connectionModbus(ip, port);
            } catch (Exception e) {
                logger.warn("", e);
                ++retry;
                if (retry >= 3) {
                    throw e;
                }
            }
        }
    }

    public ModbusConnPool(String ip, int port, int initialConnectionCount) throws Exception {
        this.ip = ip;
        this.port = port;

        while (availConns.size() < initialConnectionCount) {
            TCPMasterConnection conn;
            try {
                conn = connOneWithRetry();
            } catch (Exception e) {
                if (!availConns.isEmpty()) {
                    logger.warn("", e);
                    return;
                } else {
                    throw e;
                }
            }

            availConns.add(conn);
        }
    }


    /**
     * 检查连接池中的可用连接的连接状态
     * */
    public synchronized void checkConnStatus() {
        if (!flag) return;

        if (!availConns.isEmpty()) {
            for (int i = availConns.size()-1; i >= 0; --i) {
                TCPMasterConnection conn = availConns.get(i);
                if (!conn.isConnected()) {
                    availConns.remove(i);
                }
            }
        }
    }

    /**
     * 从连接池获取一个连接
     * */
    public synchronized TCPMasterConnection getOneConn() {
        if (!flag) return null;

        if (!availConns.isEmpty()) {
            int lastIdx = availConns.size()-1;
            TCPMasterConnection conn = availConns.get(lastIdx);
            availConns.remove(lastIdx);

            usingConns.add(conn);

            return conn;
        } else {
            try {
                TCPMasterConnection conn = connOneWithRetry();
                usingConns.add(conn);

                return conn;
            } catch (Exception e) {
                logger.warn("", e);
                return null;
            }
        }
    }

    /**
     * 归还连接到连接池
     * */
    public synchronized void returnOneConn(TCPMasterConnection conn) {
        availConns.add(conn);

        usingConns.remove(conn);
    }

    /**
     * 关闭连接池所有连接
     * */
    public void closeAllConn() {
        flag = false;

        // 先关闭可用连接
        synchronized (this) {
            for (int i = 0; i < availConns.size(); ++i) {
                availConns.get(i).close();
            }
            availConns.clear();

            if (usingConns.isEmpty())
                return;
        }

        // 等待3秒
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            logger.warn("", e);
        }

        // 强制关闭所有连接
        synchronized (this) {
            for (int i = 0; i < availConns.size(); ++i) {
                availConns.get(i).close();
            }
            for (int i = 0; i < usingConns.size(); ++i) {
                usingConns.get(i).close();
            }
        }
    }
}
