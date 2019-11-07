package com.miaoshaproject.miaosha.serviceImpl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.miaoshaproject.miaosha.dao.UserDOMapper;
import com.miaoshaproject.miaosha.dao.UserPasswordDOMapper;
import com.miaoshaproject.miaosha.dataObject.UserDO;
import com.miaoshaproject.miaosha.dataObject.UserPasswordDO;
import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.service.UserService;
import com.miaoshaproject.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author cuizhiyuan
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private UserPasswordDOMapper userPasswordDOMapper;
    @Override
    public UserModel getUserById(Integer id) {
        //调用userDOMapper获取到对应的用户dataObject,UserDO绝对不能给前端
        UserDO userDO =  userDOMapper.selectByPrimaryKey(id);

        if (userDO == null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelphone())){

            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //实现 model--->dataObject方法
        UserDO userDO = convertFromModel(userModel);
        //这里为什么使用insertSelective而不使用insert，因为insertSelective，加入了判断，较为准确
        userDOMapper.insertSelective(userDO);

        UserPasswordDO userPasswordDO = converyPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }


    //密码
    private UserPasswordDO converyPasswordFromModel(UserModel userModel){
        if (userModel  == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }


    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO == null){
            return  null;
        }
        UserModel userModel = new UserModel();
        //将userdo 的属性copy到userModel内
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
