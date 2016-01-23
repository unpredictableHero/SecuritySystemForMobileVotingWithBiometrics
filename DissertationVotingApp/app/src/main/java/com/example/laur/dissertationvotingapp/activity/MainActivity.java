package com.example.laur.dissertationvotingapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.laur.dissertationvotingapp.R;
import com.example.laur.dissertationvotingapp.application.VotingState;

import org.apache.http.message.BasicLineFormatter;


public class MainActivity extends Activity {

    TextView finalResult, bottomText;
    Button reg, lost;
    ImageButton vote;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MainActivity.context = getApplicationContext();

        reg = (Button) findViewById(R.id.reg_btn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg.getText().toString().contains("Register")) {
                    Intent intent = new Intent();
                    intent.setClass(context , RegisterActivity.class);
                    startActivity(intent);
                } else {

                }


            }
        });
        final VotingState state = ((VotingState) getApplicationContext());

        vote = (ImageButton) findViewById(R.id.vote_btn);
        bottomText = (TextView) findViewById(R.id.mainTextViewBottom);
        vote.setOnClickListener(new View.OnClickListener() {

            @Override
                public void onClick(View v) {
                String stateStr = state.getVotingState();
                if("0".equals(stateStr)) {
                    bottomText.setText("Your are not able to vote! The verification process for your identity failed or you already voted once");
                } else {
                    Intent intent = new Intent();
                    intent.setClass(context, VoteActivity.class);
                    startActivity(intent);
                }
            }
        });

        lost = (Button) findViewById(R.id.lostMain);
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lost.getText().toString().contains("Lost")) {
                    Intent intent = new Intent();
                    intent.setClass(context , LostPhoneActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        });
        /*un = (EditText) findViewById(R.id.edit_username);
        pw = (EditText) findViewById(R.id.edit_pass);
        ok = (Button) findViewById(R.id.button);
        finalResult = (TextView) findViewById(R.id.error);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Runner runner = new Runner();
                String userName = un.getText().toString();
                String password = pw.getText().toString();
                asyncTask = runner.execute(userName, password);
                try {
                    String asyncResultText = asyncTask.get();
                    response = asyncResultText.trim();
                } catch (InterruptedException e1) {
                    response = e1.getMessage();
                } catch (ExecutionException e1) {
                    response = e1.getMessage();
                } catch (Exception e1) {
                    response = e1.getMessage();
                }
                finalResult.setText(response);
            }
        });*/

       // addListenerOnVoteButton();
    }

 /*   public void addListenerOnVoteButton() {

        image = (ImageView) findViewById(R.id.imageView1);

        button = (Button) findViewById(R.id.btnChangeImage);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                image.setImageResource(R.drawable.android3d);
            }

        });

    }
*/
    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
