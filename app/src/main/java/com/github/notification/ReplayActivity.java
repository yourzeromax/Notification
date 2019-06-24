package com.github.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ReplayActivity extends AppCompatActivity {

    TextView tvName, tvLabel;

    public static final int REPLY_INTENT_ID = 0;
    public static final int ARCHIVE_INTENT_ID = 1;


    public static final String REPLY_ACTION = "com.github.notification.ACTION_MESSAGE_REPLY";
    public static final String KEY_PRESSED_ACTION = "KEY_PRESSED_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        tvLabel = findViewById(R.id.tv_label);
        tvName = findViewById(R.id.tv_name);
        getData();
        getLabel();
        cancelNotification();
    }

    private void getData() {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(getIntent());
        if (remoteInput != null) {
            tvName.setText(remoteInput.getCharSequence(MainActivity.KEY_TEXT_REPLY));
        }
    }

    private void getLabel() {
        String label = getIntent().getStringExtra(KEY_PRESSED_ACTION);
        if (label != null) {
            tvLabel.setText(label);
        }
    }

    private void cancelNotification() {
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(MainActivity.TYPE_REPLY);
    }


}
