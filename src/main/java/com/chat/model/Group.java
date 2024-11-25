/*
对于group，主要有两方面的功能：
1. 用户可以创建群组。用户创建群组的时候需要输入群组名称。
用户输入groupId,强制以G开头，且需要是唯一的。
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
    private final String groupId; //
    private String groupName; // founder of the group can change it
    private final String groupAdmin; // userId of the group founder
    private List<String> groupMembers; //list of current group members

    //constructor
    public Group(String groupId, String groupName, String groupAdmin, List<String> groupMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAdmin = groupAdmin;
        this.groupMembers = groupMembers;
    }

    //getter and setter
    //groupId
    public String getGroupId(){
        return groupId;
    }


    //groupName
    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
