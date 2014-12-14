//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.R;

import android.widget.TextView;

import java.util.List;

/**
 * Created by Marc on 13/12/14.
 */
public class RoomAdapter extends BaseAdapter {

    // List of messages
    private List<Message>   messages_;

    // Context of the application
    private Context         context_;

    public RoomAdapter(List<Message> messages, Context context) {
        messages_ = messages;
        context_ = context;
    }

    @Override
    public int getCount() {
        return messages_.size();
    }

    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {
        View    view = convert_view;
        // view is the view which is reused after scrolling when a row is no longer visible
        // if the view is null we have to create one to avoid of creating new view each
        // time that a row disappear
        if (view == null) {
            view = LayoutInflater.from(context_).inflate(R.layout.item_message, parent, false);
        }

        // Update the row
        Message message = (Message)getItem(position);
        if (message != null) {
            TextView    room_login = (TextView)view.findViewById(R.id.room_login);
            TextView    room_message = (TextView)view.findViewById(R.id.room_message_text);

            room_login.setText(message.getRoomLogin());
            room_message.setText(message.getMessageText());
        }
        return view;
    }

    @Override
    public long getItemId(int pos) {
        // Temporary
        return pos;
    }

    @Override
    public Object getItem(int pos) {
        return messages_.get(pos);
    }
}
