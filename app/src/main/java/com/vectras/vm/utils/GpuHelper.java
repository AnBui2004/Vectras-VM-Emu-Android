package com.vectras.vm.utils;

public class GpuHelper {
    static {
        System.loadLibrary("native_helper");
    }

    public native boolean isAdreno();
}
