package com.example.jwmoon.sampleactivitywithnativemethod;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SampleActivity extends Activity {

    static {
        System.loadLibrary("sample");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        setupview();

    }

    // sample  라이브러리의 네이티브 메소드
    public native String whatAmI();

    private void setupview(){
        findViewById(R.id.whatami).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String whatami = whatAmI();
                Toast.makeText(getBaseContext(), "CPU:" + whatami, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
