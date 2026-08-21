package com.vectras.vm.creator.editor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.R;
import com.vectras.vm.creator.utils.EditorUtils;
import com.vectras.vm.creator.utils.VMCreatorSelector;
import com.vectras.vm.databinding.CreatorInputDevicesDialogBinding;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.manager.ParamManager;
import com.vectras.vm.utils.DialogUtils;

import java.util.Objects;

public class InputDevicesConfigsDialog extends BottomSheetDialogFragment {
    final String TAG = "InputDevicesConfigsDialog";

    String vmId;
    DataMainRoms configs;

    boolean isSave = true;

    public void setConfigs(DataMainRoms configs) {
        this.configs = configs;
        if (configs != null) {
            vmId = configs.vmID;
        }
    }

    CreatorInputDevicesDialogBinding binding;

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

        binding = CreatorInputDevicesDialogBinding.inflate(getLayoutInflater());

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

    InputDevicesConfigsDialogCallback callback;

    public void setOnDismiss(InputDevicesConfigsDialogCallback callback) {
        this.callback = callback;
    }

    public interface InputDevicesConfigsDialogCallback {
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

        binding.sbvUsbController.setOnClickListener(v -> VMCreatorSelector.usbController(requireActivity(), configs.usbController, ((position, name, value) -> {
            if (position == 0 && ParamManager.isUsbControllerRequired(configs)) {
                DialogUtils.oopsDialog(requireActivity(), getString(R.string.usb_controller_required_content));
                return;
            }

            configs.usbController = position;
            binding.sbvUsbController.setSubtitle(name);
        })));

        binding.sbvMouse.setOnClickListener(v -> VMCreatorSelector.mouse(requireActivity(), MainSettingsManager.getArch(requireContext()), configs.mouse, ((position, name, value) -> {
            configs.mouse = position;
            binding.sbvMouse.setSubtitle(name);

            checkUsbController();
        })));

        binding.sbvKeyboard.setOnClickListener(v -> VMCreatorSelector.keyboard(requireActivity(), MainSettingsManager.getArch(requireContext()), configs.keyboard, ((position, name, value) -> {
            configs.keyboard = position;
            binding.sbvKeyboard.setSubtitle(name);

            checkUsbController();
        })));

        load();
    }

    private void load() {
        if (!isAdded()) return;

        binding.sbvUsbController.setSubtitle(Objects.requireNonNull(VMCreatorSelector.getUsbController(requireActivity(), configs.usbController).get("name")).toString());
        binding.sbvMouse.setSubtitle(Objects.requireNonNull(VMCreatorSelector.getMouse(requireActivity(), MainSettingsManager.getArch(requireContext()), configs.mouse).get("name")).toString());
        binding.sbvKeyboard.setSubtitle(Objects.requireNonNull(VMCreatorSelector.getKeyboard(requireActivity(), MainSettingsManager.getArch(requireContext()), configs.keyboard).get("name")).toString());
    }

    private void save() {

    }

    void checkUsbController() {
        if (configs.usbController == 0 && ParamManager.isUsbControllerRequired(configs)) {
            configs.usbController = 1;
            binding.sbvUsbController.setSubtitle(Objects.requireNonNull(VMCreatorSelector.getUsbController(requireActivity(), configs.usbController).get("name")).toString());
        }
    }
}