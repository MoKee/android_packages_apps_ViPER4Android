package com.vipercn.viper4android_v2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EqualizerViewBar extends HorizontalScrollView {

    private float mMinDB = -12;
    private float mMaxDB = 12;

    private int mNumBands = 10;

    private float[] mLevels = new float[mNumBands];
    private float[] mCenterFreqs = new float[mNumBands];

    private List<VerticalSeekBar> seekBars = new ArrayList<>();

    private BandUpdatedListener mBandUpdatedListener;

    public EqualizerViewBar(Context context) {
        super(context);
        initView();
    }

    public EqualizerViewBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EqualizerViewBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        mCenterFreqs = new float[mNumBands];
        for (int i = 0; i < mLevels.length; i ++) {
            mCenterFreqs[i] = (float) (15.625 * Math.pow(2, i + 1));
        }

        LinearLayout subLayout = new LinearLayout(getContext());
        subLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        subLayout.setPadding(dip2px(getContext(), 15),0,dip2px(getContext(), 15),dip2px(getContext(), 15));

        LinearLayout col;
        VerticalSeekBar seekBar;
        VerticalSeekBarWrapper barWrapper;
        for (short i = 0; i < mNumBands; i++) {
            final short band = i;

            col = new LinearLayout(getContext());
            col.setLayoutParams(new LinearLayout.LayoutParams(
                    dip2px(getContext(), 55),
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            col.setOrientation(LinearLayout.VERTICAL);
            col.setGravity(Gravity.CENTER);

            barWrapper = new VerticalSeekBarWrapper(getContext());
            barWrapper.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            seekBar = new VerticalSeekBar(getContext());
            seekBar.setLayoutParams(new ViewGroup.LayoutParams(
                    0,
                    0
            ));
            seekBar.setMax((int) ((mMaxDB - mMinDB) * 10));
            seekBar.setProgress((int) (mLevels[band] * 10.0 + 120));

            final TextView textLevel = new TextView(getContext());
            textLevel.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textLevel.setText(String.format(mLevels[band] == 0.0 ? "%.0fdb" : "%+.1fdb", mLevels[band]));

            TextView textFreq = new TextView(getContext());
            textFreq.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            float freq = mCenterFreqs[band];
            textFreq.setText(String.format(freq < 1000 ? "%.0f" : "%.0fk",
                    freq < 1000 ? freq : freq / 1000));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float level = (float) ((progress - 120) / 10.0);
                    textLevel.setText(String.format(level == 0.0 ? "%.0fdb" : "%+.1fdb", level));
                    mLevels[band] = level;
                    if (mBandUpdatedListener != null) {
                        mBandUpdatedListener.onBandUpdated(mLevels);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (mBandUpdatedListener != null) {
                        mBandUpdatedListener.onBandStartTracking();
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            seekBars.add(seekBar);
            barWrapper.addView(seekBar);
            col.addView(barWrapper);
            col.addView(textFreq);
            col.addView(textLevel);
            subLayout.addView(col);
        }
        this.addView(subLayout);

    }

    public void setBands(float[] bands) {
        mLevels = bands;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (short i = 0; i < mNumBands; i++) {
                    seekBars.get(i).setProgress((int) (mLevels[i] * 10.0 + 120));
                }
            }
        }).start();

    }

    public void setBandUpdatedListener(BandUpdatedListener bandUpdatedListener) {
        this.mBandUpdatedListener = bandUpdatedListener;
    }

    public interface BandUpdatedListener {
        public void onBandUpdated(float[] mLevels);
        public void onBandStartTracking();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
