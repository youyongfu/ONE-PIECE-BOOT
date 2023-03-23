package com.you.common;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 统一结果数据封装类
 * @author yyf
 * @version 1.0
 * @date 2023/2/2
 */
@Data
public class ResultBean implements Serializable {

    protected static final Logger logger = LoggerFactory.getLogger(ResultBean.class);

    private String code;

    private String msg;

    private Object data;

    public static ResultBean success(){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode("200");
        resultBean.setMsg("success");
        return resultBean;
    }

    public static ResultBean success(Object data){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode("200");
        resultBean.setMsg("success");
        resultBean.setData(data);
        return resultBean;
    }

    public static ResultBean fail(String msg){
        ResultBean result = new ResultBean();
        result.setCode("500");
        result.setMsg(msg);
        return result;
    }

    public static ResultBean fail(String msg,Object data){
        ResultBean result = new ResultBean();
        result.setCode("500");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static ResultBean fail(Exception e){
        ResultBean result = new ResultBean();
        result.setCode("500");
        StringBuffer sb = new StringBuffer(e.toString() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        int length = messages.length;
        for (int i = 0; i < length; i++) {
            sb.append("\t"+messages[i].toString()+"\n");
        }
        result.setMsg(e.getMessage());
        logger.info(sb.toString());
        return result;
    }

}
