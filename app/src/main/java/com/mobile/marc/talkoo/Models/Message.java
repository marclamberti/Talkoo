//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.InetAddress;

/**
 *  Message definition.
 */
public class Message implements Serializable {
    /**
     *  Used for debug logs.
     */
    private static final String TAG = "Message";

    /**
     *  Basic header information.
     */
    private int         message_type_;
    private String      message_text_;
    private String      login_;
    private int         chat_id_;
    private InetAddress sender_address_;
    private boolean     is_owner_;

    /**
     *  The different message types.
     */
    public static final int MESSAGE_TEXT = 1;
    public static final int MESSAGE_INVITATION = 2;

    /**
     *  Constructor.
     */
    public Message(int message_type, String message_text, InetAddress sender_address,
                   String group_name, int chat_id) {
        message_type_ = message_type;
        message_text_ = message_text;
        sender_address_ = sender_address;
        login_ = group_name;
        chat_id_ = chat_id;
    }

    /**
     *  Getters and Setters.
     */
    public int getMessageType() {
        return message_type_;
    }
    public void setMessageType(int message_type) {
        this.message_type_ = message_type;
    }
    public String getMessageText() {
        return message_text_;
    }
    public void setMessageText(String message_text) {
        this.message_text_ = message_text;
    }
    public String getRoomLogin() {
        return login_;
    }
    public void setRoomLogin(String login) {
        this.login_ = login;
    }
    public int getChatId() {
        return chat_id_;
    }
    public void setChatId(int chat_id) {
        this.chat_id_ = chat_id;
    }
    public InetAddress getSenderAddress() {
        return sender_address_;
    }
    public void setSenderAddress(InetAddress senderAddress) {
        this.sender_address_ = senderAddress;
    }
    public boolean isOwner() {
        return is_owner_;
    }
    public void setOwner(boolean isOwner) {
        this.is_owner_ = isOwner;
    }
}
