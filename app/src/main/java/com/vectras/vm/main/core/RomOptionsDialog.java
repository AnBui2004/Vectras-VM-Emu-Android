package com.vectras.vm.main.core;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vectras.qemu.Config;
import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.creator.VMCreatorActivity;
import com.vectras.vm.ExportRomActivity;
import com.vectras.vm.R;
import com.vectras.vm.VMManager;
import com.vectras.vm.file.FilePickerDialog;
import com.vectras.vm.main.MainActivity;
import com.vectras.vm.main.vms.DataMainRoms;
import com.vectras.vm.manager.FirmwareManager;
import com.vectras.vm.manager.VmActions;
import com.vectras.vm.manager.VmAudioManager;
import com.vectras.vm.manager.VmControllerDialog;
import com.vectras.vm.manager.VmFileManager;
import com.vectras.vm.utils.DialogUtils;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.ProgressDialog;

import java.io.File;

public class RomOptionsDialog {
    public static void show(Activity activity, DataMainRoms vmConfig) {
        if (VMManager.isVMRunning(activity, vmConfig.vmID)) {
            Config.vmID = vmConfig.vmID;

            VmControllerDialog vmControllerDialog = new VmControllerDialog();
            vmControllerDialog.streamAudio = VmAudioManager.streamAudio;
            vmControllerDialog.vmConfig = vmConfig;
            vmControllerDialog.show(((FragmentActivity) activity).getSupportFragmentManager(), "VmControllerDialog");
        } else {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);

            bottomSheetDialog.setOnShowListener(d -> {
                BottomSheetBehavior<FrameLayout> behavior = bottomSheetDialog.getBehavior();
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            });

            View v = activity.getLayoutInflater().inflate(R.layout.rom_options_dialog, null);
            bottomSheetDialog.setContentView(v);

            ImageView thumbnail = v.findViewById(R.id.ivIcon);
            TextView name = v.findViewById(R.id.textName);
            TextView arch = v.findViewById(R.id.textArch);

            name.setText(vmConfig.itemName);
            arch.setText(vmConfig.itemArch);


            if (!vmConfig.itemIcon.isEmpty() && FileUtils.isFileExists(vmConfig.itemIcon)){
                File file = new File(vmConfig.itemIcon);
                Glide.with(thumbnail)
                        .load(file)
                        .signature(new ObjectKey(file.lastModified()))
                        .placeholder(R.drawable.ic_computer_180dp_with_padding)
                        .error(R.drawable.ic_computer_180dp_with_padding)
                        .into(thumbnail);
            } else if (VmFileManager.isScreenshotPngExists(vmConfig.vmID)) {
                File file = new File(VmFileManager.getScreenshotPng(vmConfig.vmID));
                Glide.with(thumbnail)
                        .load(file)
                        .signature(new ObjectKey(file.lastModified()))
                        .placeholder(R.drawable.ic_computer_180dp_with_padding)
                        .error(R.drawable.ic_computer_180dp_with_padding)
                        .into(thumbnail);
            } else {
                VMManager.setIconWithName(thumbnail, vmConfig.itemName);
            }

            v.findViewById(R.id.btn_start).setOnClickListener(v4 -> {
                MainStartVM.startNow(activity, vmConfig);

                if (activity instanceof MainActivity mainActivity)
                    mainActivity.binding.searchview.hide();

                bottomSheetDialog.cancel();
            });

            if (vmConfig.pin) {
                ((ImageView) v.findViewById(R.id.iv_pin)).setImageDrawable(AppCompatResources.getDrawable(activity, R.drawable.keep_off_24px));
                ((TextView) v.findViewById(R.id.tv_pin)).setText(activity.getString(R.string.unpin));
            }

            v.findViewById(R.id.ln_pin).setOnClickListener(v9 -> {
                if (VMManager.pinVm(vmConfig)) {
                    MainConfigs.refreshVmIds.add(vmConfig.vmID);

                    SharedViewModel sharedViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(SharedViewModel.class);
                    sharedViewModel.requestRefreshVmList.setValue(new Event<>(false));

                    if (activity instanceof MainActivity mainActivity)
                        mainActivity.binding.searchview.hide();
                } else {
                    DialogUtils.oopsDialog(activity, activity.getString(R.string.pin_failed_content));
                }

                bottomSheetDialog.cancel();
            });

            v.findViewById(R.id.ln_edit).setOnClickListener(v3 -> {
                activity.startActivity(new Intent(activity, VMCreatorActivity.class).putExtra("data", vmConfig).putExtra("VMID", vmConfig.vmID));

                if (activity instanceof MainActivity mainActivity)
                    mainActivity.binding.searchview.hide();

                bottomSheetDialog.cancel();
            });

            v.findViewById(R.id.ln_export).setOnClickListener(v2 -> {
                Intent intent = new Intent();
                intent.setClass(activity, ExportRomActivity.class);
                intent.putExtra("data", vmConfig);
                activity.startActivity(intent);
                bottomSheetDialog.cancel();
            });

            v.findViewById(R.id.ln_clone).setOnClickListener(v8 -> {
                DialogUtils.twoDialog(
                        activity,
                        activity.getString(R.string.clone),
                        activity.getString(R.string.would_you_like_to_create_a_copy_of_this_virtual_machine),
                        activity.getString(R.string.clone),
                        activity.getString(R.string.cancel),
                        true,
                        R.drawable.content_copy_24px,
                        true,
                        () -> {
                            ProgressDialog progressDialog = new ProgressDialog(activity);
                            progressDialog.setText(activity.getString(R.string.cloning));
                            progressDialog.show();

                            new Thread(() -> {
                                boolean isCloned = VmActions.clone(vmConfig);
                                activity.runOnUiThread(() -> {
                                    progressDialog.reset();

                                    if (isCloned) {
                                        SharedViewModel sharedViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(SharedViewModel.class);
                                        sharedViewModel.requestRefreshVmList.setValue(new Event<>(false));
                                    } else {
                                        DialogUtils.oopsDialog(activity, activity.getString(R.string.clone_failed_content));
                                    }
                                });
                            }).start();
                        },
                        null,
                        null
                );

                if (activity instanceof MainActivity mainActivity)
                    mainActivity.binding.searchview.hide();

                bottomSheetDialog.cancel();
            });

            v.findViewById(R.id.ln_cleanup).setOnClickListener(v5 -> {
                ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setText(activity.getString(R.string.just_a_sec));
                progressDialog.show();

                new Thread(() -> {
                    VmFileManager.removeTemp(activity, vmConfig.vmID);
                    activity.runOnUiThread(() -> {
                        progressDialog.reset();
                        Toast.makeText(activity, activity.getString(R.string.done), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.cancel();
                    });
                }).start();
            });

            if (vmConfig.isUseDefaultBios && FirmwareManager.isAVarFileExist(VmFileManager.getPath(vmConfig.vmID)) && !VMManager.isNeedLoadMigrate()) {
                v.findViewById(R.id.ln_reset_uefi_bios).setOnClickListener(v7 -> {
                    DialogUtils.twoDialog(
                            activity,
                            activity.getString(R.string.reset_uefi_bios),
                            activity.getString(R.string.reset_uefi_bios_note),
                            activity.getString(R.string.reset),
                            activity.getString(R.string.cancel),
                            true,
                            R.drawable.restore_page_24px,
                            true,
                            () -> {
                                ProgressDialog progressDialog = new ProgressDialog(activity);
                                progressDialog.show();

                                new Thread(() -> {
                                    FirmwareManager.erase(VmFileManager.getPath(vmConfig.vmID));
                                    activity.runOnUiThread(() -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(activity, activity.getString(R.string.done), Toast.LENGTH_LONG).show();
                                    });
                                }).start();
                            },
                            null,
                            null);

                    bottomSheetDialog.cancel();
                });
            } else {
                v.findViewById(R.id.ln_reset_uefi_bios).setVisibility(GONE);
            }

            v.findViewById(R.id.ln_show_in_folder).setOnClickListener(v6 -> {
                if (MainSettingsManager.getBuiltInFilePicker(activity)) {
                    FilePickerDialog filePickerDialog = new FilePickerDialog();
                    filePickerDialog.setHomeName(vmConfig.itemName);
                    filePickerDialog.setLockHome(true);
                    filePickerDialog.browse(activity, VmFileManager.getPath(vmConfig.vmID));
                } else {
                    FileUtils.openFolder(activity, VmFileManager.getPath(vmConfig.vmID));
                }
                bottomSheetDialog.cancel();
            });

            v.findViewById(R.id.ln_remove).setOnClickListener(v1 -> {
                VMManager.deleteVMDialog(vmConfig.itemName, vmConfig.vmID, activity);

                if (activity instanceof MainActivity mainActivity)
                    mainActivity.binding.searchview.hide();

                bottomSheetDialog.cancel();
            });

            bottomSheetDialog.show();
        }
    }
}
