package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterVehicleBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.VehicleTypeAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TransporterVehicleActivity extends AppCompatActivity {
    ActivityTransporterVehicleBinding binding;
    TSessionManager session;
    TransporterDatabase db;
    VehicleTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_vehicle);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        binding.btnContinue.setOnClickListener(v -> {
            // Go to next activity.
            if (!isAnyVehicleSelected()) {
                AlertDialog select_vehicle_check = new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setMessage("Kindly select at least 1 vehicle type")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();
                select_vehicle_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            } else {
                String selected_vehicles = getSelectedVehicles();
                session.SET_REG_VEHICLE_TYPE(selected_vehicles);
                Toast.makeText(this, selected_vehicles, Toast.LENGTH_LONG).show();

                startActivity(new Intent(this, TransporterLocationActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isAnyVehicleSelected() {
        return binding.mcbTricycle.isChecked() || binding.mcbBus.isChecked() ||
                binding.mcbCanter.isChecked() || binding.mcbCar.isChecked() || binding.mcbMotorcycle.isChecked();
    }

    public String getSelectedVehicles() {
        StringBuilder vehicles = new StringBuilder();

        if (binding.mcbTricycle.isChecked()) {
            vehicles.append("Tricycle, ");
        }
        if (binding.mcbMotorcycle.isChecked()) {
            vehicles.append("Motorcycle, ");
        }
        if (binding.mcbCar.isChecked()) {
            vehicles.append("Car, ");
        }
        if (binding.mcbCanter.isChecked()) {
            vehicles.append("Canter, ");
        }
        if (binding.mcbBus.isChecked()) {
            vehicles.append("Bus, ");
        }

        return vehicles.toString().trim();
    }

    /*public void initVehicleRecycler() {
        List<Vehicle> vehicles = createVehicleData();

        if (!vehicles.isEmpty()) {
            adapter = new VehicleTypeAdapter(TransporterVehicleActivity.this, vehicles, vehicle -> {
                //Add to shared prefs.
                session.SET_REG_VEHICLE_TYPE(vehicle.getVehicle_type());
                binding.btnContinue.setEnabled(true);
                Toast.makeText(TransporterVehicleActivity.this, "'" + vehicle.getVehicle_type() + "' selected", Toast.LENGTH_LONG).show();
            });

            binding.recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.recyclerViewVehicle.setAdapter(adapter);

            //RecyclerView.LayoutManager vLayoutManager = new LinearLayoutManager(getApplicationContext());
            //binding.recyclerViewVehicle.setItemAnimator(new DefaultItemAnimator());
            //binding.recyclerViewVehicle.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        }
    }

    public List<Vehicle> createVehicleData() {
        List<Vehicle> list = new ArrayList<>();

        list.add(new Vehicle("Motorcycle", R.drawable.ic_motorcycle_vehicle_type_1));
        list.add(new Vehicle("Tricycle", R.drawable.ic_pedicab_vehicle_type_2));
        list.add(new Vehicle("Car", R.drawable.ic_car_vehicle_type));
        list.add(new Vehicle("Bus", R.drawable.ic_bus_vehicle_type));
        list.add(new Vehicle("Canter", R.drawable.ic_canter_vehicle_type));

        return list;
    }*/
}