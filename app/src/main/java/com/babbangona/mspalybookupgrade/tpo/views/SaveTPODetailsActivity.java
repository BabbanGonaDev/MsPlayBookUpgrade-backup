package com.babbangona.mspalybookupgrade.tpo.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTpoSaveDetailsBinding;
import com.babbangona.mspalybookupgrade.tpo.data.TPOSessionManager;
import com.babbangona.mspalybookupgrade.tpo.data.models.MemberModel;
import com.google.gson.Gson;

public class SaveTPODetailsActivity extends AppCompatActivity {
    ActivityTpoSaveDetailsBinding binding;
    TPOSessionManager session;
    MemberModel selectedMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tpo_save_details);

        session = new TPOSessionManager(this);

        try {
            selectedMember = new Gson().fromJson(session.GET_SELECTED_MEMBER(), MemberModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnSubmitAttendance.setOnClickListener(v -> saveDetails());
    }

    public void saveDetails() {
        if (binding.editEnterAmount.getText().toString().trim().equals(binding.editConfirmAmount.getText().toString().trim())
                && !binding.editEnterAmount.getText().toString().isEmpty()
                && !binding.editEnterAmount.getText().toString().trim().equals("0")) {
            //Save details
        } else {
            Toast.makeText(this, "Kindly check amount and try again", Toast.LENGTH_LONG).show();
        }
    }
}