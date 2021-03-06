package com.miaoshaproject.miaosha.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.miaosha.controller.viewobject.UserVO;
import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.response.CommonReturnType;
import com.miaoshaproject.miaosha.service.UserService;
import com.miaoshaproject.miaosha.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.miaoshaproject.miaosha.controller.BaseController.CONTENT_TYPE_FORMED;


/**
 * @author cuizhiyuan
 * controller层：到用户viewObject之间的传递，保证了ui只使用到需要展示的字段即可
 * @CrossOrigin:加入这个注解可以让我们完成所有的spring boot对应返回web请求当中加入跨域
 */
@RestController("user")
@RequestMapping(value = "/user")
//Defult_Allow_Credentials = "true":需要配合前端设置xhrFields授信后使得session共享
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    //通过bean的方式注入进来，其实通过spring bean包装的HttpServletRequest本质是一个procise
    //内部拥有sereadlocal方式的map，让用户在每个线程中处理自己的线程request。
    @Resource
    private HttpServletRequest httpServletRequest;
    //用户登陆接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password ")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验，保证用户输入的手机号不能为空
        if (org.apache.commons.lang3.StringUtils.isEmpty(telphone) ||
                org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登陆服务，用来校验用户登陆是否合法
        UserModel userModel = userService.validataLogin(telphone,this.EncodeByMD5(password));
        //将登陆凭证（token）加入到用户登陆成功的session,如果用户登陆的会话标识中有IS_LOGIN，就证明登陆成功,如果用户登陆成功，将userModel放到session内，
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }




    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpCode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
//        为什么要用alibaba.druid中的equals呢？因为在内部为我们进行了null字符串进行判断，如果两个字符串都为null的话返回true，否则调用equals方法
        if (!StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender)));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMD5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64 = new BASE64Encoder();
        //加密字符串
        String newstr = base64.encode(md5 .digest(str.getBytes("UTF-8")));
        return newstr;
    }

    //用户获取otp短信接口,method必须映射到http post 请求才能生效
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telephone){
        //需要按照一定的规则生成Otp验证码
        Random random = new Random();
        //是对应的数字到最大的数字[0,9999）
        int randomInt = random.nextInt(9999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将otp验证码同用户的手机号关联,使用httpSession的方式绑定用户的手机号与OtpCode
        httpServletRequest.getSession().setAttribute(telephone,otpCode);

        //将otp验证码通过短信通道发送给用户,省略。（涉及到短信通道的流程，可以买第三方短信的通道，通过httpPost方式，模版的方式post到对应的手机号）
        System.out.println("telphone = "+telephone+"& optCode ="+otpCode);
        return CommonReturnType.create(null);
    }

    //接受入参RequestParam注解
    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        //调用Service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if (userModel == null){
            userModel.setEncrptPassword("231");
//            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转化为可供UI使用的viewObject
        UserVO userVO =  convertFromModel(userModel );

        //返回通用对象
        return CommonReturnType.create(userVO);
    }
    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
