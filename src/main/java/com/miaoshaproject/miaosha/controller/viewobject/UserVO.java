package com.miaoshaproject.miaosha.controller.viewobject;

public class UserVO {
    private Integer id;
    private String name;
    private Byte  gender;
    private Integer age;
    private String telphone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}
