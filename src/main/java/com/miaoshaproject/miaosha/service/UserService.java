package com.miaoshaproject.miaosha.service;

import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.service.model.UserModel;

/**
 * 必须有model的概念
 */
public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel  getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
}
