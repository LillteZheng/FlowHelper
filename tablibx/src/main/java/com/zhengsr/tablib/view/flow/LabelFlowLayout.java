package com.zhengsr.tablib.view.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.zhengsr.tablib.R;
import com.zhengsr.tablib.bean.LabelBean;
import com.zhengsr.tablib.callback.FlowListenerAdapter;
import com.zhengsr.tablib.view.adapter.LabelFlowAdapter;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther by zhengshaorui on 2020/1/8
 * describe: 标签瀑布流布局，支持单选，多选
 */
public class LabelFlowLayout extends ScrollFlowLayout {
    private static final String TAG = "LabelFlowLayout";
    /**
     * logic
     */
    private LabelFlowAdapter mAdapter;
    private int mMaxSelectCount;
    private int mLastPosition = -1;
    private boolean isHasMoreView;
    private boolean isHasHandUpView;

    /**
     * attrs
     */
    private boolean isAutoScroll;
    private int mShowMoreColor;
    private int mShowMoreLayoutId;
    private int mHandUpLayoutId;


    /**
     * canvas
     */
    private Paint mPaint;
    private View mMoreView;
    private Bitmap mBitmap;
    private RectF mBitRect;
    private int mShowMoreLines = -1;
    private View mHandUpView;


    public LabelFlowLayout(Context context) {
        this(context, null);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelFlowLayout);
        mMaxSelectCount = ta.getInteger(R.styleable.LabelFlowLayout_label_maxSelectCount, 1);
        isAutoScroll = ta.getBoolean(R.styleable.LabelFlowLayout_label_isAutoScroll, true);
        mShowMoreLines = ta.getInteger(R.styleable.LabelFlowLayout_label_showLine, -1);
        setLabelLines(mShowMoreLines);

        mShowMoreColor = ta.getColor(R.styleable.LabelFlowLayout_label_showMore_Color, Color.RED);
        mShowMoreLayoutId = ta.getResourceId(R.styleable.LabelFlowLayout_label_showMore_layoutId, -1);

        mHandUpLayoutId = ta.getResourceId(R.styleable.LabelFlowLayout_label_handUp_layoutId,-1);

        ta.recycle();

        if (mShowMoreLayoutId != -1) {
            mMoreView = LayoutInflater.from(getContext()).inflate(mShowMoreLayoutId, LabelFlowLayout.this, false);
            isHasMoreView = true;
        }

        if (mHandUpLayoutId != -1){
            mHandUpView = LayoutInflater.from(getContext()).inflate(mHandUpLayoutId,LabelFlowLayout.this,false);
        }

