package com.zhengsr.tablib.view.flow.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zhengsr.tablib.FlowConstants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.TabBean;
import com.zhengsr.tablib.bean.TabConfig;
import com.zhengsr.tablib.bean.TextConfig;
import com.zhengsr.tablib.callback.FlowListener;
import com.zhengsr.tablib.callback.FlowListenerAdapter;
import com.zhengsr.tablib.utils.AttrsUtils;
import com.zhengsr.tablib.utils.DisplayUtil;
import com.zhengsr.tablib.view.TabColorTextView;
import com.zhengsr.tablib.view.action.BaseAction;
import com.zhengsr.tablib.view.action.ColorAction;
import com.zhengsr.tablib.view.action.RectAction;
import com.zhengsr.tablib.view.action.ResAction;
import com.zhengsr.tablib.view.action.RoundAction;
import com.zhengsr.tablib.view.action.TriAction;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

/**
 * @author by zhengshaorui 2021/5/23 06:40
 * describe：用来获取通用的自定义属性，和一些常用的配置
 */
public class AbsFlowLayout extends ScrollFlowLayout {
    private static final String TAG = AbsFlowLayout.class.getSimpleName();
    /**
     * attrs
     */
    protected TabBean mTabBean;

    /**
     * logic
     */
    protected BaseAction mAction;
    private TabConfig mTabConfig;
    protected TabFlowAdapter mAdapter;
    protected Scroller mScroller;
    protected int mLastScrollPos = 0;
    protected int mLastIndex = 0;
    protected int mCurrentIndex = 0;

    public AbsFlowLayout(Context context) {
        this(context, null);
    }

