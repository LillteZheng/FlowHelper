# FlowHelper
FlowHelper 可以帮助您迅速构建Tab，比如热搜、搜索记录、与ViewPager搭配的工具类;

**注意注意注意！！！**
**提问题的时候，请遵循以下标准**

- **现象: 操作步骤，应用场景**
- **对应代码: 贴图或者贴代码**
- **机型或版本: 可选**

**后面对描述不清的问题，不予理会，精力有限，感谢理解**

**具体原理可参考着四篇文章：**

**如果你也想自己写一个，可以参考以下几篇文章**

[实现一个可定制化的TabFlowLayout(一) -- 测量与布局](https://blog.csdn.net/u011418943/article/details/103804677)

[实现一个可定制化的TabFlowLayout(二) -- 实现滚动和平滑过渡](https://blog.csdn.net/u011418943/article/details/103807920)

[实现一个可定制化的TabFlowLayout(三) -- 动态数据添加与常用接口封装](https://blog.csdn.net/u011418943/article/details/103817967)

[实现一个可定制化的TabFlowLayout(四) -- 与ViewPager 结合，实现炫酷效果](https://blog.csdn.net/u011418943/article/details/103851359)

[实现一个可定制化的TabFlowLayout -- 原理篇](https://juejin.im/post/5e365f52f265da3e3d511d65)

[实现一个可定制化的TabFlowLayout -- 说明文档](https://juejin.im/post/5e2f9dc7e51d4558836e3f99)


[FlowLayout 和 Recyclerview 实现双联表联动](https://blog.csdn.net/u011418943/article/details/104083568)

如果您也想快速实现 **Banner 轮播图**，可以使用这个库 https://github.com/LillteZheng/ViewPagerHelper

**工程实际使用 - 玩Android 客户端 : https://github.com/LillteZheng/WanAndroid**


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
implementation 'com.github.LillteZheng:FlowHelper:v1.23'
```

**如果要支持 AndroidX ，如果你的工程已经有以下代码，直接关联即可:**

```
android.useAndroidX=true
#自动支持 AndroidX 第三方库
android.enableJetifier=true
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

<table  align="center">
 <tr>
    <th>竖直效果</th>
  </tr>
   <tr>
    <td><a href="url"><img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/tab_vertical.gif" align="left"height="789" width="479"></a></td>
  </tr>

</table>

**目前TabFlowLayout 支持以下效果:**
- **矩形**
- **三角形**
- **圆角**
- **shape 或者 bitmap 等资源**
- **自定义功能**
- **放大Item效果，与上述效果可共用**
- **颜色渐变效果，需要使用 TabColorTextView 控件，与上述效果可共用，只支持有viewpager 的情况**
- **竖直效果，需要设置 tab_orientation = vertical**
- **宽度均分**

[TabFlowLayout 使用说明](https://github.com/LillteZheng/FlowHelper/blob/master/TAB_README.md)

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


**LabelFlowLayout 支持以下效果:**
- **单选**
- **多选**
- **长按**
- **显示更多**
- **收起**


[LabelFlowLayout 使用说明](https://github.com/LillteZheng/FlowHelper/blob/master/Label_README.md)


**其实TabFlowLayout也可以支持底部导航栏，但其实没必要，可以参考这个库 https://github.com/LillteZheng/CusBottomHelper**

<table>
 <tr>
    <th>底部凸起</th>
    <th>结合ViewPager</th>
  </tr>
   <tr>
    <td><img src="https://github.com/LillteZheng/CusBottomHelper/raw/master/gif/cus_fragment.gif" align="left"height="789" width="479"></td>
    <td><img src="https://github.com/LillteZheng/CusBottomHelper/raw/master/gif/cus_viewpager.gif" align="left" height="789" width="479" ></td>
  </tr>
</table>


## 如果该项目对您有帮助，赞赏一下吧 ^_^

<img src="https://github.com/LillteZheng/FlowHelper/raw/master/gif/wechat.png" height="320" width="240">

## 参考

本工程参考以下优秀项目：

[鸿洋的 flowLayout](https://github.com/hongyangAndroid/FlowLayout)

[Flyco 大佬的TabLayout](https://github.com/H07000223/FlycoTabLayout)




