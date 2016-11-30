package com.github.yujintao529.library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


/**
 * Created by yujintao on 16/11/21.
 */

public class CollapseTextView extends TextView {


    private enum TextState {
        none,
        expand,
        collapse,
        expanding
    }

    private boolean enableExpandMode = true;

    private TextState textState;
    private Drawable mDrawable;//icon

    private boolean needDrawMore = false;

    private Rect mDrawableRect = new Rect();

    private Rect temp = new Rect();

    private int mRecorderMaxLineHeight = -1;//记录最大行的高度
    private int mRecorderMaxHeight = -1;
    private int mRecorderLines;
    private int drawableHeight = -1;//下标的高度
    private boolean hasRecorder = false;
    private int mCurrentHeight;
    private Runnable mSetMaxLinesRunnable = new Runnable() {
        @Override
        public void run() {
            setMaxLines(mRecorderLines);
        }
    };

    public CollapseTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CollapseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CollapseTextView(Context context) {
        super(context);
        init(null);

    }

    public boolean isEnableExpandMode() {
        return enableExpandMode;
    }

    public void setEnableExpandMode(boolean enableExpandMode) {
        this.enableExpandMode = enableExpandMode;
    }

    private void onHeightMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int maxHeight = getMeasuredHeight();
        needDrawMore = false;
        mCurrentHeight = getMeasuredHeight();
        if (maxHeight == mRecorderMaxLineHeight + drawableHeight) {//如果height模式，高度与line模式相同的话，则是collapse模式
            setSingleLine(false);
            post(mSetMaxLinesRunnable);
            return;
        } else if (maxHeight == this.mRecorderMaxHeight) {
            textState = TextState.expand;
        } else {
            textState = TextState.expanding;
        }
    }


    public int getLocalMaxLines() {
        return TextViewCompat.getMaxLines(this);
    }

    public int getLocalMinLines() {
        return TextViewCompat.getMinLines(this);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void onLinesMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int lines = getLineCount();
        if (getLocalMaxLines() != Integer.MAX_VALUE) {
            mRecorderLines = getLocalMaxLines();
        }

        int width = getMeasuredWidth();
        mCurrentHeight = getMeasuredHeight();
        if (mRecorderLines <= 0 || lines <= getLocalMaxLines() || mDrawable == null) {
            needDrawMore = false;
            return;
        }
        textState = TextState.collapse;
        hasRecorder = true;
        needDrawMore = true;
        drawableHeight = mDrawable.getIntrinsicHeight();
        getLayout().getLineBounds(mRecorderLines - 1, temp);
        mRecorderMaxLineHeight = temp.bottom;
        getLayout().getLineBounds(lines - 1, temp);
        mRecorderMaxHeight = temp.bottom;
        mDrawableRect.set((width - mDrawable.getIntrinsicWidth()) / 2, mRecorderMaxLineHeight, (width - mDrawable.getIntrinsicWidth()) / 2 + mDrawable.getIntrinsicWidth(), mRecorderMaxLineHeight + drawableHeight);
        mDrawable.setBounds(mDrawableRect);
        setMeasuredDimension(getMeasuredWidthAndState(), View.MeasureSpec.makeMeasureSpec(mRecorderMaxLineHeight + drawableHeight, View.MeasureSpec.getMode(getMeasuredHeightAndState())));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (enableExpandMode) {
            final int maxLines = getLocalMaxLines();
            if (maxLines > 0) {
                onLinesMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                onHeightMeasure(widthMeasureSpec, heightMeasureSpec);
            }

        }
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
    }

    /**
     * draw icon
     *
     * @param canvas
     */
    private void drawIconIfNeed(Canvas canvas) {
        if (enableExpandMode && needDrawMore) {
            mDrawable.draw(canvas);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIconIfNeed(canvas);

    }

    public void toggle(boolean anim) {
        if (!enableExpandMode || !hasRecorder) {
            return;
        }
        if (textState == TextState.expand) {
            collapse(mRecorderMaxHeight, mRecorderMaxLineHeight + drawableHeight, anim);
        } else if (textState == TextState.collapse) {
            expand(mRecorderMaxLineHeight + drawableHeight, mRecorderMaxHeight, anim);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private ValueAnimator mValueAnimator;


    private void internal(int start, int end) {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(start, end);
            mValueAnimator.setDuration(300);
            mValueAnimator.addUpdateListener(mAnimatorUpdateListener);
            mValueAnimator.addListener(mAnimatorListener);
        } else {
            mValueAnimator.setIntValues(start, end);
        }
        if (mValueAnimator.isRunning() || mValueAnimator.isStarted()) {
            mValueAnimator.cancel();
        }
        mValueAnimator.start();
    }


    private void expand(int startHeight, int endHeight, boolean anim) {
        if (anim) {
            internal(startHeight, endHeight);
        } else {
            textState = TextState.expand;
            setMaxLines(Integer.MAX_VALUE);
        }

    }

    private void collapse(int startHeight, int endHeight, boolean anim) {
        if (anim) {
            internal(startHeight, endHeight);
        } else {
            textState = TextState.collapse;
            setMaxLines(mRecorderLines);

        }
    }


    private void init(AttributeSet set) {
        if (set != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(set, R.styleable.CollapseTextView);
            setEnableExpandMode(typedArray.getBoolean(R.styleable.CollapseTextView_enableCollapse, enableExpandMode));
            typedArray.recycle();
        }
        mDrawable = getContext().getResources().getDrawable(R.drawable.icon_arrow_down);
    }


    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            setHeight(value);
        }
    };

    private ValueAnimator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };



}
