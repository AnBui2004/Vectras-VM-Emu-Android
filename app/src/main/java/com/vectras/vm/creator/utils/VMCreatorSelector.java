package com.vectras.vm.creator.utils;

import android.app.Activity;
import android.content.Context;

import com.vectras.vm.R;
import com.vectras.vm.creator.configs.ListManager;
import com.vectras.vm.utils.UniversalPickerDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class VMCreatorSelector {
    private static final String TAG = "VMCreatorSelector";

    public static void cpu(Activity activity, String arch, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.cpus(activity, arch), position, callback, activity.getString(R.string.processor));
    }

    public static HashMap<String, Object> getCpu(Context context, String arch, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.cpus(context, arch);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void cpuCore(Activity activity, String arch, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.cores(arch), position, callback, activity.getString(R.string.core));
    }

    public static int getCpuCorePosition(int core) {
        if (core > 8) return 7;
        if (core > 2) return core - 2;
        return core - 1;
    }

    public static HashMap<String, Object> getCpuCore(String arch, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.cores(arch);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void cpuThread(Activity activity, String arch, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.threads(arch), position, callback, activity.getString(R.string.thread));
    }

    public static HashMap<String, Object> getMachine(Context context, String arch, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.machines(context, arch);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void machine(Activity activity, String arch, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.machines(activity, arch), position, callback, activity.getString(R.string.machine));
    }

    public static void memory(Activity activity, String arch, boolean limit32bit, int position, boolean markSelected, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.memoryCapacity(activity, arch, limit32bit), position, callback, activity.getString(R.string.memory_capacity_mb), markSelected);
    }

    public static void graphicsCard(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.graphicCards(activity), position, callback, activity.getString(R.string.cards));
    }

    public static HashMap<String, Object> getGraphicsCard(Context context, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.graphicCards(context);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void networkCard(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.networkCards(activity), position, callback, activity.getString(R.string.cards));
    }

    public static HashMap<String, Object> getNetworkCard(Context context, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.networkCards(context);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void usbController(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.usbControllers(activity), position, callback, activity.getString(R.string.controllers));
    }

    public static HashMap<String, Object> getUsbController(Context context, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.usbControllers(context);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void mouse(Activity activity, String arch, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.mouseTypes(activity, arch), position, callback, activity.getString(R.string.types));
    }

    public static HashMap<String, Object> getMouse(Context context, String arch, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.mouseTypes(context, arch);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void keyboard(Activity activity, String arch, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.keyboardTypes(activity, arch), position, callback, activity.getString(R.string.types));
    }

    public static HashMap<String, Object> getKeyboard(Context context, String arch, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.keyboardTypes(context, arch);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void soundCard(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.soundCards(activity), position, callback, activity.getString(R.string.cards));
    }

    public static HashMap<String, Object> getSoundCard(Context context, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.soundCards(context);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static HashMap<String, Object> getBootFrom(Context context, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.bootFrom(context);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void bootFrom(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.bootFrom(activity), position, callback, activity.getString(R.string.boot_from));
    }

    public static void accel(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.accelTypes(activity), position, callback, activity.getString(R.string.types));
    }

    public static HashMap<String, Object> getAccel(Context context, int position) {
        ArrayList<HashMap<String, Object>> list = ListManager.accelTypes(context);
        return list.get(position < 0 ? 0 : Math.min(position, list.size() - 1));
    }

    public static void showDialog(Activity activity, ArrayList<HashMap<String, Object>> list, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback, String title) {
        UniversalPickerDialog.show(activity, list, position, callback, title);
    }

    public static void showDialog(Activity activity, ArrayList<HashMap<String, Object>> list, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback, String title, boolean markSelected) {
        UniversalPickerDialog.show(activity, list, position, callback, title, markSelected);
    }
}
