package com.example.quickindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ngyb.quickindex.QuickIndexView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private QuickIndexView ql;
    ArrayList<Friend> friends = new ArrayList<>();
    private ListView lv;
    private TextView tv;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ql = findViewById(R.id.ql);
        lv = findViewById(R.id.lv);
        tv = findViewById(R.id.tv);
        ql.setOnLetterChangeListener(new QuickIndexView.onLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                if (letter != null) {
                    showCenterLetter(letter);
                }
                //根据当前触摸的字母，去集合中寻找首字母和letter一样的数据，然后将这个数据置顶
                for (int i = 0; i < friends.size(); i++) {
                    String word = friends.get(i).sPy.toUpperCase().substring(0, 1);
                    if (word.equals(letter)) {
                        //说明找到了触摸字母和首字母一样的数据
                        lv.setSelection(i);
                        break;
                    }
                }
            }
        });
        prepareData();
        //对数据进行排序
        Collections.sort(friends);
        //给listview填充数据
        lv.setAdapter(new MyAdapter(this, R.layout.item, friends));
    }

    private void showCenterLetter(String letter) {
        tv.setText(letter);
        tv.setVisibility(View.VISIBLE);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setVisibility(View.GONE);
            }
        }, 2000);
    }

    /**
     * 准备数据
     */
    private void prepareData() {
        friends.add(new Friend("北京市"));
        friends.add(new Friend("上海市"));
        friends.add(new Friend("天津市"));
        friends.add(new Friend("重庆市"));
        friends.add(new Friend("河北省"));
        friends.add(new Friend("山西省"));
        friends.add(new Friend("内蒙古自治区"));
        friends.add(new Friend("黑龙江省"));
        friends.add(new Friend("吉林省"));
        friends.add(new Friend("辽宁省"));
        friends.add(new Friend("陕西省"));
        friends.add(new Friend("甘肃省"));
        friends.add(new Friend("青海省"));
        friends.add(new Friend("新疆维吾尔自治区"));
        friends.add(new Friend("宁夏回族自治区"));
        friends.add(new Friend("山东省"));
        friends.add(new Friend("河南省"));
        friends.add(new Friend("江苏省"));
        friends.add(new Friend("浙江省"));
        friends.add(new Friend("安徽省"));
        friends.add(new Friend("江西省"));
        friends.add(new Friend("福建省"));
        friends.add(new Friend("台湾省"));
        friends.add(new Friend("湖北省"));
        friends.add(new Friend("湖南省"));
        friends.add(new Friend("广东省"));
        friends.add(new Friend("广西壮族自治区"));
        friends.add(new Friend("海南省"));
        friends.add(new Friend("四川省"));
        friends.add(new Friend("云南省"));
        friends.add(new Friend("贵州省"));
        friends.add(new Friend("西藏自治区"));
        friends.add(new Friend("香港特别行政区"));
        friends.add(new Friend("澳门特别行政区"));
    }
}
