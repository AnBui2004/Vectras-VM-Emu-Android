package com.vectras.vm.settings;

import android.app.Activity;

import com.vectras.vm.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ListSettingsManager {
    public static ArrayList<HashMap<String, Object>> vncRefreshRate(Activity activity) {
        int max = DeviceUtils.getMaxRefreshRate(activity);
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, "20", "20");
        putToList(list, "25", "25");
        putToList(list, "30", "30");
        putToList(list, "45", "45");
        putToList(list, "60", "60");
        if (max >= 75) putToList(list, "75", "75");
        if (max >= 90) putToList(list, "90", "90");
        if (max >= 120) putToList(list, "120", "120");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> vncRefreshRate() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, "20", "20");
        putToList(list, "25", "25");
        putToList(list, "30", "30");
        putToList(list, "45", "45");
        putToList(list, "60", "60");
        putToList(list, "75", "75");
        putToList(list, "90", "90");
        putToList(list, "120", "120");
        return list;
    }

    public static void putToList
            (
                    ArrayList<HashMap<String, Object>> listMap,
                    String name,
                    String value
            ) {
        HashMap<String, Object> thisItem = new HashMap<>();
        thisItem.put("name", name);
        thisItem.put("value", value);
        listMap.add(thisItem);
    }
}
