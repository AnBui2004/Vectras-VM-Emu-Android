package com.vectras.vm.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vectras.qemu.Config;
import com.vectras.vm.AppConfig;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.UniversalPickerDialog;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class VmListManager {
    public static ArrayList<HashMap<String, Object>> getAllVmForPickRunningNoVncSocketOnly(Context context) {
        ArrayList<HashMap<String, Object>> listVm = getAllVmForPickRunningOnly(context);
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < listVm.size(); i++) {
            if (!FileUtils.isFileExists(Config.getLocalVNCSocketPath(Objects.requireNonNull(listVm.get(i).get("value")).toString()))) {
                list.add(listVm.get(i));
            }
        }

        return list;
    }

    public static ArrayList<HashMap<String, Object>> getAllVmForPickRunningVncSocketOnly(Context context) {
        ArrayList<HashMap<String, Object>> listVm = getAllVmForPickRunningOnly(context);
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < listVm.size(); i++) {
            if (FileUtils.isFileExists(Config.getLocalVNCSocketPath(Objects.requireNonNull(listVm.get(i).get("value")).toString()))) {
                list.add(listVm.get(i));
            }
        }

        return list;
    }

    public static ArrayList<HashMap<String, Object>> getAllVmForPickRunningOnly(Context context) {
        ArrayList<HashMap<String, Object>> listVm = getAllVmForPick(context);
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < listVm.size(); i++) {
            if (FileUtils.isFileExists(Config.getLocalQMPSocketPath(Objects.requireNonNull(listVm.get(i).get("value")).toString()))) {
                list.add(listVm.get(i));
            }
        }

        return list;
    }

    public static ArrayList<HashMap<String, Object>> getAllVmForPick(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        List<DataMainRoms> data = getAllVm(context);

        for (int i = 0; i < data.size(); i++) {
            UniversalPickerDialog.putToList(list, data.get(i).itemName, data.get(i).vmID);
        }

        return list;
    }

    public static List<DataMainRoms> getAllVm(Context context) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<DataMainRoms>>(){}.getType();

        String json = FileUtils.readFromFile(context, new File(AppConfig.romsdatajson));

        return gson.fromJson(json, listType);
    }

    public static boolean isSameVmConfig(DataMainRoms config1, DataMainRoms config2) {
        if (config1.pin != config2.pin) return false;

        if (!config1.vmID.equals(config2.vmID)) return false;
        if (!config1.itemArch.equals(config2.itemArch)) return false;
        if (!config1.itemIcon.equals(config2.itemIcon)) return false;
        if (!config1.itemName.equals(config2.itemName)) return false;

        if (config1.machine != config2.machine) return false;
        if (config1.nvirt != config2.nvirt) return false;

        if (config1.cpu != config2.cpu) return false;
        if (config1.cores != config2.cores) return false;
        if (config1.threads != config2.threads) return false;

        if (config1.memory != config2.memory) return false;

        if (config1.battery != config2.battery) return false;
        if (config1.wifi != config2.wifi) return false;

        if (!config1.itemPath.equals(config2.itemPath)) return false;
        if (!config1.hd1.equals(config2.hd1)) return false;

        if (!config1.imgCdrom.equals(config2.imgCdrom)) return false;
        if (!config1.cdrom1.equals(config2.cdrom1)) return false;

        if (!config1.fda.equals(config2.fda)) return false;
        if (!config1.fdb.equals(config2.fdb)) return false;

        if (config1.sharedFolder != config2.sharedFolder) return false;

        if (config1.mouse != config2.mouse) return false;
        if (config1.keyboard != config2.keyboard) return false;

        if (config1.graphicCard != config2.graphicCard) return false;

        if (config1.networkCard != config2.networkCard) return false;

        if (config1.soundCard != config2.soundCard) return false;

        if (config1.bootFrom != config2.bootFrom) return false;
        if (config1.isShowBootMenu != config2.isShowBootMenu) return false;
        if (config1.isUseLocalTime != config2.isUseLocalTime) return false;
        if (config1.isUseUefi != config2.isUseUefi) return false;
        if (config1.isUseDefaultBios != config2.isUseDefaultBios) return false;

        if (config1.accel != config2.accel) return false;

        if (!config1.itemExtra.equals(config2.itemExtra)) return false;

        return true;
    }
}
