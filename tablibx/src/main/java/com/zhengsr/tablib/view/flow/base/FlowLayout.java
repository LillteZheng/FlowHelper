package com.zhengsr.tablib.view.flow.base;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.BaseLabelItem;
import com.zhengsr.tablib.view.adapter.BaseFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 瀑布流布局,这个类只用来测量子控件，不做其他操作
 */
class FlowLayout<T extends BaseFlowAdapter> extends ViewGroup {
    private static final String TAG = "FlowLayout";
    protected int mViewWidth;
    protected int mViewHeight;
    /**
     * tabflowlayout
     */
    private int mTabOrientation;
    protected int mVisibleCount = -1;
    /**
     * labelflowlayout
     */
    protected List<Integer> mLineHeights = new ArrayList<>();
    private List<List<View>> mAllViews = new ArrayList<>();
    private int mLabelLines = -1;
    private boolean isLabelMoreLine;
    protected int mLineWidth;
    protected T mAdapter;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isLabelFlow()) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            if (mTabOrientation == FlowConstants.HORIZONTATAL) {
                measureTabHorizontal(widthMeasureSpec, heightMeasureSpec);
            } else {
                measureTabVertical(widthMeasureSpec, heightMeasureSpec);
            }
        }


    }

    protected void setVisibleCount(int visualCount) {
        mVisibleCount = visualCount;
    }

    /**
     * 设置行数
     *
     * @param lines
     */
    protected void setLabelLines(int lines) {
        mLabelLines = lines;
    }

    /**
     * tabflowlayout 的方向
     *
     * @param orientation
     */
    public void setTabOrientation(int orientation) {
        mTabOrientation = orientation;
    }

    protected boolean isLabelFlow() {
        return true;
    }


    /**
     * 测试tabflowlayout竖直状态
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void measureTabVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int width = 0;
        int height = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            //拿到 子控件宽高 + margin
            int cw = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int ch = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            height += ch;

            width = Math.max(width, cw);
        }

        if (MeasureSpec.EXACTLY == widthMode) {
            width = widthSize;
        } else {
            width += getPaddingLeft() + getPaddingRight();
        }
        if (MeasureSpec.EXACTLY == heightMode) {
            height = heightSize;
        } else {
            height += getPaddingTop() + getPaddingBottom();
        }

        mViewHeight = height;
        setMeasuredDimension(width, height);
    }


    /**
     * TabFlowLayout 测量横向方向
     * 和轻量级的 tag
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void measureTabHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        /**
         * 计算宽高
         */
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        if (MeasureSpec.EXACTLY == widthMode) {
            widthPixels = widthSize;
        } else {
            if (mVisibleCount != -1) {
                MarginLayoutParams parentParams = (MarginLayoutParams) getLayoutParams();
                widthPixels -= (getPaddingStart() + getPaddingEnd() + parentParams.leftMargin + parentParams.rightMargin);
            }
        }

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            //拿到 子控件宽高 + margin
            int cw;
            if (mVisibleCount != -1) {
                cw = (int) (widthPixels * 1.0f / mVisibleCount);
                params.width = cw;
                child.setLayoutParams(params);
                width = widthPixels;
            } else {
                cw = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                width += cw;
            }
            int ch = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            //拿到 子控件高度，拿到最大的那个高度
            height = Math.max(height, ch);

        }
        //具体大小，padding不受影响
        if (MeasureSpec.EXACTLY == heightMode) {
            height = heightSize;
        } else {
            height = height + getPaddingTop() + getPaddingBottom();
        }
        if (mVisibleCount == -1) {
            if (MeasureSpec.EXACTLY == widthMode) {
                width = widthSize;
            } else {
                width += getPaddingLeft() + getPaddingRight();
            }
        }
        mViewWidth = width;
        setMeasuredDimension(width, height);
    }


    /**
     * LabelFlowLayout
     * 测量竖直方向，常用瀑布流布局 tag 标签
     * 其中宽度是不变的，而高度是根据子控件来的
     */
    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        //可能添加多次，所以需要清掉
        mAllViews.clear();
        mLineHeights.clear();
        /**
         * 拿到viewgroup为子控件推荐的宽高和模式
         */
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        // 计算所有的 children 的宽高
        int count = getChildCount();
        int lineWidth = 0;
        //拿到行高，取换行前，child 的最大值
        int lineHeight = 0;
        // viewgroup 的高度
        int height = 0;


        List<View> lineViews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            /**
             * child 的宽高由两方面确定
             *  1、自己本身在 xml 的大小，比如 30dp，match_parent,wrap_content
             *  2、父控件的模式对它其子控件的测量影响，如下：
             *
             * 当 父控件的的模式为 EXACTLY 时：
             *  1、如果 child 的宽是确定的，或者 match_parent ，则 child 的模式也是 EXACTLY
             *  2、如果 chihld 的宽时 wrap_parent ，则child 为 AT_MOST 模式。
             * 当 父控件的模式 AT_MOST是：
             *  1、child 是确定的，则 mode 为 EXACTLY,其他的都是  AT_MOST 模式
             * 当 父控件的模式为 UNSPECIFIED 时；
             *  1、child 如果是 EXACTLY ，则 mode 也是 EXACTLY ，其他都为 UNSPECIFIED
             */
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            /**
             * 经过measureChild 之后，就可以拿到 child 的测量宽高了
             */
            //这里直接强转，为什么不报错，由下面4个方法去转化
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int cHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;


            /**
             * 确定是否换行
             */
            boolean isFull = lineWidth + cWidth > (widthSize - (getPaddingLeft() + getPaddingRight()));
            boolean isHeader = isHeader(i);


            if (isFull) {
                //换行
                height += lineHeight;

                mLineHeights.add(lineHeight);
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineViews.add(child);
                //重置为下一个child 的宽度
                lineWidth = cWidth;
                lineHeight = cHeight;

                /**
                 * 是否设置了显示行数
                 */
                if (mLabelLines != -1 && mLineHeights.size() >= mLabelLines) {
                    isLabelMoreLine = true;
                    break;
                } else {
                    isLabelMoreLine = false;
                }

            } else {
                //未换行
                if (isHeader) {
                    //查看上一个
                    if (i > 0 && getChildAt(i - 1) != null) {
                        //// 如果当前子视图是头部视图，并且上一个子视图不为空，则另起一行
                        height += lineHeight;
                        mLineHeights.add(lineHeight);
                        mAllViews.add(lineViews);
                        lineViews = new ArrayList<>();
                        //重置为下一个child 的宽度
                        lineWidth = 0;
                        lineHeight = 0;
                    }
                }
                lineWidth += cWidth;
                lineHeight = Math.max(lineHeight, cHeight);
                mLineWidth = Math.max(mLineWidth, lineWidth);
                lineViews.add(child);

                if (isHeader) {
                    //换行
                    height += lineHeight;
                    mLineHeights.add(lineHeight);
                    mAllViews.add(lineViews);
                    lineViews = new ArrayList<>();
                    //重置为下一个child 的宽度
                    lineWidth = 0;
                    lineHeight = 0;
                }


            }

            //加最后一行
            if (i == count - 1) {
                height += lineHeight;
                mLineHeights.add(lineHeight);
                mAllViews.add(lineViews);
            }
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(height, heightSize);
            height += getPaddingTop() + getPaddingBottom();
        } else {
            height += getPaddingTop() + getPaddingBottom();
        }
        mViewHeight = height;
        //把测量完成的高，重设置给父控件

        setMeasuredDimension(widthSize, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /**
         * View 的摆放
         * 需要给每一行的 View 设置 child.layout(l,t,r,b) ，行高也要设置，
         * 这些数据从onMeasure 中已经计算好了，所以，只需要把值拿到就可以了
         */
        if (isLabelFlow()) {
            int size = mAllViews.size();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            for (int i = 0; i < size; i++) {
                List<View> lineViews = mAllViews.get(i);
                int count = lineViews.size();
                for (int j = 0; j < count; j++) {
                    View child = lineViews.get(j);
                    MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                    int cWidth = child.getMeasuredWidth();
                    int cHeight = child.getMeasuredHeight();

                    //确定位置和具体控件大小

                    int cl = left + params.leftMargin;
                    int ct = top + params.topMargin;
                    int cr = cl + cWidth;
                    int cb = ct + cHeight;
                    child.layout(cl, ct, cr, cb);
                    left += cWidth + params.leftMargin + params.rightMargin;
                }
                left = getPaddingLeft();
                top += mLineHeights.get(i);

            }

        } else {
            int count = getChildCount();
            //父空间的padding
            int left = getPaddingStart();
            int top = getPaddingTop();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int cl;
                if (isVertical()) {
                    cl = (getWidth() - child.getMeasuredWidth()) / 2;
                } else {
                    cl = left + params.leftMargin;
                }
                int cr;
                if (mVisibleCount != -1) {
                    cr = cl + params.width - getPaddingStart() - params.leftMargin - params.rightMargin;
                } else {
                    cr = cl + child.getMeasuredWidth();
                }
                int ct = top + params.topMargin;
                int cb = ct + child.getMeasuredHeight();
                child.layout(cl, ct, cr, cb);
                //下个控件的起始位置
                if (mTabOrientation == FlowConstants.HORIZONTATAL) {
                    if (mVisibleCount != -1) {
                        left += params.width;
                    } else {
                        left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                    }
                } else {
                    top += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

                }
            }
        }

    }

    public boolean isVertical() {
        return mTabOrientation == FlowConstants.VERTICAL;
    }

    public boolean isVerticalMove() {
        return isVertical() || isLabelFlow();
    }

    private boolean isHeader(int index) {
        if (mAdapter != null && mAdapter.getDatas() != null) {
            Object bean = mAdapter.getDatas().get(index);
            if (bean instanceof BaseLabelItem) {
                return ((BaseLabelItem) bean).isHeader;
            }
        }
        return false;
    }

    public boolean isLabelMoreLine() {
        return isLabelMoreLine;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}
