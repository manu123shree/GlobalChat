package manu.globalchat;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            Log.d("messaging services", "notification: "+ notification.getBody());

            LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent(ChatActivity.action);
            intent.putExtra("msg", notification.getBody());
            mgr.sendBroadcast(intent);
        }
    }
}
