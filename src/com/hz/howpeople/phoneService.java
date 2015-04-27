package com.hz.howpeople;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by admin on 15-4-22.
 */
public class phoneService extends Service {
    private static String lastIncoming = "";
    private NotificationManager nm;
    private BroadcastReceiver phoneStateReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");


        if (phoneStateReceiver == null) {
            phoneStateReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(final Context context, Intent intent) {

                    String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    String msg = "友评提示：（";

                    final String oldNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);


                    //电话打出.
                    if(oldNumber != null){
                        msg += oldNumber + "):友评得分：8分";

                        lastIncoming = oldNumber;

                        final Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                        mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
                        mToast.show();


                        //notification.
                        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        //设置不同Id，同一个App可以显示多次.
                        nm.cancel(1);


                        //防止不同手机拨号后清除和设置被OS优化 而不显示 notification.
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                Calendar calendar = Calendar.getInstance();
                                Notification notification = new Notification(R.drawable.ic, "友评：点此匿名评价益之", calendar.getTimeInMillis());

                                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                                        new Intent(context, MyActivity.class), 0);

                                notification.setLatestEventInfo(context, "友评：点此匿名评价益之", lastIncoming, contentIntent);
                                notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

                                nm.notify(1, notification);
                            }
                        }, 1000);
                    }
                    // 打进
                    else {
                        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            msg += incomingNumber + "):友评得分：8分";

                            lastIncoming = incomingNumber;

                            final Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                            mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
                            mToast.show();

                        } else {
                            if (lastIncoming != "") {
                                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                                    nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                    //设置不同Id，同一个App可以显示多次.
                                    nm.cancel(1);


                                    //防止不同手机拨号后清除和设置被OS优化 而不显示 notification.
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {

                                            Calendar calendar = Calendar.getInstance();
                                            Notification notification = new Notification(R.drawable.ic, "友评：点此匿名评价益之", calendar.getTimeInMillis());

                                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                                                    new Intent(context, MyActivity.class), 0);

                                            notification.setLatestEventInfo(context, "友评：点此匿名评价益之", lastIncoming, contentIntent);
                                            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

                                            nm.notify(1, notification);
                                        }
                                    }, 1000);
                                }
                            }
                        }

                    }
                }
            };
        }

        registerReceiver(phoneStateReceiver, filter);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }

}
