package com.vectras.vm.creator;

import android.content.Context;

import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.R;
import com.vectras.vm.utils.CpuHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ListManager {
    public static ArrayList<HashMap<String, Object>> bootFrom(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, context.getString(R.string.defaulttext), "");
        putToList(list, context.getString(R.string.hard_disk), "c");
        putToList(list, context.getString(R.string.cdrom), "d");
        putToList(list, context.getString(R.string.floppy_disk), "a");
        putToList(list, context.getString(R.string.network), "n");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cpus(Context context, String arch) {
        return switch (arch) {
            case MainSettingsManager.ARM64_ARCH -> cpusArm64(context);
            case MainSettingsManager.PPC_ARCH -> cpusPpc(context);
            default -> cpusX8664(context);
        };
    }

    public static ArrayList<HashMap<String, Object>> cpusX8664(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, context.getString(R.string.defaulttext), "");
        putToList(list, context.getString(R.string.qemu64), "qemu64");
        putToList(list, context.getString(R.string.qemu32), "qemu32");
        putToList(list, context.getString(R.string.base_cpu), "base");
        putToList(list, context.getString(R.string.max_cpu), "max");
        //putToList(list, context.getString(R.string.kvm64), "kvm64");
        //putToList(list, context.getString(R.string.kvm32), "kvm32");
        //putToList(list, context.getString(R.string.host_cpu), "host");
        putToList(list, context.getString(R.string.intel_snow_ridge), "Snowridge");
        putToList(list, context.getString(R.string.intel_cooper_lake), "Cooperlake");
        putToList(list, context.getString(R.string.intel_cascade_lake_server), "Cascadelake-Server");
        putToList(list, context.getString(R.string.intel_knights_mill), "KnightsMill");
        putToList(list, context.getString(R.string.intel_denverton), "Denverton");
        putToList(list, context.getString(R.string.intel_skylake_server), "Skylake-Server");
        putToList(list, context.getString(R.string.intel_skylake_client), "Skylake-Client");
        putToList(list, context.getString(R.string.intel_broadwell), "Broadwell");
        putToList(list, context.getString(R.string.intel_haswell), "Haswell");
        putToList(list, context.getString(R.string.intel_ivy_bridge), "IvyBridge");
        putToList(list, context.getString(R.string.intel_sandy_bridge), "SandyBridge");
        putToList(list, context.getString(R.string.intel_westmere), "Westmere");
        putToList(list, context.getString(R.string.intel_nehalem), "Nehalem");
        putToList(list, context.getString(R.string.intel_altom_n270), "n270");
        putToList(list, context.getString(R.string.intel_penryn), "Penryn");
        putToList(list, context.getString(R.string.intel_core_2_duo), "core2duo");
        putToList(list, context.getString(R.string.intel_core_duo), "coreduo");
        putToList(list, context.getString(R.string.intel_conroe), "Conroe");
        putToList(list, context.getString(R.string.intel_pentium_iii), "pentium3");
        putToList(list, context.getString(R.string.intel_pentium_ii), "pentium2");
        putToList(list, context.getString(R.string.intel_pentium), "pentium");
        putToList(list, context.getString(R.string.intel_486), "486");
        putToList(list, context.getString(R.string.amd_epyc_milan), "EPYC-Milan");
        putToList(list, context.getString(R.string.amd_epyc_rome), "EPYC-Rome");
        putToList(list, context.getString(R.string.amd_epyc), "EPYC");
        putToList(list, context.getString(R.string.amd_opteron_g5), "Opteron_G5");
        putToList(list, context.getString(R.string.amd_opteron_g4), "Opteron_G4");
        putToList(list, context.getString(R.string.amd_phenom), "phenom");
        putToList(list, context.getString(R.string.amd_opteron_g3), "Opteron_G3");
        putToList(list, context.getString(R.string.amd_opteron_g2), "Opteron_G2");
        putToList(list, context.getString(R.string.amd_opteron_g1), "Opteron_G1");
        putToList(list, context.getString(R.string.amd_athlon), "athlon");
        putToList(list, context.getString(R.string.hygon_dhyana), "Dhyana");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cpusArm64(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, context.getString(R.string.defaulttext), "max");
        putToList(list, context.getString(R.string.max_cpu), "max");
        //putToList(list, context.getString(R.string.host_cpu), "host");
        putToList(list, context.getString(R.string.cortex_a76), "cortex-a76");
        putToList(list, context.getString(R.string.cortex_a72), "cortex-a72");
        putToList(list, context.getString(R.string.cortex_a57), "cortex-a57");
        putToList(list, context.getString(R.string.cortex_a55), "cortex-a55");
        putToList(list, context.getString(R.string.cortex_a53), "cortex-a53");
        putToList(list, context.getString(R.string.cortex_a35), "cortex-a35");
        putToList(list, context.getString(R.string.cortex_a15), "cortex-a15");
        putToList(list, context.getString(R.string.cortex_a9), "cortex-a9");
        putToList(list, context.getString(R.string.cortex_a8), "cortex-a8");
        putToList(list, context.getString(R.string.cortex_a7), "cortex-a7");
        putToList(list, context.getString(R.string.cortex_m55), "cortex-m55");
        putToList(list, context.getString(R.string.cortex_m33), "cortex-m33");
        putToList(list, context.getString(R.string.cortex_m7), "cortex-m7");
        putToList(list, context.getString(R.string.cortex_m4), "cortex-m4");
        putToList(list, context.getString(R.string.cortex_m3), "cortex-m3");
        putToList(list, context.getString(R.string.cortex_m0), "cortex-m0");
        putToList(list, context.getString(R.string.cortex_r52), "cortex-r52");
        putToList(list, context.getString(R.string.cortex_r5f), "cortex-r5f");
        putToList(list, context.getString(R.string.cortex_r5), "cortex-r5");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cpusPpc(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, context.getString(R.string.defaulttext), "g3");
        putToList(list, context.getString(R.string.cpu_460ex), "460ex");
        putToList(list, context.getString(R.string.e500v2_cpu), "e500v2");
        putToList(list, context.getString(R.string.g4_cpu_turbo), "7457");
        putToList(list, context.getString(R.string.g4_cpu_basic), "g4");
        putToList(list, context.getString(R.string.g3_cpu), "g3");
        putToList(list, context.getString(R.string.g2_cpu_workstation), "604e");
        putToList(list, context.getString(R.string.g2_cpu_low_power), "603e");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cores(String arch) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        if (arch.equals(MainSettingsManager.PPC_ARCH)) {
            putToList(list, "1", "1");
            putToList(list, "2", "2");
        } else {
            CpuHelper cpuHelper = new CpuHelper();
            int cores = cpuHelper.getCpuCores();
            int addedCore = 1;
            while (addedCore < cores + 1 && addedCore <= 8) {
                if (addedCore == 1 || addedCore % 2 == 0)
                    putToList(list, String.valueOf(addedCore), String.valueOf(addedCore));
                addedCore++;
            }
        }

        return list;
    }

    public static ArrayList<HashMap<String, Object>> threads(String arch) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        putToList(list, "1", "1");

        if (!(arch.equals(MainSettingsManager.ARM64_ARCH)
                || arch.equals(MainSettingsManager.PPC_ARCH)))
            putToList(list, "2", "2");

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
