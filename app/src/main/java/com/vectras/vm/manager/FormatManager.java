package com.vectras.vm.manager;

public class FormatManager {
    public static boolean isHardDriveFileFormat(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".qcow2")
                || lower.endsWith(".img")
                || lower.endsWith(".vhd")
                || lower.endsWith(".vhdx")
                || lower.endsWith(".vdi")
                || lower.endsWith(".qcow")
                || lower.endsWith(".vmdk")
                || lower.endsWith(".vpc");
    }
    public static boolean isOpticalFileFormat(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".iso")
                || lower.endsWith(".img")
                || lower.endsWith(".cdr")
                || lower.endsWith(".toast");
    }
}
