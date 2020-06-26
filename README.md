# QuickIndex
# 快速索引 #

### 1.对快速索引效果进行模块划分，分为左右2部分，每部分的功能如下 ###

①右边字母View,需要绘制26个字母，并且能够获取当前所触摸的字母；
 
②左边listview，需要对条目数据按照首字母进行分割，并且根据当前所触摸的字母将对应的条目放置到顶端；
 
### 2.实现左边字母View，定义QuickIndexBar继承自View类，并且对画笔进行初始化 ###

    private void init(){
	    paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
	    paint.setColor(COLOR_DEFAULT);
	    paint.setTextSize(16);
	    paint.setTextAlign(Align.CENTER);//设置文字绘制的起点是底边中心
	}

### 3.在onDraw方法中等分绘制出26个字母，所以需要遍历进行绘制，并计算每个字母的x，y坐标 ###


①绘制文字的x起点：宽度的一半 

②绘制文字的y起点：格子高度的一半 + 文字高度的一半 + position*格子高度

    
	float x = width/2;
	//遍历数组，将26个字母全部绘制上来
	for (int i = 0; i < indexArr.length; i++) {
	    String text = indexArr[i];
	    //y:格子高度的一半 + 文字高度的一半 + position*格子高度
	    float y = cellHeight/2 + getTextHeight(text, paint)/2 + i*cellHeight;
	    canvas.drawText(text, x, y, paint);
	}

③其中获取文字高度的方法如下：

    private int getTextHeight(String text,Paint paint){
	    Rect bounds = new Rect();
	    paint.getTextBounds(text,0,text.length(), bounds);//方法执行完，bounds就有值了
	    return bounds.height();
	}

### 4.绘制完26个字母之后，需要求出当前触摸点对应的是哪个字母，思路是：直接将触摸点的y坐标除以格子的高度，得到的值就是字母对应的索引，所以在onTouchEvent方法中编写代码如下 ###

    case MotionEvent.ACTION_DOWN:
	case MotionEvent.ACTION_MOVE:
	    touchY = event.getY();
	    if(index!=(int) (touchY/cellHeight)){
	    index = (int) (touchY/cellHeight);
	    Log.e("tag", indexArr[index]);
	    break;
	case MotionEvent.ACTION_UP:
	    //抬起的时候清空index
	    index = -1;
	    break;
	}


### 5.计算出当前触摸的字母之后，需要将触摸字母改变的事件暴露给外界，所以定义监听器，并在字母改变的时候进行回调，代码如下 ###


    private OnTouchLetterChangeListner listner;
	public void setOnTouchLetterChangeListener(OnTouchLetterChangeListner listner){
	    this.listner = listner;
	}
	/**
	 * 定义监听器
	 */
	public interface OnTouchLetterChangeListner{
	    void onLetterChange(String letter);
	}


并且在触摸字母发生改变的时候去回调接口的方法：


    case MotionEvent.ACTION_MOVE:
	    touchY = event.getY();
	    if(index!=(int) (touchY/cellHeight)){
	        index = (int) (touchY/cellHeight);
	        //对index进行安全性的检测
	        if(index>=0 && index<indexArr.length){
	            if(listner!=null){
	                listner.onLetterChange(indexArr[index]);
	            }
	        }
	    }
	break;


### 6.为了增加用户体验，需要将按下的字母改变颜色，思路是：在触摸的时候得到了当前触摸字母的索引，然后一直引起重绘，这样在onDraw方法中进行遍历绘制的时候去判断，当前触摸字母的索引是否等于i，如果等于则改变画笔的颜色，代码如下 ###

    //引起重绘
	invalidate();


并且在onDraw方法的for循环中去判断和改变画笔颜色：

    //判断如果当前正在绘制的字母和index相同，那么就改变颜色
	paint.setColor(i==index?COLOR_PRESSED:COLOR_DEFAULT);

