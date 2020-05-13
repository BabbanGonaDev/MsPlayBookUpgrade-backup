package com.babbangona.mspalybookupgrade;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComingSoon extends AppCompatActivity {
    @BindView(R.id.btn_coming_soon)
    MaterialButton btn_coming_soon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_coming_soon)
    public void goBack(){
        finish();
    }
}
