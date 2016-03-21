package com.vipercn.viper4android_v2.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.vipercn.viper4android_v2.R;
import com.vipercn.viper4android_v2.service.ViPER4AndroidService;
import com.vipercn.viper4android_v2.widgets.EqualizerViewBar;
import com.vipercn.viper4android_v2.widgets.Gallery;

import java.util.Arrays;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class FireqActivity extends SwipeBackActivity {

    private EqualizerViewBar mEqualizerView;
    private Gallery mEqGallery;

    private int mEQPreset;
    private String[] mEQPresetNames;
    private List<String> mEQPresetValues;

    private SharedPreferences mPreferences;

    private ViPER4AndroidService mAudioService;

    private final ServiceConnection connectionForFireq = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mAudioService = ((ViPER4AndroidService.LocalBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAudioService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        setupActionBar();


        setFireq();

    }

    private void setFireq() {
        String config = getIntent().getStringExtra("config");
        mPreferences = getSharedPreferences(
                ViPER4Android.SHARED_PREFERENCES_BASENAME + "." + config, 0);


        mEqualizerView = (EqualizerViewBar) findViewById(R.id.FrequencyResponse);
        mEqGallery = (Gallery) findViewById(R.id.eqPresets);

        mEQPresetNames = getResources().getStringArray(R.array.equalizer_preset_modes);
        mEQPresetValues = Arrays.asList(getResources().getStringArray(R.array.equalizer_preset_values));

        mEQPreset = mEQPresetValues.indexOf(mPreferences.getString("viper4android.headphonefx.fireq",
                "0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.equalizer_presets,
                mEQPresetNames);
        mEqGallery.setAdapter(adapter);
        mEqGallery.setEnabled(true);
        mEqGallery.setSelection(mEQPreset);
        mEqGallery.setOnItemSelectedListener(new Gallery.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                mEQPreset = position;
                equalizerSetPreset(mEQPreset);
                if (mEQPreset >= mEQPresetNames.length - 1) {
                    mPreferences.edit().putString("viper4android.headphonefx.fireq", "custom").apply();
                } else {
                    mPreferences.edit().putString("viper4android.headphonefx.fireq", mEQPresetValues.get(mEQPreset)).apply();
                    mPreferences.edit().putString("viper4android.headphonefx.fireq.custom", mEQPresetValues.get(mEQPreset)).apply();
                }
                if (mAudioService != null) {
                    mAudioService.setEqualizerLevels(null);
                }
            }
        });

        Intent serviceIntent = new Intent(this, ViPER4AndroidService.class);
        this.bindService(serviceIntent, connectionForFireq, 0);

        mEqualizerView.setBandUpdatedListener(new EqualizerViewBar.BandUpdatedListener() {
            @Override
            public void onBandUpdated(float[] mLevels) {
                if (mEQPreset >=  mEQPresetNames.length - 1) {
                    StringBuilder sb = new StringBuilder();
//                    mPreferences.edit().putString("viper4android.headphonefx.fireq", "custom").apply();
                    for (float f : mLevels) {
                        sb.append(f).append(";");
                    }
                    mPreferences.edit().putString("viper4android.headphonefx.fireq.custom", sb.toString()).apply();
                }
//                else {
//                    mPreferences.edit().putString("viper4android.headphonefx.fireq", mEQPresetValues.get(mEQPreset)).apply();
//                    mPreferences.edit().putString("viper4android.headphonefx.fireq.custom", mEQPresetValues.get(mEQPreset)).apply();
//                }
                if(mAudioService != null) {
                    mAudioService.setEqualizerLevels(mLevels);
                }
            }

            @Override
            public void onBandStartTracking() {
                mEQPreset = mEQPresetNames.length - 1;
                mEqGallery.setSelection(mEQPreset);
            }
        });
    }

    private void equalizerSetPreset(final int preset) {
        Log.d("", "equalizerSetPreset(" + preset + ")");
        String value = mEQPresetValues.get(preset);
        String[] values;
        if ("custom".equals(value)) {
            values = mPreferences.getString("viper4android.headphonefx.fireq.custom",
                    "0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;").split(";");
        } else {
            values = mEQPresetValues.get(preset).split(";");
        }

        float[] bands = new float[10];
        for (short i = 0; i < bands.length; i++) {
            bands[i] = Float.parseFloat(values[i]);
        }
        mEqualizerView.setBands(bands);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        this.unbindService(connectionForFireq);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
