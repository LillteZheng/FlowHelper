package com.zhengsr.tablib.view.action;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.TabColorTextView;

/**
 * @author by zhengshaorui 2022/3/19
 * describe：专门处理viewpager相关的action
 */
public abstract class BaseVpAction extends BaseAction {


    private TabValue tabValue = new TabValue();
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * 为了避免卡顿，当点击时，我们不应该让它持续变化，比如从0，点击到 9；
         * 那么动画会从 0 执行到 9，如果此时加上 scrollto 移动 view 等操作，那么
         * 整个动画会变得卡顿，这点从 profiler 就能拿到，因此，如果是点击的控件与
         * 上次相差超过 1，则不执行这个指令，直接在点击的时候，就呈现改效果。
         */
        if (mParentView != null) {

            View curView = mParentView.getChildAt(position);
            if (curView == null) {
                return;
            }
            float offset = curView.getMeasuredWidth() * positionOffset;
            int scrollX = (int) (curView.getLeft() + offset);
            if (offset > 0 && positionOffset > 0) {
                if (needSmooth) {
                    if (position < mParentView.getChildCount() - 1) {
                        //要偏移的view
                        final View transView = mParentView.getChildAt(position + 1);

                        //左边偏移量
                        float left;
                        //右边表示宽度变化
                        float right;

                        int width = curView.getMeasuredWidth();
                        int textWidth = 0;
                        if (mTabBean.tabWidth != -1) {
                            textWidth = mTabBean.tabWidth;
                            left = curView.getLeft() + (width - textWidth) * 1.0f / 2;
                            //再拿到偏移坐标
                            left = left + positionOffset * (curView.getMeasuredWidth() + transView.getMeasuredWidth()) / 2;
                            right = left + textWidth;
                        } else if (mTabBean.tabWidthEqualsText && mTabBean.tabType == FlowConstants.RECT) {
                            TextView transText = mParentView.getTextView(position + 1);
                            TextView curText = mParentView.getTextView(position);
                            if (curText != null) {
                                int cw = (int) curText.getPaint().measureText(curText.getText().toString());
                                textWidth = cw;
                                if (transText != null) {
                                    int tw = (int) transText.getPaint().measureText(transText.getText().toString());
                                    textWidth += (int) ((tw - cw) * positionOffset);
                                }
                            }
                            left = curView.getLeft() + (width - textWidth) * 1.0f / 2;
                            //再拿到偏移坐标
                            left = left + positionOffset * (curView.getMeasuredWidth() + transView.getMeasuredWidth()) / 2;
                            right = left + textWidth;
                        } else {
                            left = curView.getLeft() + positionOffset * (transView.getLeft() - curView.getLeft());
                            right = curView.getRight() + positionOffset * (transView.getRight() - curView.getRight());
                        }


                        //处理颜色渐变
                        if (isColorText) {
                            TextView leftView = mParentView.getTextView(position);
                            TextView rightView = mParentView.getTextView(position + 1);
                            TabColorTextView colorLeft = (TabColorTextView) leftView;
                            TabColorTextView colorRight = (TabColorTextView) rightView;
                            colorLeft.setprogress(1 - positionOffset, TabColorTextView.DEC_RIGHT);
                            colorRight.setprogress(positionOffset, TabColorTextView.DEC_LEFT);
                        }

                        //大小渐变效果
                        if (mTabBean.autoScale && mTabBean.scaleFactor > 0) {
                            float factor = mTabBean.scaleFactor % 1;
                            float transScale = 1 + factor * positionOffset;
                            float curScale = 1 + factor * (1 - positionOffset);
                            transView.setScaleX(transScale);
                            transView.setScaleY(transScale);
                            curView.setScaleX(curScale);
                            curView.setScaleY(curScale);
                        }
                        if (isSupportMargin()) {
                            mTabRect.left = left + mTabBean.tabMarginLeft;
                            mTabRect.right = right - mTabBean.tabMarginRight;
                        } else {
                            mTabRect.left = left;
                            mTabRect.right = right;
                        }
                        tabValue.left = mTabRect.left;
                        tabValue.right = mTabRect.right;
                        valueChange(tabValue);
                        mParentView.postInvalidate();


                    }
                }
                //超过中间了，让父控件也跟着移动
                if (mParentView.isCanMove()) {
                    if (scrollX > (mViewWidth / 2 - mParentView.getPaddingLeft())) {
                        scrollX -= (mViewWidth / 2 - mParentView.getPaddingLeft());
                        //有边界提醒
                        if (scrollX <= mRightBound - mViewWidth) {
                            mParentView.scrollTo(scrollX, 0);
                        } else {
                            int dx = mRightBound - mViewWidth;
                            mParentView.scrollTo(dx, 0);
                        }
                    } else {
                        mParentView.scrollTo(0, 0);
                    }
                }

            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = position;
        chooseSelectedPosition(position);
        checkIfNeedScroll(position);

    }

    @Override
    public void chooseSelectedPosition(int position) {
        super.chooseSelectedPosition(position);
        if (!needSmooth){
            View curView = mParentView.getChildAt(position);
            int width = curView.getMeasuredWidth();
            float left;
            float right;
            int textWidth = 0;
            if (mTabBean.tabWidth != -1) {
                textWidth = mTabBean.tabWidth;
                left = curView.getLeft() + (width - textWidth) * 1.0f / 2;
                right = left + textWidth;
            } else if (mTabBean.tabWidthEqualsText && mTabBean.tabType == FlowConstants.RECT) {
                TextView curText = mParentView.getTextView(position);
                if (curText != null) {
                    textWidth = (int) curText.getPaint().measureText(curText.getText().toString());
                }
                left = curView.getLeft() + (width - textWidth) * 1.0f / 2;
                right = left + textWidth;
            } else {
                left = curView.getLeft();
                right = curView.getRight();
            }
            mTabRect.left = left;
            mTabRect.right = right;
            tabValue.left = mTabRect.left;
            tabValue.right = mTabRect.right;
            valueChange(tabValue);
        }
    }

    private void checkIfNeedScroll(int position){
        if (isChooseItemWhenPageSelected) {
            if (mParentView != null) {
                if (mParentView.isCanMove()) {
                    View curView = mParentView.getChildAt(position);
                    int scrollX = (int) (curView.getLeft());
                    if (scrollX > (mViewWidth / 2 - mParentView.getPaddingLeft())) {
                        scrollX -= (mViewWidth / 2 - mParentView.getPaddingLeft());
                        //有边界提醒
                        if (scrollX <= mRightBound - mViewWidth) {
                            mParentView.scrollTo(scrollX, 0);
                        } else {
                            int dx = mRightBound - mViewWidth;
                            mParentView.scrollTo(dx, 0);
                        }
                    } else {
                        mParentView.scrollTo(0, 0);
                    }
                }
                isChooseItemWhenPageSelected = false;
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        /**
         * 这里的处理是因为外部调用了 setCurrentItem ，此时 item 之间的差值也大于1;
         * 但因为没有回调 onItemClick，所以 draw 、动画等效果没法实现，因此，可以先通过
         * Viewpager 拿到 setCurrentItem(position) 中的position，赋值给当前的 mCurrentIndex；
         * 且两者之间大于1时，直接使用draw和动画效果；不再让 onPageScrolled 去执行动画，避免卡顿
         */
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            mLastIndex = mCurrentIndex;
            if (mViewPager != null) {
                mCurrentIndex = mViewPager.getCurrentItem();
            }
            if (mViewPager2 != null) {
                mCurrentIndex = mViewPager2.getCurrentItem();
            }
            if (Math.abs(mCurrentIndex - mLastIndex) > SMOOTH_Threshold) {
                clearColorText();
                doAnim(mLastIndex, mCurrentIndex, mTabBean.tabClickAnimTime);
                autoScaleView();
            }
        }

        if (state == ViewPager.SCROLL_STATE_IDLE) {
            needSmooth = true;
        }

    }

    private boolean isSupportMargin() {
        if (FlowConstants.RES == mTabBean.tabType ||
                FlowConstants.ROUND == mTabBean.tabType
        ) {
            return true;
        }
        return false;
    }
}
