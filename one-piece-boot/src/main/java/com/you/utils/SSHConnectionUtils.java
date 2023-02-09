package com.you.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

/**
 * SSH连接类
 * @author yyf
 * @version 1.0
 * @date 2023/1/31
 */
@Component
public class SSHConnectionUtils {

    // 服务器登录名
    String user = "root";
    // 登陆密码
    String password = "Yyfyp@@123";
    //服务器公网IP
    String host = "58.67.221.164";
    // 跳板机ssh开放的接口   默认端口 22
    int port = 22;
    // 要访问的mysql所在的host
    String remote_host = "localhost";
    // 服务器上数据库端口号
    int mysql_remote_port = 3306;
    // Mysql 本地的端口
    int mysql_local_port = 3307;
    // redis 本地的端口
    int redis_local_port = 6379;
    // 服务器上redis端口号
    int redis_remote_port = 6379;

    Session session = null;

    /**
     * 建立SSH连接
     * @throws Exception
     */
    public void SSHConnection() throws Exception{
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(mysql_local_port, remote_host, mysql_remote_port);
            session.setPortForwardingL(redis_local_port,remote_host,redis_remote_port);
            System.out.println("成功建立SSH连接！\n");
        } catch (Exception e) {
            System.out.println("SSH连接失败！\n");
            e.printStackTrace();
        }
    }

    /**
     * 断开SSH连接
     * @throws Exception
     */
    public void closeSSH () throws Exception
    {
        this.session.disconnect();
    }
}
