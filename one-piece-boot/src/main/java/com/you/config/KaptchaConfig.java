package com.you.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 生成验证码 配置类
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */
@Configuration
public class KaptchaConfig {

    @Bean
    DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");     //图片边框
        properties.put("kaptcha.textproducer.font.color", "black");    //字体颜色
        properties.put("kaptcha.textproducer.char.space", "4");   //文字间隔
        properties.put("kaptcha.image.height", "40");     //图片高度
        properties.put("kaptcha.image.width", "120");     //图片宽度
        properties.put("kaptcha.textproducer.font.size", "30");   //字体大小

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
