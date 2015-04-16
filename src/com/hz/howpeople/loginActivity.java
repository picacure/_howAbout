package com.hz.howpeople;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by admin on 15-4-16.
 */
public class loginActivity extends Activity {

    private Button mLogin;
    private Button mBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.login);

        mLogin = (Button)findViewById(R.id.nLogin);
        mBack = (Button)findViewById(R.id.nBack);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultData = new Intent();
                resultData.putExtra("login", "confirm");
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultData = new Intent();
                resultData.putExtra("login", "back");
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });
    }
}
