package com.luciofm.presentation.droiconit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luciofm.presentation.droiconit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        startActivity(new Intent(this, FirstActivity.class));
    }
}