package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Main2Activity extends AppCompatActivity {

    String mStaffName,mStaffRole, mStaffId, mStaffProgram;

    @BindView(R.id.home_page_rcv)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar_landing)
    MaterialToolbar toolbar_landing;

    @BindView(R.id.btn_load_more)
    MaterialButton btn_load_more;

    @BindView(R.id.mtv_staff_id)
    MaterialTextView mtv_staff_id;

    @BindView(R.id.mtv_staff_name)
    MaterialTextView mtv_staff_name;

    @BindView(R.id.mtv_app_version)
    MaterialTextView mtv_app_version;

    @BindView(R.id.mtv_user_role)
    MaterialTextView mtv_user_role;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        sharedPrefs = new SharedPrefs(this);
        setSupportActionBar(toolbar_landing);
        saveAccessControlDetails();
        collapsingToolbarTitle();
        displayUserDetails();

    }

    void saveAccessControlDetails(){
        try {

            //This gets the intents sent from access control and saves it to shared prefs
            Intent intent  = getIntent();
            Bundle b       = intent.getExtras();
            if (b != null) {
                mStaffName     = (String) b.get("staff_name");
                mStaffRole     = (String) b.get("staff_role");
                mStaffId       = (String) b.get("staff_id");
                mStaffProgram  = (String) b.get("staff_program");
            }

            new SharedPrefs(this).CreateLoginSession(mStaffName,mStaffId,mStaffRole,mStaffProgram);


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void collapsingToolbarTitle() {
        //Use this function to display the title in the collapsing toolbar, ONLY when the toolbar has been collapsed.
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getResources().getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    //Remove the menu here too.
                    isShow = false;
                }
            }
        });
    }

    public void displayUserDetails(){
        mtv_staff_name.setText(sharedPrefs.getStaffName());
        mtv_staff_id.setText(sharedPrefs.getStaffID());
        mtv_user_role.setText(sharedPrefs.getStaffRole());
        String app_version_text = "\u00a9" + " " + "Version" + " ";
        mtv_app_version.setText(app_version_text);
    }

}
