
###  CollapseTextView 使用

首先给大家看下效果，很简单。
![text.gif](https://i.loli.net/2018/02/01/5a7290936a8fb.gif)

使用的话，主要是通过设置maxlines，然后通过调用collapse，expand，toggle等方法进行展开收起的操作
### CollapseTextView实现原理
实现的原理也比较简单，就是通过onMeasure的时候，进行高度的控制，需要注意的是，高度控制有两种情形：
- 一个是textView的height进行控制
- 一个是通过maxLines进行控制。

> height控制，主要是为了进行展开和收起动画时使用。maxlines控制则是为了实际的效果。

### 以前的一个bug
首先惭愧一下，以前在最终设置高度是这样的
 ```
错误的:
setMeasuredDimension(getMeasuredWidthAndState(), View.MeasureSpec.makeMeasureSpec(mRecorderMaxLineHeight + drawableHeight, View.MeasureSpec.getMode(getMeasuredHeightAndState())));

应该正确的
setMeasuredDimension(getMeasuredWidthAndState(), resolveSize(mRecorderMaxLineHeight + drawableHeight,heightMeasureSpec));

```

错误的原因在于，我把MeasureSpec生成的高度和状态当作设置本身的高度和状态使用了。其实，应该是MeasureSpec生成的应该是viewGroup给其子view进行测量使用的。而本身的高度和状态应该是直接进行设置高度或者通过resolveSize方法来设置。

### 自身的高度和状态

目前设置自身的高度和状态，我只知道有这一种MEASURED_STATE_TOO_SMALL，含义是你给我的宽度太小了。那么这个有什么作用呢？经过查看源码ViewRootImpl，发现其实这个状态主要应该是针对弹出框的窗口来使用的。

弹出框的窗口如果包涵一个textView,里面有个段很长的文字，那么这个弹出框的窗口宽度应该设置多少才合适呢?因为系统也不知道需要多少，所以它采用了两次询问的策略，首先他用默认的一个宽度进行宽度的起始的宽度测量，如果，最终结果包涵了MEASURED_STATE_TOO_SMALL,那么系统会用起始宽度加上屏幕宽度除以2的宽度再次进行测量，如果还有MEASURED_STATE_TOO_SMALL,那么系统就会用最终宽度进行测量，以后就不管了。因为他没有地方给控件。

### 总结

所以，看似一个很简单的小控件，引申了一些我们经常忽略的问题。所以在平常的创作中，千万不要忽略这些细节的东西，他往往是google的心血呀







