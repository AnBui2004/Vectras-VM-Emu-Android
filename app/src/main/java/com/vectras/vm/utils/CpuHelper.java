package com.vectras.vm.utils;

public class CpuHelper {
    static {
        System.loadLibrary("native_helper");
    }

    public native int getCpuCores();
    public native int getActiveCpuCores();
    public native int getCpuThreads();
    public native int check64Bit();

    public boolean is64Bit() {
        return check64Bit() == 1;
    }
}