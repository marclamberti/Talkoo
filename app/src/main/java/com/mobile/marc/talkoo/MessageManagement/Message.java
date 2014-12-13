//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private int message_type_;
    private String message_text_;
    private String login_;
    private int chat_id_;
    private InetAddress sender_address_;
    private boolean is_owner_;

    /**
     *  The different message types.
     */
    public static final int MESSAGE_TEXT = 1;
    public static final int MESSAGE_IMAGE = 2;
    public static final int MESSAGE_VIDEO = 4;
    public static final int MESSAGE_AUDIO = 8;
    public static final int MESSAGE_FILE = 16;
    public static final int MESSAGE_INVITATION = 32;

    /**
     *  These field are used is the message contains multimedia content.
     */
    private String file_name_;
    private long file_length_;
    private String file_path_;
    private byte[] data_array_;
//    private boolean is_mine_;

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
    public int getMessageType() { return message_type_; }
    public void setMessageType(int message_type) { this.message_type_ = message_type; }
    public String getMessageText() { return message_text_; }
    public void setMessageText(String message_text) { this.message_text_ = message_text; }
    public String getRoomLogin() { return login_; }
    public void setRoomLogin(String login) { this.login_ = login; }
    public int getChatId() { return chat_id_; }
    public void setChatId(int chat_id) { this.chat_id_ = chat_id; }
    public InetAddress getSenderAddress() { return sender_address_; }
    public void setSenderAddress(InetAddress senderAddress) { this.sender_address_ = senderAddress; }
    public byte[] getDataArray() { return data_array_; }
    public void setDataArray(byte[] byteArray) { this.data_array_ = byteArray; }
    public String getFileName() { return file_name_; }
    public void setFileName(String fileName) { this.file_name_ = fileName; }
    public long getFileLength() { return file_length_; }
    public void setFileLength(long fileLength) { this.file_length_ = fileLength; }
    public String getFilePath() { return file_path_; }
    public void setFilePath(String filePath) { this.file_path_ = filePath; }
    public boolean isOwner() { return is_owner_; }
    public void setOwner(boolean isOwner) { this.is_owner_ = isOwner; }


//    public Bitmap byteArrayToBitmap(byte[] b) {
//        Log.v(TAG, "Convert byte array to image (bitmap)");
//        return BitmapFactory.decodeByteArray(b, 0, b.length);
//    }

    /**
     *  Put the content of bytes_array_ to a file.
     */
    public void saveByteArrayToFile(Context context) {
        setFilePath(context);
        createFileAndWriteByteArray();
    }

    /**
     *  Create the file path using the environment directory and the file name.
     */
    public void setFilePath(Context context) {
        switch(message_type_){
            case Message.MESSAGE_FILE:
                file_path_ = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath() + "/" + file_name_;
                break;
            case Message.MESSAGE_AUDIO:
                file_path_ = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                        .getAbsolutePath() + "/" + file_name_;
                break;
            case Message.MESSAGE_VIDEO:
                file_path_ = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                        .getAbsolutePath() + "/" + file_name_;
                break;
        }
    }

    /**
     *  Creates a file and fills it with the content of data_array_.
     */
    public void createFileAndWriteByteArray() {
        File file = new File(file_path_);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());

            fos.write(data_array_);
            fos.close();
            Log.v(TAG, "Byte array written in file.");
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot write byte array to file.");
        }
    }
}
