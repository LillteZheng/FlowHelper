package com.zhengsr.tablib.view.action;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabTypeEvaluator;
import com.zhengsr.tablib.bean.TabValue;
import com.zhengsr.tablib.view.TabColorTextView;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;
import com.zhengsr.tablib.view.flow.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制基类
 */
public abstract class BaseAction implements ViewPager.OnPageChangeListener {
    private static final String TAG = "BaseAction";
    public Paint mPaint;
    public RectF mTabRect;
    protected TabFlowLayout mParentView;

    /**
     * logic
     */
    protected int mViewWidth;
    protected int mRightBound;
    private ValueAnimator mAnimator;
    protected float mOffset;
    protected Context mContext;
    protected ViewPager mViewPager;
    private int mTextViewId = -1;
    private int mUnSelectedColor = -1;
    private int mSelectedColor = -1;
    protected int mCurrentIndex;
    private int mLastIndex;
    private boolean isColorText = false;
    private boolean isTextView = false;
    private boolean isTabClick = false;

    protected TabBean mTabBean;

    public BaseAction() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTabRect = new RectF();
    }

    /**
     * 配置数据
     * @param parentView
     */
    public void config(TabFlowLayout parentView) {
        mParentView = parentView;
        if (parentView.getChildCount() > 0 && mTabBean!= null) {
            mContext = mParentView.getContext();
            mViewWidth = mParentView.getViewWidth();
            int childCount = mParentView.getChildCount();
            if (childCount > 0) {
                View child = mParentView.getChildAt(childCount - 1);
                mRightBound = child.getRight() + mParentView.getPaddingRight();
            }

            View child = mParentView.getChildAt(0);
            if (child != null) {
                if (isVertical()){
                    mOffset = mTabBean.tabHeight * 1.0f / child.getMeasuredHeight();
                }else {
                    mOffset = mTabBean.tabWidth * 1.0f / child.getMeasuredWidth();
                }
                if (mTextViewId != -1) {
                    View textView = child.findViewById(mTextViewId);
                    if (textView instanceof TabColorTextView) {
                        isColorText = true;
                        TabColorTextView colorTextView = (TabColorTextView) textView;
                        colorTextView.setTextColor(colorTextView.getChangeColor());
                    }
                    if (textView instanceof TextView){
                        isTextView = true;
                    }

                }
                if (mTabBean.autoScale && mTabBean.scaleFactor > 1) {
                    child.setScaleX(mTabBean.scaleFactor);
                    child.setScaleY(mTabBean.scaleFactor);
                }
                mParentView.getAdapter().onItemSelectState(child,true);
            }
        }

    }

    /**
     * 设置 viewpager
     *
     */
    public BaseAction setViewPager(final ViewPager viewPager) {
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(null);
        viewPager.addOnPageChangeListener(this);
        return this;

    }

    /**
     * 设置textview 的id ，不然颜色不起作用
     * @param textId
     */
    public  BaseAction setTextId(int textId){
        mTextViewId = textId;
        return this;
    }

    /**
     * 设置选中颜色，在 TabTextColorView 不起作用
     * @param selectedColor
     */
    public BaseAction setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
        return this;
    }
    /**
     * 设置默认颜色，在 TabTextColorView 不起作用
     * @param unSelectedColor
     */
    public BaseAction setUnSelectedColor(int unSelectedColor) {
        this.mUnSelectedColor = unSelectedColor;
        return this;
    }

    /**
     * 点击事件
     *
     * @param lastIndex
     * @param curIndex
     */

    private boolean isClickMore = false;

    public void onItemClick(int lastIndex, int curIndex) {
        isTabClick = true;
        mCurrentIndex = curIndex;
        mLastIndex = lastIndex;
        if (mViewPager == null) {
            autoScaleView();
            doAnim(lastIndex, curIndex,mTabBean.tabClickAnimTime);
        } else {
            if (Math.abs(mCurrentIndex - mLastIndex) > 1) {
                clearColorText();
                isClickMore = true;
                autoScaleView();
                doAnim(lastIndex, curIndex,mTabBean.tabClickAnimTime);
            }
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
                    View view = mParentView.getChildAt(i);
                    TabColorTextView textview = view.findViewById(mTextViewId);
                    if (textview != null) {
                        textview.setTextColor(textview.getDefaultColor());
                    }
                }
                View view = mParentView.getChildAt(mCurrentIndex);
                TabColorTextView colorTextView = view.findViewById(mTextViewId);
                if (colorTextView != null) {
                    colorTextView.setTextColor(colorTextView.getChangeColor());
                }
            }
        }
    }

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
            float offset = curView.getMeasuredWidth() * positionOffset;
            int scrollX = (int) (curView.getLeft() + offset);
            if (offset > 0 && positionOffset > 0) {
                if (!isClickMore) {
                    if (position < mParentView.getChildCount() - 1) {
                        //要偏移的view
                        final View transView = mParentView.getChildAt(position + 1);
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
                        //左边偏移量
                        float left = curView.getLeft() + positionOffset * (transView.getLeft() - curView.getLeft());
                        //右边表示宽度变化
                        float right = curView.getRight() + positionOffset * (transView.getRight() - curView.getRight());

                        if (mTabBean.tabWidth != -1) {
                            //拿到左边初始坐标
                            int width = curView.getMeasuredWidth();
                            //todo 动态改变rect大小的，后面再看看
                            left = curView.getLeft() + (width - mTabBean.tabWidth) / 2;
                            //再拿到偏移坐标
                            left = left + positionOffset * (curView.getMeasuredWidth() + transView.getMeasuredWidth()) / 2;
                            right = left + mTabBean.tabWidth;
                        }
                        mTabRect.left = left;
                        mTabRect.right = right;
                        valueChange(new TabValue(mTabRect.left, mTabRect.right));

                        mParentView.postInvalidate();

                        //处理颜色渐变
                        if (mTextViewId != -1 && isColorText) {
                            View leftView = curView.findViewById(mTextViewId);
                            View rightView = transView.findViewById(mTextViewId);
                            TabColorTextView colorLeft = (TabColorTextView) leftView;
                            TabColorTextView colorRight = (TabColorTextView) rightView;
                            colorLeft.setprogress(1 - positionOffset, TabColorTextView.DEC_RIGHT);
                            colorRight.setprogress(positionOffset, TabColorTextView.DEC_LEFT);
                        }
                    }
                }
                //超过中间了，让父控件也跟着移动
                if (mParentView.isCanMove()) {
                    if (scrollX > mViewWidth / 2 - mParentView.getPaddingLeft()) {
                        scrollX -= mViewWidth / 2 - mParentView.getPaddingLeft();
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
    }


    @Override
    public void onPageScrollStateChanged(int state) {

        /**
         * 这里的处理是因为外部调用了 setCurrentItem ，此时 item 之间的差值也大于1;
         * 但因为没有回调 onItemClick，所以 draw 、动画等效果没法实现，因此，可以先通过
         * Viewpager 拿到 setCurrentItem(position) 中的position，赋值给当前的 mCurrentIndex；
         * 且两者之间大于1时，直接使用draw和动画效果；不再让 onPageScrolled 去执行动画，避免卡顿
         */
        if (state == ViewPager.SCROLL_STATE_SETTLING){
            if (!isTabClick && mViewPager != null){
                mLastIndex = mCurrentIndex;
                mCurrentIndex = mViewPager.getCurrentItem();
                if (Math.abs(mCurrentIndex - mLastIndex) > 1){
                    isClickMore = true;
                    clearColorText();
                    doAnim(mLastIndex, mCurrentIndex,mTabBean.tabClickAnimTime);
                    autoScaleView();
                }
            }
        }

        if (state == ViewPager.SCROLL_STATE_IDLE){
            isClickMore = false;
            isTabClick = false;
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
    public void doAnim(int lastIndex, final int curIndex,int animTime) {
        if (mCurrentIndex == mLastIndex){
            return;
        }
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
                if (isVertical()){
                    if (mTabBean.tabHeight != -1){
                        lastValue.top = mTabRect.top;
                        lastValue.bottom = mTabRect.bottom;
                        int height = curView.getMeasuredHeight();
                        //竖直方向不去理会
                        curValue.top = (height - mTabBean.tabHeight) / 2 + curView.getTop();
                        curValue.bottom = mTabBean.tabHeight + curValue.top;
                    }
                }else {
                    if (mTabBean.tabWidth != -1) {
                        lastValue.left = mTabRect.left;
                        lastValue.right = mTabRect.right;
                        int width = curView.getMeasuredWidth();
                        if (mTabBean.tabType == FlowConstants.RECT) {
                            curValue.left = (1 - mOffset) * width / 2 + curView.getLeft();
                            curValue.right = width * mOffset + curValue.left;
                        } else {
                            curValue.left = (width - mTabBean.tabWidth) / 2 + curView.getLeft();
                            curValue.right = mTabBean.tabWidth + curValue.left;
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
                                    if (i == mCurrentIndex){
                                        adapter.onItemSelectState(child, true);
                                    }else{
                                        adapter.onItemSelectState(child, false);
                                    }
                                }
                            }
                        }
                    }
                });
                mAnimator.start();
            }else{
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
        if (mTextViewId != -1 && mParentView != null) {
            if (isTextView && !isColorText) {
                int childCount = mParentView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = mParentView.getChildAt(i);
                    TextView textView = view.findViewById(mTextViewId);
                    if (i == position) {
                        textView.setTextColor(mSelectedColor);
                    } else {
                        textView.setTextColor(mUnSelectedColor);
                    }
                }
            }
        }
    }


    /**
     * 选中默认的颜色
     * @param lastIndex
     * @param curIndex
     */
    public void chooseIndex(int lastIndex,int curIndex){
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
                doAnim(mLastIndex,mCurrentIndex,0);
                mOffset = mTabBean.tabWidth * 1.0f / child.getMeasuredWidth();
                if (mTextViewId != -1) {
                    View textView = child.findViewById(mTextViewId);
                    if (textView instanceof TabColorTextView) {
                        isColorText = true;
                        TabColorTextView colorTextView = (TabColorTextView) textView;
                        colorTextView.setTextColor(colorTextView.getChangeColor());
                    }
                    if (textView instanceof TextView){
                        isTextView = true;
                    }
                }
                if (mTabBean.autoScale && mTabBean.scaleFactor > 1) {
                    child.setScaleX(mTabBean.scaleFactor);
                    child.setScaleY(mTabBean.scaleFactor);
                }
            }
        }

    }


    /**
     * 清掉属性动画
     */
    private void clearScale(){
        if (mParentView != null) {
            if (mTabBean.autoScale && mTabBean.scaleFactor >1) {
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
     * @param bean
     */
    public void configAttrs(TabBean bean) {
        mTabBean = bean;
        if (bean.tabColor != -2) {
            mPaint.setColor(bean.tabColor);
        }
    }

    /**
     * 绘制不同的view
     */
    public abstract void draw(Canvas canvas);


    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public int getLastIndex() {
        return mLastIndex;
    }



    public ViewPager getViewPager() {
        return mViewPager;
    }


    /**
     * tab 的方向
     * @return
     */
    public boolean isVertical(){
        return mTabBean.tabOrientation == FlowConstants.VERTICAL;
    }

    public boolean isLeftAction(){
        return mTabBean.actionOrientation != -1 && mTabBean.actionOrientation == FlowConstants.LEFT;
    }
    public boolean isRightAction(){
        return mTabBean.actionOrientation != -1 && mTabBean.actionOrientation == FlowConstants.RIGHT;
    }

    public  void setContext(Context context){
        mContext = context;
    }
}
