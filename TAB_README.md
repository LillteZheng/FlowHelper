## TabFlowLayout 效果图

<table  align="center">
 <tr>
    <th>没有结合ViewPager</th>
    <th>结合ViewPager</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/tab_click.gif" align="left"height="789" width="479"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/tab_viewpager.gif" align="left" height="789" width="479" ></a></td>
  </tr>

</table>


### 1、正常使用

首先是在 xml 中，填写 TabFlowLayout 控件，它支持横向排列，一个不带效果的 TabFlowLayout 如下：

```
<com.zhengsr.tablib.view.flow.TabFlowLayout
    android:id="@+id/resflow"
    android:layout_width="wrap_content"
    android:layout_marginTop="5dp"
    android:background="#6D8FB0"
    android:layout_height="wrap_content"/>
```
比如要加矩形，三角形，可以使用 app:tab_type 这个属性，比如一个矩形：
```
<com.zhengsr.tablib.view.flow.TabFlowLayout
    android:id="@+id/rectflow"
    android:layout_width="wrap_content"
    android:layout_marginTop="5dp"
    app:tab_type="rect"
    app:tab_color="@color/colorPrimary"
    app:tab_height="3dp"
    app:tab_width="20dp"
    app:tab_margin_b="3dp"
    android:background="@color/black_ff_bg"
    android:layout_height="wrap_content"/>
```
其中tab_width 可以不填，这样的话，它的大小，就是根据子控件本身的宽度的；当然 rect 是会随着控件的大小而变化的。
其他自定义属性，可以参考下面的 自定义属性列表。

那么，在 xml 写好了，接着，就是在 Activity 中，这样写：
```
private void rectFlow(){
    TabFlowLayout flowLayout = findViewById(R.id.rectflow);
    flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
        @Override
        public void onItemSelectState(View view, boolean isSelected) {
            super.onItemSelectState(view, isSelected);
            if (isSelected){
                setTextColor(view,R.id.item_text,Color.WHITE);
            }else{
                setTextColor(view,R.id.item_text,getResources().getColor(R.color.unselect));
            }
        }

        @Override
        public void bindView(View view, String data, int position) {
            setText(view,R.id.item_text,data)
                    .setTextColor(view,R.id.item_text,getResources().getColor(R.color.unselect));
            if (position == 0){
                setVisible(view,R.id.item_msg,true);
            }


        }
    });

}
```
可以看到，只需要设置 adapter 就行了，需要注意的是你要传入子控件的 layout，这样方便你自定义你的布局，比如添加一个红点点。

如果你需要使用颜色渐变的效果，TextView 可以使用 ColorTextView 就可以了。


### 2、结合Viewpager
结合 ViewPager 非常简单，如下：
```
flowLayout.setViewPager(...) 即可.
```
它有几个方法,参考最后一个的解释就可以了。
```
  public void setViewPager(ViewPager viewPager) {
      setViewPager(viewPager, -1, 0, 0, 0);
  }


  public void setViewPager(ViewPager viewPager, int textId) {
      setViewPager(viewPager, textId, 0, -1, -1);
  }
  public void setViewPager(ViewPager viewPager, int textId, int unselectedColor, int selectedColor) {
      setViewPager(viewPager, textId, 0, unselectedColor, selectedColor);
  }

  /**
   * 配置viewpager
   * @param viewPager
   * @param textId  view 中 textview 的id
   * @param selectedIndex 默认选中的item，初始值为0，也可以从第二页或者其他 位置
   * @param unselectedColor 没有选中的颜色 ColorTextView 中失效
   * @param selectedColor 选中的颜色 ColorTextView 中失效
   */
  public void setViewPager(ViewPager viewPager, int textId, int selectedIndex, int unselectedColor, int selectedColor) {

  }
```

### 3、自定义属性动态配置

可能你不想在 xml 直接定死，那么可以直接使用 TabBean 去配置，一个使用 shape 当移动背景的配置如下：
```
private void resFlow(){
    final TabFlowLayout flowLayout = findViewById(R.id.resflow);

    flowLayout.setViewPager(mViewPager);
    /**
     * 配置自定义属性
     */

    TabBean bean = new TabBean();
    bean.tabType = FlowConstants.RES;
    bean.tabItemRes = R.drawable.shape_round;
    bean.tabClickAnimTime = 300;
    bean.tabMarginLeft = 5;
    bean.tabMarginTop = 12;
    bean.tabMarginRight = 5;
    bean.tabMarginBottom = 10;
    flowLayout.setTabBean(bean);

    flowLayout.setAdapter(new TabAdapter<String>(R.layout.item_msg,mTitle) {
        @Override
        public void bindView(View view, String data, int position) {
            setText(view,R.id.item_text,data);
        }

        @Override
        public void onItemClick(View view, String data, int position) {
            super.onItemClick(view, data, position);
            mViewPager.setCurrentItem(position);
        }
    });
}
```

### 4、自定义action
可能上面的一些效果没有你想要的，那么项目也支持用户自定义，比如自定义一个白色圆点效果，只需要继承 BASEAction：

```

    /**
     * 绘制一个圆的指示器
     */
    class CircleAction extends BaseAction{
        private static final String TAG = "CircleAction";
        @Override
        public void config(TabFlowLayout parentView) {
            super.config(parentView);
            View child = parentView.getChildAt(0);
            if (child != null) {
                float l = parentView.getPaddingLeft() + child.getMeasuredWidth()/2;
                float t = parentView.getPaddingTop() +  child.getMeasuredHeight() - mTabHeight/2 -mMarginBottom;
                float r = mTabWidth + l;
                float b = child.getMeasuredHeight() - mMarginBottom;
                mRect.set(l,t,r,b);
            }
        }


        @Override
        protected void valueChange(TabValue value) {
            super.valueChange(value);
            
            /**
             * value 子控件在滚动时的 left 和 right，可以理解为偏移量
             * Rect 为这个偏移量的局域。
             */
            //由于自定义的，都是从left 开始算起的，所以这里还需要加上圆的半径
            mRect.left = value.left + mTabWidth/2;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(mRect.left,mRect.top,mTabWidth/2,mPaint);
        }
    }
```
然后通过 flowLayout.setAction 即可。

### 5、参考代码

[有ViewPager的布局代码](https://github.com/LillteZheng/FlowHelper/blob/master/app/src/main/res/layout/activity_tab.xml)

[有ViewPager的Activity](https://github.com/LillteZheng/FlowHelper/blob/master/app/src/main/java/com/zhengsr/tabhelper/activity/TabActivity.java)

### 6、自定义属性列表
**TabFlowLayout**

| 名称 | 类型 |说明 |
|---|---|---|
|tab_type|rect,tri,round,color,res|tab的类型，目前支持矩形，三角形、圆角、颜色渐变、资源res|
|tab_color|color|指示器的颜色，当类型为 rect、tri、roud是可以通过它定义|
|tab_width|dimension|指示器的宽度，如果不写，则根据控件自身大小|
|tab_height|dimension|指示器高度|
|tab_item_res|reference|指示器的背景，比如shape，bitmap等，只对 res 起作用|
|tab_round_size|dimension|圆角的大小，只对round起作用|
|tab_margin_l|dimension|左偏移|
|tab_margin_t|dimension|上偏移|
|tab_margin_r|dimension|右偏移|
|tab_margin_b|dimension|下偏移|
|tab_click_animTime|integer|点击动画的时间，默认300ms|

**ColorTextView**

| 名称 | 类型 |说明 |
|---|---|---|
|colortext_default_color|color|默认颜色|
|colortext_change_color|color|需要渐变颜色|



