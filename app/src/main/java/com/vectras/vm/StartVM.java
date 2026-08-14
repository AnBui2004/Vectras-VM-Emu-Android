package com.vectras.vm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.vectras.qemu.Config;
import com.vectras.qemu.MainSettingsManager;
import com.vectras.qemu.utils.RamInfo;
import com.vectras.vm.creator.configs.ListManager;
import com.vectras.vm.creator.utils.VMCreatorSelector;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.manager.BatteryEmulatorManager;
import com.vectras.vm.manager.FirmwareManager;
import com.vectras.vm.manager.ParamManager;
import com.vectras.vm.manager.QemuManager;
import com.vectras.vm.manager.VmFileManager;
import com.vectras.vm.manager.WifiCardEmulatorManager;
import com.vectras.vm.settings.ItemSettingsSelector;
import com.vectras.vm.settings.SettingsData;
import com.vectras.vm.utils.CpuHelper;
import com.vectras.vm.utils.DeviceUtils;
import com.vectras.vm.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class StartVM {
    public static String cache;
    private static DataMainRoms vmConfigs;

    public static String env(Activity activity, DataMainRoms vmData) {
        if (VMManager.isVMRunning(activity, vmData.vmID)) return "";

        vmConfigs = vmData;

        if (VMManager.isNeedLoadMigrate() && FileUtils.isFileExists(VmFileManager.getSnapshotSh(Config.vmID))) {
            String snapshotParams = FileUtils.readAFile(VmFileManager.getSnapshotSh(Config.vmID)).replace("\n", "");
            if (VMManager.isthiscommandsafe(snapshotParams, activity)) {
                snapshotParams = removeQemuSystem(snapshotParams);
                snapshotParams = removeQmpParams(snapshotParams);
                snapshotParams = removeDisplayParams(snapshotParams);
                snapshotParams = getQmpParams() + " " + snapshotParams;
                snapshotParams = QemuManager.getQemuExecutableFile(activity) + " " + snapshotParams;
                snapshotParams += " " + getDisplayParams(activity, vmConfigs.itemExtra);
                if (!snapshotParams.contains("-incoming defer"))
                    snapshotParams += " -incoming defer";
                Log.d("StartVM.env", snapshotParams);

                FileUtils.copyFile(VmFileManager.getPath(vmConfigs.vmID, VmFileManager.COMPILED_BATERRY_ACPI_FILE_NAME), VmFileManager.getTempPath(activity, vmConfigs.vmID));
                FileUtils.copyFile(VmFileManager.getPath(vmConfigs.vmID, VmFileManager.COMPILED_WIFI_CARD_ACPI_FILE_NAME), VmFileManager.getTempPath(activity, vmConfigs.vmID));

                return snapshotParams;
            }
        }

        String extraParams = vmData.itemExtra;

        String machineParams = "";
        if (!ParamManager.hasMachine(extraParams)) {
            String machine = Objects.requireNonNull(VMCreatorSelector.getMachine(activity, MainSettingsManager.getArch(activity), vmData.machine).get("value")).toString();

            machineParams = "-M ";

            if (!machine.isEmpty()) {
                machineParams += machine;
            }

            if (
                    vmConfigs.hpet &&
                            (
                                    MainSettingsManager.getArch(activity).equals(MainSettingsManager.X86_64_ARCH) ||
                                            MainSettingsManager.getArch(activity).equals(MainSettingsManager.I386_ARCH)
                            )
            ) {
                if (!machineParams.equals("-M ")) machineParams += ",";
                machineParams += "hpet=off";
            }

            if (
                    vmConfigs.nvirt &&
                            !MainSettingsManager.getArch(activity).equals(MainSettingsManager.PPC_ARCH) &&
                            MainSettingsManager.getArch(activity).equals(MainSettingsManager.ARM64_ARCH)
            ) {
                if (!machineParams.equals("-M ")) machineParams += ",";
                machineParams += "virtualization=true";
            }

            machineParams += " ";
        }

        String cpuParams = "";
        if (!ParamManager.hasCpu(extraParams)) {
            String cpu = Objects.requireNonNull(VMCreatorSelector.getCpu(activity, MainSettingsManager.getArch(activity), vmData.cpu).get("value")).toString();

            String flags = "";
            if (
                    vmConfigs.nvirt &&
                            (
                                    MainSettingsManager.getArch(activity).equals(MainSettingsManager.X86_64_ARCH) ||
                                            MainSettingsManager.getArch(activity).equals(MainSettingsManager.I386_ARCH)
                            )
            ) {
                flags += ",+vmx,+svm";
            }

            if (!flags.isEmpty() && cpu.isEmpty()) {
                if (MainSettingsManager.getArch(activity).equals(MainSettingsManager.X86_64_ARCH)) {
                    cpu = "qemu64";
                } else if (MainSettingsManager.getArch(activity).equals(MainSettingsManager.I386_ARCH)) {
                    cpu = "qemu32";
                }
            }

            if (!cpu.isEmpty()) {
                cpuParams = " -cpu " + cpu + flags;
            }
        }


        if (!ParamManager.hasSmp(extraParams)) {
            CpuHelper cpuHelper = new CpuHelper();

            int cores = Integer.parseInt(Objects.requireNonNull(VMCreatorSelector.getCpuCore(MainSettingsManager.getArch(activity), vmConfigs.cores).get("value")).toString());
            int threads = Math.max(1, vmConfigs.threads + 1);

            if (cores * threads > cpuHelper.getCpuThreads()) {
                cpuParams += " -smp sockets=1,cores=" + cpuHelper.getCpuCores() + ",threads=1";
            } else {
                cpuParams += " -smp sockets=1,cores=" + cores + ",threads=" + threads;
            }
        }

        if (!cpuParams.isEmpty()) cpuParams += " ";


        String memoryParams = "";
        if (!ParamManager.hasMemory(extraParams)) {
            memoryParams = "-m ";
            if (vmConfigs.memory > 0) {
                memoryParams += vmConfigs.memory;
            } else {
                memoryParams += (int) (RamInfo.vectrasMemory(activity) / 100 * 80);
            }
        }

        String inputDevicesParams = "";
        String mouse = Objects.requireNonNull(VMCreatorSelector.getMouse(activity, vmData.mouse).get("value")).toString();
        if (!mouse.isEmpty()) {
            inputDevicesParams += " -device " + mouse;
        }
        String keyboard = Objects.requireNonNull(VMCreatorSelector.getKeyboard(activity, vmData.keyboard).get("value")).toString();
        if (!keyboard.isEmpty()) {
            inputDevicesParams += " -device " + keyboard;
        }
        if (!inputDevicesParams.isEmpty() && !ParamManager.hasUsb(extraParams)) inputDevicesParams = " -usb" + inputDevicesParams;

        String graphicsParams = "";
        String graphics = Objects.requireNonNull(VMCreatorSelector.getGraphicsCard(activity, vmData.graphicCard).get("value")).toString();
        if (!graphics.isEmpty()) {
            graphicsParams = graphics.equals(ListManager.NONE_VALUE) ? " -vga none" : " -device " + graphics;
        }

        String networkParams = "";
        String network = Objects.requireNonNull(VMCreatorSelector.getNetworkCard(activity, vmData.networkCard).get("value")).toString();
        if (!network.isEmpty()) {
            String netDevId = "netdev" + System.currentTimeMillis();

            networkParams = " -device " + network + ",netdev=" + netDevId + " -netdev user,id=" + netDevId;
        }

        String soundParams = "";
        String sound = Objects.requireNonNull(VMCreatorSelector.getSoundCard(activity, vmData.soundCard).get("value")).toString();
        if (!sound.isEmpty()) {
            String audioDevId = "audiodev" + System.currentTimeMillis();

            soundParams = " -device " +
                    sound +
                    (
                            sound.contains("intel-hda") ?
                                    " -device hda-duplex" :
                                    ""
                    ) +
                    ",audiodev=" +
                    audioDevId +
                    " -audiodev wav,id=" +
                    audioDevId +
                    ",out.frequency=48000,path=" +
                    VmFileManager.getAudioRaw(VectrasApp.getContext(), vmConfigs.vmID);
        }

        String bootFromParams = Objects.requireNonNull(VMCreatorSelector.getBootFrom(activity, vmData.bootFrom).get("value")).toString();
        String showBootMenuParams = vmData.isShowBootMenu ? "menu=on" : "";
        String bootParams = "";
        if (!bootFromParams.isEmpty() || !showBootMenuParams.isEmpty()) {
            bootParams = " -boot " + bootFromParams + (!bootFromParams.isEmpty() && !showBootMenuParams.isEmpty() ? "," : "") + showBootMenuParams + " ";
        }

        String accelParams = "";
        String accel = Objects.requireNonNull(VMCreatorSelector.getAccel(activity, vmData.accel).get("value")).toString();
        if (!accel.isEmpty()) {
            accelParams = " -accel " + ((vmData.accel > 1 && !DeviceUtils.is64bit()) ? Objects.requireNonNull(VMCreatorSelector.getAccel(activity, 1).get("value")).toString() : accel);
        }

        extraParams = machineParams + inputDevicesParams + cpuParams + memoryParams + bootParams + graphicsParams + networkParams + soundParams + accelParams + " " + extraParams;
        return env(activity, extraParams, vmData.itemPath, false);
    }

    public static String env(Activity activity, String extras, String img, boolean isQuickRun) {
        if (isQuickRun) {
            vmConfigs = new DataMainRoms();
            vmConfigs.isUseLocalTime = false;
        }

        String filesDir = activity.getFilesDir().getAbsolutePath();

        String[] qemu = new String[0];

        String bios = "";

        String finalextra = extras;

        ArrayList<String> params = new ArrayList<>(Arrays.asList(qemu));

        if (!isQuickRun) {
            if (MainSettingsManager.getArch(activity).equals("I386"))
                params.add("qemu-system-i386");
            else if (MainSettingsManager.getArch(activity).equals("X86_64"))
                params.add("qemu-system-x86_64");
            else if (MainSettingsManager.getArch(activity).equals("ARM64"))
                params.add("qemu-system-aarch64");
            else if (MainSettingsManager.getArch(activity).equals("PPC"))
                params.add("qemu-system-ppc");

            params.add(getQmpParams());

            String ifType;
            ifType = MainSettingsManager.getIfType(activity);

            String cdrom = "";
            String hdd0;
            String hdd1;

            if (!img.isEmpty()) {
                if (ifType.isEmpty()) {
                    hdd0 = "-hda";
                    hdd0 += " '" + img + "'";
                } else {
                    hdd0 = "-drive";
                    hdd0 += " media=disk";
                    hdd0 += ",if=" + ifType;
                    hdd0 += ",file='" + img + "'";

                    if ((MainSettingsManager.getArch(activity).equals("ARM64") && ifType.equals("ide")) || MainSettingsManager.getArch(activity).equals("PPC")) {
                        hdd0 = "-drive";
                        hdd0 += " media=disk";
                        hdd0 += ",file='" + img + "'";
                    }
                }
                params.add(hdd0);
            }

            if (!vmConfigs.hd1.isEmpty()) {
                hdd0 = "-drive";
                hdd0 += " media=disk";
                hdd0 += ",file='" + vmConfigs.hd1 + "'";

                params.add(hdd0);
            }

            if (!vmConfigs.imgCdrom.isEmpty()) {
                if (MainSettingsManager.getArch(activity).equals("ARM64")) {
                    cdrom += " -device";
                    cdrom += " nec-usb-xhci,id=usbopticaldiscreader0";
                    cdrom += " -device";
                    cdrom += " usb-storage,bus=usbopticaldiscreader0.0,drive=cdromdrive0";
                    cdrom += " -drive";
                    cdrom += " if=none,id=cdromdrive0,format=raw,media=cdrom,file='" + vmConfigs.imgCdrom + "'";
                } else {
                    if (!extras.contains("-cdrom ")) {
                        cdrom = "-cdrom";
                        cdrom += " '" + vmConfigs.imgCdrom + "'";
                    } else {
                        cdrom = "-drive";
                        cdrom += " media=cdromdrive1";
                        cdrom += ",file='" + vmConfigs.imgCdrom + "'";
                    }
                }
                params.add(cdrom);
            }

            if (!vmConfigs.cdrom1.isEmpty()) {
                String cdromParams;
                String controllerId = vmConfigs.imgCdrom.isEmpty() ? "usbopticaldiscreader0" : "usbopticaldiscreader1";

                if (MainSettingsManager.getArch(activity).equals("ARM64")) {
                    cdromParams = " -device";
                    cdromParams += " nec-usb-xhci,id=" + controllerId;
                    cdromParams += " -device";
                    cdromParams += " usb-storage,bus=" + controllerId + ".0,drive=cdromdrive1";
                    cdromParams += " -drive";
                    cdromParams += " if=none,id=cdromdrive1,format=raw,media=cdrom,file='" + vmConfigs.cdrom1 + "'";
                } else {
                    cdromParams = "-drive";
                    cdromParams += " media=cdromdrive1";
                    cdromParams += ",file='" + vmConfigs.cdrom1 + "'";
                }
                params.add(cdromParams);
            }

            File hdd1File = new File(filesDir + "/data/Vectras/hdd1.qcow2");

            if (hdd1File.exists()) {
                hdd1 = "-drive";
                hdd1 += " media=disk";
                hdd1 += ",file='" + hdd1File.getPath() + "'";

                params.add(hdd1);
            }

            if (!vmConfigs.fda.isEmpty() && !MainSettingsManager.getArch(activity).equals("ARM64")) {
                params.add("-drive if=floppy,file='" + vmConfigs.fda + "'");
            }

            if (!vmConfigs.fdb.isEmpty() && !MainSettingsManager.getArch(activity).equals("ARM64")) {
                params.add("-drive if=floppy,file='" + vmConfigs.fdb + "'");
            }

            if (vmConfigs.sharedFolder) {
                String driveParams = "-drive ";
                driveParams += "file=fat:rw:'"; //Disk Drives are always Read/Write
                driveParams += AppConfig.sharedFolder + "',format=raw";
                params.add(driveParams);
            }

            if (vmConfigs.isUseDefaultBios) {
                extractFirmware(activity);

                if (MainSettingsManager.getArch(activity).equals("PPC")) {
                    bios = "-L ";
                    bios += "pc-bios";
                } else if (MainSettingsManager.getArch(activity).equals("ARM64")) {
                    if (!FileUtils.isFileExists(VmFileManager.getPath(vmConfigs.vmID, "QEMU_VARS.img")))
                        FileUtils.copyFile(AppConfig.basefiledir + "QEMU_VARS.img", VmFileManager.getPath(vmConfigs.vmID));

                    bios = "-drive ";
                    bios += "file=" + AppConfig.basefiledir + "QEMU_EFI.img,format=raw,readonly=on,if=pflash";
                    bios += " -drive ";
                    bios += "file=" + VmFileManager.getPath(vmConfigs.vmID, "QEMU_VARS.img") + ",format=raw,if=pflash";
                } else if (vmConfigs.isUseUefi) {
                    String codeFile = "edk2-" + MainSettingsManager.getArch(activity).toLowerCase() + "-code.fd";
                    String varsFile = "edk2-" + MainSettingsManager.getArch(activity).toLowerCase() + "-vars.fd";

                    if (!FileUtils.isFileExists(VmFileManager.getPath(vmConfigs.vmID, varsFile)))
                        FileUtils.copyFile(AppConfig.basefiledir + varsFile, VmFileManager.getPath(vmConfigs.vmID));

                    bios = "-drive ";
                    bios += "file=" + AppConfig.basefiledir + codeFile + ",format=raw,readonly=on,if=pflash";
                    bios += " -drive ";
                    bios += "file=" + VmFileManager.getPath(vmConfigs.vmID, varsFile) + ",format=raw,if=pflash";
                } else {
                    bios = "-bios ";
                    bios += AppConfig.basefiledir + "bios-vectras.bin";
                }
            }

            if (vmConfigs.isUseLocalTime) {
                params.add("-rtc");
                params.add("base=localtime");
            }

            params.add(bios);
        }

        params.add(finalextra);

        if (isQuickRun) {
            params.add(getQmpParams());
        }

        params.add(getDisplayParams(activity, extras));

        if (VMManager.isNeedLoadMigrate()) {
            params.add("-incoming");
            params.add("defer");
        }

        if (vmConfigs.battery) {
            if (BatteryEmulatorManager.setup(activity, vmConfigs.vmID))
                params.add("-acpitable file=" + VmFileManager.getCompiledBatteryAcpi(activity, vmConfigs.vmID));
        }

        if (vmConfigs.wifi) {
            if (WifiCardEmulatorManager.setup(activity, vmConfigs.vmID))
                params.add("-acpitable file=" + VmFileManager.getCompiledWifiCardAcpi(activity, vmConfigs.vmID));
        }

        return String.join(" ", params);
    }

    public static String removeQemuSystem(String params) {
        return params.replaceAll("^qemu-system-\\S+\\s*", "");
    }


    public static String getQmpParams() {
        return "-qmp unix:" + Config.getLocalQMPSocketPath() + ",server,nowait";
    }

    public static String removeQmpParams(String params) {
        return params.replaceAll("(?<=\\s|^)-qmp\\s+(\"[^\"]+\"|\\S+)", "");
    }


    public static String getDisplayParams(Context context, String mainParams) {
        String params = "";

        if (MainSettingsManager.getVmUi(context).equals("VNC")) {
            if (!MainSettingsManager.getVncExternalPassword(context).isEmpty()) {
                params += "-object secret,id=vncpass,data=\"" + MainSettingsManager.getVncExternalPassword(context) + "\"";
            }

            params += (params.isEmpty() ? "" : " ") + "-vnc ";
            // Allow connections only from localhost using localsocket without a password
            if (MainSettingsManager.getVncExternal(context)) {
                String vncParams = Config.defaultVNCHost + ":" + Config.defaultVNCPort;

                if (!MainSettingsManager.getVncExternalPassword(context).isEmpty())
                    vncParams += ",password-secret=vncpass";

                params += vncParams;
            } else {
                params += "unix:" + Config.getLocalVNCSocketPath() + (MainSettingsManager.getVncLosslessQuality(context) ? ",lossy=off" : "");

                if (QemuManager.isSupportSetRefreshRate(context))
                    params += ",refresh-rate=" + ((context instanceof Activity activity) ? QemuManager.getAppropriateRefreshRate(activity, mainParams, DeviceUtils.getMaxRefreshRate(activity)) : Integer.parseInt(ItemSettingsSelector.getVncRefreshRateValue(MainSettingsManager.getVncRefreshRate(context))));
            }

            params += " -monitor vc";
        } else if (MainSettingsManager.getVmUi(context).equals("SPICE")) {
            params += "-spice port=6999,disable-ticketing=on";
        } else if (MainSettingsManager.getVmUi(context).equals("X11")) {
            params += "-display ";
            params += MainSettingsManager.getUseSdl(context) ? "sdl" : "gtk";
            if (SettingsData.opengl(context)) params += ",gl=on";
            params += " -monitor ";
            params += MainSettingsManager.getRunQemuWithXterm(context) ? "stdio" : "vc";
        }

        return params;
    }

    public static String removeDisplayParams(String params) {
        return params
                .replaceAll("(?<=\\s|^)-object\\s+secret,id=vncpass[^\\s]*", "")
                .replaceAll("(?<=\\s|^)-vnc\\b.*?(?=\\s+-monitor\\s+\\S+|$)", "")
                .replaceAll("(?<=\\s|^)-vnc\\b[^\\s]*", "")
                .replaceAll("(?<=\\s|^)-monitor\\s+\\S+", "")
                .replaceAll("(?<=\\s|^)-display\\b.*?(?=\\s+-monitor\\s+\\S+|$)", "")
                .replaceAll("(?<=\\s|^)-display\\s+\\S+", "")
                .replaceAll("(?<=\\s|^)-spice\\s+\\S+", "")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }

    public static void extractFirmware(Context context) {
        FirmwareManager.extract(context, vmConfigs.isUseUefi);
    }
}
