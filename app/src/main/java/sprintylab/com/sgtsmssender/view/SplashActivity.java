package sprintylab.com.sgtsmssender.view;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import sprintylab.com.sgtsmssender.R;


public class SplashActivity extends AppCompatActivity{
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private ImageView spinner;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        spinner = (ImageView) findViewById(R.id.spinner);
        startTime = System.currentTimeMillis();

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(spinner, "alpha",  1f, .3f);
        fadeOut.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(spinner, "alpha", .3f, 1f);
        fadeIn.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                requestPermission();
            }
        });
        mAnimationSet.start();
        //requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
        }else {
//            while (System.currentTimeMillis() - startTime < 3000) {
//
//            }
            goToNextActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int permisson : grantResults) {
                if (permisson != PackageManager.PERMISSION_GRANTED) {
                    showMessageOKCancel("You need to provide permisson to access SMS to continue.", null);
                    return;
                }
            }
//            while (System.currentTimeMillis() - startTime < 3000) {
//
//            }
            goToNextActivity();
        }
    }

    private void goToNextActivity() {
        //startService(new Intent(this, SMSReceivingService.class));
        //SharedPreferences mPrefs = getSharedPreferences("malchat.username", Context.MODE_PRIVATE);
        //String username = mPrefs.getString("username", null);
            Intent intent = new Intent(this, SPChoosingActivity.class);
            startActivity(intent);
            finish();
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
