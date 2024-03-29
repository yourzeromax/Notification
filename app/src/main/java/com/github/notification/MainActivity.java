package com.github.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_PROGRESS = 2;
    public static final int TYPE_BIG_TEXT = 3;
    public static final int TYPE_INBOX = 4;
    public static final int TYPE_BIG_PICTURE = 5;
    public static final int TYPE_CUSTOM = 6;
    public static final int TYPE_HANGUP = 7;
    public static final int TYPE_REPLY = 8;

    public static final String KEY_TEXT_REPLY = "KEY_TEXT_REPLY";

    private Button btnSimple;
    private Button btnProgress;
    private Button btnBigText;
    private Button btnInBox;
    private Button btnBigPicture;
    private Button btnCustomView;
    private Button btnHangUp;
    private Button btnReply;

    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSimple = (Button) findViewById(R.id.simple_notify);
        btnSimple.setOnClickListener(this);

        btnProgress = (Button) findViewById(R.id.progress);
        btnProgress.setOnClickListener(this);

        btnBigText = (Button) findViewById(R.id.big_text);
        btnBigText.setOnClickListener(this);

        btnInBox = (Button) findViewById(R.id.inbox);
        btnInBox.setOnClickListener(this);

        btnBigPicture = (Button) findViewById(R.id.big_picture);
        btnBigPicture.setOnClickListener(this);

        btnCustomView = (Button) findViewById(R.id.custom);
        btnCustomView.setOnClickListener(this);

        btnHangUp = (Button) findViewById(R.id.hangup);
        btnHangUp.setOnClickListener(this);

        btnReply = (Button) findViewById(R.id.replay);
        btnReply.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btnSimple) {
            simpleNotify();
        } else if (v == btnProgress) {
            progressNotify();
        } else if (v == btnBigText) {
            bigTextNotify();
        } else if (v == btnInBox) {
            inboxNotify();
        } else if (v == btnBigPicture) {
            bigPictureNotify();
        } else if (v == btnHangUp) {
            hangUpNotify();
        } else if (v == btnCustomView) {
            customNotify();
        } else if (v == btnReply) {
            replyNotify();
        }
    }

    private void simpleNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //todo Android 8.0以上可以用以下直接制定好channelId
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel_01");
        //Ticker是状态栏显示的提示
        builder.setTicker("简单Notification");
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle("标题");
        //第二行内容 通常是通知正文
        builder.setContentText("通知内容");
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        builder.setSubText("这里显示的是通知第三行内容！");
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
        builder.setNumber(2);
        //设置小图标颜色，api>=21,6.0以上也会改变字体颜色
        builder.setColor(Color.RED);
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //显示右上角的时间显示
        builder.setShowWhen(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //下拉显示的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        //点击跳转的intent
        builder.setContentIntent(pIntent);
        //通知默认的声音 震动 呼吸灯
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_01");
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel("channel_01", "helo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
            manager.notify(TYPE_NORMAL, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_NORMAL, notification);
        }
    }

    private void progressNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(true);
        //禁止滑动删除
        builder.setOngoing(true);
        //取消右上角的时间显示
        builder.setShowWhen(false);
        builder.setContentTitle("下载中...7%");
        builder.setProgress(100, 7, false);
        //builder.setContentInfo(progress+"%");
        builder.setOngoing(true);
        builder.setShowWhen(false);
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("command", 1);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        //点击跳转的intent
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_02");
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel("channel_02", "helo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
            manager.notify(TYPE_PROGRESS, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_PROGRESS, notification);
        }
    }

    private void bigTextNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BigTextStyle");
        builder.setContentText("BigTextStyle演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
        android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
        style.bigText("这里是点击通知后要显示的正文，可以换行可以显示很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        style.setBigContentTitle("点击后的标题");
        //SummaryText没什么用 可以不设置
        style.setSummaryText("末尾只一行的文字内容");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.addAction(R.drawable.ic_pawprint, "action", pIntent);
        builder.addAction(R.drawable.ic_pawprint, "action2", pIntent);

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_03");
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel("channel_03", "helo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
            manager.notify(TYPE_BIG_TEXT, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_BIG_TEXT, notification);
        }

    }

    private void inboxNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app.NotificationCompat.InboxStyle();
        style.setBigContentTitle("BigContentTitle")
                .addLine("第一行，第一行，第一行，第一行，第一行，第一行，第一行")
                .addLine("第二行")
                .addLine("第三行")
                .addLine("第四行")
                .addLine("第五行")
                .setSummaryText("SummaryText");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_04");
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel("channel_04", "helo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
            manager.notify(TYPE_INBOX, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_INBOX, notification);
        }
    }

    private void bigPictureNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BigPictureStyle");
        builder.setContentText("BigPicture演示示例");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        //   builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
        android.support.v4.app.NotificationCompat.BigPictureStyle style = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        style.bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.img_power_popup_notice));
        style.bigLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.img_power_popup_notice));
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        //设置点击大图后跳转
        builder.setContentIntent(pIntent);
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_05");
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel("channel_05", "helo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
            manager.notify(TYPE_BIG_PICTURE, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_BIG_PICTURE, notification);
        }
    }

    private void customNotify() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);
        expandedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));
        expandedView.setTextViewText(R.id.notification_message, "yyyyyyzzzzzzzmmmmmmmm");

        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.view_collapsed_notification);
        collapsedView.setTextViewText(R.id.timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));

        // adding action to left button
        Intent leftIntent = new Intent(this, NotificationIntentService.class);
        leftIntent.setAction("left");
        expandedView.setOnClickPendingIntent(R.id.left_button, PendingIntent.getService(this, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        // adding action to right button
        Intent rightIntent = new Intent(this, NotificationIntentService.class);
        rightIntent.setAction("right");
        expandedView.setOnClickPendingIntent(R.id.right_button, PendingIntent.getService(this, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT));


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // these are the three things a NotificationCompat.Builder object requires at a minimum
                .setSmallIcon(R.drawable.ic_pawprint)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(CONTENT_TEXT)
                // notification will be dismissed when tapped
                .setAutoCancel(true)
                // tapping notification will open MainActivity
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                // setting the custom collapsed and expanded views
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView);
        // setting style to DecoratedCustomViewStyle() is necessary for custom views to display
        //如果要完全自定义，就别加Style
        //.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_01");
            builder.setGroup("2");
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel("channel_01", "helo", NotificationManager.IMPORTANCE_HIGH);
            //  manager.createNotificationChannel(channel);
            manager.notify(TYPE_CUSTOM, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_CUSTOM, notification);
        }

    }


    private void hangUpNotify() {
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("横幅通知");
        builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 1, intent, 0);
        builder.setContentIntent(pIntent);
        //这句是重点
        builder.setFullScreenIntent(pIntent, true);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        manager.notify(TYPE_HANGUP, notification);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manager.cancel(TYPE_HANGUP);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                builder.setContentTitle("横幅通知");
                builder.setContentText("请在设置通知管理中开启消息横幅提醒权限");
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact));
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);
                builder.setContentIntent(pIntent);
                builder.setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= 26) {
                    builder.setChannelId("channel_07");
                    Notification notification = builder.build();
                    NotificationChannel channel = new NotificationChannel("channel_07", "helo", NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    manager.notify(TYPE_HANGUP, notification);
                } else {
                    Notification notification = builder.build();
                    manager.notify(TYPE_HANGUP, notification);
                }
            }
        }, 2000);
    }


    public static final String LABEL_REPLY = "Reply";
    public static final String LABEL_ARCHIVE = "Archive";

    private void replyNotify() {
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("Reply")
                .build();

        PendingIntent replyIntent = PendingIntent.getActivity(this,
                ReplayActivity.REPLY_INTENT_ID,
                getMessageReplyIntent(LABEL_REPLY),
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent archiveIntent = PendingIntent.getActivity(this,
                ReplayActivity.ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, replyIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Remote input")
                .setContentText("Try typing some text!")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.contact))
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAutoCancel(true)
                .addAction(replyAction)
                .addAction(archiveAction);

        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId("channel_08");
            Notification notification = builder.build();
            NotificationCompat.MessagingStyle styel = NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(notification);
            NotificationChannel channel = new NotificationChannel("channel_08", "helo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
            manager.notify(TYPE_REPLY, notification);
        } else {
            Notification notification = builder.build();
            manager.notify(TYPE_REPLY, notification);
        }
    }

    private Intent getMessageReplyIntent(String label) {
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(ReplayActivity.REPLY_ACTION)
                .putExtra(ReplayActivity.KEY_PRESSED_ACTION, label);
    }


}
