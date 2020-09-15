package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemberList extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_search)
    TextView et_search;

    @BindView(R.id.search_edit_text)
    EditText search_edit_text;

    @BindView(R.id.back_to_toolbar)
    ImageView back_to_toolbar;

    @BindView(R.id.search_linear_layout)
    LinearLayout search_linear_layout;

    @BindView(R.id.toolbar_linear_layout)
    LinearLayout toolbar_linear_layout;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        ButterKnife.bind(MemberList.this);
    }

    @OnClick(R.id.et_search)
    public void move_to_search(View view){
        hideView(toolbar_linear_layout);
        showView(search_linear_layout);
        search_edit_text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).showSoftInput(search_edit_text, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.back_to_toolbar)
    public void move_away_from_search(View view){
        removeSearchTray();
    }

    void removeSearchTray(){
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        search_edit_text.setText(null);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void loadPreviousActivity() {
        startActivity(new Intent(MemberList.this, ThreshingActivity.class));
    }

    @Override
    public void onBackPressed(){
        if (search_linear_layout.getVisibility() == View.VISIBLE){
            removeSearchTray();
        }else{
            loadPreviousActivity();
        }
    }
}