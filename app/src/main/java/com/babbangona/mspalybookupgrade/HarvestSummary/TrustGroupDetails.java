package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.HarvestSummary.data.entities.CollectionCenterEntity;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class TrustGroupDetails extends AppCompatActivity {

    AppDatabase appDatabase;
    SharedPrefs sharedPrefs;
    List<CollectionCenterEntity> allTgHarvestList;
    String ikNumber;
    MaterialButton btn_continue;
    TextView iknumber, no_of_bags_marketed, no_of_bags_transported, expected_bags, harvest_complete, payment_status;
    int noOfBagsMarketed, noOfBagsTransported, expectedBags;
    int i, count;
    TextView dateContainer, staffIdContainer, appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust_group_details);

        btn_continue = findViewById(R.id.btn_continue_icon);

        appDatabase = AppDatabase.getInstance(this);
        sharedPrefs = new SharedPrefs(this);
        viewItemId();

        ikNumber = sharedPrefs.getKeyHarvestSummaryIkNumber();
        allTgHarvestList = appDatabase.getCollectionCenterDao().getAllTgHarvestDetails(ikNumber);

        setItemViews();
        displayDetails();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrustGroupDetails.this, HarvestSummaryPage.class);
                startActivity(intent);
            }
        });
    }

    public void viewItemId(){

        iknumber = findViewById(R.id.ik_number_tv);
        no_of_bags_marketed = findViewById(R.id.no_of_bags_marketed_tv);
        no_of_bags_transported = findViewById(R.id.no_of_bags_transported_tv);
        expected_bags = findViewById(R.id.expected_bags_tv);
        harvest_complete = findViewById(R.id.harvest_complete_tv);
        payment_status = findViewById(R.id.payment_status_tv);
        dateContainer = findViewById(R.id.sync_date_notifier);
        staffIdContainer = findViewById(R.id.staff_id_notifier);
        appVersion = findViewById(R.id.donotpay_app_version);
    }

    public void setItemViews(){

        i = 0;
        count = 0;
        noOfBagsMarketed = 0;
        noOfBagsTransported = 0;
        expectedBags = 0;

        if (allTgHarvestList.size() != 0){

            while (i < allTgHarvestList.size()){
                noOfBagsMarketed = noOfBagsMarketed + Integer.parseInt(allTgHarvestList.get(i).getNo_of_bags_marketed());
                noOfBagsTransported = noOfBagsTransported + Integer.parseInt(allTgHarvestList.get(i).getNo_of_bags_transported());
                expectedBags = expectedBags + Integer.parseInt(allTgHarvestList.get(i).getExpected_bags());

                if (allTgHarvestList.get(i).getHarvest_complete().equalsIgnoreCase("No")){
                    count++;
                }
                i++;
            }

            iknumber.setText(ikNumber);
            no_of_bags_marketed.setText(String.valueOf(noOfBagsMarketed));
            no_of_bags_transported.setText(String.valueOf(noOfBagsTransported));
            expected_bags.setText(String.valueOf(expectedBags));
            if (count > 0){
                harvest_complete.setText("No");
                payment_status.setText("NA");
            } else{
                harvest_complete.setText("Yes");
                payment_status.setText(allTgHarvestList.get(0).getPayment_status());
            }

        } else {

            iknumber.setText(ikNumber);
            no_of_bags_marketed.setText("No Record");
            no_of_bags_transported.setText("No Record");
            expected_bags.setText("No Record");
            harvest_complete.setText("No Record");
            payment_status.setText("No Record");
        }
    }

    public void displayDetails(){

        staffIdContainer.setText(sharedPrefs.getStaffID());
        dateContainer.setText(sharedPrefs.getKeyLastSyncTime());
        appVersion.setText(BuildConfig.VERSION_NAME);
    }


}
