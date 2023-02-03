package com.you.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.you.common.lang.ResultBean;
import com.you.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 权限控制层
 * @author yyf
 * @version 1.0
 * @date 2023/2/3
 */

@RestController
public class AuthController extends BaseController{

    @Autowired
    private Producer producer;

    /**
     * 获取验证码信息
     * @return
     */
    @GetMapping("/captcha")
    public ResultBean captcha() throws IOException {

        String key = UUID.randomUUID().toString();
        String code = producer.createText();            //生成验证码信息

        //生成验证码图片
        BufferedImage bufferedImage = producer.createImage(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"jpg",byteArrayOutputStream);

        //将图片转为Base64格式
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + base64Encoder.encode(byteArrayOutputStream.toByteArray());

//        redisUtil.hset(RedisConstant.CAPTCHA_KEY,key,code,120);                     //存储到Redis

        return ResultBean.success(MapUtil.builder().put("token",key).put("captchaImg",base64Img).build());
    }
}
