package com.example.heyukun.animtablerun;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by heyukun on 2017/9/4.
 */

public class AppointHeightActivity extends FragmentActivity {
    private static final int MIN_HEIGHT = 80;
    private static final int MAX_HEIGHT = 179;


    private EditText mFromEt, mToEt;
    private Button mBtnRun;
    private ImageView mImageView;


    private ValueAnimator valueAnimator;
    private int[] t;
    private boolean isDown = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);
        initWidgets();
    }

    private void initWidgets() {
        mFromEt = (EditText) findViewById(R.id.et_from);
        mToEt = (EditText) findViewById(R.id.et_to);
        mBtnRun = (Button) findViewById(R.id.btn_run);
        mImageView = (ImageView) findViewById(R.id.iv_tb);

        mBtnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRun(Integer.parseInt(mFromEt.getText().toString()), Integer.parseInt(mToEt.getText().toString()), 200);
            }
        });

    }


    /**
     * 由于我们读取设备信息是每隔timeout时间进行的
     * 所以每个timeout后都会返回一个高度值
     *
     * @param startHeight
     * @param endHeight
     * @param timeout
     * @return
     */
    private int tableRun(int startHeight, int endHeight, int timeout) {
        startHeight = heightFormat(startHeight);
        endHeight = heightFormat(endHeight);

        if (startHeight == endHeight) {
            return endHeight;
        }

        if (startHeight > endHeight) {
            isDown = true;
        } else if (startHeight < endHeight) {
            isDown = false;
        }

        t = new int[Math.abs(startHeight - endHeight) + 1];
        if (isDown) {
            for (int i = startHeight - endHeight; i >= 0; i--) {
                t[startHeight - endHeight - i] = endHeight + i - 80;
            }
        } else {
            for (int i = 0; i <= endHeight - startHeight; i++) {
                t[i] = startHeight + i - 80;
                Log.d("Widget", "resImg-t[" + i + "]=" + t[i]);
            }
        }

        valueAnimator = ValueAnimator.ofInt(t);

        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(timeout);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mImageView.setBackground(getDrawable(HeightPng.ResImgs[value]));
            }
        });
        t = null;
        return endHeight;
    }

    private int heightFormat(int height) {
        height = height > MAX_HEIGHT ? MAX_HEIGHT : height;
        height = height < MIN_HEIGHT ? MIN_HEIGHT : height;
        return height;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