### 7.QuickIndexBarBar搞定之后，在界面中添加左边的ListView，并填充数据 ### 

    //对listview填充数据
	fillData();
	listView.setAdapter(new FriendAdapter(this,friends));


其中填充数据的方法如下,我们封装了Friend类作为数据实体：

    private void fillData() {
	    // 虚拟数据
	    friends.add(new Friend("李伟"));
	    friends.add(new Friend("张三"));
	    friends.add(new Friend("阿三"));
	    friends.add(new Friend("阿四"));
	    friends.add(new Friend("段誉"));
	    friends.add(new Friend("段正淳"));
	    friends.add(new Friend("张三丰"));
	    friends.add(new Friend("陈坤"));
	    friends.add(new Friend("林俊杰1"));
	    friends.add(new Friend("陈坤2"));
	    friends.add(new Friend("王二a"));
	    friends.add(new Friend("林俊杰a"));
	    friends.add(new Friend("张四"));
	    friends.add(new Friend("林俊杰"));
	    friends.add(new Friend("王二"));
	    friends.add(new Friend("王二b"));
	    friends.add(new Friend("赵四"));
	    friends.add(new Friend("杨坤"));
	    friends.add(new Friend("赵子龙"));
	    friends.add(new Friend("杨坤1"));
	    friends.add(new Friend("李伟1"));
	    friends.add(new Friend("宋江"));
	    friends.add(new Friend("宋江1"));
	    friends.add(new Friend("李伟3"));
	}

### 8.在编写adapter的布局文件时，需要注意的是布局文件要包含首字母TextView和名字TextView，一会儿去判断如果当前首字母和上一个条目的首字母相同，则隐藏当前的，反之则设置当前的首字母，布局代码省略  ###
 
### 9.由于需要获取首字母，那么则利用Pinyin4j类库去获取汉字的拼音，由于只能对单个汉字获取，并且当前的字符串中可能包含空格以及非汉字字符，所以在获取拼音过程中进行了细致的判断，代码如下 ###

    /**
	 * 获取汉字的拼音,本质是读取xml，所以该方法不应该被频繁调用，会销毁一定资源
	 * @param chinese
	 * @return
	 */
	public static String getPinYin(String chinese){
	    if(TextUtils.isEmpty(chinese)) return null;
	
	    //创建转化拼音的格式化,用来控制转化后的拼音字母是否是大小写，是否带有音标
	    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	    format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//大写字母
	    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不需要音调
	
	    //由于只支持对单个汉字进行获取，不支持词语,所以要将chinese转成字符数组
	    char[] chineseArr = chinese.toCharArray();
	    StringBuilder sb = new StringBuilder();
	    //遍历所有的字符进行逐一获取
	    for (int i = 0; i < chineseArr.length; i++) {
	        char c = chineseArr[i];
	        //对字符进行过滤
	        //1.过滤空格,如果是空格，则不处理，进行下一次遍历
	        //南宫燚滨     -> nangongyibin
	        if(Character.isWhitespace(c))continue;
	        //2.对非中文字符进行简单过滤
	        //a南宫b*·！<>，。……、燚滨
	        //由于中文占2个字节，一个字节是-128-127；
	        if(c > 127){
	            //说明有可能汉字
	            try {
	                //由于多音字的存在，所以返回的是数组, 单: dan  shan
	                String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(c, format);
	                if(pinyinArr!=null){
	                    //此处只能取第0个，首先因为大部分汉字只有一个拼音：
	                    //如果遇到多音字，也暂时取第一个，因为没有办法判断到底取哪个
	                    sb.append(pinyinArr[0]);
	                }
	            } catch (BadHanyuPinyinOutputFormatCombination e) {
	                e.printStackTrace();
	                //转化失败说明不是正确的汉字，则不用处理
	            }
	        }else {
	            //说明肯定不是汉字，一般都是字母，比如：a,b,x,
	            //针对这些，我们选择直接拼接
	            sb.append(c);
	        }
	    }
	    return sb.toString();
	}

