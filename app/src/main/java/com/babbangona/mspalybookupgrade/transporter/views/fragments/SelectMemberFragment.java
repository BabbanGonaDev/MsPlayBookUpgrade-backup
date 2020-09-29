package com.babbangona.mspalybookupgrade.transporter.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogTransporterNoOfBagsBinding;
import com.babbangona.mspalybookupgrade.databinding.FragmentSelectMemberBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.ViewMembersAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.Members;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.babbangona.mspalybookupgrade.transporter.viewmodels.MembersViewModel;
import com.babbangona.mspalybookupgrade.transporter.views.activities.AssigningActivity;
import com.babbangona.mspalybookupgrade.transporter.views.activities.ViewMemberFields;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SelectMemberFragment extends Fragment implements SearchView.OnQueryTextListener {
    ViewMembersAdapter adapter;
    private MembersViewModel model;
    private TSessionManager session;
    TransporterDatabase db;
    private FragmentSelectMemberBinding binding;
    private AppDatabase appDb;

    public SelectMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = new ViewModelProvider(requireActivity()).get(MembersViewModel.class);

        session = new TSessionManager(getActivity());

        appDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        db = TransporterDatabase.getInstance(getActivity().getApplicationContext());

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_member, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ViewMembersAdapter(new ViewMembersAdapter.OnItemClickListener() {
            @Override
            public void goToFields(Members member) {
                //Navigate to fields page
                startActivity(new Intent(getActivity(), ViewMemberFields.class)
                        .putExtra("member_details", member));
            }

            @Override
            public void assignTransporter(Members member) {
                //Begin Transporter assign process
                session.SET_UNIQUE_MEMBER_ID(member.getUniqueMemberId());
                requestTransportedBy(member);

            }
        });

        binding.rcvMembers.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        binding.rcvMembers.setItemAnimator(new DefaultItemAnimator());
        binding.rcvMembers.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        binding.rcvMembers.setAdapter(adapter);

        //First time set an empty value to get all data.
        model.setSearchText("");

        model.getPagedMembersList(appDb.membersDao()).observe(getViewLifecycleOwner(), members -> {
            //Put adapter here
            adapter.submitList(members);
        });

        binding.searchViewMembers.setOnQueryTextListener(this);

        binding.searchViewMembers.setOnClickListener(v -> binding.searchViewMembers.setIconified(false));
    }

    public void requestTransportedBy(Members mem) {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.btnLeft.setText("Member");
        binding.btnRight.setText("BG");
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.mtvDialogText.setText("By whom has the product been transported ?");

        binding.btnRight.setOnClickListener(v -> {
            //BG Transported
            dialog.dismiss();
            session.SET_TRANSPORTED_BY("BG");
            startActivity(new Intent(getActivity(), AssigningActivity.class));
        });

        binding.btnLeft.setOnClickListener(v -> {
            //Member Transported
            dialog.dismiss();
            session.SET_TRANSPORTED_BY("Member");
            requestNoOfBagsTransported(mem);
        });

        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void confirmTransportedByMember() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.btnPrimary.setText("Continue");
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.mtvDialogText.setText("The product has been transported by the member");

        binding.btnPrimary.setOnClickListener(v -> dialog.dismiss());
        //binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void requestNoOfBagsTransported(Members mem) {
        DialogTransporterNoOfBagsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_transporter_no_of_bags, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);

        binding.btnContinue.setOnClickListener(v -> {
            String numberOfBags = binding.editEnterNoBags.getText().toString().trim();
            String confirmNumberOfBags = binding.editConfirmNoBags.getText().toString().trim();

            if (numberOfBags.equals(confirmNumberOfBags) && !numberOfBags.isEmpty()) {
                //Save details
                dialog.dismiss();
                requestMarkAsTransported(mem, Integer.parseInt(numberOfBags));
            } else {
                Toast.makeText(getActivity(), "Kindly fill in the inputs correctly", Toast.LENGTH_LONG).show();
            }
        });

        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void requestMarkAsTransported(Members mem, Integer qty) {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.mtvDialogText.setText("Do you want to mark the member as transported ?");
        binding.btnRight.setText("Yes");
        binding.btnLeft.setText("No");

        binding.btnRight.setOnClickListener(v -> {
            //Save details and dismiss.
            dialog.dismiss();
            saveTransportedByMember(mem, qty);
        });
        binding.btnLeft.setOnClickListener(v -> {
            //Dismiss
            dialog.dismiss();
        });
        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void saveTransportedByMember(Members mem, Integer qty) {

        CoachLogsTable log = new CoachLogsTable(mem.getUniqueMemberId(),
                qty,
                "Member",
                "N/A",
                "N/A",
                0,
                "N/A",
                0,
                session.GET_LOG_IN_STAFF_ID(),
                AppUtils.getDeviceID(getActivity().getApplicationContext()),
                BuildConfig.VERSION_NAME,
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()),
                0);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.getCoachLogsDao().insertSingleCoachLog(log);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                confirmTransportedByMember();
            }
        }.execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        model.setSearchText("%" + newText.toLowerCase() + "%");
        return false;
    }
}