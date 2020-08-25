package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterVehicleBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.VehicleTypeAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.Vehicle;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;

import java.util.ArrayList;
import java.util.List;

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

        //Disable to continue button at first so they can select a vehicle type before moving forward.
        binding.btnContinue.setEnabled(false);
        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        initVehicleRecycler();

        binding.btnContinue.setOnClickListener(v -> {
            // Go to next activity.
            startActivity(new Intent(this, TransporterLocationActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    public void initVehicleRecycler() {
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
    }
}