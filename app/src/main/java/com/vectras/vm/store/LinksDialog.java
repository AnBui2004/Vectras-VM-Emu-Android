package com.vectras.vm.store;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vectras.vm.R;
import com.vectras.vm.creator.utils.EditorUtils;
import com.vectras.vm.databinding.LinksDialogBinding;
import com.vectras.vm.utils.IntentUtils;
import com.vectras.vm.utils.TextUtils;

public class LinksDialog extends BottomSheetDialogFragment {

    Activity activity;

    LinksDialogBinding binding;

    String title1;
    String link1;
    String title2;
    String link2;
    String title3;
    String link3;

    public void setLink1(String link) {
        setLink1(TextUtils.getLinkName(activity, link), link);
    }

    public void setLink1(String title, String link) {
        title1 = title;
        link1 = link;
    }

    public void setLink2(String link) {
        setLink2(TextUtils.getLinkName(activity, link), link);
    }

    public void setLink2(String title, String link) {
        title2 = title;
        link2 = link;
    }

    public void setLink3(String link) {
        setLink3(TextUtils.getLinkName(activity, link), link);
    }

    public void setLink3(String title, String link) {
        title3 = title;
        link3 = link;
    }

    public LinksDialog(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public BottomSheetDialog onCreateDialog(Bundle savedInstanceState) {
        if (title1 == null || link1 == null || title2 == null || link2 == null) {
            dismiss();
            return EditorUtils.getDummyDialog(requireActivity());
        }

        binding = LinksDialogBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(binding.getRoot());

        dialog.setOnShowListener(d -> {
            BottomSheetBehavior<FrameLayout> behavior = dialog.getBehavior();
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);
        });

        binding.tvTitle1.setText(title1);
        binding.tvUrl1.setText(getHostName(link1));
        binding.ivUrl1.setImageResource(TextUtils.getLinkFavicon(link1));
        binding.lnUrl1.setOnClickListener(v -> {
            IntentUtils.openUrl(requireContext(), link1);
            dismiss();
        });

        binding.tvTitle2.setText(title2);
        binding.tvUrl2.setText(getHostName(link2));
        binding.ivUrl2.setImageResource(TextUtils.getLinkFavicon(link2));
        binding.lnUrl2.setOnClickListener(v -> {
            IntentUtils.openUrl(requireContext(), link2);
            dismiss();
        });

        if (title3 == null || link3 == null) {
            binding.lnUrl3.setVisibility(View.GONE);
            binding.lnUrl2.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.object_shape_bottom_high));
        } else {
            binding.tvTitle3.setText(title3);
            binding.tvUrl3.setText(getHostName(link3));
            binding.ivUrl3.setImageResource(TextUtils.getLinkFavicon(link3));
            binding.lnUrl3.setOnClickListener(v -> {
                IntentUtils.openUrl(requireContext(), link3);
                dismiss();
            });
        }
        return dialog;
    }

    public String getHostName(String url) {
        String host = Uri.parse(url).getHost();
        return host == null ? activity.getString(R.string.unknow) : host;
    }
}
