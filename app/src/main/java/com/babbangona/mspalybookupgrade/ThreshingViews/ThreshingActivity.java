package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.babbangona.mspalybookupgrade.R;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreshingActivity extends AppCompatActivity {

    @BindView(R.id.btnScheduleThreshing)
    MaterialButton btnScheduleThreshing;

    @BindView(R.id.btnUpdateScheduleThreshing)
    MaterialButton btnUpdateScheduleThreshing;

    @BindView(R.id.btnConfirmThreshing)
    MaterialButton btnConfirmThreshing;

    @BindView(R.id.btnSummary)
    MaterialButton btnSummary;

    @BindView(R.id.btnMarkHG)
    MaterialButton btnMarkHG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshing);
        ButterKnife.bind(ThreshingActivity.this);
    }

    @OnClick(R.id.btnScheduleThreshing)
    public void setBtnScheduleThreshing(){

    }
}