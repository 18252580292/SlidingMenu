package com.jskj.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;

/**
 * Created by cui on 2016/10/27.
 */

public class ScrollLayout extends HorizontalScrollView {
    private View leftMenu;
    private View mainContent;
    private int mScreenWidth;
    private float mMargin;

    public ScrollLayout(Context context) {
        this(context, null);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics
                ());
    }

    private boolean flag = true;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(flag) {
            leftMenu.getLayoutParams().width = (int) (mScreenWidth - mMargin);
            mainContent.getLayoutParams().width = mScreenWidth;
            flag = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (getScaleX() >= (mScreenWidth - mMargin) / 2) {
                    this.smoothScrollTo((int) (mScreenWidth - mMargin), 0);
                } else {
                    this.smoothScrollTo(0, 0);
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    private boolean once = true;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo((int) (mScreenWidth - mMargin), 0);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewGroup content = (ViewGroup) getChildAt(0);

        leftMenu = content.getChildAt(0);
        mainContent = content.getChildAt(1);
    }
}