### 10.完成获取汉字的拼音后，对汉字的首字母进行显示，并在adapter的getView方法中去判断当前首字母和上一条的首字母是否相同，如果相同，则隐藏当前的首字母TextView，如果不相同，则显示当前的首字母 ###

    FriendHolder holder = FriendHolder.getHolder(convertView);
	//如果当前的首字母和上一个首字母相同，那么我当前的字母View应该隐藏
	String currentLetter = friends.get(position).getPinyin().charAt(0)+"";
	if(position>0){
	    //获取上一个首字母
	    String lastLetter = friends.get(position-1).getPinyin().charAt(0+"";
	    if(currentLetter.equals(lastLetter)){
	        //隐藏当前的字母View
	        holder.tv_letter.setVisibility(View.GONE);
	    }else {
	        //说明不一样，那么应该显示当前的首字母
	        //由于条目是复用的，所以必须再次设置为可见
	        holder.tv_letter.setVisibility(View.VISIBLE);
	        holder.tv_letter.setText(currentLetter);
	    }
	}else {
	    //说明当前是第0个
	    //由于条目是复用的，所以必须再次设置为可见
	    holder.tv_letter.setVisibility(View.VISIBLE);
	    holder.tv_letter.setText(currentLetter);
	}
	//显示名字
	holder.tv_name.setText(friends.get(position).getName());

需要注意的是，由于adapter的view是复用的，所以当需要显示首字母的时候需要将TextView重新设置为可见； 

### 11.显示完首字母后，数据是乱序的，那么需要对数据进行排序 ###

    //对数据进行排序
	Collections.sort(friends);

### 12.然后给QuickIndexBar设置字母改变的监听器，在回调方法中可以获取当前触摸的字母，那么我们需要遍历当前集合，找出首字母和触摸字母相同的条目，然后将其设置到listview的顶部，代码如下 ###

    //去集合中查找首字母和触摸字母相同的条目
	for (int i = 0; i < friends.size(); i++) {
	    String firstLetter = friends.get(i).getPinyin().charAt(0)+"";
	    if(firstLetter.equals(letter)){
	        //说明当前的i就是要找的条目的索引
	        //将其放置到listview的顶部
	        listView.setSelection(i);
	        //找到就立即停止
	        break;
	    }
	}

需要注意的是，首字母和触摸字母的条目会有多个，我们只需要将第一个放置到顶端，所以当找到后就立即break；
 
### 13.根据当前触摸的字母，在界面中间显示出当前触摸字母，代码如下 ###

    quickIndexView.setOnTouchLetterChangeListener(new OnTouchLetterChangeListner() {
	    @Override
	    public void onLetterChange(String letter) {
	        //去集合中查找首字母和触摸字母相同的条目
	        ...
	
	        //显示出当前的字母
	        showCurrentWord(letter);
	    }
	});

showCurrentWord的方法实现如下：

    protected void showCurrentWord(String letter) {
    tv_word.setText(letter);
	//  tv_word.setVisibility(View.VISIBLE);
	    ViewPropertyAnimator.animate(tv_word).scaleX(1f).scaleY(1f)
	                        .setDuration(350).start();
	
	    //移除所有的任务
	    handler.removeCallbacksAndMessages(null);
	    handler.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	//          tv_word.setVisibility(View.GONE);
	
	            //抬起的时候缩小
	            ViewPropertyAnimator.animate(tv_word).scaleX(0f).scaleY(0f)
	            .setDuration(350).start();
	        }
	    }, 2000);
	}

# 头部视差效果 #

### 1.分析思路：先给listview头部添加ImageView，然后根据手指往下拖动的距离，去让ImageView的高度不断增高，当手指抬起的时候让ImageView恢复至最初高度 ###
 
### 2.首先，自定义ParallaxListView，继承ListView ###

