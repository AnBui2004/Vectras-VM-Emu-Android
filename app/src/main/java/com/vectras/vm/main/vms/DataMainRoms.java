package com.vectras.vm.main.vms;

import com.google.gson.annotations.SerializedName;

// Edit in ExportRomActivity and VmListManager.isSameVmConfig too.
public class DataMainRoms {
    public String vmID = "";

    @SerializedName(
            value = "arch",
            alternate = { "imgArch" }
    )
    public String itemArch = "";

    // Personalize

    @SerializedName(
            value = "icon",
            alternate = { "imgIcon" }
    )
    public String itemIcon = "";

    @SerializedName(
            value = "title",
            alternate = { "imgName" }
    )
    public String itemName = "";

    // Board
    public int machine;
    public boolean nvirt; // Nested virtualization

    public int cpu;
    public int cores;
    public int threads;

    public int memory;

    // Dummy devices

    public boolean battery;
    public boolean wifi;

    // Storage

    @SerializedName(
            value = "drive",
            alternate = { "imgPath" }
    )
    public String itemPath = "";

    public String hd1 = "";


    @SerializedName(
            value = "cdrom",
            alternate = { "imgCdrom" }
    )
    public String imgCdrom = "";

    public String cdrom1 = "";


    public String fda = "";

    public String fdb = "";


    public boolean sharedFolder;

    // Input devices

    public int mouse;

    public int keyboard;

    // Graphics

    public int graphicCard;

    // Network

    public int networkCard;

    // Sound

    public int soundCard;

    // Firmware

    public int bootFrom;

    public boolean isShowBootMenu;

    public boolean isUseLocalTime = true;

    public boolean isUseUefi;

    public boolean isUseDefaultBios = true;

    // Acceleration

    public int accel;

    // Advanced

    @SerializedName(
            value = "qemu",
            alternate = { "imgExtra" }
    )
    public String itemExtra = "";

    // Deprecated

    @Deprecated
    public int qmpPort;
}
