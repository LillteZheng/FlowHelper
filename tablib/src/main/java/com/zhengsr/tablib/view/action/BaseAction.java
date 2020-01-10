package com.zhengsr.tablib.view.action;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabTypeEvaluator;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.ColorTextView;
import com.zhengsr.tablib.view.adapter.TabAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制基类
 */
public abstract class BaseAction implements ViewPager.OnPageChangeListener {
    private static final String TAG = "BaseAction";
    public Paint mPaint;
    public RectF mRect;
    protected TabFlowLayout mParentView;

    /**
     * logic
     */
    protected int mScreenWidth;
    protected int mRightBound;
    private ValueAnimator mAnimator;
    protected float mOffset;
    protected Context mContext;
    protected ViewPager mViewPager;
    private int mTextViewId = -1;
    private int mUnSelectedColor = -1;
    private int mSelectedColor = -1;
    private int mCurrentIndex;
    private int mLastIndex;
    /**
     * attrs
     */
    protected float mMarginLeft;
    protected float mMarginTop;
    protected float mMarginRight;
    protected float mMarginBottom;
    protected int mType;
    protected int mTabWidth = -1;
    protected int mTabHeight = -1;
    protected int mAnimTime;


    public BaseAction() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new RectF();
    }

    public void config(TabFlowLayout parentView) {
        mParentView = parentView;
        mContext = mParentView.getContext();
        mScreenWidth = mParentView.getResources().getDisplayMetrics().widthPixels;
        int childCount = mParentView.getChildCount();
        if (childCount > 0) {
            View child = mParentView.getChildAt(childCount - 1);
            mRightBound = child.getRight() + mParentView.getPaddingRight();
        }

        View child = mParentView.getChildAt(0);
        if (child != null) {
            mOffset = mTabWidth * 1.0f / child.getMeasuredWidth();
            if (mTextViewId != -1) {
                TextView textView = child.findViewById(mTextViewId);
                if (textView instanceof ColorTextView) {
                    textView.setTextColor(((ColorTextView) textView).getChangeColor());
                }
            }
        }

    }

    /**
     * 设置 viewpager 的点击选中颜色效果
     *
     * @param viewPager
     * @param textId
     * @param unselectedColor
     * @param selectedColor
     */
    public void setViewPager(final ViewPager viewPager, int textId, int unselectedColor, int selectedColor) {
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(null);
        viewPager.addOnPageChangeListener(this);
        mTextViewId = textId;
        mUnSelectedColor = unselectedColor;
        mSelectedColor = selectedColor;
    }


    /**
     * 点击事件
     *
     * @param lastIndex
     * @param curIndex
     */
    public void onItemClick(int lastIndex, int curIndex) {
        mCurrentIndex = curIndex;
        mLastIndex = lastIndex;
        clearColorText();
        if (mViewPager == null) {
            doAnim(lastIndex, curIndex);
        }
    }

    /**
     * 为了防止 colortextView 滚动时的残留，先清掉
     */
    public void clearColorText() {

        if (mParentView != null && Math.abs(mCurrentIndex - mLastIndex) > 1) {
            int childCount = mParentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = mParentView.getChildAt(i);
                View textview = view.findViewById(mTextViewId);
                if (textview instanceof ColorTextView) {
                    ColorTextView colorTextView = (ColorTextView) textview;
                    colorTextView.setTextColor(colorTextView.getDefaultColor());
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position 当前第一页的索引，比较有意思的是，当右滑时，position 表示当前页面，当左滑时，为当前页面减1；
         * positionOffset 当前页面移动的百分比
         * positionOffsetPixels 当前页面移动的像素
         */
        if (mParentView != null) {
            View curView = mParentView.getChildAt(position);
            float offset = curView.getMeasuredWidth() * positionOffset;
            int scrollX = (int) (curView.getLeft() + offset);
            if (offset > 0 && positionOffset > 0) {
                if (position < mParentView.getChildCount() - 1) {
                    //要偏移的view
                    final View transView = mParentView.getChildAt(position + 1);

                    //左边偏移量
                    float left = curView.getLeft() + positionOffset * (transView.getLeft() - curView.getLeft());
                    //右边表示宽度变化
                    float right = curView.getRight() + positionOffset * (transView.getRight() - curView.getRight());


                    if (mTabWidth != -1) {
                        //拿到左边初始坐标
                        int width = curView.getMeasuredWidth();
                        //todo 动态改变rect大小的，后面再看看
                        //(transView.getWidth() - width) * positionOffset
                        left = curView.getLeft() + (width - mTabWidth) / 2;
                        //再拿到偏移坐标
                        left = left + positionOffset * (curView.getMeasuredWidth() + transView.getMeasuredWidth()) / 2;
                        right = left + mTabWidth;
                    }
                    mRect.left = left;
                    mRect.right = right;
                    valueChange(new TabValue(mRect.left, mRect.right));
                    mParentView.postInvalidate();


                    //处理颜色渐变
                    if (mTextViewId != -1) {
                        View leftView = curView.findViewById(mTextViewId);
                        View rightView = transView.findViewById(mTextViewId);
                        if (leftView instanceof ColorTextView && rightView instanceof ColorTextView) {
                            ColorTextView colorLeft = (ColorTextView) leftView;
                            ColorTextView colorRight = (ColorTextView) rightView;
                            if (Math.abs(mCurrentIndex - mLastIndex) == 1) {
                                colorLeft.setprogress(1 - positionOffset, ColorTextView.DEC_RIGHT);
                                colorRight.setprogress(positionOffset, ColorTextView.DEC_LEFT);
                            }
                        }
                    }

                }
                //超过中间了，让父控件也跟着移动
                if (scrollX > mScreenWidth / 2 - mParentView.getPaddingLeft()) {
                    scrollX -= mScreenWidth / 2 - mParentView.getPaddingLeft();
                    //有边界提醒
                    if (scrollX <= mRightBound - mScreenWidth) {
                        mParentView.scrollTo(scrollX, 0);
                    } else {
                        int dx = mRightBound - mScreenWidth;
                        mParentView.scrollTo(dx, 0);
                    }
                } else {
                    mParentView.scrollTo(0, 0);
                }

            }


        }

    }

    @Override
    public void onPageSelected(int position) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = position;
        chooseSelectedPosition(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        /**
         * 滚动结束时，再来改变colortextview，防止闪烁问题和卡顿问题
         */
        if (state == ViewPager.SCROLL_STATE_IDLE && mTextViewId != -1) {
            if (mParentView != null && Math.abs(mCurrentIndex - mLastIndex) > 1) {
                /**
                 * 在这里加这个，是为了多个 flowlayout 跟 同个 viewpager 结合时；使用了 viewpager
                 * 的 setCurrentItem，但是却没清其他 textview 的颜色值，所以在这里做个保险；
                 */
                clearColorText();
                View view = mParentView.getChildAt(mCurrentIndex);
                TextView colorTextView = view.findViewById(mTextViewId);
                if (colorTextView instanceof ColorTextView) {
                    colorTextView.setTextColor(((ColorTextView) colorTextView).getChangeColor());
                }
            }
        }
    }


    /**
     * 执行点击移动动画
     *
     * @param lastIndex
     * @param curIndex
     */
    public void doAnim(int lastIndex, final int curIndex) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.end();
            mAnimator = null;
        }
        if (mParentView != null) {
            final View curView = mParentView.getChildAt(curIndex);
            final View lastView = mParentView.getChildAt(lastIndex);

            TabValue lastValue = getValue(lastView);
            TabValue curValue = getValue(curView);
            if (mTabWidth != -1) {
                lastValue.left = mRect.left;
                lastValue.right = mRect.right;
                int width = curView.getMeasuredWidth();
                if (mType == FlowConstants.RECT) {
                    curValue.left = (1 - mOffset) * width / 2 + curView.getLeft();
                    curValue.right = width * mOffset + curValue.left;
                } else {
                    curValue.left = (width - mTabWidth) / 2 + curView.getLeft();
                    curValue.right = mTabWidth + curValue.left;
                }
            }
            mAnimator = ObjectAnimator.ofObject(new TabTypeEvaluator(), lastValue, curValue);
            mAnimator.setDuration(mAnimTime);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    TabValue value = (TabValue) animation.getAnimatedValue();
                    valueChange(value);
                    mParentView.postInvalidate();
                }
            });
            mAnimator.start();
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mParentView != null && mViewPager == null) {
                        TabAdapter adapter = mParentView.getAdapter();
                        if (adapter != null) {
                            int childCount = mParentView.getChildCount();
                            for (int i = 0; i < childCount; i++) {
                                View view = mParentView.getChildAt(i);
                                if (i == mCurrentIndex){
                                    adapter.onItemSelectState(view,true);
                                }else{
                                    adapter.onItemSelectState(view,false);
                                }
                            }
                        }
                    }
                }
            });
        }


    }

    /**
     * 选择某个item
     *
     * @param position
     */
    public void chooseSelectedPosition(int position) {
        if (mTextViewId != -1 && mParentView != null) {
            int childCount = mParentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = mParentView.getChildAt(i);
                View textView = view.findViewById(mTextViewId);
                if (!(textView instanceof ColorTextView)) {
                    if (textView instanceof TextView) {
                        if (i == position) {
                            ((TextView) textView).setTextColor(mSelectedColor);
                        } else {
                            ((TextView) textView).setTextColor(mUnSelectedColor);
                        }
                    }
                }

            }
        }
    }

    private TabValue getValue(View view) {
        TabValue value = new TabValue();
        value.left = view.getLeft();
        value.right = view.getRight();
        return value;
    }


    /**
     * item 偏移的变化率
     * @param value
     */
    protected void valueChange(TabValue value) {
        mRect.left = value.left;
        mRect.right = value.right;
    }

    /**
     * 拿到自定义属性
     *
     * @param ta
     */
    public void configAttrs(TypedArray ta) {
        mTabWidth = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_width, -1);
        mTabHeight = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_height, -1);
        int color = ta.getColor(R.styleable.TabFlowLayout_tab_color, Color.RED);
        mPaint.setColor(color);
        mType = ta.getInteger(R.styleable.TabFlowLayout_tab_type, -1);
        mMarginLeft = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_l, 0);
        mMarginTop = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_t, 0);
        mMarginRight = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_r, 0);
        mMarginBottom = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_b, 0);
        mAnimTime = ta.getInteger(R.styleable.TabFlowLayout_tab_click_animTime, 300);


    }

    /**
     * 绘制不同的view
     */
    public abstract void draw(Canvas canvas);


    public int getCurrentIndex() {
        return mCurrentIndex;
    }


    /**
     * 配置动态属性
     *
     * @param bean
     */
    public void setBean(TabBean bean) {
        if (bean.tabColor != -2) {
            mPaint.setColor(bean.tabColor);
        }
        if (bean.tabWidth != -1) {
            mTabWidth = bean.tabWidth;
        }
        if (bean.tabHeight != -1) {
            mTabHeight = bean.tabHeight;
        }
        if (bean.tabClickAnimTime != -1) {
            mAnimTime = bean.tabClickAnimTime;
        }
        if (bean.tabMarginLeft != -1) {
            mMarginLeft = bean.tabMarginLeft;
        }
        if (bean.tabMarginTop != -1) {
            mMarginTop = bean.tabMarginTop;
        }
        if (bean.tabMarginRight != -1) {
            mMarginRight = bean.tabMarginRight;
        }
        if (bean.tabMarginBottom != -1) {
            mMarginBottom = bean.tabMarginBottom;
        }

    }

    public ViewPager getViewPager() {
        return mViewPager;
    }
}
