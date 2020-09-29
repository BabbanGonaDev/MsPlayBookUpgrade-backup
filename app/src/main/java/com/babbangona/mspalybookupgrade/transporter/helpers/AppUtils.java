package com.babbangona.mspalybookupgrade.transporter.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class AppUtils {

    public final static Type stringType = new TypeToken<List<String>>() {
    }.getType();

    public static final String[] bank_names = new String[]{"Access Bank",
            "ALAT by WEMA",
            "ASO Savings and Loans",
            "Citibank Nigeria",
            "Ecobank Nigeria",
            "Ekondo Microfinance Bank",
            "Fidelity Bank",
            "First Bank of Nigeria",
            "First City Monument Bank",
            "Guaranty Trust Bank",
            "Heritage Bank",
            "Jaiz Bank",
            "Keystone Bank",
            "Parallex Bank",
            "Polaris Bank",
            "Providus Bank",
            "Stanbic IBTC Bank",
            "Standard Chartered Bank",
            "Sterling Bank",
            "Suntrust Bank",
            "Union Bank of Nigeria",
            "United Bank For Africa",
            "Unity Bank",
            "Wema Bank",
            "Zenith Bank"};

    public static final String[] phone_prefix = new String[]{"0701",
            "0702", "0703", "0704", "0705", "0706", "0707", "0708",
            "0709", "0802", "0803", "0804", "0805", "0806", "0807",
            "0808", "0809", "0810", "0811", "0812", "0813", "0814",
            "0815", "0816", "0817", "0818", "0819", "0909", "0908",
            "0901", "0902", "0903", "0904", "0905", "0906", "0907"};

    public final static String bg_cards_location = "MsPlaybookPictures/Transporter_Activities/BG Cards";

    public final static String facial_captures_location = "MsPlaybookPictures/Transporter_Activities/Facial Captures";

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

    public static boolean compressImage(String file_name, String file_location) {
        String storage_state = Environment.getExternalStorageState();

        if (storage_state.equals(Environment.MEDIA_MOUNTED)) {
            File img_dir = new File(Environment.getExternalStorageDirectory().getPath(), file_location);
            if (!img_dir.exists() && !img_dir.mkdirs()) {
                //Unable to create directory.
            } else {
                Bitmap new_bitmap = BitmapFactory.decodeFile(img_dir.getPath() + File.separator + file_name);

                if (new_bitmap != null) {
                    FileOutputStream compressed;
                    try {
                        compressed = new FileOutputStream(img_dir.getPath() + File.separator + file_name);
                        new_bitmap.compress(Bitmap.CompressFormat.JPEG, 70, compressed);
                        return true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //Not fully sure if this is needed.
                        return false;
                    }
                } else {

                    return false;
                }
            }
        }

        return false;
    }

    public static String getDeviceID(Context mCtx) {
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(mCtx).getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                device_id = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
                device_id = "";
            }
            if (device_id == null) {
                device_id = "";
            }
        } else {
            device_id = "";
        }
        return device_id;
    }
}
