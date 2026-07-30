package com.vectras.vm.main.core;

import com.vectras.vm.main.romstore.DataRoms;
import com.vectras.vm.main.vms.DataMainRoms;

import java.util.ArrayList;
import java.util.List;

public class SharedData {
    public static List<DataMainRoms> dataVms = new ArrayList<>();
    public static List<DataRoms> dataRomStore = new ArrayList<>();
    public static List<DataRoms> dataSoftwareStore = new ArrayList<>();
}
