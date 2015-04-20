package com.hz.howpeople;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by admin on 15-4-16.
 */
public class registerActivity extends Activity {

    private Button mRegister;
    private Button mBack;
    private TextView mAgreementTv;
    private WebView mAgreementWv;
    private LinearLayout mLL;

    private View.OnClickListener onClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.register);

        initClick();

        mLL = (LinearLayout) findViewById(R.id.nRegisterLL);
        mLL.setOnClickListener(onClickListener);

        mRegister = (Button)findViewById(R.id.nRegister);
        mBack = (Button)findViewById(R.id.nBack);
        mAgreementTv = (TextView)findViewById(R.id.nAgreementTv);

        mRegister.setOnClickListener(onClickListener);
        mBack.setOnClickListener(onClickListener);
        mAgreementTv.setOnClickListener(onClickListener);

        mAgreementWv = (WebView) findViewById(R.id.nAgreementWv);
        mAgreementWv.getSettings().setJavaScriptEnabled(true);

    }

    private void initClick(){

        if(onClickListener == null){
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){

                        case R.id.nAgreementTv:{
                            mAgreementWv.loadUrl("file:///android_asset/www/agreement.html");

                            mAgreementWv.setVisibility(View.VISIBLE);
                            break;
                        }
                        case R.id.nBack:{
                            Intent resultData = new Intent();
                            resultData.putExtra("register", "back");
                            setResult(Activity.RESULT_OK, resultData);
                            finish();

                            break;
                        }
                        case R.id.nRegister:{
                            Intent resultData = new Intent();
                            resultData.putExtra("register", "confirm");
                            setResult(Activity.RESULT_OK, resultData);
                            finish();

                            break;
                        }
                        default:{
                            mAgreementWv.setVisibility(View.INVISIBLE);
                            break;
                        }
                    }
                }
            };
        }

    }
}
