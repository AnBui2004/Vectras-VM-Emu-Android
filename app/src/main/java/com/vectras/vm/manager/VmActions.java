package com.vectras.vm.manager;

import android.content.Context;
import android.net.Uri;

import com.vectras.qemu.Config;
import com.vectras.vm.VMManager;
import com.vectras.vm.main.core.MainConfigs;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.ImageUtils;

import java.io.File;

public class VmActions {
    public static boolean takeScreenshot(Context context, boolean isSaveToGallery) {
        boolean isSaved = QmpSender.takeScreenshot();
        if (isSaved) {
            try {
                if (isSaveToGallery) {
                    Uri imageFile = ImageUtils.saveToGallery(
                            context,
                            ImageUtils.ppmToBitmap(new File(VmFileManager.findScreenshotPpm(context, Config.vmID))),
                            String.valueOf(System.currentTimeMillis())
                    );

                    FileUtils.copyFileFromUri(context, imageFile, VmFileManager.getScreenshotPng(Config.vmID));
                } else {
                    ImageUtils.saveBitmapToPNGFile(
                            ImageUtils.ppmToBitmap(new File(VmFileManager.findScreenshotPpm(context, Config.vmID))),
                            VmFileManager.getPath(Config.vmID),
                            "screenshot.png"
                    );
                }

                if (FileUtils.isFileExists(VmFileManager.getThumbnail(Config.vmID)))
                    MainConfigs.refreshVmIds.add(Config.vmID);
            } catch (Exception e) {
                isSaved = false;
            }
        }

        VmFileManager.removeScreenshotPpm(context, Config.vmID);

        return isSaved;
    }

    public static boolean clone(DataMainRoms vmConfig) {
        String mainVMFolder = VmFileManager.getPath(vmConfig.vmID);
        File[] files = new File(mainVMFolder).listFiles();

        if (files == null) return false;

        String newId = VMManager.idGenerator();
        while (FileUtils.isFileExists(VmFileManager.quickGetPath(newId))) {
            newId = VMManager.idGenerator();
        }

        String newVMFolder = VmFileManager.getPath(newId);
        if (!FileUtils.createDirectory(newVMFolder))
            return false;

        boolean isCopied = true;
        for (File file : files) {
            if (!FileUtils.copyFile(file.getAbsolutePath(), newVMFolder))
                isCopied = false;
        }

        if (!isCopied) return false;

        vmConfig.vmID = newId;
        vmConfig.itemPath = vmConfig.itemPath.replace(mainVMFolder, newVMFolder);
        vmConfig.hd1 = vmConfig.hd1.replace(mainVMFolder, newVMFolder);
        vmConfig.imgCdrom = vmConfig.imgCdrom.replace(mainVMFolder, newVMFolder);
        vmConfig.cdrom1 = vmConfig.cdrom1.replace(mainVMFolder, newVMFolder);
        vmConfig.fda = vmConfig.fda.replace(mainVMFolder, newVMFolder);
        vmConfig.fdb = vmConfig.fdb.replace(mainVMFolder, newVMFolder);
        vmConfig.itemExtra = vmConfig.itemExtra.replace(mainVMFolder, newVMFolder);

        if (VMManager.addVM(vmConfig, -1)) {
            return true;
        } else {
            FileUtils.delete(newVMFolder);
            return false;
        }
    }
}
