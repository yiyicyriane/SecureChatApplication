/*
contact界面会显示用户的userid, name, profilepicture，phonenumber和在线状态
userid, name, profilepicture，phonenumber只能查看，不能修改。
status由系统自动检测联系人的在线状态（online or offline)自动实时更改
 */
package com.chat.model;

public class Contact{
    //field
    private final String userId; // use final as in contact, user can only check others' information
    private final String name;
    private final String profilePicture;
    private final String phoneNumber;
    private String status; // 服务层需要实现动态更新状态，online or offline


    //constructor
    public Contact(String userId, String name, String profilePicture, String phoneNumber, String status){
        this.userId = userId;
        this.name = name;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    //Getter
    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getProfilePicture(){
        return profilePicture;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus(){
        return status;
    }

}