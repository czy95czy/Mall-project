package com.miaoshaproject.miaosha.error;

/**
 * @author cuizhiyuan
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);


}
