package com.vectras.vm.manager;

import android.content.Context;

import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.settings.ItemSettingsSelector;
import com.vectras.vterm.Terminal2;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QemuManager {
    public static final int DEFAULT_REFRESH_RATE = 60;
    public static final int DEFAULT_LOW_REFRESH_RATE = 30;

    // Probing runs "qemu-system-* -vnc help" in a proot shell (1-3s) and
    // the answer cannot change while the app runs, so cache it per arch.
    private static final Map<String, Object> refreshRateSupportCache = new HashMap<>();

    public static boolean isSupportSetRefreshRate(Context context) {
        String arch = MainSettingsManager.getArch(context);
        File file = new File(getQemuExecutablePath(context, arch));

        if (!file.exists()) return false;

        // Users can reinstall it, so it needs to be checked.
        String fingerprint = file.length() + "_" + file.lastModified();

        synchronized (refreshRateSupportCache) {
            Object cached = refreshRateSupportCache.get(arch);
            Object cachedMd5 = refreshRateSupportCache.get(arch + "fingerprint");
            if (cached instanceof Boolean && fingerprint.equals(cachedMd5)) {
                return (boolean) cached;
            }
        }
        boolean supported = new Terminal2(context).executeOnThisThread(getQemuExecutableFile(context, arch) + " -vnc help").contains("refresh-rate");
        synchronized (refreshRateSupportCache) {
            refreshRateSupportCache.put(arch, supported);
            refreshRateSupportCache.put(arch + "fingerprint", fingerprint);
        }
        return supported;
    }

    public static boolean isSupportAcpiTable(Context context) {
        String currentArch = MainSettingsManager.getArch(context);

        return currentArch.equals(MainSettingsManager.X86_64_ARCH) || currentArch.equals(MainSettingsManager.I386_ARCH);
    }

    public static int getAppropriateRefreshRate(Context context, String params, int max) {
        int result = DEFAULT_REFRESH_RATE;

        if (params.contains("-vga vmware")
                || params.contains(" vmware-svga")
                || params.contains("-vga qxl")
                || params.contains(" qxl-vga")) result = 75;

        if (params.contains(" virio-vga") || params.contains(" virio-gpu")) result = max;

        int refreshRateSetting = Integer.parseInt(ItemSettingsSelector.getVncRefreshRateValue(MainSettingsManager.getVncRefreshRate(context)));

        result = Math.min(result, refreshRateSetting);

        return result;
    }

    public static String getQemuExecutablePath(Context context, String arch) {
        return context.getFilesDir() + "/distro/usr/local/bin/" + getQemuExecutableFile(context, arch);
    }

    public static String getQemuExecutableFile(Context context, String arch) {
        return switch (arch) {
            case MainSettingsManager.I386_ARCH -> "qemu-system-i386";
            case MainSettingsManager.ARM64_ARCH -> "qemu-system-aarch64";
            case MainSettingsManager.PPC_ARCH -> "qemu-system-ppc";
            default -> "qemu-system-x86_64";
        };
    }

    public static String getQemuExecutableFile(Context context) {
        return switch (MainSettingsManager.getArch(context)) {
            case MainSettingsManager.I386_ARCH -> "qemu-system-i386";
            case MainSettingsManager.ARM64_ARCH -> "qemu-system-aarch64";
            case MainSettingsManager.PPC_ARCH -> "qemu-system-ppc";
            default -> "qemu-system-x86_64";
        };
    }
}
