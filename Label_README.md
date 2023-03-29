## LabelFlowLayout 效果图

<table  align="center">
 <tr>
    <th>LabelFlowLayout</th>
    <th>LabelFlowLayout 显示更多</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/label.gif" align="left" height="789" width="479"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/label_showmore.gif" align="left" height="789" width="479"></a></td>
  </tr>

</table>

## 3.3 LabelFlowLayout 
LabelFlowLayout 竖向布局，支持自动换行，单选、多选、长按等功能.

**它的状态变化，根据 view 的 selected 来，所以大家可以写 selector 当背景，或者在方法中自己设置** 


**LabelFlowLayout 支持以下效果:**
- **单选**
- **多选**
- **长按**
- **显示更多**
- **收起**

### 3.3.1 使用

LabelFlowLayout 默认单选，在 xml 这样配置：

```
<com.zhengsr.tablib.view.flow.LabelFlowLayout
    android:id="@+id/singleflow"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
/>
```
FlowLayout 使用也使用 adapter 去配置数据：

```
LabelFlowLayout flowLayout = findViewById(R.id.singleflow);
final LabelFlowAdapter adapter;
flowLayout.setAdapter(adapter = new LabelFlowAdapter<String>(R.layout.item_textview,mTitle){
    /**
     * 绑定数据，可以使用 setText(..) 等快捷方式，也可以视同 view.findViewById()
     * 同时，当你的子控件需要点击事件时，可以通过  addChildrenClick() 注册事件，
     * 然后重写 onItemChildClick(..) 即可拿到事件，否则就自己写。
     * 自己的点击和长按不需要注册
     */
    @Override
    public void bindView(View view, String data, int position) {
        setText(view,R.id.item_text,data);
        // 注册子控件的点击事件
        //addChildrenClick(view,R.id.item_text,position);
    }
    @Override
    public void onItemSelectState(View view, boolean isSelected) {
        super.onItemSelectState(view, isSelected);
        TextView textView = view.findViewById(R.id.item_text);
        if (isSelected) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.GRAY);
        }
    }
});
```

**如果需要数据更新，使用adapger.notifyDataChanged()即可**

## 3.3.2 多选
其实只需要配置 flowLayout.setMaxSelectCount(3); 就可以了，然后adapter 中重写：
```
@Override
public void onReachMaxCount(List<Integer> ids, int count) {
    super.onReachMaxCount(ids, count);
    Toast.makeText(LabelActivity.this, "最多只能选中 "+count+" 个"+" 已选中坐标: "+ids, Toast.LENGTH_SHORT).show();
}
```
**如果您选默认选中其中一些item，可以使用 flowLayout.setSelects(2,3,5);**

## 3.3.3 长按
其实就是长按view，至于状态的变化，由自己去写：

```
    private void canLongFlow(){
        LabelFlowLayout flowLayout = findViewById(R.id.longflow);
        flowLayout.setAdapter(new LabelFlowAdapter<String>(R.layout.item_search_layout,mTitle2) {
            @Override
            public void bindView(View view, String data, int position) {
                setText(view,R.id.search_msg_tv,data)
                        .addChildrenClick(view,R.id.search_delete_iv,position);
            }

            @Override
            public void onItemSelectState(View view, boolean isSelected) {
                super.onItemSelectState(view, isSelected);
                if (!isSelected){
                    view.setBackgroundResource(R.drawable.shape_search);
                    setVisible(view,R.id.search_delete_iv,false);
                }
            }

            @Override
            public void onItemClick(View view, String data, int position) {
                super.onItemClick(view, data, position);
                Toast.makeText(LabelActivity.this, "点击了: "+data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(View childView, int position) {
                super.onItemChildClick(childView, position);
                if (childView.getId() == R.id.search_delete_iv){
                    mTitle2.remove(position);
                    notifyDataChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view,int position) {
                /**
                 * 置所有view 的 select 为 false
                 */
                resetStatus();
                view.setBackgroundResource(R.drawable.shape_search_select);
                setVisible(view,R.id.search_delete_iv,true);
                return super.onItemLongClick(view,position);
            }


        });
    }

```

### 3.3.5 显示更多和收起

LabelFlowLayout 还支持显示更多和收起的功能，如图：

<img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/label_showmore.gif" align="left" height="789" width="479">

配置的 xml 如下：
```
    <com.zhengsr.tablib.view.flow.LabelFlowLayout
        android:id="@+id/labelflow"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="10dp"
        android:layout_marginStart="10dp"
        app:label_showLine="3"
        app:label_handUp_layoutId="@layout/handup"
        app:label_showMore_layoutId="@layout/show_more"
        app:label_showMore_Color="@color/white"/>
```
- label_showLine 表示最多显示的行数
- label_showMore_layoutId 表示显示更多的layoutId，这样方便客制化
- label_showMore_Color 表示主背景色，用来设置 shader 虚化
- label_handUp_layoutId 收起的 LayoutId，方便自己客制化

上面的 label_showMore_Color 可能不太理解，其实就是把 **显示更多的 LayoutId** 转成bitmap，显示在下面；虚化怎么办呢？

其实就是给 paint 设置一个 shader，上面为透明色，下面给背景色一样，就能达到虚化的效果。如：

```
 /**
 * 同时加上一个 shader，让它有模糊效果
 */
Shader shader = new LinearGradient(0, 0, 0,
        getHeight(), Color.TRANSPARENT, mShowMoreColor, Shader.TileMode.CLAMP);
mPaint.setShader(shader);
```

### 3.3.6 配置自定义属性
当然，也支持动态配置自定义属性。如下:

```
LabelBean bean = new LabelBean();
bean.showLines = 2;
bean.showMoreLayoutId = R.layout.show_more;
bean.showMoreColor = Color.WHITE;
flowLayout.setLabelBean(bean);
```

### 3.3.7 主动换行

你可能想有个 Header 换行，然后再显示不同的数据，这个时候，需要你的数据类继承 BaseLabelItem，把 isHeader 设置为ture，
即可实现自动换行。

### 参考代码：

[布局代码](https://github.com/LillteZheng/FlowHelper/blob/master/app/src/main/res/layout/activity_label.xml)

[Activity代码](https://github.com/LillteZheng/FlowHelper/blob/master/app/src/main/java/com/zhengsr/tabhelper/activity/LabelActivity.java)

### 自定义属性列表
**LabelFlowLayout**

| 名称 | 类型 |说明 |
|---|---|---|
|label_maxcount|integer|最大选择个数|
|label_iaAutoScroll|boolean|是否支持自动滚动|
|label_showLine|integer|最多显示的行数|
|label_showMore_layoutId|integer|显示更多的layoutId|
|label_showMore_Color|color|显示更多的背景色，为了虚化作用|
|label_handUp_layoutId|integer|收起的layoutId|