    public AbsFlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsFlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        mScroller = new Scroller(getContext());
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AbsFlowLayout);
        mTabBean = AttrsUtils.getTabBean(ta);
        ta.recycle();
        setVisibleCount(mTabBean.visualCount);
        setTabOrientation(mTabBean.tabOrientation);
        chooseTabTpye(mTabBean.tabType);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

    }

    /**
     * 如果超过了屏幕大小，且父布局是 LinearLayout ，gravity 或 自身的 layout_gravity 不是 bottom；
     * 则需要自身去重新设置，不然初始位置是在中间开始去layout的。
     * 如果是 ConstraintLayout ，width 又是 wrap_content 的，只需要改成0即可
     */
    private void reAdjustLayoutParams() {
        if (!isVertical()) {
            if (getWidth() > mWidth) {
                ViewGroup parent = (ViewGroup) getParent();
                if (parent instanceof LinearLayout) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
                    params.gravity = Gravity.START;
                    setLayoutParams(params);
                } else if (parent instanceof ConstraintLayout) {
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) getLayoutParams();
                    boolean isWrapContent = params.width == ConstraintLayout.LayoutParams.WRAP_CONTENT;
                    if (isWrapContent && isCanMove()) {
                        params.width = 0;
                        setLayoutParams(params);
                    }
                }
            }
        }
    }

    /**
     * 选中不同的 action
     * @param tabStyle
     */
    private void chooseTabTpye(int tabStyle) {
        if (tabStyle != -1) {
            switch (tabStyle) {
                case FlowConstants.RECT:
                    mAction = new RectAction();
                    break;
                case FlowConstants.TRI:
                    mAction = new TriAction();
                    break;
                case FlowConstants.ROUND:
                    mAction = new RoundAction();
                    break;
                case FlowConstants.RES:
                    mAction = new ResAction();
                    break;
                case FlowConstants.COLOR:
                    mAction = new ColorAction();
                    break;
                default:
                    break;
            }
        }
        //配置自定义属性给 action
        if (mAction != null) {
            mAction.setContext(this.getContext());
            mAction.configAttrs(mTabBean);
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //绘制tab标签
        if (mAction != null) {
            mAction.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 添加adapter，
     *  如果有其他配置项，可以使用 {@link #setAdapter(TabConfig, TabFlowAdapter)}
     * @param adapter
     */
    public void setAdapter(TabFlowAdapter adapter) {
        setAdapter(null, adapter);
    }

    public void setAdapter(TabConfig tabConfig, TabFlowAdapter adapter) {
        mAdapter = adapter;
        if (tabConfig != null) {
            setTabConfig(tabConfig);
        }
        adapter.setListener(new FlowListener());
        //实现数据更新
        notifyChanged(adapter);
    }


    /**
     * 更新数据
     */
    private void notifyChanged(final TabFlowAdapter adapter) {
        if (adapter == null) {
            return;
        }
        removeAllViews();
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View view ;
            if (adapter.getLayoutId() != -1) {
                view = LayoutInflater.from(getContext()).inflate(adapter.getLayoutId(), this, false);
                if (mTabConfig != null) {
                    if (mTabConfig.getTextId() == -1) {
                        throw new RuntimeException("you need to use TabConfig setTextId() to config TextView");
                    }
                }else{
                    throw new RuntimeException("you need to use TabConfig setTextId() to config TextView");
                }
            }else{
                if (mTabConfig != null) {
                    if (mTabConfig.getTextId() != -1) {
                        throw new RuntimeException("you need to use setAdapter(layoutId,*) to set layoutId ");
                    }
                }
                view = getTextview(i,mTabBean.textType);;
            }
            addView(view);
            if (mVisibleCount != -1) {
                MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                if (mViewWidth != 0) {
                    params.width = (int) (mViewWidth * 1.0f / mVisibleCount);
                    view.setLayoutParams(params);
                }
            }
            Object data = adapter.getDatas().get(i);
            if (view instanceof TextView && data instanceof String){
                ((TextView) view).setText((String) data);
            }
            adapter.bindView(view, data, i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(view, finalI);
                   // adapter.onItemClick(view, adapter.getDatas().get(finalI), finalI);
                }
            });

        }
        //如果是一开始没有数据
        if (getChildCount() > 0) {
            getChildAt(0).post(new Runnable() {
                @Override
                public void run() {
                    reAdjustLayoutParams();
                    if (mAction != null) {
                        mAction.config(AbsFlowLayout.this);
                        onViewVisible();
                    }
                }
            });
        }
    }

    /**
     * 更新滚动
     */
    protected void updateScroll(View view, boolean smoothScroll) {
        if (isCanMove() && view != null) {
            //超过中间了，让父控件也跟着移动
            int scrollPos;
            if (isVertical()) {
                scrollPos = view.getTop();
            } else {
                scrollPos = view.getLeft();
            }
            int offset;
            if (scrollPos != mLastScrollPos) {
                if (isVertical()) {
                    if (scrollPos > mHeight / 2) {
                        scrollPos -= mHeight / 2;
                        //下边界
                        if (scrollPos < mBottomRound - mHeight) {
                            offset = scrollPos - mLastScrollPos;
                            if (smoothScroll) {
                                mScroller.startScroll(0, getScrollY(), 0, offset);
                            } else {
                                scrollTo(0, offset);
                            }
                            mLastScrollPos = scrollPos;
                        } else {
                            offset = mBottomRound - mHeight - getScrollY();
                            if (getScrollY() >= mBottomRound - mHeight) {
                                offset = 0;
                            }
                            if (smoothScroll) {
                                mScroller.startScroll(0, getScrollY(), 0, offset);
                            } else {
                                scrollTo(0, mBottomRound - mHeight);
                            }
                            mLastScrollPos = mBottomRound - mHeight - offset;
                        }
                    } else {
                        offset = -scrollPos;
                        if (smoothScroll) {
                            mScroller.startScroll(0, getScrollY(), 0, offset);
                        } else {
                            scrollTo(0, 0);
                        }
                        mLastScrollPos = 0;
                    }
                } else {
                    if (scrollPos > mWidth / 2) {
                        scrollPos -= mWidth / 2;
                        //有边界提醒
                        if (scrollPos < mRightBound - mWidth) {
                            offset = scrollPos - mLastScrollPos;
                            if (smoothScroll) {
                                mScroller.startScroll(getScrollX(), 0, offset, 0);
                            } else {
                                scrollTo(offset, 0);
                            }
                            mLastScrollPos = scrollPos;
                        } else {
                            offset = mRightBound - mWidth - getScrollX();
                            if (getScrollX() >= mRightBound - mWidth) {
                                offset = 0;
                            }
                            if (smoothScroll) {
                                mScroller.startScroll(getScrollX(), 0, offset, 0);
                            } else {
                                scrollTo(mRightBound - mWidth, 0);
                            }
                            mLastScrollPos = mRightBound - mWidth - offset;
                        }
                    } else {
                        offset = -scrollPos;
                        if (smoothScroll) {
                            mScroller.startScroll(getScrollX(), 0, offset, 0);
                        } else {
                            scrollTo(0, 0);
                        }
                        mLastScrollPos = 0;
                    }
                }
            }
        }
    }


    class FlowListener extends FlowListenerAdapter {
        @Override
        public void notifyDataChanged() {
            final TabFlowAdapter adapter = mAdapter;
            //一开始没有数据，直接刷新即可
            int childCount = getChildCount();
            int dataSize = adapter.getDatas().size();
            if (childCount != 0 && childCount == dataSize) {
                //数据变化
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    adapter.bindView(view, adapter.getDatas().get(i), i);
                }
            } else {
                notifyChanged(mAdapter);
            }
        }

        @Override
        public void resetAllStatus() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                view.setSelected(false);
                mAdapter.onItemSelectState(view, false);
            }
        }
    }


    /**
     * 设置配置项
     * @param config
     */
    public void setTabConfig(TabConfig config) {
        if (mTabConfig == null) {
            mTabConfig = config;
        }
        if (mTabConfig != null) {
            if (mTabConfig.getVisibleCount() != -1) {
                setVisibleCount(mTabConfig.getVisibleCount());
            }
            Log.d(TAG, "setTabConfig() called with: config = [" + mTabConfig.toString() + "]");
            if (mTabConfig.getSelectedColor() != FlowConstants.COLOR_ILLEGAL) {
                mTabBean.selectedColor = mTabConfig.getSelectedColor();
            }
            if (mTabConfig.getUnSelectColor() != FlowConstants.COLOR_ILLEGAL) {
                mTabBean.unSelectedColor = mTabConfig.getUnSelectColor();
            }
        }
        onTabConfig(mTabConfig);
        if (mAction != null) {
            mAction.setTabConfig(mTabConfig);
        }
    }
    /**
     * 自定义的action
     *
     * @param action
     */
    public void setCusAction(BaseAction action) {
        mAction = action;
        mAction.configAttrs(mTabBean);

        if (mAction != null) {
            mAction.setTabConfig(mTabConfig);

        }
    }
    /**
     * 自定义属性的配置,设置该属性会覆盖xml的属性
     */

    public AbsFlowLayout setTabBean(TabBean bean) {
        if (bean == null) {
            return this;
        }
        mTabBean = AttrsUtils.diffTabBean(mTabBean,bean);
        if (bean.tabType != -1) {
            chooseTabTpye(bean.tabType);
        }

        if (mAction != null) {
            if (mTabBean != null) {
                mAction.configAttrs(mTabBean);
                mAction.setTabConfig(mTabConfig);
            }
        }


        setTabOrientation(bean.tabOrientation);

        if (bean.visualCount != -1) {
            setVisibleCount(bean.visualCount);
        }
        return this;
    }

    public TabFlowAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected boolean isLabelFlow() {
        return false;
    }

    /**
     * 此时View可用
     */
    protected void onViewVisible() {
    }

    protected void onTabConfig(TabConfig config) {
    }

    protected void onItemClick(View view, int position) {
        if (mAdapter != null) {
            mAdapter.onItemClick(view, mAdapter.getDatas().get(position), position);
        }
    }



    public TextView getTextView(int pos){
        if (mTabConfig != null) {
            if (mTabConfig.getTextId() != -1) {
                return getChildAt(pos).findViewById(mTabConfig.getTextId());
            }
        }
        return (TextView) getChildAt(pos);
    }


    private TextView getTextview(int pos,int textType){
        boolean useColorText = false;
        TextView textView ;
        if (mTabConfig != null) {
            useColorText = mTabConfig.isUseColorText();
        }
        if (textType == FlowConstants.COLORTEXT || useColorText) {
            TabColorTextView colorTextView = new TabColorTextView(getContext());

            colorTextView.setCusTextColor(Color.GRAY,Color.RED);
            if (mTabBean != null) {
                if (mTabBean.selectedColor != FlowConstants.COLOR_ILLEGAL
                        && mTabBean.unSelectedColor != FlowConstants.COLOR_ILLEGAL) {
                    colorTextView.setCusTextColor(mTabBean.unSelectedColor,mTabBean.selectedColor);
                }
            }
            textView = colorTextView;


        }else {
            textView = new TextView(getContext());
            textView.setTextColor(Color.BLACK);
            if (mTabBean != null) {
                if (mTabBean.selectedColor != FlowConstants.COLOR_ILLEGAL) {
                    if (pos == 0) {
                        textView.setTextColor(mTabBean.selectedColor);
                    }else{
                        textView.setTextColor(mTabBean.unSelectedColor);
                    }
                }
            }

        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setGravity(Gravity.CENTER);
        int l = DisplayUtil.dip2px(getContext(), 10);
        int t = DisplayUtil.dip2px(getContext(), 6);
        textView.setPadding(l, t, l, t);
        textView.setGravity(Gravity.CENTER);

        if (mTabConfig != null) {
            TextConfig config = mTabConfig.getTextConfig();
            if (config != null) {
                if (config.getPadding() != null) {
                    Rect rect = config.getPadding();
                    textView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
                }
                if (config.getTextSize() != 0) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.getTextSize());
                }
                if (config.getTypefaces() != null) {
                    for (Typeface typeface : config.getTypefaces()) {
                        textView.setTypeface(typeface);
                    }
                }
            }

        }
        return textView;
    }

}
