package com.vectras.vm.main.vms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.vectras.vm.R;
import com.vectras.vm.VMManager;
import com.vectras.vm.main.MainActivity;
import com.vectras.vm.main.core.MainStartVM;
import com.vectras.vm.main.core.RomOptionsDialog;
import com.vectras.vm.manager.VmFileManager;
import com.vectras.vm.utils.FileUtils;
import com.vectras.vm.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VmsSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity activity;
    private final LayoutInflater inflater;
    private List<DataMainRoms> fullList;
    private final List<DataMainRoms> displayList;
    private final boolean isBrighterItemBackground;

    public VmsSearchAdapter(Activity activity, List<DataMainRoms> data, boolean isBrighterItemBackground) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        fullList = data;
        this.isBrighterItemBackground = isBrighterItemBackground;
        displayList = new ArrayList<>();
        loadMore();
    }

    // Inflate the layout when viewholder created
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_roms, parent, false);
        return new MyHolder(view);
    }

    // Bind data
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        final DataMainRoms current = displayList.get(position);

        myHolder.textName.setText(current.itemName);
        myHolder.textSize.setText(current.itemArch);
        if (!current.itemIcon.isEmpty() && FileUtils.isFileExists(current.itemIcon)){
            File file = new File(current.itemIcon);
            Glide.with(myHolder.ivIcon)
                    .load(file)
                    .signature(new ObjectKey(file.lastModified()))
                    .placeholder(R.drawable.ic_computer_180dp_with_padding)
                    .error(R.drawable.ic_computer_180dp_with_padding)
                    .into(myHolder.ivIcon);
        } else if (VmFileManager.isScreenshotPngExists(current.vmID)) {
            File file = new File(VmFileManager.getScreenshotPng(current.vmID));
            Glide.with(myHolder.ivIcon)
                    .load(file)
                    .signature(new ObjectKey(file.lastModified()))
                    .placeholder(R.drawable.ic_computer_180dp_with_padding)
                    .error(R.drawable.ic_computer_180dp_with_padding)
                    .into(myHolder.ivIcon);
        } else {
            VMManager.setIconWithName(myHolder.ivIcon, current.itemName);
        }

        myHolder.linearItem.setOnClickListener(view -> RomOptionsDialog.show(activity, position, current));

        myHolder.textAvail.setVisibility(View.GONE);

        UIUtils.setBackgroundItemInList(myHolder.linearItem, position, displayList.size(), isBrighterItemBackground);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return displayList == null ? 0 : displayList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        TextView textName, textAvail, textSize;
        ImageView ivIcon;
        LinearLayout linearItem;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            textSize = itemView.findViewById(R.id.textSize);
            textAvail = itemView.findViewById(R.id.textAvail);

            linearItem = itemView.findViewById(R.id.linearItem);
        }

    }

    public void loadMore() {
        int currentSize = displayList.size();
        int nextLimit = Math.min(currentSize + 10, fullList.size());

        if (currentSize >= nextLimit) return;

        displayList.addAll(fullList.subList(currentSize, nextLimit));
        if (currentSize == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemChanged(currentSize - 1);
            notifyItemRangeInserted(currentSize, nextLimit - currentSize);
        }
    }

    public void submitList(List<DataMainRoms> newData) {
        fullList.clear();
        fullList = new ArrayList<>(newData);

        displayList.clear();

        loadMore();
    }
}
