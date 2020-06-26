package com.ngyb.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/26 08:23
 */
public class QuickIndexView extends View {
    private static final String TAG = "QuickIndexView";
    private String[] letterArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint paint;
    private float cellHeight;//格子的高度
    int touchIndex = -1; //记录每次触摸的索引
    private onLetterChangeListener listener;

    public QuickIndexView(Context context) {
        this(context, null);
    }

    public QuickIndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        int size = getResources().getDimensionPixelSize(R.dimen.sp_16);
        paint.setColor(Color.WHITE);
        paint.setTextSize(size);
        //paint绘制文字默认的起点是文字的左下角
        paint.setTextAlign(Paint.Align.CENTER);//标识设置文字绘制的起点为文字底边中心
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cellHeight == 0f) {
            cellHeight = getMeasuredHeight() * 1.0f / letterArr.length;
        }
        for (int i = 0; i < letterArr.length; i++) {
            float x = getMeasuredWidth() / 2;
            String text = letterArr[i];
            //y==格子的高度的一半+文字的高度的一半+索引*格子的高度
            float y = cellHeight / 2 + getTextHeight(text) / 2 + i * cellHeight;
            if (touchIndex == i) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(Color.WHITE);
            }
            canvas.drawText(text, x, y, paint);
        }
    }

    /**
     * 获取文字的高度
     *
     * @param text
     * @return
     */
    private float getTextHeight(String text) {
        Rect rect = new Rect();
        //在这个方法执行完毕，矩形就有值了
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //触摸点的y坐标除以格子的高度等到的就是字母的索引
                int index = (int) (event.getY() / cellHeight);
                if (index != touchIndex) {
                    //进行代码健壮性的判断
                    if (index >= 0 && index < letterArr.length) {
                        if (listener != null) {
                            listener.onLetterChange(letterArr[index]);
                        }
                    }
                }
                touchIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                //抬起的时候应该重置一下
                touchIndex = -1;
                break;
        }
        //重绘，会引起onDraw调用
        invalidate();
        return true;
    }

    public interface onLetterChangeListener {
        void onLetterChange(String letter);
    }

    public void setOnLetterChangeListener(onLetterChangeListener listener) {
        this.listener = listener;
    }
}
