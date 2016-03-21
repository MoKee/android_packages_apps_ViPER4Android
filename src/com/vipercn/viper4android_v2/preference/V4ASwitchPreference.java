package com.vipercn.viper4android_v2.preference;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.vipercn.viper4android_v2.R;
import com.vipercn.viper4android_v2.activity.SettingsActivity;

public class V4ASwitchPreference extends Preference {

    private SwitchCompat mSwitch;

    public V4ASwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.v4a_switch);
    }

    public V4ASwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.v4a_switch);
    }

    public V4ASwitchPreference(Context context) {
        super(context);
        setWidgetLayoutResource(R.layout.v4a_switch);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        if (getKey() == null || "".equals(getKey())) {
            setWidgetLayoutResource(0);
        }
        return super.onCreateView(parent);
    }

    @Override
    protected void onBindView(View view) {
        mSwitch = (SwitchCompat) view.findViewById(R.id.v4a_switch);

        if (mSwitch != null) {
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    persistBoolean(isChecked);
                    if (!callChangeListener(isChecked)) {
                        return;
                    }
                }
            });

            updateSwitch();
        }

        super.onBindView(view);
    }

    public void updateSwitch() {
        if (mSwitch != null) {
            boolean value = getPersistedBoolean(false);
            mSwitch.setChecked(value);
        }
    }

    @Override
    protected void onClick() {
        String fragment = getFragment();
        if (fragment == null || "".equals(fragment)) {
            if (mSwitch != null) {
                if (getPersistedBoolean(false)) {
                    persistBoolean(false);
                } else {
                    persistBoolean(true);
                }
                updateSwitch();
            }
        } else {
            Intent intent = null;
            String values[] = fragment.split("\\|");
            try {
                intent = new Intent(getContext(), Class.forName(values[1]));
                intent.putExtra("config", values[0]);
            } catch (ClassNotFoundException e) {
                intent = new Intent(getContext(), SettingsActivity.class);
                intent.putExtra("config", values[0]);
                intent.putExtra("flag", values[1]);
            }
            getContext().startActivity(intent);
        }
    }

}