### 3.然后，给ListView填充数据，并addHeaderView，添加ImageView作为头布局 ###

    //1.添加头部IMageView
	View view = View.inflate(this, R.layout.layout_header, null);
	parallaxImage = (ImageView) view.findViewById(R.id.parallaxImage);
	listView.addHeaderView(view);
	//2.填充数据
	listView.setAdapter(new MyAdapter());

### 4.给ParallaxListView添加setParallaxImageView方法，让外界传入ImageView的引用，因为后面需要用到 ###

	private ImageView parallaxImageView;
	public void setParallaxImageView(final ImageView parallaxImageView) {
	    this.parallaxImageView = parallaxImageView;
	}

并且在Activity中传入ImageView对象：

	//设置ImageView
	listView.setParallaxImageView(parallaxImage);

### 5.根据观察，我们需要在listview滑动到头的时候去计算手指移动的距离，但是不采用去监听onTouchEvent的move事件了，我们去重写overScrollBy方法，该方法就是在listview滑动到头的时候才调用，并且可以获取到继续滑动的距离；所以我们进行判断如下 ###

	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
	        int scrollY, int scrollRangeX, int scrollRangeY,
	        int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) { isTouchEvent:"+isTouchEvent);
	    //表示手指拖动，并且是顶部到头了，
	    if(deltaY<0 && isTouchEvent){
	        ...
	    }
	    return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
	            scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}

### 6.在overScrollBy方法中判断，如果是顶部滑动到头，并且是手指继续拖动，则不断增加ImageView的高度，并且对高度作最大值的限制，代码如下 ###

	//根据继续滑动的距离，就是deltaY，让ImageView的高度变高
	int newHeight = parallaxImageView.getHeight()-deltaY/3;
	//对高度进行限制
	if(newHeight>maxHeight)newHeight = maxHeight;
	
	//将ImageView的高变成newHeight
	android.view.ViewGroup.LayoutParams params = parallaxImageView.getLayoutParams();
	params.height = newHeight;
	//重新设置高度
	parallaxImageView.setLayoutParams(params);

### 7.当手指抬起的时候让ImageView恢复到最初高度，首先求出最初高度，需要注意的是不能够直接get，因为可能还没有测量完，我们选择添加一个全局的布局监听器，在setParallaxImageView方法中去获取 ###

	parallaxImageView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	            //用完立即移除
	            parallaxImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	            originalHeight = parallaxImageView.getHeight();
	        }
	});

### 8.然后在onTouchEvent中去判断TOUCH_UP的事件，最终通过ValueAnimator实现自定义的动画，此处自定义动画的逻辑就是让ImageView的高度进行变化到指定高度 ###

	if(ev.getAction()==MotionEvent.ACTION_UP){
	    //让ImageView缓慢恢复到最初的高度
	    ValueAnimator animator = ValueAnimator.ofInt(parallaxImageView.getHeight()
	                ,originalHeight);
	    animator.addUpdateListener(new AnimatorUpdateListener() {
	        @Override
	        public void onAnimationUpdate(ValueAnimator animator) {
	            //获取动画的值，
	            int animatedValue = (Integer) animator.getAnimatedValue();
	            //将动画的值设置给parallaxImageView的高度
	            android.view.ViewGroup.LayoutParams params = parallaxImageView.getLayoutParams();
	            params.height = animatedValue;
	            //重新设置高度
	            parallaxImageView.setLayoutParams(params);
	        }
	    });
	    //给动画添加速度插值器
	    animator.setInterpolator(new OvershootInterpolator(4));//超过一点再回来
	    animator.setDuration(350);
	    animator.start();
	}




## 步骤1.将JitPack存储库添加到构建文件中 ##


将其添加到存储库末尾的根build.gradle中：


    	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}

## 步骤2.添加依赖项 ##


    dependencies {
	        implementation 'com.github.nangongyibin:Android_QuickIndex:1.0.0'
	}


![](https://github.com/nangongyibin/Android_QuickIndex/blob/master/rr.gif)