## LabelFlowLayout 效果图

<table  align="center">
 <tr>
    <th>LabelFlowLayout</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/label.gif" align="left" height="789" width="479"></a></td>
  </tr>

</table>

LabelFlowLayout 竖向布局，支持自动换行，单选、多选、长按等功能.

**它的状态变化，根据 view 的 selected 来，所以大家可以写 selector 当背景，或者在方法中自己设置** 

## 使用

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
flowLayout.setAdapter(adapter = new LabelFlowAdapter<String>(R.layout.item_textview,mTitle) {
    @Override
    public void bindView(View view, String data, int position) {
        setText(view,R.id.item_text,data);
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

## 多选
其实只需要配置 flowLayout.setMaxCount(3); 就可以了，然后adapter 中重写：
```
@Override
public void onReachMacCount(List<Integer> ids, int count) {
    super.onReachMacCount(ids, count);
    Toast.makeText(LabelActivity.this, "最多只能选中 "+count+" 个"+" 已选中坐标: "+ids, Toast.LENGTH_SHORT).show();
}
```
**如果您选默认选中其中一些item，可以使用 flowLayout.setSelects(2,3,5);**

## 长按
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

### 参考代码：

[布局代码](https://github.com/LillteZheng/FlowHelper/blob/master/app/src/main/res/layout/activity_label.xml)

[Activity代码](https://github.com/LillteZheng/FlowHelper/blob/master/app/src/main/java/com/zhengsr/tabhelper/activity/LabelActivity.java)

### 自定义属性列表
**LabelFlowLayout**

| 名称 | 类型 |说明 |
|---|---|---|
|LabelFlowLayout|integer|最大选择个数|

