package com.babbangona.mspalybookupgrade.HarvestSummary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.HarvestSummary.data.entities.CollectionCenterEntity;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MemberDetails extends AppCompatActivity {

    AppDatabase appDatabase;
    List<CollectionCenterEntity> memberCollectionCenterList;
    SharedPrefs sharedPrefs;
    String uniqueMemberId;
    TextView name, iknumber, no_of_bags_marketed, no_of_bags_transported, expected_bags;
    TextView harvest_complete, payment_status, harvest_advance_paid, transporter_name;
    TextView transporter_phone_number, bgt_name, bgt_staffId;
    MaterialButton btn_continue;
    ImageView backNav;
    TextView dateContainer, staffIdContainer, appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        appDatabase = AppDatabase.getInstance(this);
        sharedPrefs = new SharedPrefs(this);
        viewItemId();
        buttonClick();
        setBackNav();
        displayDetails();

        uniqueMemberId = sharedPrefs.getKeyIndividualHarvestSummaryUniqueMemberId();
        memberCollectionCenterList = appDatabase.getCollectionCenterDao().getIndividualHarvestDetails(uniqueMemberId);

        setItemViews();
    }

    public void viewItemId(){
        name = findViewById(R.id.tg_leader_tv);
        iknumber = findViewById(R.id.ik_number_tv);
        no_of_bags_marketed = findViewById(R.id.no_of_bags_marketed_tv);
        no_of_bags_transported = findViewById(R.id.no_of_bags_transported_tv);
        expected_bags = findViewById(R.id.expected_bags_tv);
        harvest_complete = findViewById(R.id.harvest_complete_tv);
        payment_status = findViewById(R.id.payment_status_tv);
        harvest_advance_paid = findViewById(R.id.harvest_advance_paid_tv);
        transporter_name = findViewById(R.id.transporter_name_tv);
        transporter_phone_number = findViewById(R.id.transporter_phone_number_tv);
        bgt_name = findViewById(R.id.bgt_name_tv);
        bgt_staffId = findViewById(R.id.bgt_staffId_tv);
        btn_continue = findViewById(R.id.btn_continue_icon);
        backNav = findViewById(R.id.back_arrow);
        dateContainer = findViewById(R.id.sync_date_notifier);
        staffIdContainer = findViewById(R.id.staff_id_notifier);
        appVersion = findViewById(R.id.donotpay_app_version);
    }

    public void setItemViews(){

        if (memberCollectionCenterList.size() != 0){
            name.setText(sharedPrefs.getKeyIndividualHarvestSummaryName());
            iknumber.setText(memberCollectionCenterList.get(0).getIk_number());
            no_of_bags_marketed.setText(memberCollectionCenterList.get(0).getNo_of_bags_marketed());
            no_of_bags_transported.setText(memberCollectionCenterList.get(0).getNo_of_bags_transported());
            expected_bags.setText(memberCollectionCenterList.get(0).getExpected_bags());
            harvest_complete.setText(memberCollectionCenterList.get(0).getHarvest_complete());
            payment_status.setText(memberCollectionCenterList.get(0).getPayment_status());
            harvest_advance_paid.setText(memberCollectionCenterList.get(0).getHarvest_advance_paid());
            transporter_name.setText(memberCollectionCenterList.get(0).getTransporter_name());
            transporter_phone_number.setText(memberCollectionCenterList.get(0).getTransporter_phone_number());
            bgt_name.setText(memberCollectionCenterList.get(0).getBgt_name());
            bgt_staffId.setText(memberCollectionCenterList.get(0).getBgt_staff_id());
        } else{

            name.setText(sharedPrefs.getKeyIndividualHarvestSummaryName());
            iknumber.setText(sharedPrefs.getKeyIndividualHarvestSummaryIkNumber());
            no_of_bags_marketed.setText("No Record");
            no_of_bags_transported.setText("No Record");
            expected_bags.setText("No Record");
            harvest_complete.setText("No Record");
            payment_status.setText("No Record");
            harvest_advance_paid.setText("No Record");
            transporter_name.setText("No Record");
            transporter_phone_number.setText("No Record");
            bgt_name.setText("No Record");
            bgt_staffId.setText("No Record");
        }

    }

    public void displayDetails(){

        staffIdContainer.setText(sharedPrefs.getStaffID());
        dateContainer.setText(sharedPrefs.getKeyLastSyncTime());
        appVersion.setText(BuildConfig.VERSION_NAME);
    }

    public void buttonClick(){

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MemberDetails.this, TrustGroupMembers.class);
                startActivity(intent);*/
                finish();
            }
        });
    }

    public void setBackNav(){

        backNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MemberDetails.this, TrustGroupMembers.class);
                startActivity(intent);*/
                finish();
            }
        });
    }
}
