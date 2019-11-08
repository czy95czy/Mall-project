package com.miaoshaproject.miaosha.error;

public enum EmBusinessError implements CommonError {

    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),

     VALIDATION_ERROR(10002,"未知错误"),

    //20000开头的是表示用户相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),

    USER_LOGIN_FAIL(2002,"用户手机号或密码哺正确")
    ;

    private EmBusinessError(int errCode,String errMsg){


        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;
    @Override
    public int getErrCode() {

        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
