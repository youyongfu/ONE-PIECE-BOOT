package com.you.listener;

import com.you.utils.SSHConnection;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * SSH连接 监听类
 * @author yyf
 * @version 1.0
 * @date 2023/2/2
 */
@WebListener
@Component
public class ContextListener implements ServletContextListener {

    private SSHConnection conexionssh;
    public ContextListener() {
        super();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // 建立连接
        System.out.println("Context initialized ... !\n");
        try {
            conexionssh = new SSHConnection();
            conexionssh.SSHConnection();
            System.out.println("成功建立SSH连接！\n");
        } catch (Throwable e) {
            System.out.println("SSH连接失败！\n");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // 断开连接
        System.out.println("Context destroyed ... !\n");
        try {
            conexionssh.closeSSH();
            System.out.println("成功断开SSH连接!\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("断开SSH连接出错\n");
        }
    }
}
