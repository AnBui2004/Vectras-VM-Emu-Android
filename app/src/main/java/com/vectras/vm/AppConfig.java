package com.vectras.vm;

import android.content.Context;
import android.os.Environment;

import com.vectras.qemu.Config;
import com.vectras.vm.utils.DeviceUtils;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.PackageUtils;

import java.io.File;

/**
 * @author dev
 */
public class AppConfig {

    // App Config
    public static String vectrasVersion;
    public static int vectrasVersionCode;
    public static final int standardSetupVersion = 922202607;
    public static final int coreSetupVersion = 0;
    public static String vectrasWebsite = "https://anbui.ovh/";
    public static String vectrasWebsiteRaw = "https://gitlab.com/anbui62/vectras-vm-android-emu/-/raw/master/web/";
    public static String vectrasWebsiteRawSecondary = "https://anbui.ovh/vec/db/";
    public static String bootstrapfileslink = vectrasWebsiteRaw + "data/setupfiles4.json";
    public static String community = vectrasWebsite + "community";
    public static String vectrasRaw = vectrasWebsiteRaw + "data/";
    public static String updateJson = vectrasRaw + "UpdateConfig.json";

    public static String gitHub = "https://github.com/AnBui2004/Vectras-VM-Emu-Android";

    // App config
    public static String datadirpath(Context activity) {
        File f = new File(activity.getExternalFilesDir("data") + "/Vectras");
        return activity.getExternalFilesDir("data") + "/Vectras";
        //return FileUtils.getExternalFilesDirectory(activity).getPath();
    }
    public static String internalDataDirPath = "/data/data/com.vectras.vm/files/";
    public static String basefiledir = "";
    public static String maindirpath = "/sdcard/Documents/VectrasVM/";
    public static String recyclebin = maindirpath + "RecycleBin/";
    //public static String basefiledir = datadirpath(SplashActivity.activity) + "/.qemu/";
    //public static String maindirpath = FileUtils.getExternalFilesDirectory(SplashActivity.activity).getPath() + "/";
    public static String sharedFolder = maindirpath + "SharedFolder/";
    public static String romsdatajson = maindirpath + "roms-data.json";
    public static String vmFolder = maindirpath + "roms/";
    public static String lastCrashLogPath = internalDataDirPath + "logs/lastcrash.txt";

    public static String neededPkgs() {
        if (DeviceUtils.isArm()) {
            return "sudo bash aria2 tar dwm xterm libslirp pixman" +
                    " spice libusbredirparser sndio-libs sdl2 sdl2_image-dev libepoxy-dev virglrenderer rdma-core fluxbox" +
                    " libusb libaio ncurses-libs curl libnfs gtk+3.0 libseccomp jack liburing pulseaudio" +
                    " mesa-dri-gallium mesa-vulkan-swrast vulkan-loader mesa-utils mesa-egl mesa-gbm mesa-vulkan-ati mesa-vulkan-broadcom mesa-vulkan-freedreno mesa-vulkan-panfrost" +
                    " capstone libcbor snappy lzo ndctl keyutils-libs vde2-libs libbpf linux-pam fuse3-libs libssh vte3 iasl cdrkit";
        } else {
            return "sudo bash aria2 tar dwm xterm libslirp pixman" +
                    " spice libusbredirparser sndio-libs sdl2 sdl2_image-dev libepoxy-dev virglrenderer rdma-core fluxbox" +
                    " libusb libaio ncurses-libs curl libnfs gtk+3.0 libseccomp jack liburing pulseaudio" +
                    " mesa-dri-gallium mesa-vulkan-swrast vulkan-loader mesa-utils mesa-egl" +
                    " capstone libcbor snappy lzo ndctl keyutils-libs vde2-libs libbpf linux-pam fuse3-libs libssh vte3 iasl cdrkit";
        }
    }

    public static String neededPkgs32bit() {
        if (DeviceUtils.isArm()) {
            return "sudo bash aria2 tar dwm xterm libslirp pixman" +
                    " spice libusbredirparser sndio-libs sdl2 sdl2_image-dev libepoxy-dev virglrenderer rdma-core fluxbox" +
                    " libusb libaio ncurses-libs curl libnfs gtk+3.0 libseccomp jack liburing pulseaudio" +
                    " mesa-dri-gallium mesa-vulkan-swrast vulkan-loader mesa-utils mesa-egl" +
                    " capstone libcbor snappy lzo ndctl keyutils-libs vde2-libs libbpf linux-pam fuse3-libs libssh vte3 iasl cdrkit";
        } else {
            return "sudo bash aria2 tar dwm xterm libslirp pixman" +
                    " spice libusbredirparser sndio-libs sdl2 sdl2_image-dev libepoxy-dev virglrenderer rdma-core fluxbox" +
                    " libusb libaio ncurses-libs curl libnfs gtk+3.0 libseccomp jack liburing pulseaudio" +
                    " mesa-dri-gallium mesa-vulkan-swrast vulkan-loader mesa-utils mesa-egl" +
                    " capstone libcbor snappy lzo ndctl keyutils-libs vde2-libs libbpf linux-pam fuse3-libs libssh vte3 libatomic iasl cdrkit";
        }
    }

    public static boolean needreinstallsystem = false;

    public static String virtIOWinUrl = "https://fedorapeople.org/groups/virt/virtio-win/direct-downloads/archive-virtio/virtio-win-0.1.285-1/virtio-win.iso";
    public static String virtIOWinUrlMd5 = "9e650d0e7c6e017a91ca299c8f7ed766";

    public static boolean isGmsAvailable = false;

    public static boolean forceDisableQuickLaunch;

    public static void setup(Context context) {
        vectrasVersion = PackageUtils.getThisVersionName(context);
        vectrasVersionCode = PackageUtils.getThisVersionCode(context);
        internalDataDirPath = context.getFilesDir().getPath() + "/";
        basefiledir = datadirpath(context) + "/.qemu/";
        maindirpath = FileUtils.getExternalFilesDirectory(context).getPath() + "/";
        sharedFolder = maindirpath + "SharedFolder/";
        romsdatajson = maindirpath + "roms-data.json";
        vmFolder = maindirpath + "roms/";
        recyclebin = maindirpath + "RecycleBin/";
        lastCrashLogPath = internalDataDirPath + "logs/lastcrash.txt";

        Config.cacheDir = context.getCacheDir().getAbsolutePath();
        Config.storagedir = Environment.getExternalStorageDirectory().toString();
    }

    public static void switchToSecondaryServer() {
        vectrasWebsiteRaw = vectrasWebsiteRawSecondary;
        vectrasRaw = vectrasWebsiteRaw + "data/";
        updateJson = vectrasRaw + "UpdateConfig.json";
        bootstrapfileslink = vectrasWebsiteRaw + "data/setupfiles4.json";
    }
}
