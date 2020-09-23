package com.babbangona.mspalybookupgrade.transporter.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogTransporterNoOfBagsBinding;
import com.babbangona.mspalybookupgrade.databinding.FragmentSelectMemberBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.ViewMembersAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.Members;
import com.babbangona.mspalybookupgrade.transporter.viewmodels.MembersViewModel;
import com.babbangona.mspalybookupgrade.transporter.views.activities.ViewMemberFields;


public class SelectMemberFragment extends Fragment {
    ViewMembersAdapter adapter;
    private MembersViewModel model;
    private TSessionManager session;
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
    }

    public void requestTransportedBy(Members mem) {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.btnLeft.setText("Member");
        binding.btnRight.setText("BG");
        binding.btnLeft.setAllCaps(false);
        binding.btnRight.setAllCaps(false);
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.mtvDialogText.setText("By whom has the product been transported ?");

        binding.btnRight.setOnClickListener(v -> {
            //BG Transported
            dialog.dismiss();
        });

        binding.btnLeft.setOnClickListener(v -> {
            //Member Transported
            dialog.dismiss();
            requestNoOfBagsTransported(mem);
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void confirmTransportedByMember(Members mem) {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(getActivity()), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.btnPrimary.setText("Continue");
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.mtvDialogText.setText("The product has been transported by the member");

        binding.btnPrimary.setOnClickListener(v -> dialog.dismiss());
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
                confirmTransportedByMember(mem);
            } else {
                Toast.makeText(getActivity(), "Kindly fill in the inputs correctly", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}