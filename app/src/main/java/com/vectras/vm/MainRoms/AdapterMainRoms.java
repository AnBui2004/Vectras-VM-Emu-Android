package com.vectras.vm.MainRoms;

import static android.content.Intent.ACTION_OPEN_DOCUMENT;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vectras.qemu.Config;
import com.vectras.qemu.MainSettingsManager;
import com.vectras.qemu.MainVNCActivity;
import com.vectras.vm.AppConfig;
import com.vectras.vm.CustomRomActivity;
import com.vectras.vm.ExportRomActivity;
import com.vectras.vm.MainActivity;
import com.vectras.vm.MainService;
import com.vectras.vm.R;
import com.vectras.vm.StartVM;
import com.vectras.vm.VMManager;
import com.vectras.vm.VectrasApp;
import com.vectras.vm.logger.VectrasStatus;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.zeroturnaround.zip.FileSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdapterMainRoms extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    public List<DataMainRoms> data = Collections.emptyList();
    int currentPos = 0;
    private int mSelectedItem = -1;
    public static boolean isKeptSomeFiles = false;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterMainRoms(Context context, List<DataMainRoms> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_main_roms, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    public static final String CREDENTIAL_SHARED_PREF = "settings_prefs";

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final DataMainRoms current = data.get(position);
        myHolder.textName.setText(current.itemName);
        myHolder.textArch.setText(current.itemArch);
        if (current.itemIcon.isEmpty()){
            VMManager.setIconWithName(myHolder.ivIcon, current.itemName);
        } else {
            Bitmap bmImg = BitmapFactory.decodeFile(current.itemIcon);
            myHolder.ivIcon.setImageBitmap(bmImg);
        }
        myHolder.optionsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.activity);
                View v = MainActivity.activity.getLayoutInflater().inflate(R.layout.rom_options_dialog, null);
                bottomSheetDialog.setContentView(v);

                Button modifyRomBtn = v.findViewById(R.id.modifyRomBtn);
                modifyRomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.vectras.vm.CustomRomActivity.current = data.get(position);
                        VMManager.setArch(current.itemArch, MainActivity.activity);
                        MainActivity.activity.startActivity(new Intent(MainActivity.activity, CustomRomActivity.class).putExtra("POS", position).putExtra("MODIFY", true).putExtra("VMID", current.vmID));
                        bottomSheetDialog.cancel();
                    }
                });
                Button exportRomBtn = v.findViewById(R.id.exportRomBtn);
                exportRomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExportRomActivity.pendingPosition = position;
                        Intent intent = new Intent();
                        intent.setClass(context.getApplicationContext(), ExportRomActivity.class);
                        context.startActivity(intent);
                        bottomSheetDialog.cancel();
                    }
                });
                Button removeRomBtn = v.findViewById(R.id.removeRomBtn);
                removeRomBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VMManager.deleteVMDialog(current.itemName, position, MainActivity.activity);
                        bottomSheetDialog.cancel();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        myHolder.cdRoms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VMManager.setArch(current.itemArch, MainActivity.activity);
                StartVM.cdrompath = current.imgCdrom;
                if (current.qmpPort == 0) {
                    Config.setDefault();
                } else {
                    Config.QMPPort = current.qmpPort;
                }
                Config.vmID = current.vmID;
                String env = StartVM.env(MainActivity.activity, current.itemExtra, current.itemPath, "");
                MainActivity.startVM(current.itemName, env, current.itemExtra, current.itemPath);
            }
        });
        myHolder.cdRoms.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                VMManager.deleteVMDialog(current.itemName, position, MainActivity.activity);
                return false;
            }
        });
    }

    public class RomJson extends JSONObject {

        public JSONObject makeJSONObject(String imgName, String imgArch, String imgAuthor, String imgDesc, String imgIcon, String imgDrive, String imgQemu) {

            JSONObject obj = new JSONObject();

            try {
                obj.put("title", imgName);
                obj.put("arch", imgArch);
                obj.put("author", imgAuthor);
                obj.put("desc", imgDesc);
                obj.put("kernel", "windows");
                obj.put("icon", imgIcon);
                obj.put("drive", imgDrive);
                obj.put("qemu", imgQemu);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return obj;
        }
    }

    private void showDialog(String title, String path, String pathIcon) {

        final Dialog dialog = new Dialog(MainActivity.activity, R.style.MainDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rom_options_layout);

        LinearLayout removeLayout = dialog.findViewById(R.id.layoutRemove);

        removeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad;
                ad = new AlertDialog.Builder(MainActivity.activity, R.style.MainDialogTheme).create();
                ad.setTitle(MainActivity.activity.getString(R.string.remove) + " " + title);
                ad.setMessage(MainActivity.activity.getString(R.string.are_you_sure));
                ad.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.activity.getString(R.string.remove) + " " + title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(path);
                        try {
                            file.delete();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        MainActivity.mMainAdapter = new AdapterMainRoms(MainActivity.activity, MainActivity.data);
                        MainActivity.data.remove(currentPos);
                        MainActivity.mRVMainRoms.setAdapter(MainActivity.mMainAdapter);
                        MainActivity.mRVMainRoms.setLayoutManager(new GridLayoutManager(MainActivity.activity, 2));
                        MainActivity.jArray.remove(currentPos);
                        try {
                            Writer output = null;
                            File jsonFile = new File(AppConfig.maindirpath + "roms-data" + ".json");
                            output = new BufferedWriter(new FileWriter(jsonFile));
                            output.write(MainActivity.jArray.toString().replace("\\", "").replace("//", "/"));
                            output.close();
                            MainActivity.loadDataVbi();
                        } catch (Exception e) {
                            UIUtils.toastLong(MainActivity.activity, e.toString());
                        }
                        UIUtils.toastLong(MainActivity.activity, title + MainActivity.activity.getString(R.string.are_removed_successfully));
                        return;
                    }
                });
                ad.setButton(Dialog.BUTTON_POSITIVE, MainActivity.activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                ad.show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CardView cdRoms;
        TextView textName, textArch;
        ImageView ivIcon;
        ImageButton optionsBtn;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            cdRoms = (CardView) itemView.findViewById(R.id.cdItem);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textArch = (TextView) itemView.findViewById(R.id.textArch);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            optionsBtn = (ImageButton) itemView.findViewById(R.id.optionsButton);
        }

    }

}
