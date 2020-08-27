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
