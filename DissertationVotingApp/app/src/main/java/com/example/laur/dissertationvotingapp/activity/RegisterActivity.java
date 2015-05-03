package com.example.laur.dissertationvotingapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.laur.dissertationvotingapp.R;

public class RegisterActivity extends Activity {

    Button ok;
    EditText cnp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ok = (Button) findViewById(R.id.reg_ok_btn);
        cnp = (EditText) findViewById(R.id.cnp_editText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnpStr = cnp.getText().toString();

            }
        });
    }
}
