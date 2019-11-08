package com.miaoshaproject.miaosha.service;

import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.service.model.UserModel;

/**
 * 必须有model的概念
 */
public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel  getUserById(Integer id);

    /*
       telphone：用户注册的手机
       password：用户加密后的密码
     */
    void register(UserModel userModel) throws BusinessException;
    UserModel validataLogin(String telphone,String encrptPassword) throws BusinessException;
}
