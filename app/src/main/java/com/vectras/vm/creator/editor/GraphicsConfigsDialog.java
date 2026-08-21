package com.vectras.vm.creator.editor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vectras.vm.R;
import com.vectras.vm.creator.utils.EditorUtils;
import com.vectras.vm.creator.utils.VMCreatorSelector;
import com.vectras.vm.databinding.CreatorGraphicsDialogBinding;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.utils.DialogUtils;

import java.util.Objects;

public class GraphicsConfigsDialog extends BottomSheetDialogFragment {
    final String TAG = "GraphicsConfigsDialog";

    String vmId;
    DataMainRoms configs;

    boolean isSave = true;

    public void setConfigs(DataMainRoms configs) {
        this.configs = configs;
        if (configs != null) {
            vmId = configs.vmID;
        }
    }

    CreatorGraphicsDialogBinding binding;

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

        binding = CreatorGraphicsDialogBinding.inflate(getLayoutInflater());

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

    GraphicsConfigsDialogCallback callback;

    public void setOnDismiss(GraphicsConfigsDialogCallback callback) {
        this.callback = callback;
    }

    public interface GraphicsConfigsDialogCallback {
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

        binding.sbvCardType.setOnClickListener(v -> VMCreatorSelector.graphicsCard(requireActivity(), configs.graphicCard, ((position, name, value) -> {
            configs.graphicCard = position;
            binding.sbvCardType.setSubtitle(name);
        })));

        load();
    }

    private void load() {
        if (!isAdded()) return;

        binding.sbvCardType.setSubtitle(Objects.requireNonNull(VMCreatorSelector.getGraphicsCard(requireActivity(), configs.graphicCard).get("name")).toString());
    }

    private void save() {

    }
}