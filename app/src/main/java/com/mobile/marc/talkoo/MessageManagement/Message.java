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
    private static final String TAG = "Message";

    // The enumeration of the different message types.
    public static final int MESSAGE_TEXT = 1;
    public static final int MESSAGE_IMAGE = 2;
    public static final int MESSAGE_VIDEO = 4;
    public static final int MESSAGE_AUDIO = 8;
    public static final int MESSAGE_FILE = 16;
    public static final int MESSAGE_INVITATION = 32;

    // Basic header information.
    private int messageType;
    private String messageText;
    private String chatName;
    private int chatId;
    private InetAddress senderAddress;

    // These field are empty for text and invitation messages.
    private byte[] byteArray;
    private String fileName;
    private long fileSize;
    private String filePath;
    private boolean isMine;

    //Getters and Setters
    public int getMessageType() { return messageType; }
    public void setMessageType(int messageType) { this.messageType = messageType; }
    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }
    public int getChatId() { return chatId; }
    public void setChatId(int chatId) { this.chatId = chatId; }
    public byte[] getByteArray() { return byteArray; }
    public void setByteArray(byte[] byteArray) { this.byteArray = byteArray; }
    public InetAddress getSenderAddress() { return senderAddress; }
    public void setSenderAddress(InetAddress senderAddress) { this.senderAddress = senderAddress; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public boolean isMine() { return isMine; }
    public void setMine(boolean isMine) { this.isMine = isMine; }

    public Message(int type, String text, InetAddress sender, String name, int chat_id){
        messageType = type;
        messageText = text;
        senderAddress = sender;
        chatName = name;
        chatId = chat_id;
    }

    public Bitmap byteArrayToBitmap(byte[] b){
        Log.v(TAG, "Convert byte array to image (bitmap)");
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    // Create the filePath to .
    public void setFilePath(Context context) {
        switch(messageType){
            case Message.MESSAGE_AUDIO:
                filePath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath()+"/"+fileName;
                break;
            case Message.MESSAGE_VIDEO:
                filePath = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath()+"/"+fileName;
                break;
            case Message.MESSAGE_FILE:
                filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+fileName;
                break;
        }
    }

    public void createFileAndWriteBytes() {
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(file.getPath());

            fos.write(byteArray);
            fos.close();
            Log.v(TAG, "Write byte array to file DONE !");
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Write byte array to file FAILED !");
        }
    }

    public void saveByteArrayToFile(Context context){
//        Log.v(TAG, "Save byte array to file");
        setFilePath(context);
        createFileAndWriteBytes();
//        switch(mType){
//            case Message.MESSAGE_AUDIO:
//                filePath = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath()+"/"+fileName;
//                break;
//            case Message.MESSAGE_VIDEO:
//                filePath = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath()+"/"+fileName;
//                break;
//            case Message.MESSAGE_FILE:
//                filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+fileName;
//                break;
 //       }

    }
}
