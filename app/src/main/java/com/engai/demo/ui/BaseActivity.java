package com.engai.demo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.engai.demo.App;
import com.engai.demo.R;

import java.util.Vector;

/**
 * Created by saurabhtomar.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle bundle) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(bundle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /*
    ProgressBar Vector
     */
    Vector<ProgressDialog> progress = new Vector<>();

    /*
    Show Progressbar dialog
     */
    public void showDialog(String message, boolean canCancelled) {
        try {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.show();
            progress.add(dialog);
        } catch (Exception ignored) {

        }
    }

    public void showProgress() {
        if (App.isNetworkConnected()) {
            showDialog(getString(R.string.str_loading), true);
        } else {
            App.showToast(BaseActivity.this, "Please connect to internet for accessing server");
            return;
        }
    }


    public void dismissProgress() {
        try {
            if (progress != null)
                for (ProgressDialog prog : progress)
                    prog.dismiss();
        } catch (Exception ignored) {
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(new Bundle());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
