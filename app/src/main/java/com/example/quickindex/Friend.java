package com.example.quickindex;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/26 08:59
 */
public class Friend implements Comparable<Friend> {
    String name;
    public String sPy;

    public Friend(String name) {
        this.name = name;
        try {
            this.sPy = PinyinHelper.getShortPinyin(name);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Friend o) {
        //对汉字排序就是对汉字的拼音字母排序
        //拼音字母排序的规则就是ASCII表的顺序
        return this.sPy.compareTo(o.sPy);
    }
}
