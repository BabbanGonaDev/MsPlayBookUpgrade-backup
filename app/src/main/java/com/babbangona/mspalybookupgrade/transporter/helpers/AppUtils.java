package com.babbangona.mspalybookupgrade.transporter.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

}
