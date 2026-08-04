package com.vectras.vm.manager;

import java.util.regex.Pattern;

public class ParamManager {
    public static boolean hasMachine(String param) {
        return Pattern.compile("(?<![\\w-])--?(M|machine)\\s[a-zA-Z0-9._-]*(?![\\w=-])").matcher(param).find();
    }

    public static boolean hasCpu(String param) {
        return Pattern.compile("(?<![\\w-])--?cpu\\s[a-zA-Z0-9_-]*(?![\\w=-])").matcher(param).find();
    }

    public static boolean hasSmp(String param) {
        return Pattern.compile("(?<![\\w-])--?smp\\s[a-zA-Z0-9,=]*(?![\\w=-])").matcher(param).find();
    }

    public static boolean hasMemory(String param) {
        return Pattern.compile("(?<![\\w-])--?m\\s+(\\d+)([a-zA-Z]*)(?![\\w=-])").matcher(param).find();
    }

    public static boolean hasUsb(String param) {
        return Pattern.compile("(?<=^|\\s)-usb(?=$|\\s)(?![\\w=-])").matcher(param).find();
    }

    public static boolean hasVga(String param) {
        return Pattern.compile("(?<![\\w-])--?vga\\s(std|cirrus|vmware|qxl|virtio)(?![\\w=-])").matcher(param).find() ||
                Pattern.compile("(?<![\\w-])--?device\\s(ati-vga|bochs-display|cirrus-vga|isa-cirrus-vga|isa-vga|qxl|qxl-vga|secondary-vga|svga|VGA|(virtio-gpu[a-z-]*)|(virtio-vga[a-z-]*)|vmware-svga)[a-zA-Z0-9,_]*(?![\\w=-])").matcher(param).find();
    }
}
