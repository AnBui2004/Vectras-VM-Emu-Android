package com.vectras.vm.creator.editor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vectras.qemu.MainSettingsManager;
import com.vectras.qemu.utils.RamInfo;
import com.vectras.vm.R;
import com.vectras.vm.creator.configs.ListManager;
import com.vectras.vm.creator.utils.EditorUtils;
import com.vectras.vm.creator.utils.VMCreatorSelector;
import com.vectras.vm.databinding.CreatorAccelerationDialogBinding;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AccelerationConfigsDialog extends BottomSheetDialogFragment {
    final String TAG = "AccelerationConfigsDialog";

    String vmId;
    DataMainRoms configs;

    boolean isSave = true;

    int availableMemory = Integer.MAX_VALUE;
    int warningMemory = Integer.MAX_VALUE;

    public void setConfigs(DataMainRoms configs) {
        this.configs = configs;
        if (configs != null) {
            vmId = configs.vmID;
        }
    }

    CreatorAccelerationDialogBinding binding;

    @NonNull
    @Override
    public BottomSheetDialog onCreateDialog(Bundle savedInstanceState) {
        // This can happen after the app is freed from memory and then reopened.
        if (configs == null) {
            isSave = false;
            if (savedInstanceState == null) DialogUtils.oopsDialog(requireActivity(), getString(R.string.something_went_wrong));
            dismiss();
            return EditorUtils.getDummyDialog(requireActivity());
        }

        availableMemory = RamInfo.vectrasMemory(requireActivity());
        warningMemory = availableMemory / 100 * 20;

        binding = CreatorAccelerationDialogBinding.inflate(getLayoutInflater());

        BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
        dialog.setContentView(binding.getRoot());

        initialize();

        dialog.setOnShowListener(d -> {
            BottomSheetBehavior<FrameLayout> behavior = dialog.getBehavior();
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);
        });

        return dialog;
    }

    AccelerationConfigsDialogCallback callback;

    public void setOnDismiss(AccelerationConfigsDialogCallback callback) {
        this.callback = callback;
    }

    public interface AccelerationConfigsDialogCallback {
        void onDismiss(DataMainRoms configs);
    }

    public void onDismiss(@NonNull DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (callback != null && isSave) {
            save();
            callback.onDismiss(configs);
        }
    }

    // Auto save.
    public void onPause() {
        super.onPause();
        if (callback != null && isSave) {
            save();
            callback.onDismiss(configs);
        }
    }

    private void initialize() {
        if (!isAdded()) return;

        binding.sbvType.setOnClickListener(v -> VMCreatorSelector.accel(requireActivity(), configs.accel, ((position, name, value) -> {
            configs.accel = position;
            binding.sbvType.setSubtitle(name);
            binding.cpiCacheSize.setEnabled(position > 0);
            binding.tietCacheSize.setEnabled(position > 0);
        })));

        binding.tietCacheSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && Integer.parseInt(editable.toString()) >= warningMemory) {
                    binding.cpiCacheSize.setError(getString(R.string.capacity_too_large));
                } else {
                    binding.cpiCacheSize.setError(null);
                }
            }
        });

        binding.cpiCacheSize.setEndIconOnClickListener(v -> {
            // 7 = 128
            int postion = 7;
            boolean markSelected = false;

            if (binding.tietCacheSize.getText() != null && !binding.tietCacheSize.getText().toString().isEmpty()) {
                int current = Integer.parseInt(binding.tietCacheSize.getText().toString());
                int nearest = Integer.MAX_VALUE;
                ArrayList<HashMap<String, Object>> list = ListManager.memoryCapacity(requireContext(), MainSettingsManager.getArch(requireContext()), true);

                for (int i = 0; i < list.size(); i++) {
                    int distance = Math.abs(((int) list.get(i).get("value")) - current);
                    if (distance == 0) {
                        postion = i;
                        markSelected = true;
                        break;
                    } else if (distance < nearest) {
                        nearest = distance;
                        postion = i;
                    }
                }
            }


            VMCreatorSelector.memory(requireActivity(), MainSettingsManager.getArch(requireContext()), true, postion, markSelected, ((position, name, value) -> {
                binding.tietCacheSize.setText(value);
                binding.tietCacheSize.setSelection(value.length());
            }));
        });

        load();
    }

    private void load() {
        if (!isAdded()) return;

        binding.sbvType.setSubtitle(Objects.requireNonNull(VMCreatorSelector.getAccel(requireActivity(), configs.accel).get("name")).toString());
        if (configs.accelCacheSize > 0) binding.tietCacheSize.setText(String.valueOf(configs.accelCacheSize));

        binding.cpiCacheSize.setEnabled(configs.accel > 0);
        binding.tietCacheSize.setEnabled(configs.accel > 0);
    }

    private void save() {
        configs.accelCacheSize = binding.tietCacheSize.getText() == null || binding.tietCacheSize.getText().toString().isEmpty() ? 0 : Integer.parseInt(binding.tietCacheSize.getText().toString());
    }
}