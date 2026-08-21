package com.vectras.vm.main.core;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel  extends ViewModel {
    public MutableLiveData<Event<Boolean>> onVmsLoaded = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> onRomStoreLoaded = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> onSoftwareStoreLoaded = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> requestRomStoreLoad = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> requestSoftwareStoreLoad = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> openRomStore = new MutableLiveData<>();

    public MutableLiveData<Event<Boolean>> requestRefreshVmList = new MutableLiveData<>();
}
