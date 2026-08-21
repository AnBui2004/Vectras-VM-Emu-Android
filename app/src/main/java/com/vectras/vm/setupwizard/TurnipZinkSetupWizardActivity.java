package com.vectras.vm.setupwizard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.TransitionManager;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anbui.elephant.retrofit2utils.Retrofit2Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vectras.vm.AppConfig;
import com.vectras.vm.R;
import com.vectras.vm.databinding.ActivityTurnipZinkSetupWizardBinding;
import com.vectras.vm.utils.DeviceUtils;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.GpuHelper;
import com.vectras.vm.utils.JSONUtils;
import com.vectras.vm.utils.UIUtils;
import com.vectras.vterm.Terminal2;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class TurnipZinkSetupWizardActivity extends AppCompatActivity {
    final String TAG = "TurnipZinkSetupWizardActivity";

    final String COMPLETED_MARK = "Turnip and Zink installation process is complete.";

    final int STEP_CHECKING = 0;
    final int STEP_CONNECT_TO_SERVER = 1;
    final int STEP_MAIN = 2;
    final int STEP_UNSUPPORTED = 3;
    final int STEP_INSTALLING = 4;
    final int STEP_INSTALLED = 5;
    final int STEP_ERROR = 6;
    final int STEP_OFFLINE = 7;

    int STEP = STEP_CHECKING;

    String mesaFileUrl;
    String libglvndFileUrl;

    ActivityTurnipZinkSetupWizardBinding binding;

    Terminal2 terminal2;
    String logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.edgeToEdge(this);
        binding = ActivityTurnipZinkSetupWizardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UIUtils.setOnApplyWindowInsetsListener(binding.main);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (STEP != STEP_CHECKING && STEP != STEP_CONNECT_TO_SERVER && STEP != STEP_INSTALLING) {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        initialize();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadingIndicatorController(STEP);
    }

    void initialize() {
        terminal2 = new Terminal2(this);

        binding.btnInstall.setOnClickListener(v -> install());
        binding.btnExit.setOnClickListener(v -> finish());
        binding.btnTryAgain.setOnClickListener(v -> getData());
        binding.btnInstalledExit.setOnClickListener(v -> finish());

        check();
    }

    void check() {
        uiControllder(STEP_CHECKING);
        if (!DeviceUtils.is64bit() || !new GpuHelper().isAdreno()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> uiControllder(STEP_UNSUPPORTED), 1000);
            return;
        }

        if (FileUtils.isFileExists(AppConfig.internalDataDirPath + "/distro/usr/lib/dri/zink_dri.so")) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> uiControllder(STEP_INSTALLED), 1000);
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(this::getData, 1000);
    }

    void getData() {
        uiControllder(STEP_CONNECT_TO_SERVER);
        AtomicInteger nextStep = new AtomicInteger(STEP_OFFLINE);

        Retrofit2Utils.get(AppConfig.bootstrapfileslink, ((isSuccess, body, status, error) -> {
            if (isSuccess) {
                if (JSONUtils.isValidFromString(body)) {
                    HashMap<String, Object> mmap;
                    mmap = new Gson().fromJson(body, new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                    if (
                            mmap != null &&
                                    mmap.containsKey("aarch64_adreno") &&
                                    mmap.get("aarch64_adreno") != null &&
                                    mmap.containsKey("aarch64_libglvnd") &&
                                    mmap.get("aarch64_libglvnd") != null
                    ) {
                        mesaFileUrl = Objects.requireNonNull(mmap.get("aarch64_adreno")).toString();
                        libglvndFileUrl = Objects.requireNonNull(mmap.get("aarch64_libglvnd")).toString();

                        nextStep.set(STEP_MAIN);
                    }
                }
            }

            if (!isFinishing() && !isDestroyed())
                new Handler(Looper.getMainLooper()).postDelayed(() -> uiControllder(nextStep.get()), 1000);
        }));
    }

    void install() {
        uiControllder(STEP_INSTALLING);

        terminal2.clearLog();

        String command = " set -e;" +
                " echo \"Starting setup...\";" +
                " apk update;" +
                " echo \"Installing packages...\";" +
                " apk add aria2 mesa-dri-gallium mesa-vulkan-swrast vulkan-loader mesa-utils vulkan-tools mesa-egl mesa-gbm mesa-vulkan-ati mesa-vulkan-broadcom mesa-vulkan-freedreno mesa-vulkan-panfrost;" +
                " echo \"Downloading...\";" +
                " aria2c -x 4 --async-dns=false --disable-ipv6 --check-certificate=false -o setup0.tar.gz " + libglvndFileUrl + ";" +
                " aria2c -x 4 --async-dns=false --disable-ipv6 --check-certificate=false -o setup1.tar.gz " + mesaFileUrl + ";" +
                " echo \"Installing...\";" +
                " tar -xzvf setup0.tar.gz -C /;" +
                " tar -xzvf setup1.tar.gz -C /;" +
                " echo \"Cleaning...\";" +
                " rm setup0.tar.gz;" +
                " rm setup1.tar.gz;" +
                " echo \"Finishing...\";" +
                " chmod 755 /usr/local/bin/*;" +
                " echo " + COMPLETED_MARK + ";";

        terminal2.execute(command, new Terminal2.Terminal2Callback() {
            @Override
            public void onRunning(String command, String newLine) {
                // Nothing to do.
            }

            @Override
            public void onFinished(String command, String log, int status) {
                if (isFinishing() || isDestroyed()) return;

                runOnUiThread(() -> {
                    if (log.contains(COMPLETED_MARK) && FileUtils.isFileExists(AppConfig.internalDataDirPath + "/distro/usr/lib/dri/zink_dri.so")) {
                        uiControllder(STEP_INSTALLED);
                    } else {
                        logs = log;
                        uiControllder(STEP_ERROR);
                    }
                });
            }

            @Override
            public void onError(String command, Exception exception) {
                if (isFinishing() || isDestroyed()) return;

                logs = exception.getMessage();
                runOnUiThread(() -> uiControllder(STEP_ERROR));
            }
        });
    }

    void uiControllder(int step) {
        if (isFinishing() || isDestroyed()) return;

        if (step == STEP) return;

        STEP = step;

        TransitionManager.beginDelayedTransition(binding.main);

        binding.lnChecking.setVisibility(View.GONE);
        binding.lnGettingData.setVisibility(View.GONE);
        binding.lnInstall.setVisibility(View.GONE);
        binding.lnInstalling.setVisibility(View.GONE);
        binding.lnOffline.setVisibility(View.GONE);
        binding.lnUnsupportDevice.setVisibility(View.GONE);
        binding.lnInstallingPackagesFailed.setVisibility(View.GONE);

        View selectedView = switch (step) {
            case STEP_CONNECT_TO_SERVER -> binding.lnGettingData;
            case STEP_MAIN -> binding.lnInstall;
            case STEP_UNSUPPORTED -> binding.lnUnsupportDevice;
            case STEP_INSTALLING -> binding.lnInstalling;
            case STEP_INSTALLED -> binding.lnInstalled;
            case STEP_ERROR -> binding.lnInstallingPackagesFailed;
            case STEP_OFFLINE -> binding.lnOffline;
            default -> binding.lnChecking;
        };

        TransitionManager.beginDelayedTransition(binding.main);

        selectedView.setVisibility(View.VISIBLE);

        if (STEP == STEP_ERROR)
            binding.tvErrorLogContent.setText(logs.isEmpty() ? getString(R.string.there_are_no_logs) : logs);
    }

    private void loadingIndicatorController(int step) {
        if (isFinishing() || isDestroyed()) return;

        float dp = 200f;
        float px = dp * getResources().getDisplayMetrics().density;

        if (step == STEP_CHECKING) {
            binding.lnCheckingCpiContainer.post(() -> {
                int heightPx = binding.lnCheckingCpiContainer.getHeight();

                if (heightPx < px) {
                    binding.cpiChecking.setVisibility(View.GONE);
                    binding.lpiChecking.setVisibility(View.VISIBLE);
                } else {
                    binding.cpiChecking.setVisibility(View.VISIBLE);
                    binding.lpiChecking.setVisibility(View.GONE);
                }
            });
        } else if (step == STEP_CONNECT_TO_SERVER) {
            binding.lnGettingDataCpiContainer.post(() -> {
                int heightPx = binding.lnGettingDataCpiContainer.getHeight();

                if (heightPx < px) {
                    binding.cpiGettingData.setVisibility(View.GONE);
                    binding.lpiGettingData.setVisibility(View.VISIBLE);
                } else {
                    binding.cpiGettingData.setVisibility(View.VISIBLE);
                    binding.lpiGettingData.setVisibility(View.GONE);
                }
            });
        } else if (step == STEP_INSTALLING) {
            binding.lnInstallingCpiContainer.post(() -> {
                int heightPx = binding.lnInstalling.getHeight();

                if (heightPx < px) {
                    binding.cpiInstalling.setVisibility(View.GONE);
                    binding.lpiInstalling.setVisibility(View.VISIBLE);
                } else {
                    binding.cpiInstalling.setVisibility(View.VISIBLE);
                    binding.lpiInstalling.setVisibility(View.GONE);
                }
            });
        }
    }
}