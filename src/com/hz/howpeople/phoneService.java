package com.hz.howpeople;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by admin on 15-4-22.
 */
public class phoneService extends Service {
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onStart(Intent intent, int startid) {

        mIntent = intent;

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    class TeleListener extends PhoneStateListener {


        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);


            if (state == TelephonyManager.CALL_STATE_RINGING) {
                final Toast mToast = Toast.makeText(getApplicationContext(), "友评提示 :(" + incomingNumber + ")友评得分 8.0", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
                mToast.show();
            }
            else{
                if (state == TelephonyManager.CALL_STATE_RINGING)
                {
                    // DO SOME CODE HERE...
                }
                else{
                    if (state == TelephonyManager.CALL_STATE_IDLE)
                    {
//                        final Toast aToast = Toast.makeText(getApplicationContext(), "前往友评，对ta进行匿名评价", Toast.LENGTH_LONG);
//                        aToast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
//                        aToast.show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage("前往友评，对ta进行匿名评价?")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog ad = builder.create();
                        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        ad.setCanceledOnTouchOutside(false);
                        ad.show();

                    }
                }
            }

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                default:
                    break;
            }

        }
    }

}
