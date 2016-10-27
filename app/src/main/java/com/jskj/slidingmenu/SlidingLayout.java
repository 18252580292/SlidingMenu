package com.jskj.slidingmenu;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by cui on 2016/10/26.
 */

public class SlidingLayout extends FrameLayout {
    private View leftMenu;
    private View mainContent;
    private int mMainContentWidth;
    private int mViewHeight;
    /**
     * 能拖动的最大范围
     */
    private float mRange;
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mainContent) {
                left = fixLeft(left);
            }
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) mRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == leftMenu) {
                leftMenu.layout(0, 0, mMainContentWidth, mViewHeight);
                int oldLeft = mainContent.getLeft();
                oldLeft = fixLeft(oldLeft);
                mainContent.layout(dx + oldLeft, 0, mainContent.getRight() + dx, mViewHeight);
            }
            float fraction = mainContent.getLeft() / mRange;
//            execAnimate(fraction);
            execAnimate(fraction);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mainContent.getLeft() > mRange / 2) {
                mDragHelper.smoothSlideViewTo(mainContent, (int) mRange, 0);
                mOnWindowChanged.open();
            } else {
                mDragHelper.smoothSlideViewTo(mainContent, 0, 0);
                mOnWindowChanged.close();
            }
            ViewCompat.postInvalidateOnAnimation(SlidingLayout.this);
        }
    };


    /**
     * 执行动画
     */
    private void execAnimate(float fraction) {
        FloatEvaluator evaluator = new FloatEvaluator();
        ViewCompat.setScaleX(mainContent, evaluator.evaluate(fraction,1,0.8));
        ViewCompat.setScaleY(mainContent,1 - 0.3f * fraction);
    }


    public float calcValue(float fraction) {
        int startX = (int) (-mRange / 2);
        return 1 - startX * fraction;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 修正拖动的距离
     *
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if (left < 0) {
            return 0;
        } else if (left > mRange) {
            return (int) mRange;
        }
        return left;
    }

    private ViewDragHelper mDragHelper;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        mDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMainContentWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mRange = mMainContentWidth * 0.6f;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 2) {
            leftMenu = getChildAt(0);
            mainContent = getChildAt(1);
        } else {
            throw new RuntimeException("只能有两个子View");
        }
    }
    private OnWindowChanged mOnWindowChanged;

    public void setOnWindowChanged(OnWindowChanged onWindowChanged) {
        mOnWindowChanged = onWindowChanged;
    }

    interface OnWindowChanged {
        void open();
        void close();
    }
}
