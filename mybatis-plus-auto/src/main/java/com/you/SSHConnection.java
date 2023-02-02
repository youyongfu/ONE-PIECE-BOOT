package com.you;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author yyf
 * @version 1.0
 * @date 2023/1/31
 */
public class SSHConnection {

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
    int remote_port = 3306;
    // 本地的端口
    int local_port = 3307;

    Session session = null;

    /**
     *    建立SSH连接
     */
    public void SSHConnection() throws Exception{
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(local_port, remote_host, remote_port);
        } catch (Exception e) {
        }
    }

    /**
     *    断开SSH连接
     */
    public void closeSSH () throws Exception
    {
        this.session.disconnect();
    }
}
