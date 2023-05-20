package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if (userMobile.contains(mobile)){
            throw new Exception("User already exists");
        } else {
            User user = new User(name, mobile);
            userMobile.add(mobile);
            return "SUCCESS";
        }
    }


    public Group createGroup(List<User> users) {
        int size = users.size();
        Group group;
        if (size == 2){
            String name = users.get(1).getName();
            group = new Group(name, 2);
        } else {
            this.customGroupCount++;
            String name = "Group " + this.customGroupCount;
            group = new Group(name, size);
        }
        groupUserMap.put(group, users);
        adminMap.put(group, users.get(0));
        groupMessageMap.put(group, new ArrayList<>());
        return group;
    }

    public int createMessage(String content) {
        this.messageId++;
        Message message = new Message(this.messageId, content);
        return this.messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if (!groupMessageMap.containsKey(group)){
            throw new Exception("Group does not exist");
        } else {
            List<User> users = groupUserMap.get(group);
            if (!users.contains(sender)){
                throw new Exception("You are not allowed to send message");
            }
        }
        List<Message> msg = groupMessageMap.get(group);
        msg.add(message);
        senderMap.put(message, sender);
        return msg.size();
    }

}
