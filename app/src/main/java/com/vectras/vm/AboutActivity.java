package com.vectras.vm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.vectras.vm.databinding.ActivityAboutBinding;
import com.vectras.vm.utils.CommandUtils;
import com.vectras.vm.utils.IntentUtils;
import com.vectras.vm.utils.UIUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AboutActivity extends AppCompatActivity {
    public String TAG = "AboutActivity";

    ActivityAboutBinding binding;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.edgeToEdge(this);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        executor.execute(() -> {
            String qemuVersionName = CommandUtils.getQemuVersionName(this);
            runOnUiThread(() -> {
                if (!qemuVersionName.isEmpty()) binding.qemuVersion.setText(qemuVersionName); else getString(R.string.unknow);
            });
        });

        binding.btnGithub.setOnClickListener(v -> IntentUtils.openUrl(this, AppConfig.gitHub));

        binding.btnCommunity.setOnClickListener(v -> IntentUtils.openUrl(this, AppConfig.community));

        binding.btnOpenSourceLicenses.setOnClickListener(v -> startActivity(new Intent(this, OssLicensesMenuActivity.class)));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
