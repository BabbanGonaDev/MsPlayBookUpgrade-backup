package com.babbangona.mspalybookupgrade.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.PWSFieldListPage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFragmentRecycler.PWSClaimAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFragmentRecycler.PWSClaimPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.PWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PWSDialogFragment extends DialogFragment {

    private Unbinder unbinder;

    @BindView(R.id.toolbar_hg_fragment)
    Toolbar toolbar_hg_fragment;

    @BindView(R.id.tv_field_r_id)
    TextView tv_field_r_id;

    @BindView(R.id.tv_member_r_id)
    TextView tv_member_r_id;

    @BindView(R.id.tv_ik_number)
    TextView tv_ik_number;

    @BindView(R.id.tv_member_name)
    TextView tv_member_name;

    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;

    @BindView(R.id.tv_field_size)
    TextView tv_field_size;

    @BindView(R.id.tv_village_name)
    TextView tv_village_name;

    @BindView(R.id.tv_crop_type)
    TextView tv_crop_type;

    @BindView(R.id.tv_pws_list)
    TextView tv_pws_list;

    @BindView(R.id.btnAddPWSActivity)
    MaterialButton btnAddPWSActivity;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    PWSClaimPageListModelClass pwsClaimPageListModelClass;

    PWSClaimAdapter pwsClaimAdapter;

    private SharedPrefs sharedPrefs;

    private AppDatabase appDatabase;

    PWSFieldListRecyclerModel pwsFieldListRecyclerModel;

    private GPSController.LocationGetter locationGetter;

    @BindView(R.id.emptyView)
    TextView emptyView;

    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View fragmentLayout = inflater.inflate(R.layout.pws_log_fragment, container, false);
        ButterKnife.bind(this, fragmentLayout);
        unbinder = ButterKnife.bind(this, fragmentLayout);
        appDatabase = AppDatabase.getInstance(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        pwsFieldListRecyclerModel = new PWSFieldListRecyclerModel();
        pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar_hg_fragment);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Poor Weather Support");
        GPSController.initialiseLocationListener(getActivity());

        toolbar_hg_fragment.setNavigationOnClickListener(v -> dismissAndRefresh());

        String field_r_id = pwsFieldListRecyclerModel.getUnique_field_id();
        String ik_number = Objects.requireNonNull(getActivity()).getResources().getString(R.string.ik_number) +" "+ pwsFieldListRecyclerModel.getIk_number();
        String member_name = getActivity().getResources().getString(R.string.member_name) +" "+ pwsFieldListRecyclerModel.getMember_name();
        String phone_number = getActivity().getResources().getString(R.string.member_phone_number) +" "+ pwsFieldListRecyclerModel.getPhone_number();
        String field_size = getActivity().getResources().getString(R.string.field_size) +" "+ pwsFieldListRecyclerModel.getField_size();
        String village = getActivity().getResources().getString(R.string.member_village) +" "+ pwsFieldListRecyclerModel.getVillage_name();
        String crop_type = getActivity().getResources().getString(R.string.member_crop_type) +" "+ pwsFieldListRecyclerModel.getCrop_type();
        String member_r_id = getActivity().getResources().getString(R.string.member_r_id) +" "+ pwsFieldListRecyclerModel.getField_r_id();
        tv_member_r_id.setText(member_r_id);

        tv_field_r_id.setText(field_r_id);
        tv_ik_number.setText(ik_number);
        tv_member_name.setText(member_name);
        tv_phone_number.setText(phone_number);
        tv_field_size.setText(field_size);
        tv_village_name.setText(village);
        tv_crop_type.setText(crop_type);

        fillRecycler(pwsFieldListRecyclerModel.getUnique_field_id());

        int offset;
        if (sharedPrefs.getKeyAdapterOffsetPWSList() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffsetPwsList(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);


        return fragmentLayout;
    }

    /**
     * Very important to unbind when using butterKnife with fragments
     */
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     *  The system calls this only when creating the layout in a dialog.
     */
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.btnAddPWSActivity)
    public  void next(View view){
        locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();
        pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
        double min_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat());
        double max_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat());
        double min_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng());
        double max_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng());
        double mid_lat = (max_lat+min_lat)/2.0;
        double mid_lng = (max_lng+min_lng)/2.0;

        double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
        double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

        if (locationDistance <= allowedDistance){
            addPWSFragment();
        }else{
            locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                    getActivity(),pwsFieldListRecyclerModel.getUnique_field_id(),
                    Objects.requireNonNull(getActivity()).getResources().getString(R.string.wrong_location));
        }

    }

    void dismissAndRefresh(){
        dismiss();
        ((PWSFieldListPage) Objects.requireNonNull(getActivity())).myMethod();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void locationMismatchedDialog(double latitude,
                                          double longitude,
                                          double min_lat,
                                          double max_lat,
                                          double min_lng,
                                          double max_lng,
                                          Context context,
                                          String unique_field_id,
                                          String message) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        locationMismatchedDialog(latitude, longitude, min_lat, max_lat,
                min_lng, max_lng, context, unique_field_id, builder, message);
    }

    private void locationMismatchedDialog(final double latitude,
                                          final double longitude,
                                          final double min_lat,
                                          final double max_lat,
                                          final double min_lng,
                                          final double max_lng,
                                          Context context,
                                          String unique_field_id,
                                          MaterialAlertDialogBuilder builder,
                                          String message) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final TextView fieldId = new TextView(context);
        fieldId.setText("Field ID: " + unique_field_id);
        fieldId.setPadding(paddingPixel, 0, 0, 0);
        fieldId.setTypeface(null, Typeface.ITALIC);
        layout.addView(fieldId);

        final TextView minlat = new TextView(context);
        minlat.setText("\nMin/Max Lat= " + min_lat + "/"  + max_lat );
        minlat.setPadding(paddingPixel, 0, 0, 0);
        minlat.setTypeface(null, Typeface.ITALIC);
        layout.addView(minlat);

        final TextView minlng = new TextView(context);
        minlng.setText("Min/Max Long= " + min_lng + "/"  + max_lng );
        minlng.setPadding(paddingPixel, 0, 0, 0);
        minlng.setTypeface(null, Typeface.ITALIC);
        layout.addView(minlng);

        final TextView tvId = new TextView(context);
        tvId.setText("\nYou are on Latitude: " + latitude);
        tvId.setPadding(paddingPixel, 0, 0, 0);
        tvId.setTypeface(null, Typeface.ITALIC);
        layout.addView(tvId);

        final TextView tvIK = new TextView(context);
        tvIK.setText("You are on Longitude: " + longitude);
        tvIK.setPadding(paddingPixel, 0, 0, 0);
        tvIK.setTypeface(null, Typeface.ITALIC);
        layout.addView(tvIK);

        builder.setCancelable(false)
                .setView(layout)
                .setTitle("Not on Field.")
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> dialog.cancel())
                .show();
    }

    private double locationDistanceToFieldCentre(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private double allowedDistanceToFieldBoundary(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(Math.abs(lat2 - lat1));  // deg2rad below
        double dLon = deg2rad(Math.abs(lon2 - lon1));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in m
        Log.d("distanceDami",String.valueOf(d));
        return d+0.0002;
        // return Math.sqrt((lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1)) * 110000;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public void addPWSFragment() {
        LogPWSDialogFragment nextFrag= new LogPWSDialogFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(((ViewGroup) Objects.requireNonNull(getView()).getParent()).getId(), nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private PWSActivitiesFlagDao application;
        String unique_field_id;

        MyViewModelFactory(PWSActivitiesFlagDao application, String unique_field_id) {
            this.application = application;
            this.unique_field_id = unique_field_id;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PWSClaimPageListModelClass(application, unique_field_id);
        }
    }

    private void fillRecycler(String unique_field_id){
        pwsClaimPageListModelClass = new ViewModelProvider(this,
                new PWSDialogFragment.MyViewModelFactory(appDatabase.pwsActivitiesFlagDao(), unique_field_id))
                .get(PWSClaimPageListModelClass.class);
        pwsClaimPageListModelClass.filterTextAll.setValue("");
    }

    private void setAdapter(int offset) {
        pwsClaimAdapter = new PWSClaimAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if (offset == 1){
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
            VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
            recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        }
        recycler_view.setLayoutManager(layoutManager);

        pwsClaimPageListModelClass.pwsListModel.observe(this,pwsListModels -> {


            updateView(pwsListModels.size());
            pwsClaimAdapter.submitList(pwsListModels);

            pwsListModels.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int position, int count) {
                    updateView(pwsListModels.size());
                }

                @Override
                public void onInserted(int position, int count) {

                }

                @Override
                public void onRemoved(int position, int count) {

                }
            });
        });
        recycler_view.setAdapter(pwsClaimAdapter);

    }

    private void updateView(int itemCount) {
        if (itemCount > 0) {
            // The list is not empty. Show the recycler view.
            showView(recycler_view);
            hideView(emptyView);
        } else {
            // The list is empty. Show the empty list view
            hideView(recycler_view);
            showView(emptyView);
        }

    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

}
