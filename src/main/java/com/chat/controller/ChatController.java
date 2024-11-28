/*
1.button跳转，跳转到chats, contacts, settings界面。
2.和服务器获取这个人当前的所有聊天室，加载到chatlistview界面上。
要具备更新功能，如我收到了新的消息（之前没有聊天过的人的消息）要在窗口中加入一个新的对话框。
如果我收到了之前聊过天的人发送的新消息，要在chatlistview这个界面中和这个人的对话框中更新显示收到的最新的消息。
3. 界面跳转，点击聊天对话框之后会跳转到跟这个人的聊天窗口界面chatwindowview。
4. 和服务器获取这个chatroom的chatRoomName显示在chatwindowview的最上方，并且找到这个chatroom对应的头像显示在最上方。
5. 和服务器获取和这个人的聊天记录加载到chatwindowview的界面。如果我对方发送了新的消息，这个界面要更新，如果我发送了新的消息，我也要传回给服务器，然后界面更新。
6. 和服务器获取List<String> memberIdList, 点击member button之后会显示群成员列表。
7. 有一个删除聊天记录的功能，当这条聊天记录是自己的时候就可以删除。返回服务器更新
*/

package com.chat.controller;

import com.chat.model.ChatItem;
import com.chat.model.ChatItemList;
import com.chat.model.ChatWindow;
import com.chat.view.chat.ChatWindowView;
import com.chat.view.contacts.ContactListView;
import com.chat.view.settings.ProfileSettingsView;
import javafx.stage.Stage;

public class ChatController {

    private ChatItemList chatItemList; // 存储聊天项列表

    public ChatController() {
        // 初始化聊天项列表
        this.chatItemList = new ChatItemList();
    }

    /**
     * 获取聊天项列表
     *
     * @return 返回当前的聊天窗口列表
     */
    public ChatItemList getChatItemList() {
        return chatItemList;
    }


    /**
     * 根据chatRoomid打开聊天窗口
     *
     * @param chatRoomId 聊天室的ID
     */
    public void openChatWindow(String chatRoomId) {
        // 根据 chatRoomId 查找对应的聊天内容
        System.out.println("Opening chat window for room: " + chatRoomId);

        // 打开新的聊天窗口
        new ChatWindowView(chatRoomId).start(new Stage()); //以新窗口的形式打开。
    }

    /**
     * 打开联系人视图
     *
     * @param stage 当前的舞台，用于加载联系人视图
     */
    public void openContactListView(Stage stage) {
        // 通过新的窗口打开联系人视图
        new ContactListView().start(stage);
    }

    /**
     * 打开设置视图
     *
     * @param stage 当前的舞台，用于加载设置视图
     */
    public void openSettingsView(Stage stage) {
        // 通过新的窗口打开设置视图
        new ProfileSettingsView().start(stage);
    }
}
