//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement;

import android.os.AsyncTask;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.NavigatorActivity;

/**
 * Manages the notifications sent to users when he receives a message
 */
public class NotificationHandler extends AsyncTask<Void, Message, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    /**
     * Use buildNotification to create a notification
     * @param context
     * @param message
     */
    protected void Notify(Context context, Message message) {
        Notification message_notification = buildNotification(context, message);
        NotificationManager notification_manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences shared_preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        if (!shared_preferences.getBoolean("isForeground", false)){
            notification_manager.notify(0, message_notification);
        }
    }

    /**
     * Create a notification which will be sent to the notification service
     * and bring the app to the front
     * @param context
     * @param message
     * @return
     */
    protected Notification buildNotification(Context context, Message message) {
        Intent intent = new Intent(context, NavigatorActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pending_intent = PendingIntent.getActivity(context, 0, intent, 0);
        Uri notification_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(message.getRoomLogin())
                .setContentText(message.getMessageText())
                .setContentIntent(pending_intent)
                .setSound(notification_uri)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        return notification;
    }
}
