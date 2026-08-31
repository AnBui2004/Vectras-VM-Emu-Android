package com.vectras.vm.main.vms;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.transition.MaterialFadeThrough;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vectras.vm.AppConfig;
import com.vectras.vm.R;
import com.vectras.vm.VMManager;
import com.vectras.vm.databinding.FragmentHomeVmsBinding;
import com.vectras.vm.main.core.Event;
import com.vectras.vm.main.core.SharedData;
import com.vectras.vm.main.core.SharedViewModel;
import com.vectras.vm.utils.DeviceUtils;
import com.vectras.vm.utils.DialogUtils;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.PermissionUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VmsFragment extends Fragment {
    private final String TAG = "VmsFragment";
    FragmentHomeVmsBinding binding;
    private final List<DataMainRoms> data = new ArrayList<>();
    private VmsHomeAdapter vmsHomeAdapter;
    private int spanCount = 0;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialFadeThrough());
        setReturnTransition(new MaterialFadeThrough());
        setExitTransition(new MaterialFadeThrough());
        setReenterTransition(new MaterialFadeThrough());
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAndLoad();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeVmsBinding.inflate(inflater, container, false);
        spanCount = requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.rvRomlist.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        if (!isAdded()) return;
        vmsHomeAdapter = new VmsHomeAdapter(requireActivity(), data);
        binding.rvRomlist.setAdapter(vmsHomeAdapter);

        binding.bnRomstore.setOnClickListener(v -> sharedViewModel.openRomStore.setValue(new Event<>(true)));

        binding.bnRepair.setOnClickListener(V -> {
            // The repair walk scans every VM folder and rewrites
            // roms-data.json; do it off the UI thread and report back when done.
            executor.execute(() -> {
                VMManager.startFixRomsDataJson();
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        VMManager.fixRomsDataJsonResult(requireActivity()));
            });
        });

        binding.wrlRomlist.setOnRefreshListener(() -> {
            checkAndLoad();
            binding.wrlRomlist.setRefreshing(false);
        });

        //Loaded in onResume
        //checkAndLoad();
    }

    private void loadDataVbi() {

        if (!FileUtils.isFileExists(AppConfig.romsdatajson))
            FileUtils.writeToFile(AppConfig.maindirpath, "roms-data.json", "[]");

        executor.execute(() -> {
            List<DataMainRoms> tempdata = new ArrayList<>();

            try {
                if (!isAdded()) return;

                Gson gson = new Gson();
                Type listType = new TypeToken<List<DataMainRoms>>(){}.getType();

                String json = FileUtils.readFromFile(requireActivity(),
                        new File(AppConfig.romsdatajson));

                tempdata = gson.fromJson(json, listType);

                SharedData.dataVms.clear();
                SharedData.dataVms.addAll(tempdata);

                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    binding.lnError.setVisibility(View.GONE);
                    sharedViewModel.onVmsLoaded.setValue(new Event<>(true));
                });
            } catch (Exception e) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    binding.lnError.setVisibility(View.VISIBLE);
                    sharedViewModel.onVmsLoaded.setValue(new Event<>(false));
                });
                Log.e(TAG, "loadDataVbi: ", e);
            }

            if (!isAdded()) return;
            List<DataMainRoms> finalTempdata = tempdata;
            requireActivity().runOnUiThread(() -> {
                binding.lnLoad.setVisibility(View.GONE);
                if (finalTempdata == null || finalTempdata.isEmpty()) {
                    binding.rvRomlist.setVisibility(View.GONE);
                    binding.lnNothinghere.setVisibility(View.VISIBLE);
                    sharedViewModel.onVmsLoaded.setValue(new Event<>(false));
                } else {
                    binding.rvRomlist.setVisibility(View.VISIBLE);
                    binding.lnNothinghere.setVisibility(View.GONE);
                    vmsHomeAdapter.updateData(finalTempdata);
                }
            });
        });
    }

    private void checkAndLoad() {
        if (!isAdded()) return;
        if (PermissionUtils.storagepermission(requireActivity(), true)) {
            loadDataVbi();
            executor.execute(() -> {
                if (!isAdded()) return;
                if (DeviceUtils.isStorageLow(requireActivity(), true)) {
                    if (!isAdded()) return;
                    requireActivity().runOnUiThread(() -> DialogUtils.oneDialog(requireActivity(),
                            getResources().getString(R.string.oops),
                            getResources().getString(R.string.very_low_available_storage_space_content),
                            getResources().getString(R.string.ok),
                            true,
                            R.drawable.warning_48px,
                            true,
                            null,
                            () -> {
                                if (DeviceUtils.isStorageLow(requireActivity(), true))
                                    requireActivity().finish();
                            }));
                }
            });
        }
    }

    public void refresh() {
        if (!isAdded()) return;

        int newSpanCount = requireActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        if (spanCount != newSpanCount) {
            spanCount = newSpanCount;
            binding.rvRomlist.setLayoutManager(new GridLayoutManager(getContext(), newSpanCount));
        }
        checkAndLoad();
    }
}