        setClickable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBitRect = new RectF();
        setWillNotDraw(false);
    }


    @Override
    public boolean isLabelAutoScroll() {
        return isAutoScroll;
    }

    /**
     * 设置 lable 的 adapter 数据
     * @param adapter
     */
    public void setAdapter(LabelFlowAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setListener(new LabelListener());
        notifyData();
    }


    /**
     * listener 监听 adapter 的数据
     */
    class LabelListener extends FlowListenerAdapter {
        @Override
        public void notifyDataChanged() {
            super.notifyDataChanged();
            LabelFlowAdapter adapter = mAdapter;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                adapter.bindView(view,adapter.getDatas().get(i),i);

            }
        }

        @Override
        public void resetAllStatus() {
            super.resetAllStatus();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                view.setSelected(false);
                mAdapter.onItemSelectState(view, false);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mMoreView == null || !isLabelMoreLine()) {
            return super.onInterceptTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = ev.getX();
                float y = ev.getY();
                /**
                 * 如果在范围内，截取该事件
                 */
                if (mBitRect.contains(x, y)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (mBitRect.contains(x, y)) {
                if (mAdapter != null) {
                    mBitmap = null;
                    if (isHasMoreView && isLabelMoreLine()){
                        //显示全部了
                        setLabelLines(-1);
                        mAdapter.onShowMoreClick(mMoreView);
                        if (mHandUpView != null){
                            isHasHandUpView = true;
                            isHasMoreView = false;

                        }
                    }else if (isHasHandUpView){
                        setLabelLines(mShowMoreLines);
                        mAdapter.onHandUpClick(mHandUpView);
                        if (mMoreView != null) {
                            isHasHandUpView = false;
                            isHasMoreView = true;
                        }

                    }

                    requestLayout();

                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isHasMoreView && mBitmap == null && mMoreView != null) {
            /**
             * 拿到 view 的 bitmap
             */
            mMoreView.layout(0, 0, getWidth(), mMoreView.getMeasuredHeight());
            mMoreView.buildDrawingCache();
            mBitmap = mMoreView.getDrawingCache();
            /**
             * 同时加上一个 shader，让它有模糊效果
             */
            Shader shader = new LinearGradient(0, 0, 0,
                    getHeight(), Color.TRANSPARENT, mShowMoreColor, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);
            mBitRect.set(l, getHeight() - mMoreView.getMeasuredHeight(), r, getHeight());
        }else if (isHasHandUpView && mBitmap == null){
            /**
             * 拿到 view 的 bitmap
             */
            mHandUpView.layout(0, 0, getWidth(), mHandUpView.getMeasuredHeight());
            mHandUpView.buildDrawingCache();
            mBitmap = mHandUpView.getDrawingCache();
            mBitRect.set(l, getHeight() - mMoreView.getMeasuredHeight(), r, getHeight());
        }



    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isLabelMoreLine() && isHasMoreView && mBitmap != null) {
            canvas.drawPaint(mPaint);
            canvas.drawBitmap(mBitmap, mBitRect.left, mBitRect.top, null);
        }else if (isHasHandUpView && mBitmap != null){
            canvas.drawBitmap(mBitmap, mBitRect.left, mBitRect.top, null);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * layoutId 需要父控件即 LabelFlowLayout 去帮助测量，才能通过
         * getMeasuredxxx 拿到正确的宽高、
         */
        if (isHasMoreView) {
            measureChild(mMoreView, widthMeasureSpec, heightMeasureSpec);
            //添加它的 1/2 来变模糊
            mViewHeight += mMoreView.getMeasuredHeight() / 2;
            setMeasuredDimension(mLineWidth, mViewHeight);
        }

        if (isHasHandUpView){
            measureChild(mHandUpView,widthMeasureSpec,heightMeasureSpec);
            mViewHeight += mHandUpView.getMeasuredHeight();
            setMeasuredDimension(mLineWidth,mViewHeight);
        }


    }

    /**
     * 更新数据
     */
    private void notifyData() {
        removeAllViews();
        int childCount = mAdapter.getItemCount();
        for (int i = 0; i < childCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(mAdapter.getLayoutId(), this, false);
            mAdapter.bindView(view, mAdapter.getDatas().get(i), i);
            addView(view);
            onItemViewConfig(mAdapter, view, i);
        }

    }

    private void onItemViewConfig(LabelFlowAdapter flowAdapter, View view, final int position) {
        final LabelFlowAdapter adapter = flowAdapter;
        //单选
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.onItemClick(v, adapter.getDatas().get(position), position);

                //是否为单选
                if (mMaxSelectCount == 1) {
                    if (mLastPosition != position) {
                        View selectedView = getSelectedView();
                        if (selectedView != null) {
                            selectedView.setSelected(false);
                            adapter.onItemSelectState(selectedView, false);
                        }
                        adapter.onFocusChanged(selectedView, v);
                        //进行反选
                        if (v.isSelected()) {
                            v.setSelected(false);
                            adapter.onItemSelectState(v, false);
                        } else {
                            v.setSelected(true);
                            adapter.onItemSelectState(v, true);
                        }
                    }
                } else {
                    //进行反选
                    if (v.isSelected()) {
                        v.setSelected(false);
                        adapter.onItemSelectState(v, false);
                    } else {
                        v.setSelected(true);
                        adapter.onItemSelectState(v, true);
                    }
                    if (getSelectedCount() > mMaxSelectCount) {
                        v.setSelected(false);
                        adapter.onItemSelectState(v, false);
                        adapter.onReachMaxCount(getSelecteds(), mMaxSelectCount);
                        return;
                    }
                }

                mLastPosition = position;
            }
        });

        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return adapter.onItemLongClick(view, position);
            }
        });


    }


    /**
     * 获取选中的个数
     *
     * @return
     */
    private int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 拿到当前选中的view
     * 适合单选的时候
     *
     * @return
     */
    public View getSelectedView() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                return view;
            }
        }
        return null;
    }

    /**
     * 拿到选中数据
     *
     * @return
     */
    public List<Integer> getSelecteds() {
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.isSelected()) {
                indexs.add(i);

            }
        }
        return indexs;
    }


    /**
     * 设置要选中的数据
     *
     * @param indexs
     */
    public void setSelects(Integer... indexs) {
        if (indexs != null && indexs.length > 0) {

            for (int i = 0; i < indexs.length; i++) {
                for (int j = 0; j < getChildCount(); j++) {
                    View view = getChildAt(j);
                    if (j == indexs[i]) {
                        view.setSelected(true);
                        mLastPosition = j;
                        if (mAdapter != null) {
                            mAdapter.onItemSelectState(view, true);
                        }
                        break;
                    } else {
                        if (mAdapter != null) {
                            mAdapter.onItemSelectState(view, false);
                        }
                    }
                }
            }

        }
    }

    /**
     * 设置最大个数
     *
     * @param nums
     */
    public void setMaxSelectCount(int nums) {
        if (mMaxSelectCount != nums) {
            mMaxSelectCount = nums;
        }
    }

    public void setAutoScroll(boolean autoScroll) {
        if (isAutoScroll != autoScroll) {
            isAutoScroll = autoScroll;
        }
    }

    /**
     * 设置自定义属性
     *
     * @param bean
     */
    public void setLabelBean(LabelBean bean) {
        if (isAutoScroll != bean.isAutoScroll) {
            isAutoScroll = bean.isAutoScroll;
        }
        if (mMaxSelectCount != bean.maxSelectCount) {
            mMaxSelectCount = bean.maxSelectCount;
        }

        if (mShowMoreLines != bean.showLines) {
            mShowMoreLines = bean.showLines;
            setLabelLines(mShowMoreLines);
        }

        if (bean.showMoreLayoutId != -1) {
            mShowMoreLayoutId = bean.showMoreLayoutId;
            mMoreView = LayoutInflater.from(getContext()).inflate(mShowMoreLayoutId, this, false);
            isHasMoreView = true;
        }
        if (bean.showMoreColor != -2) {
            mShowMoreColor = bean.showMoreColor;
        }

        if (bean.handUpLayoutId != -1){
            mHandUpLayoutId = bean.handUpLayoutId;
            mHandUpView = LayoutInflater.from(getContext()).inflate(mHandUpLayoutId, this, false);
        }

    }
}
