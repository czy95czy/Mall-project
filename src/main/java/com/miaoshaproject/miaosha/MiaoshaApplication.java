package com.miaoshaproject.miaosha;

import com.miaoshaproject.miaosha.dao.UserDOMapper;
import com.miaoshaproject.miaosha.dataObject.UserDO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 1.引入@EnableAutoConfiguration ：Spring Boot 会帮助我们启动一个TomCat，并且加载默认的配置
 * 2.声明RestController+RequestMapping注解：实现简单的SpringMVC之前需要配置servlet和web.xml
 * 3.包扫描scanBasePackages = {"com.miaoshaproject"}
 */
@SpringBootApplication(scanBasePackages = {"com.miaoshaproject"})
@RestController
@MapperScan("com.miaoshaproject.miaosha.dao")
public class MiaoshaApplication {

    @Resource
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String home(){
       UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO == null) {
            return "用户对象不存在";
        }else {
            return userDO.getName();
        }
     }
    public static void main(String[] args) {
        SpringApplication.run(MiaoshaApplication.class, args);
    }

}
