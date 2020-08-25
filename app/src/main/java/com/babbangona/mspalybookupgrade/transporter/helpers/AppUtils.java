package com.babbangona.mspalybookupgrade.transporter.helpers;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppUtils {

    public final static Type stringType = new TypeToken<List<String>>() {
    }.getType();

}
