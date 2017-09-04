package com.example.heyukun.animtablerun;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends FragmentActivity {
    private ImageView mImageView;
    private int height = 80;
    private boolean isDown = false;
    private Handler mHandler = new Handler();
    private ValueAnimator valueAnimator;
    private EditText mEditText;
    private int[] t;


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            if (height > 179) {
                height = 171;
                isDown = true;
            }
            if (height < 80) {
                height = 72;
                isDown = false;
            }

            int temp;

            if (!isDown) {
                temp = height + 8;
            } else {
                temp = height - 8;
            }


            if (temp > 179) {
                temp = 179;
            }
            if (temp < 80) {
                temp = 80;
            }

            if (temp == height) {
                if (height == 179) {
                    temp = 171;
                    isDown = true;
                }
                if (height == 80) {
                    temp = 86;
                    isDown = false;
                }
            }

            Log.d("Widget", "resImg- Height=" + height + ";temp=" + temp);
            t = new int[Math.abs(height - temp) + 1];
            if (height > temp) {
                for (int i = height - temp; i >= 0; i--) {
                    t[height - temp - i] = temp + i - 80;
                }
            } else if (height < temp) {
                for (int i = 0; i <= temp - height; i++) {
                    t[i] = height + i - 80;

                    Log.d("Widget", "resImg-t[" + i + "]=" + t[i]);
                }
            }


            valueAnimator = ValueAnimator.ofInt(t);

            if (valueAnimator != null && valueAnimator.isRunning()) {
                valueAnimator.end();
            }

            if (height > 150 && !isDown || height < 110 && isDown) {
                valueAnimator.setInterpolator(new DecelerateInterpolator());
            } else {
                valueAnimator.setInterpolator(new AccelerateInterpolator());
            }
            valueAnimator.setDuration(200);
            valueAnimator.start();
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    Log.d("Widget", "animation.getAnimatedValue=" + value);
//                    if(value>179){
//                        value = 179;
//                    }
//                    if(value<80){
//                        value = 80;
//                    }
                    mImageView.setBackground(getDrawable(HeightPng.ResImgs[value]));
                }
            });

            height = temp;

            mHandler.postDelayed(mRunnable, 200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        mHandler.postDelayed(mRunnable, 200);

    }

    private void initWidgets() {

        mImageView = (ImageView) findViewById(R.id.iv_table);
        mEditText = (EditText) findViewById(R.id.et);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    valueAnimator.end();
                }
                int setH = Integer.parseInt(mEditText.getText().toString());

                setH = setH > 179 ? 179 : setH;
                setH = setH < 80 ? 80 : setH;
                valueAnimator = ValueAnimator.ofArgb(height, setH);
                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                valueAnimator.setDuration((setH - height) / 4 * 200);
                valueAnimator.start();
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (height > 179) {
                            return;
                        }
                        if (height < 80) {
                            return;
                        }

                        mImageView.setBackground(AppCompatResources.getDrawable(getApplicationContext(), HeightPng.ResImgs[(int) animation.getAnimatedValue() - 80]));
                    }
                });
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mRunnable);
        valueAnimator.cancel();
    }

}
