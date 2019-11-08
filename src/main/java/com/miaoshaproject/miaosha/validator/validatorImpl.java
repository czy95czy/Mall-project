package com.miaoshaproject.miaosha.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Component:代表是spring 的bean，在进行类扫描的时候会扫描到他
 */
@Component
public class validatorImpl implements InitializingBean {
    /**
     * 当Spring  Bean 初始化完成之后会回调 对应的validatorImpl 的 afterPropertiesSet 方式
     * Validator:是javax 定义的接口实现的Validator的工具
     */
    private Validator validator;

    //实现校验方法并返回校验结果
    public validationResult validate(Object bean){
         validationResult result = new validationResult();
         //validate：源码 入参对应要校验的bean，出参返回Set就是对应方法的ConstraintViolation
        //如果对应的bean内的参数规则有违背对应validation定义的，constraintViolationSet里面就会有值
         Set<ConstraintViolation<Object>> constraintViolationSet =  validator.validate(bean);
         if (constraintViolationSet.size()>0){
             //有错误
             result.setHasErrors(true);
             //遍历对应的constraintViolationSet，constraintViolationSet每一个元素的validation的errMsg存放了出错的信息
             constraintViolationSet.forEach(constraintViolation->{
                 String errMsg = constraintViolation.getMessage();
                 //获取哪一个字段发生的错误
                 String propertyName = constraintViolation.getPropertyPath().toString();
                 result.getErrorMsgMap().put(propertyName,errMsg);
             });
         }
         return result;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator通过工厂的初始化方式使其实例化，产生一个校验器 validator
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
