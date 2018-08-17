package manu.globalchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class ChatActivity extends AppCompatActivity {

    public static final String action = "manu.globalchat.MESSAGE_ACTION";

    private LocalBroadcastManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseMessaging.getInstance().subscribeToTopic("global")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed successfully";
                        if (!task.isSuccessful()) {
                            msg = "failed to subscribe";
                        }
                        Log.d("ChatActivity", msg);
                        Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        IntentFilter filter = new IntentFilter(action);
        mgr = LocalBroadcastManager.getInstance(this);
        mgr.registerReceiver(receiver, filter);

        sendMessage("Hello World !");
    }

    private static final String SENDER_ID = "579285400160";

    private void sendMessage(String msg) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(0))
                .addData("message", msg)
                .addData("action", action)
                .build());
    }

    private void setupReceiver() {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receiver", intent.getStringExtra("msg"));
        }
    };

    @Override
    protected void onDestroy() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("global");
        mgr.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
