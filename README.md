# FlowHelper
FlowHelper 可以帮助您迅速构建Tab，比如热搜、搜索记录、与ViewPager搭配的工具类;

**具体原理可参考着四篇文章：**

[实现一个可定制化的TabFlowLayout(一) -- 测量与布局](https://blog.csdn.net/u011418943/article/details/103804677)

[实现一个可定制化的TabFlowLayout(二) -- 实现滚动和平滑过渡](https://blog.csdn.net/u011418943/article/details/103807920)

[实现一个可定制化的TabFlowLayout(三) -- 动态数据添加与常用接口封装](https://blog.csdn.net/u011418943/article/details/103817967)

[实现一个可定制化的TabFlowLayout(四) -- 与ViewPager 结合，实现炫酷效果](https://blog.csdn.net/u011418943/article/details/103851359)

[![](https://jitpack.io/v/LillteZheng/FlowHelper.svg)](https://jitpack.io/#LillteZheng/FlowHelper)
![](https://img.shields.io/github/stars/LillteZheng/FlowHelper.svg) 
![](https://img.shields.io/github/forks/LillteZheng/FlowHelper.svg)
[![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E9%83%91%E5%B0%91%E9%94%90-green.svg)](https://blog.csdn.net/u011418943)

## 使用
```
allprojects {
    repositories {
       ...
        maven { url 'https://jitpack.io' }
        
    }
}
```

```
implementation 'com.github.LillteZheng:FlowHelper:v1.14'
//如果要使用 androidx，用下面这个
implementation 'com.github.LillteZheng:FlowHelperX:v1.14'
```

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

[TabFlowLayout 使用说明](https://github.com/LillteZheng/FlowHelper/blob/master/TAB_README.md)

## LabelFlowLayout 效果图

<table  align="center">
 <tr>
    <th>LabelFlowLayout</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/label.gif" align="left" height="789" width="479"></a></td>
  </tr>

</table>

[LabelFlowLayout 使用说明](https://github.com/LillteZheng/FlowHelper/blob/master/Label_README.md)


**其实TabFlowLayout也可以支持底部导航栏，但其实没必要，可以参考这个库 https://github.com/LillteZheng/CusBottomHelper **

 <tr>
    <th>底部凸起</th>
    <th>结合ViewPager</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/CusBottomHelper/raw/master/gif/cus_fragment.gif" align="left"height="789" width="479"></a></td>
    <td><a href="url"><img src="https://github.com/LillteZheng/CusBottomHelper/raw/master/gif/cus_viewpager.gif" align="left" height="789" width="479" ></a></td>
  </tr>

</table>



