package com.zhengsr.tablib.view.action;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.bean.TabTypeEvaluator;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.TabColorTextView;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.base.AbsFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制基类
 */
public abstract class BaseAction extends BViewPager {
    private static final String TAG = "BaseAction";
    public Paint mPaint;
    public RectF mTabRect;
    protected AbsFlowLayout mParentView;

    /**
     * logic
     */
    protected int mViewWidth;
    protected int mRightBound;
    private ValueAnimator mAnimator;
    protected float mOffset;
    protected Context mContext;
    private int mUnSelectedColor = FlowConstants.COLOR_ILLEGAL;
    private int mSelectedColor = FlowConstants.COLOR_ILLEGAL;
    protected int mCurrentIndex;
    protected int mLastIndex;
    protected boolean isColorText = false;
    protected boolean isTextView = false;

    protected TabBean mTabBean;
    protected static final int SMOOTH_Threshold = 3;
    protected boolean needSmooth = true;



    public BaseAction() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTabRect = new RectF();
    }

    /**
     * 配置数据
     *
     * @param parentView
     */
    public void config(AbsFlowLayout parentView) {
        mParentView = parentView;
        if (parentView.getChildCount() > 0 && mTabBean != null) {
            mContext = mParentView.getContext();
            mViewWidth = mParentView.getViewWidth();
            int childCount = mParentView.getChildCount();
            if (childCount > 0) {
                View child = mParentView.getChildAt(childCount - 1);
                //拿到有边界
                mRightBound = child.getRight() + mParentView.getPaddingRight();
            }

            View child = mParentView.getChildAt(0);
            //初始化第一个view的效果
            if (child != null) {
                if (isVertical()) {
                    mOffset = mTabBean.tabHeight * 1.0f / child.getMeasuredHeight();
                } else {
                    mOffset = mTabBean.tabWidth * 1.0f / child.getMeasuredWidth();
                }
                View textView = parentView.getTextView(0);
                if (textView != null) {
                    if (textView instanceof TabColorTextView) {
                        isColorText = true;
                        TabColorTextView colorTextView = (TabColorTextView) textView;
                        colorTextView.setTextColor(colorTextView.getChangeColor());
                    } else {
                        isTextView = true;
                    }

                }
                if (mTabBean.autoScale && mTabBean.scaleFactor > 1) {
                    child.setScaleX(mTabBean.scaleFactor);
                    child.setScaleY(mTabBean.scaleFactor);
                }
                mParentView.getAdapter().onItemSelectState(child, true);
            }
        }

    }


    /**
     * @param config
     */
    public void setTabConfig(TabConfig config) {
        if (config != null) {
            mSelectedColor = config.getSelectedColor();
            mUnSelectedColor = config.getUnSelectColor();
            if (config.getViewPager() != null) {
                setViewPager(config.getViewPager());
            }
            if (config.getViewPager2() != null) {
                setViewPager(config.getViewPager2());
            }
        }
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
        if (!isViewPager()) {
            autoScaleView();
            doAnim(lastIndex, curIndex, mTabBean.tabClickAnimTime);
        }
    }


    /**
     * 为了防止 colortextView 滚动时的残留，先清掉
     */
    public void clearColorText() {
        if (isColorText) {
            if (mParentView != null && Math.abs(mCurrentIndex - mLastIndex) > 0) {
                int childCount = mParentView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    TextView view = mParentView.getTextView(i);
                    if (view instanceof TabColorTextView) {
                        view.setTextColor(((TabColorTextView) view).getDefaultColor());
                    }

                }
                TextView view = mParentView.getTextView(mCurrentIndex);
                if (view instanceof TabColorTextView) {
                    view.setTextColor(((TabColorTextView) view).getChangeColor());
                }
            }
        }
    }


    /**
     * 放大缩小效果
     */
    public void autoScaleView() {
        if (mParentView != null && mTabBean.autoScale && mTabBean.scaleFactor > 1) {
            View lastView = mParentView.getChildAt(mLastIndex);
            View curView = mParentView.getChildAt(mCurrentIndex);
            if (lastView != null && curView != null) {
                lastView.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(mTabBean.tabClickAnimTime)
                        .setInterpolator(new LinearInterpolator())
                        .start();
                curView.animate()
                        .scaleX(mTabBean.scaleFactor)
                        .scaleY(mTabBean.scaleFactor)
                        .setDuration(mTabBean.tabClickAnimTime)
                        .setInterpolator(new LinearInterpolator())
                        .start();
            }
        }
    }

    /**
     * 执行点击移动动画
     *
     * @param lastIndex
     * @param curIndex
     */
    public void doAnim(int lastIndex, final int curIndex, int animTime) {

        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        if (mParentView != null) {
            final View curView = mParentView.getChildAt(curIndex);
            final View lastView = mParentView.getChildAt(lastIndex);
            if (curView != null && lastView != null) {
                TabValue lastValue = getValue(lastView);
                TabValue curValue = getValue(curView);
                if (isVertical()) {
                    if (mTabBean.tabHeight != -1) {
                        lastValue.top = mTabRect.top;
                        lastValue.bottom = mTabRect.bottom;
                        int height = curView.getMeasuredHeight();
                        //竖直方向不去理会
                        curValue.top = (height - mTabBean.tabHeight) / 2 + curView.getTop();
                        curValue.bottom = mTabBean.tabHeight + curValue.top;
                    }
                } else {
                    int width = curView.getMeasuredWidth();
                    if (mTabBean.tabWidth != -1) {
                        lastValue.left = mTabRect.left;
                        lastValue.right = mTabRect.right;
                        if (mTabBean.tabType == FlowConstants.RECT) {
                            curValue.left = (1 - mOffset) * width / 2 + curView.getLeft();
                            curValue.right = width * mOffset + curValue.left;
                        } else {
                            curValue.left = (width - mTabBean.tabWidth) / 2 + curView.getLeft();
                            curValue.right = mTabBean.tabWidth + curValue.left;
                        }
                    } else if (mTabBean.tabWidthEqualsText && mTabBean.tabType == FlowConstants.RECT &&
                            (mViewPager != null || mViewPager2 != null)) {
                        TextView curText = mParentView.getTextView(curIndex);
                        int textWidth = 0;
                        //todo 点击的也要支持
                        if (curText != null) {
                            int cw = (int) curText.getPaint().measureText(curText.getText().toString());
                            textWidth = cw;
                            float offset = textWidth * 1.0f / width;
                            curValue.left = (1 - offset) * width / 2 + curView.getLeft();
                            curValue.right = curValue.left + textWidth;
                        }

                    }
                }


                mAnimator = ObjectAnimator.ofObject(new TabTypeEvaluator(), lastValue, curValue);
                mAnimator.setDuration(animTime);
                mAnimator.setInterpolator(new LinearInterpolator());
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        TabValue value = (TabValue) animation.getAnimatedValue();
                        valueChange(value);
                        mParentView.postInvalidate();
                    }
                });
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (mParentView != null && mViewPager == null) {
                            TabFlowAdapter adapter = mParentView.getAdapter();
                            if (adapter != null) {
                                int count = adapter.getItemCount();
                                for (int i = 0; i < count; i++) {
                                    View child = mParentView.getChildAt(i);
                                    if (child == null) {
                                        return;
                                    }
                                    if (i == mCurrentIndex) {
                                        if (isTextView && !isColorText && mSelectedColor != FlowConstants.COLOR_ILLEGAL) {
                                            mParentView.getTextView(i).setTextColor(mSelectedColor);
                                        }
                                        adapter.onItemSelectState(child, true);
                                    } else {
                                        if (isTextView && !isColorText && mUnSelectedColor != FlowConstants.COLOR_ILLEGAL) {
                                            mParentView.getTextView(i).setTextColor(mUnSelectedColor);
                                        }
                                        adapter.onItemSelectState(child, false);
                                    }
                                }
                            }
                        }
                    }
                });
                mAnimator.start();
            } else {
                if (mAnimator != null) {
                    mAnimator.end();
                    mAnimator = null;
                }

            }

        }


    }

    /**
     * 选择某个item
     *
     * @param position
     */
    public void chooseSelectedPosition(int position) {
        if (mParentView != null) {
            int childCount = mParentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                TextView textView = mParentView.getTextView(i);
                if (textView != null) {
                    if (i == position) {
                        if (mSelectedColor != FlowConstants.COLOR_ILLEGAL) {
                            textView.setTextColor(mSelectedColor);
                        }
                    } else {
                        if (mUnSelectedColor != FlowConstants.COLOR_ILLEGAL) {
                            textView.setTextColor(mUnSelectedColor);
                        }
                    }
                }
            }
        }
    }


    /**
     * 选中默认的颜色
     *
     * @param lastIndex
     * @param curIndex
     */
    public void chooseIndex(int lastIndex, int curIndex) {
        mCurrentIndex = curIndex;
        mLastIndex = lastIndex;
        if (mViewPager != null) {
            chooseSelectedPosition(curIndex);
        }
        clearColorText();
        clearScale();

        //再把以前的效果换回去
        if (mParentView != null) {
            View child = mParentView.getChildAt(mCurrentIndex);
            if (child != null) {
                doAnim(mLastIndex, mCurrentIndex, 0);
                mOffset = mTabBean.tabWidth * 1.0f / child.getMeasuredWidth();
                View textView = mParentView.getTextView(mCurrentIndex);
                if (textView instanceof TabColorTextView) {
                    isColorText = true;
                    TabColorTextView colorTextView = (TabColorTextView) textView;
                    colorTextView.setTextColor(colorTextView.getChangeColor());
                }
                if (textView instanceof TextView) {
                    isTextView = true;
                    if (mSelectedColor != FlowConstants.COLOR_ILLEGAL) {
                        ((TextView) textView).setTextColor(mSelectedColor);
                    }
                }
                if (mTabBean.autoScale && mTabBean.scaleFactor > 1) {
                    child.setScaleX(mTabBean.scaleFactor);
                    child.setScaleY(mTabBean.scaleFactor);
                }
            }
            mParentView.postInvalidate();
        }

    }


    /**
     * 清掉属性动画
     */
    private void clearScale() {
        if (mParentView != null) {
            if (mTabBean.autoScale && mTabBean.scaleFactor > 1) {
                int childCount = mParentView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = mParentView.getChildAt(i);
                    view.setScaleY(1);
                    view.setScaleX(1);
                }
            }
        }
    }

    /**
     * 获取 value 的数据
     *
     * @param view
     * @return
     */
    private TabValue getValue(View view) {
        TabValue value = new TabValue();
        value.left = view.getLeft() + mTabBean.tabMarginLeft;
        value.top = view.getTop() + mTabBean.tabMarginTop;
        value.right = view.getRight() - mTabBean.tabMarginRight;
        value.bottom = view.getBottom() - mTabBean.tabMarginBottom;
        return value;
    }


    /**
     * item 偏移的变化率
     *
     * @param value
     */
    protected void valueChange(TabValue value) {
        mTabRect.left = value.left;
        mTabRect.right = value.right;
    }

    /**
     * 拿到自定义属性
     *
     * @param bean
     */
    public void configAttrs(TabBean bean) {
        mTabBean = bean;
        if (bean.tabColor != FlowConstants.COLOR_ILLEGAL) {
            mPaint.setColor(bean.tabColor);
        }
        if (bean.selectedColor != FlowConstants.COLOR_ILLEGAL) {
            mSelectedColor = bean.selectedColor;
        }
        if (bean.unSelectedColor != FlowConstants.COLOR_ILLEGAL) {
            mUnSelectedColor = bean.unSelectedColor;
        }
    }

    /**
     * 绘制不同的view
     */
    public abstract void draw(Canvas canvas);



    public int getLastIndex() {
        return mLastIndex;
    }


    /**
     * 是否是Viewpager
     *
     * @return
     */
    protected boolean isViewPager() {
        return mViewPager != null || mViewPager2 != null;
    }

    /**
     * tab 的方向
     *
     * @return
     */
    public boolean isVertical() {
        return mTabBean.tabOrientation == FlowConstants.VERTICAL;
    }

    public boolean isLeftAction() {
        return mTabBean.actionOrientation != -1 && mTabBean.actionOrientation == FlowConstants.LEFT;
    }

    public boolean isRightAction() {
        return mTabBean.actionOrientation != -1 && mTabBean.actionOrientation == FlowConstants.RIGHT;
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public void updatePos(int lastIndex, int curIndex) {
        needSmooth = Math.abs(curIndex - lastIndex) <= SMOOTH_Threshold;
    }


}
