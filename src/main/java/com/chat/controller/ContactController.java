package com.chat.controller;

import com.chat.model.MembersInContactList;
import com.chat.model.ContactList;
import com.chat.service.AuthService;
import com.chat.util.CurrentUserContext;

import java.util.List;

public class ContactController {

    private final AuthService authService;

    public ContactController() throws Exception {
        this.authService = new AuthService(); // Utility class for HTTP requests
    }

    //判断当前用户
    public String currentUserId(){
        return CurrentUserContext.getInstance().getCurrentUser().getUserId();
    }

    // Method to add a contact
    public String addContact(String userId, String contactId) {
        try {
            // Send HTTP POST request to the backend API
            String response = authService.postNewFriendApplicationSenderIdSet(userId, contactId);
            // Backend response determines the result
            if (response.equals("Friend already exists.") || 
                response.equals("Invalid Friend ID.") || 
                response.equals("Friend application sent.")) {
                return response;
            } else if (response.equals("Friend application accepted.")) {
                return "success";
                // TODO: notice and update contact in view
            } else {
                return "Server error.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Server error.";
        }
    }

    // Method to join a group by searching chatRoomId
    public void joinGroupContact(String userId, String chatRoomId) {
    }

    //返回所有的list,这里不需要区分group还是personal
    public List<MembersInContactList> getContacts() {
    }

    //在个人联系人列表点击删除按钮，删除好友
    public void removeContact(String userId, String friendsUserId) {
    }


    //退群
    public void removeGroupContact(String userId, String chatRoomId) {
    }

    //根据用户id,用户写的群名和用户选择的好友列表创建群聊，返回创建的新群的chatroomid, 用于在view中直接跳转到这个聊天窗口。
    public String createNewGroup(String creatorId, String groupName, List<String> selectedMembers) {
    }
}
