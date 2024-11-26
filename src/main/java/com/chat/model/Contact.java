/*
contact界面会显示用户的userid, name, profilepicture，phonenumber和在线状态
userid, name, profilepicture，phonenumber只能查看，不能修改。
status由系统自动检测联系人的在线状态（online or offline)自动实时更改
 */
package com.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor              // a constructor with all args
public class Contact{
    //field
    private final String userId; // use final as in contact, user can only check others' information
    private final String name;
    private final String profilePicture;
}