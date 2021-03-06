package com.ceyentra.smssender.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ceyentra.smssender.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String SMS_TEMPLATE = "%d text messages(SMS) will be sent to %s with the message: \"%s %s\"\nClick OK to confirm.";
    //    private String sp;
    private EditText txt_vot;
    private EditText txt_no;
    private EditText txt_destination;
    private EditText txt_keyword;
    private Button btn_send;
    private ImageView imageView;
    View rootview;
    String destination;
    String keyword;
    String vot;
    int no;
    int count;
    private SweetAlertDialog progressDialog;
    private BroadcastReceiver broadcastReceiver;
    Context context;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
//        sp = getIntent().getStringExtra("sp");
        rootview = findViewById(R.id.rootview);
        txt_vot = (EditText) findViewById(R.id.txt_vot);
        txt_destination = (EditText) findViewById(R.id.txt_destination);
        txt_keyword = (EditText) findViewById(R.id.txt_keyword);
        txt_no = (EditText) findViewById(R.id.txt_no);
        btn_send = (Button) findViewById(R.id.btn_send);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ceyentra.com"));
                startActivity(browserIntent);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    count = 0;
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            if (sent) {
//                                new SMSSender().execute();
//                            }
//                        }
//                    }, 100, 5000);

                    showConfirmation();
                }
            }
        });
    }

    class SMSSender extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendSMS();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            return null;
        }

        protected void onPostExecute(String file_url) {

        }
    }

    public void sendSMS() {

        final SweetAlertDialog.OnSweetClickListener successListener = new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        };
        if (progressDialog == null) {
            progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.setTitleText("Sending...");
            progressDialog.setCancelable(false);
            progressDialog.setContentText("Sent " + String.valueOf(count) + "/" + String.valueOf(no));
            progressDialog.getProgressHelper().setRimColor(R.color.colorPrimary);
            progressDialog.show();
        }
        final String SENT_ACTION = "com.sprintylab.malchat.regsent" + String.valueOf(count);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT_ACTION), 0);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        count += 1;
                        if (count >= no) {
                            progressDialog.setTitleText("Success!")
                                    .setContentText("All the messages were sent")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(successListener)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        } else {
                            progressDialog.setTitleText("Sending...")
                                    .setContentText("Sent " + String.valueOf(count) + "/" + String.valueOf(no))
                                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                            sendSMS();
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent " + String.valueOf(count) + "/" + String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent " + String.valueOf(count) + "/" + String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent " + String.valueOf(count) + "/" + String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent " + String.valueOf(count) + "/" + String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(SENT_ACTION));
        String msg = keyword + " " + vot;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(destination, null, msg, sentPI, null);
//            if (sp.equals("d")){
//                smsManager.sendTextMessage("77200", null, msg, sentPI, null);
//            } else if (sp.equals("m")){
//                smsManager.sendTextMessage("3130", null, msg, sentPI, null);
//            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private boolean isValid() {
        destination = txt_destination.getText().toString();
        keyword = txt_keyword.getText().toString();
        vot = txt_vot.getText().toString();

        if (!Validator.isValidString(destination)) {
            Snackbar.make(rootview, "Please enter a sms destination", Snackbar.LENGTH_LONG).show();
        } else if (!Validator.isValidString(keyword)) {
            Snackbar.make(rootview, "Please enter a keyword", Snackbar.LENGTH_LONG).show();
        } else if (!Validator.isValidNumber(vot)) {
            Snackbar.make(rootview, "Please enter the contestant number", Snackbar.LENGTH_LONG).show();
        } else if (!Validator.isValidNumber(txt_no.getText().toString())) {
            Snackbar.make(rootview, "Please enter the number of sms to be sent", Snackbar.LENGTH_LONG).show();
        } else {
            no = Integer.parseInt(txt_no.getText().toString());
            return true;
        }
        return false;
    }

    private void showConfirmation() {
        final String prompt = String.format(SMS_TEMPLATE, no, destination, keyword, vot);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        progressDialog.setTitleText("Confirm");
        progressDialog.setCancelable(false);
        progressDialog.setContentText(prompt);
        progressDialog.getProgressHelper().setRimColor(R.color.colorPrimary);
        progressDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                progressDialog.cancel();
            }
        });
        progressDialog.setCancelText("Cancel");
        progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                progressDialog.dismiss();
                progressDialog = null;
                sendSMS();
            }
        });
        progressDialog.setConfirmText("OK");
        progressDialog.show();
    }
}
