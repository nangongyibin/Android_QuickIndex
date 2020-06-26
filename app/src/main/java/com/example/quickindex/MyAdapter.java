package com.example.quickindex;

import android.content.Context;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/26 09:12
 */
public class MyAdapter extends CommonAdapter<Friend> {
    public MyAdapter(Context context, int layoutId, List<Friend> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Friend item, int position) {
        viewHolder.setText(R.id.tv1, item.name);
        String letter = item.sPy.toUpperCase().substring(0, 1);
        if (position > 0) {
            //获取上一个条目的首字母
            String lastLetter = mDatas.get(position - 1).sPy.toUpperCase().substring(0, 1);
            if (letter.equals(lastLetter)) {
                //说明当前的首字母和上一个一样，应该将当前的隐藏
                viewHolder.setVisible(R.id.tv, false);
            } else {
                //说明和上一个不一样，应该显示
                viewHolder.setVisible(R.id.tv, true);
            }
        } else {
            //说明是第0个，什么都不用做
            viewHolder.setVisible(R.id.tv, true);
        }
        viewHolder.setText(R.id.tv, letter);
    }
}
