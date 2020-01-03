package com.zhengsr.tablib;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.zhengsr.tablib.callback.AdapterListener;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 实现数据封装与一些重绘工作
 */
public class TabFlowLayout extends ScrollFlowLayout implements AdapterListener {
    private static final String TAG = "TabFlowLayout";
    private TabAdapter mAdapter;
    private Paint mPaint;
    private RectF mRect;
    private int mLastIndex = 0;

    public TabFlowLayout(Context context) {
        this(context, null);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mRect = new RectF();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //绘制一个矩形
        // canvas.drawRect(mRect,mPaint);
        canvas.drawRoundRect(mRect, 10, 10, mPaint);
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View child = getChildAt(0);
        if (child != null) {
            //拿到第一个数据
            mRect.set(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    /**
     * 添加adapter，
     *
     * @param adapter
     */
    public void setAdapter(TabAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setListener(this);
        //实现数据更新
        notifyDataChanged();
    }


    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPagerListener());
    }

    @Override
    public void notifyDataChanged() {
        removeAllViews();
        TabAdapter adapter = mAdapter;
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(adapter.getLayoutId(), this, false);
            adapter.bindView(view, adapter.getDatas().get(i), i);
            configClick(view, i);
            addView(view);
        }
    }


    /**
     * 配置 点击事件
     *
     * @param view
     * @param i
     */
    private void configClick(final View view, final int i) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.onItemClick(view, mAdapter.getDatas().get(i), i);
                doAnim(mLastIndex, i);
                mLastIndex = i;
            }
        });
    }

    private void doAnim(int lastIndex, final int curIndex) {
        final View curView = getChildAt(curIndex);
        final View lastView = getChildAt(lastIndex);

        PointF lastPos = new PointF();
        lastPos.x = lastView.getLeft();
        lastPos.y = lastView.getRight();

        PointF curPos = new PointF();
        curPos.x = curView.getLeft();
        curPos.y = curView.getRight();

        ValueAnimator animator = ObjectAnimator.ofObject(new pointFType(), lastPos, curPos);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                mRect.left = pointF.x;
                mRect.right = pointF.y;
                postInvalidate();

            }
        });
        animator.start();
    }

    class pointFType implements TypeEvaluator<PointF> {
        PointF pointF = new PointF();

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //这里都采用匀速
            pointF.x = startValue.x + fraction * (endValue.x - startValue.x);
            pointF.y = startValue.y + fraction * (endValue.y - startValue.y);
            return pointF;
        }
    }


    class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /**
             * position 当前页面
             * positionOffset 当前页面移动的百分比
             * positionOffsetPixels 当前页面移动的像素
             */
            //居中代码
            float offset = getChildAt(position).getWidth() * positionOffset;
            int scrollX = (int) (getChildAt(position).getLeft() + offset);
            if (position < getChildCount() - 1) {
                final View lastView = getChildAt(position);
                final View curView = getChildAt(position + 1);

                float left = lastView.getLeft() + positionOffset * (curView.getLeft() - lastView.getLeft());
                float right = lastView.getRight() + positionOffset * (curView.getRight() - lastView.getRight());
                mRect.left = left;
                mRect.right = right;
                postInvalidate();
                int width = getResources().getDisplayMetrics().widthPixels;
                if (scrollX > width / 2 - getPaddingLeft()) {
                    scrollX -= width / 2 - getPaddingLeft();
                    scrollTo(scrollX, 0);
                }
            }


        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }


}
