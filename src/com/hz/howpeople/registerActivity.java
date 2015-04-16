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

/**
 * Created by admin on 15-4-16.
 */
public class registerActivity extends Activity {

    private Button mRegister;
    private Button mBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.register);

        mRegister = (Button)findViewById(R.id.nRegister);
        mBack = (Button)findViewById(R.id.nBack);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultData = new Intent();
                resultData.putExtra("register", "confirm");
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultData = new Intent();
                resultData.putExtra("register", "back");
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });

    }
}
