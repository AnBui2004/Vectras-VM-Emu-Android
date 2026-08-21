package com.vectras.vm.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.R;
import com.vectras.vm.databinding.ActivityX11DisplaySettingsBinding;
import com.vectras.vm.main.core.DisplaySystem;
import com.vectras.vm.setupwizard.TurnipZinkSetupWizardActivity;
import com.vectras.vm.utils.DialogUtils;
import com.vectras.vm.utils.PackageUtils;
import com.vectras.vm.x11.LoriePreferences;

import java.util.Objects;

public class X11DisplaySettingsActivity extends AppCompatActivity {

    ActivityX11DisplaySettingsBinding binding;
    boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_external_vnc_settings);
        binding = ActivityX11DisplaySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(view -> finish());
        initialize();
    }

    private void initialize() {
        binding.swEnabled.setChecked(MainSettingsManager.getVmUi(this).equals("X11"));
        binding.swRunQemuWithXterm.setChecked(MainSettingsManager.getRunQemuWithXterm(this));
        binding.swUseExternal.setChecked(MainSettingsManager.getExternalX11(this));
        binding.swUseSdl.setChecked(MainSettingsManager.getUseSdl(this));
        binding.swUseOpengl.setChecked(SettingsData.opengl(this));
        binding.swBubble.setChecked(SettingsData.x11Bubble(this));

        binding.swEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MainSettingsManager.setVmUi(this, isChecked ? "X11" : "VNC");
            uiController(isChecked);
        });
        binding.lnEnabled.setOnClickListener(v -> binding.swEnabled.toggle());

        binding.lnPreferences.setOnClickListener(v -> {
            if (DisplaySystem.isUseBuiltInX11()) {
                startActivity(new Intent(this, LoriePreferences.class) {{ setAction(Intent.ACTION_MAIN); }});
            } else {
                if (PackageUtils.isInstalled("com.termux.x11", this)) {
                    Intent intent = new Intent();
                    intent.setClassName("com.termux.x11", "com.termux.x11.LoriePreferences");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    DialogUtils.needInstallTermuxX11(this);
                }
            }
        });

        binding.swUseExternal.setOnCheckedChangeListener((buttonView, isChecked) -> MainSettingsManager.setExternalX11(this, isChecked));
        binding.lnUseExternal.setOnClickListener(v -> {
            binding.swUseExternal.toggle();
            uiControllerExternal(binding.swUseExternal.isChecked());
        });

        binding.swRunQemuWithXterm.setOnCheckedChangeListener((buttonView, isChecked) -> MainSettingsManager.setRunQemuWithXterm(this, isChecked));
        binding.lnRunQemuWithXterm.setOnClickListener(v -> binding.swRunQemuWithXterm.toggle());

        binding.swUseSdl.setOnCheckedChangeListener((buttonView, isChecked) -> MainSettingsManager.setUseSdl(this, isChecked));
        binding.lnUseSdl.setOnClickListener(v -> binding.swUseSdl.toggle());

        binding.swUseOpengl.setOnCheckedChangeListener((buttonView, isChecked) -> SettingsData.opengl(this, isChecked));
        binding.lnUseOpengl.setOnClickListener(v -> binding.swUseOpengl.toggle());

        binding.swBubble.setOnCheckedChangeListener((buttonView, isChecked) -> SettingsData.x11Bubble(this, isChecked));
        binding.lnBubble.setOnClickListener(v -> binding.swBubble.toggle());

        binding.lnTurnipZink.setOnClickListener(v -> startActivity(new Intent(this, TurnipZinkSetupWizardActivity.class)));

        isInitialized = true;

        uiControllerExternal(binding.swUseExternal.isChecked());
        uiController(binding.swEnabled.isChecked());
    }

    private void uiController(boolean isEnabled) {
        binding.lnAllOptions.setAlpha(isEnabled ? 1f : 0.5f);
        binding.lnPreferences.setEnabled(isEnabled);
        binding.lnUseExternal.setEnabled(isEnabled);
        binding.lnRunQemuWithXterm.setEnabled(isEnabled);
        binding.lnUseSdl.setEnabled(isEnabled);
        binding.lnUseOpengl.setEnabled(isEnabled);
        binding.lnBubble.setEnabled(isEnabled && !binding.swUseExternal.isChecked());
        binding.lnTurnipZink.setEnabled(isEnabled);
    }

    private void uiControllerExternal(boolean isEnabled) {
        binding.lnBubble.setAlpha(!isEnabled ? 1f : 0.5f);
        binding.lnBubble.setEnabled(!isEnabled);
    }
}