package com.hz.howpeople;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * Created by admin on 15-4-20.
 */
public class callInterceptor extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "友评提示：（";

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += incomingNumber + "):友评得分：8分";

            final Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
            mToast.show();

        } else {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            } else {
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("前往友评，对ta进行匿名评价?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //唤起友评.
//                            Intent i = new Intent();
//                            i.setClassName("com.hz.howpeople", "MyActivity");
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(i);
                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    ad.setCanceledOnTouchOutside(true);
                    ad.setCancelable(false);
                    ad.show();
                }
            }
        }
    }
}
