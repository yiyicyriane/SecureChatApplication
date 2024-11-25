/*
对于group，主要有两方面的功能：
1. 用户可以创建群组。用户创建群组的时候需要输入群组名称和群组描述（二者都可以后期由管理员更改）。
系统会自动生成一个唯一的UUID groupId, 便于系统和数据管理；
系统会自动生成一个唯一的groupCode(此code需要简洁，便于后期其他用户用该code搜索加入群组），并
生成一条包含管理员name, groupCode, 群组当前名称、群组当前的描述的信息（便于管理员创建群组之后手动把邀请信息复制给其他人，类似于腾讯会议复制邀请信息）。
在群组创建完成后任何时候都可以有新的人加入，
管理员有权限让制定的群成员退出。
2. 用户可以加入或退出群组。
用户可以通过搜索groupId加入群组。
用户也可以退出群组。
 */
package com.chat.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    //fields
    private final String groupId; // 需要系统在创建群组时自动生成！！！unique, system generates, used for system management
    private final String groupCode; // 需要系统在创建群组时自动生成，比id更简洁但唯一，用于加入群组！！！system automatically create unique simple code, used for other users to search and join the group.
    private String groupName; // founder of the group can change it
    private String groupDescription; // description of what the group for, founder of the group can change it
    private final String groupAdmin; // userId of the group founder
    private List<String> groupMembers; //list of current group members

    //constructor
    public Group(String groupId, String groupCode, String groupName, String groupDescription, String groupAdmin, List<String> groupMembers) {
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupAdmin = groupAdmin;
        this.groupMembers = groupMembers;
    }

    //getter and setter
    //groupId
    public String getGroupId(){
        return groupId;
    }

    //groupCode
    public String getGroupCode(){
        return groupCode;
    }

    //groupName
    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    //groupDescription
    public String getGroupDescription(){
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    //groupAdmin
    public String getGroupAdmin(){
        return groupAdmin;
    }

    //groupMembers
    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
