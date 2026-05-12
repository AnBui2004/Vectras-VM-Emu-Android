package com.vectras.vm.creator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;
import com.vectras.vm.R;
import com.vectras.vm.databinding.DialogListSelectorLayoutBinding;
import com.vectras.vm.databinding.SimpleLayoutListViewWithCheckBinding;
import com.vectras.vm.utils.UniversalPickerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    public static HashMap<String, Object> getBootFrom(Context context, int position) {
        return ListManager.bootFrom(context).get(position);
    }

    public static void bootFrom(Activity activity, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback) {
        showDialog(activity, ListManager.bootFrom(activity), position, callback, activity.getString(R.string.boot_from));
    }

    public static void showDialog(Activity activity, ArrayList<HashMap<String, Object>> list, int position, UniversalPickerDialog.UniversalPickerDialogCallback callback, String title) {
        UniversalPickerDialog.show(activity, list, position, callback, title);
    }
}
