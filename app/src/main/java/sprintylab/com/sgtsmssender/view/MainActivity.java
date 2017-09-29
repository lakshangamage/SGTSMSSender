package sprintylab.com.sgtsmssender.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import sprintylab.com.sgtsmssender.R;

public class MainActivity extends AppCompatActivity {
    private String sp;
    private EditText txt_vot;
    private EditText txt_no;
    private Button btn_send;
    int vot;
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
        sp = getIntent().getStringExtra("sp");
        txt_vot = (EditText) findViewById(R.id.txt_vot);
        txt_no = (EditText) findViewById(R.id.txt_no);
        btn_send = (Button) findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txt_vot.getText().toString().isEmpty() && !txt_no.getText().toString().isEmpty() ) {
                    vot = Integer.parseInt(txt_vot.getText().toString());
                    no = Integer.parseInt(txt_no.getText().toString());
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
                    sendSMS();
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
        final String SENT_ACTION = "com.sprintylab.malchat.regsent"+String.valueOf(count);
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT_ACTION), 0);
        broadcastReceiver  = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        count += 1;
                        if (count >= no){
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
                                .setContentText("There was an error sending. Sent "+String.valueOf(count)+"/"+String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent "+String.valueOf(count)+"/"+String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent "+String.valueOf(count)+"/"+String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        progressDialog.setTitleText("Failed!")
                                .setContentText("There was an error sending. Sent "+String.valueOf(count)+"/"+String.valueOf(no))
                                .setConfirmText("OK")
                                .setConfirmClickListener(successListener)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(SENT_ACTION));
        String msg = "SGT " + String.valueOf(vot);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            if (sp.equals("d")){
                smsManager.sendTextMessage("77200", null, msg, sentPI, null);
            } else if (sp.equals("m")){
                smsManager.sendTextMessage("3130", null, msg, sentPI, null);
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
