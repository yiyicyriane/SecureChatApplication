/*
用户在注册时系统会自动生成一个唯一的id,用于后期系统和数据管理；
用户在注册时还需自己定义一个userid, 类似于wechat id,系统要检测保证唯一性，一旦确定无法再次更改。
除此之外还需要name,phone number, password, profile picture,但是这些注册之后都可以更改
 */
//删除id, phone number,
package com.chat.model;

public class User {
    //private final String id; // 服务层要用UUID设置id的唯一性，自动生成！！！system creates unique id and can not change after being created
    private final String userId; //user creates their userid when sign up, unique and cannot change after being created
    private String name;
    //private String phoneNumber;
    private String password;
    private String profilePicture;

    //constructor
    public User(String userId, String name, String password, String profilePicture){
        this.userId = userId; //系统应确保userid的唯一性，由用户在注册时自行设置！！！
        this.name = name;
        this.password = password;
        this.profilePicture = profilePicture; //系统应保证密码加密存储！！！
    }

    //getter and setter
    //userId
    public String getUserId(){
        return userId;
    }

    //name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //profilePicture
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
