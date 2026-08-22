package com.vectras.vm.creator.configs;

import android.content.Context;

import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.R;
import com.vectras.vm.utils.CpuHelper;
import com.vectras.vm.utils.DeviceUtils;
import com.vectras.vm.utils.UniversalPickerDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class ListManager {
    public static String NONE_VALUE = "-1";

    public static ArrayList<HashMap<String, Object>> bootFrom(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.hard_disk), "c");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cdrom), "d");
        UniversalPickerDialog.putToList(list, context.getString(R.string.floppy_disk), "a");
        UniversalPickerDialog.putToList(list, context.getString(R.string.network), "n");
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
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.qemu64), "qemu64");
        UniversalPickerDialog.putToList(list, context.getString(R.string.qemu32), "qemu32");
        UniversalPickerDialog.putToList(list, context.getString(R.string.base_cpu), "base");
        UniversalPickerDialog.putToList(list, context.getString(R.string.max_cpu), "max");
        //UniversalPickerDialog.putToList(list, context.getString(R.string.kvm64), "kvm64");
        //UniversalPickerDialog.putToList(list, context.getString(R.string.kvm32), "kvm32");
        //UniversalPickerDialog.putToList(list, context.getString(R.string.host_cpu), "host");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_snow_ridge), "Snowridge");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_cooper_lake), "Cooperlake");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_cascade_lake_server), "Cascadelake-Server");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_knights_mill), "KnightsMill");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_denverton), "Denverton");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_skylake_server), "Skylake-Server");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_skylake_client), "Skylake-Client");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_broadwell), "Broadwell");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_haswell), "Haswell");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_ivy_bridge), "IvyBridge");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_sandy_bridge), "SandyBridge");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_westmere), "Westmere");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_nehalem), "Nehalem");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_altom_n270), "n270");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_penryn), "Penryn");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_core_2_duo), "core2duo");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_core_duo), "coreduo");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_conroe), "Conroe");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_pentium_iii), "pentium3");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_pentium_ii), "pentium2");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_pentium), "pentium");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_486), "486");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_epyc_milan), "EPYC-Milan");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_epyc_rome), "EPYC-Rome");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_epyc), "EPYC");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_opteron_g5), "Opteron_G5");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_opteron_g4), "Opteron_G4");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_phenom), "phenom");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_opteron_g3), "Opteron_G3");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_opteron_g2), "Opteron_G2");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_opteron_g1), "Opteron_G1");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_athlon), "athlon");
        UniversalPickerDialog.putToList(list, context.getString(R.string.hygon_dhyana), "Dhyana");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cpusArm64(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "max");
        UniversalPickerDialog.putToList(list, context.getString(R.string.max_cpu), "max");
        //UniversalPickerDialog.putToList(list, context.getString(R.string.host_cpu), "host");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a76), "cortex-a76");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a72), "cortex-a72");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a57), "cortex-a57");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a55), "cortex-a55");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a53), "cortex-a53");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a35), "cortex-a35");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a15), "cortex-a15");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a9), "cortex-a9");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a8), "cortex-a8");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_a7), "cortex-a7");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_m55), "cortex-m55");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_m33), "cortex-m33");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_m7), "cortex-m7");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_m4), "cortex-m4");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_m3), "cortex-m3");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_m0), "cortex-m0");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_r52), "cortex-r52");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_r5f), "cortex-r5f");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cortex_r5), "cortex-r5");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cpusPpc(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "g3");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cpu_460ex), "460ex");
        UniversalPickerDialog.putToList(list, context.getString(R.string.e500v2_cpu), "e500v2");
        UniversalPickerDialog.putToList(list, context.getString(R.string.g4_cpu_turbo), "7457");
        UniversalPickerDialog.putToList(list, context.getString(R.string.g4_cpu_basic), "g4");
        UniversalPickerDialog.putToList(list, context.getString(R.string.g3_cpu), "g3");
        UniversalPickerDialog.putToList(list, context.getString(R.string.g2_cpu_workstation), "604e");
        UniversalPickerDialog.putToList(list, context.getString(R.string.g2_cpu_low_power), "603e");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> cores(String arch) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        if (arch.equals(MainSettingsManager.PPC_ARCH)) {
            UniversalPickerDialog.putToList(list, "1", "1");
            UniversalPickerDialog.putToList(list, "2", "2");
        } else {
            CpuHelper cpuHelper = new CpuHelper();
            int cores = cpuHelper.getCpuCores();
            int addedCore = 1;
            while (addedCore < cores + 1 && addedCore <= 8) {
                if (addedCore == 1 || addedCore % 2 == 0)
                    UniversalPickerDialog.putToList(list, String.valueOf(addedCore), String.valueOf(addedCore));
                addedCore++;
            }
        }

        return list;
    }

    public static ArrayList<HashMap<String, Object>> threads(String arch) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, "1", "1");

        if (!(arch.equals(MainSettingsManager.ARM64_ARCH)
                || arch.equals(MainSettingsManager.PPC_ARCH)))
            UniversalPickerDialog.putToList(list, "2", "2");

        return list;
    }

    public static ArrayList<HashMap<String, Object>> machines(Context context, String arch) {
        return switch (arch) {
            case MainSettingsManager.ARM64_ARCH -> machinesArm64(context);
            case MainSettingsManager.PPC_ARCH -> machinesPpc(context);
            default -> machinesX8664(context);
        };
    }

    public static ArrayList<HashMap<String, Object>> machinesX8664(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.i440fx), "pc");
        UniversalPickerDialog.putToList(list, context.getString(R.string.q35), "q35");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> machinesArm64(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.virt), "virt");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> machinesPpc(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.mac99), "mac99");
        UniversalPickerDialog.putToList(list, context.getString(R.string.g3beige), "g3beige");
        UniversalPickerDialog.putToList(list, context.getString(R.string.pegasos2), "pegasos2");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amigaone), "amigaone");
        UniversalPickerDialog.putToList(list, context.getString(R.string.sam460ex), "sam460ex");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> networkCards(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.none), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_e1000e_82574l), "e1000e");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_igb_82576), "igb");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_e1000_82540em), "e1000");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_e1000_82545em), "e1000-82545em");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_e1000_82544gc), "e1000-82544gc");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82801), "i82801");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82562), "i82562");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82559er), "i82559er");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82558b), "i82558b");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82557c), "i82557c");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82551), "i82551");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_i82550), "i82550");
        UniversalPickerDialog.putToList(list, context.getString(R.string.realtek_rtl8139), "rtl8139");
        UniversalPickerDialog.putToList(list, context.getString(R.string.amd_pcnet), "pcnet");
        UniversalPickerDialog.putToList(list, context.getString(R.string.novell_ne2000), "ne2k_pci");
        UniversalPickerDialog.putToList(list, context.getString(R.string.vmware_vmxnet3), "vmxnet3");
        UniversalPickerDialog.putToList(list, context.getString(R.string.virtio_net), "virtio-net-pci");
        UniversalPickerDialog.putToList(list, context.getString(R.string.virtio_net_transitional), "virtio-net-pci-transitional");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> memoryCapacity(Context context, String arch, boolean limit32bit) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, 1);
        UniversalPickerDialog.putToList(list, 2);
        UniversalPickerDialog.putToList(list, 4);
        UniversalPickerDialog.putToList(list, 8);
        UniversalPickerDialog.putToList(list, 16);
        UniversalPickerDialog.putToList(list, 32);
        UniversalPickerDialog.putToList(list, 64);
        UniversalPickerDialog.putToList(list, 128);
        UniversalPickerDialog.putToList(list, 256);
        UniversalPickerDialog.putToList(list, 512);
        UniversalPickerDialog.putToList(list, 768);
        UniversalPickerDialog.putToList(list, 1024);
        UniversalPickerDialog.putToList(list, 1280);
        UniversalPickerDialog.putToList(list, 1536);
        UniversalPickerDialog.putToList(list, 1792);
        UniversalPickerDialog.putToList(list, 2048);
        UniversalPickerDialog.putToList(list, 2304);
        UniversalPickerDialog.putToList(list, 2560);
        UniversalPickerDialog.putToList(list, 2816);
        UniversalPickerDialog.putToList(list, 3072);
        UniversalPickerDialog.putToList(list, 3328);
        UniversalPickerDialog.putToList(list, 3584);
        UniversalPickerDialog.putToList(list, 3840);
        UniversalPickerDialog.putToList(list, 4096);

        if (limit32bit || (!arch.equals(MainSettingsManager.X86_64_ARCH) && !arch.equals(MainSettingsManager.ARM64_ARCH)))
            return list;

        UniversalPickerDialog.putToList(list, 4352);
        UniversalPickerDialog.putToList(list, 4608);
        UniversalPickerDialog.putToList(list, 4864);
        UniversalPickerDialog.putToList(list, 5120);
        UniversalPickerDialog.putToList(list, 5376);
        UniversalPickerDialog.putToList(list, 5632);
        UniversalPickerDialog.putToList(list, 5888);
        UniversalPickerDialog.putToList(list, 6144);
        UniversalPickerDialog.putToList(list, 6400);
        UniversalPickerDialog.putToList(list, 6656);
        UniversalPickerDialog.putToList(list, 6912);
        UniversalPickerDialog.putToList(list, 7168);
        UniversalPickerDialog.putToList(list, 7424);
        UniversalPickerDialog.putToList(list, 7680);
        UniversalPickerDialog.putToList(list, 7936);
        UniversalPickerDialog.putToList(list, 8192);
        return list;
    }

    public static ArrayList<HashMap<String, Object>> soundCards(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.none), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_hd_audio_controller_ich9), "ich9-intel-hda");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_hd_audio_controller_ich6), "intel-hda");
        UniversalPickerDialog.putToList(list, context.getString(R.string.crystal_semiconductor_cs4231a), "cs4231a");
        UniversalPickerDialog.putToList(list, context.getString(R.string.intel_82801aa_ac97_audio), "ac97");
        UniversalPickerDialog.putToList(list, context.getString(R.string.ensoniq_audiopci_es1370), "ES1370");
        UniversalPickerDialog.putToList(list, context.getString(R.string.creative_sound_blaster_16), "sb16");
        UniversalPickerDialog.putToList(list, context.getString(R.string.yamaha_ym3812_opl2), "adlib");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> graphicCards(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.none), NONE_VALUE);
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.standard_vga), "VGA");
        UniversalPickerDialog.putToList(list, context.getString(R.string.vmware_svga), "vmware-svga");
        UniversalPickerDialog.putToList(list, context.getString(R.string.cirrus_vga), "cirrus-vga");
        UniversalPickerDialog.putToList(list, context.getString(R.string.qxl_vga), "qxl-vga");
        UniversalPickerDialog.putToList(list, context.getString(R.string.virtio_vga), "virtio-vga");
        UniversalPickerDialog.putToList(list, context.getString(R.string.bochs), "bochs-display");
        UniversalPickerDialog.putToList(list, context.getString(R.string.ati), "ati-vga");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> usbControllers(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.none), NONE_VALUE);
        UniversalPickerDialog.putToList(list, context.getString(R.string.defaulttext), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.ehci), "usb-ehci");
        UniversalPickerDialog.putToList(list, context.getString(R.string.qemu_xhci), "qemu-xhci");
        UniversalPickerDialog.putToList(list, context.getString(R.string.nec_xhci), "nec-usb-xhci");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> mouseTypes(Context context, String arch) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, arch.equals(MainSettingsManager.ARM64_ARCH) ? context.getString(R.string.none) : context.getString(R.string.ps_2), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.usb), "usb-mouse");
        UniversalPickerDialog.putToList(list, context.getString(R.string.usb_tablet), "usb-tablet");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> keyboardTypes(Context context, String arch) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, arch.equals(MainSettingsManager.ARM64_ARCH) ? context.getString(R.string.none) : context.getString(R.string.ps_2), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.usb), "usb-kbd");
        return list;
    }

    public static ArrayList<HashMap<String, Object>> accelTypes(Context context) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        UniversalPickerDialog.putToList(list, context.getString(R.string.none), "");
        UniversalPickerDialog.putToList(list, context.getString(R.string.tcg_single_thread), "tcg,thread=single");
        if (DeviceUtils.is64bit()) UniversalPickerDialog.putToList(list, context.getString(R.string.tcg_multi_thread), "tcg,thread=multi");
        return list;
    }
}
