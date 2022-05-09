package com.example.litteray.utilities;

import java.util.HashMap;

public class Constants {

//  THIS IS FOR USER INFORMATION

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "litterayPereference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "profilePic";
    public static final String KEY_PROFILE_TYPE = "profileType";

//    THIS IS FOR CHAT MESSAGE

    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_MESSAGE_IMAGE = "messageImage";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_PIC = "senderPic";
    public static final String KEY_RECEIVER_PIC = "receiverPic";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";

// THIS IS FOR PUSH NOTIFICATIONS

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static HashMap<String, String> remoteMsgHeaders = null;
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAANnE6xVE:APA91bE1-v1aD47nZr-IfMCUe8dPzltN12g66lz6-AYXLFKxVq1lGKWQTwkqvVoedunUUJFiOZoTK88MJ_N-Hgwv6GT8kZPcJMt95hD5SHVH2VrJLrorcnr4Yc2RvGIxUYvI2mRaw4zK"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }

//  THIS IS FOR LINK PAGE

    public static final String KEY_TWITTER = "twitter";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_OPENSEA = "opensea";
    public static final String KEY_INSTAGRAM = "instagram";

// THIS IS FOR ARU

    public static final String KEY_STRAIGHTEN_UP = "straightenUp";

}
