package com.qun.mobilesafe.act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.db.CommonNumberDb;

public class CommonNumberActivity extends AppCompatActivity {

    private ExpandableListView mElv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number);

        initView();
    }

    private void initView() {
        mElv = (ExpandableListView) findViewById(R.id.elv);
        MyAdapter adapter = new MyAdapter();
        mElv.setAdapter(adapter);
    }

    private class MyAdapter extends BaseExpandableListAdapter {

        //设置组的数量
        @Override
        public int getGroupCount() {
            return CommonNumberDb.getGroupCount(CommonNumberActivity.this);
        }

        //设置对应组的对应的子条目的数量
        @Override
        public int getChildrenCount(int groupPosition) {
            return CommonNumberDb.getChildrenCount(CommonNumberActivity.this, groupPosition);
        }

        //类似于getItem()，获取对应组的条目对象(以下5个方法 ，如果外部需要获取数据则实现，否则不需要实现)
        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        //类似于getItem()，获取对应组的对应子条目对象
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        //类似于getItemId()，获取对应组的id
        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        //获取对应组的对应子条目的id
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        //当前适配器是否有稳定id,(一个id对应一个条目对象)
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //创建对应组的条目界面

        /**
         * 参数一：groupPosition 组的索引
         * 参数二：isExpanded 当前组是否是展开的
         * 参数三：convertView 复用类
         * 参数四：parent ExpandableListView
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView tv = new TextView(CommonNumberActivity.this);
//            tv.setText("group  ：" + groupPosition);
            String groupText = CommonNumberDb.getGroupText(CommonNumberActivity.this, groupPosition);
            tv.setText(groupText);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.parseColor("#9C9A9C"));
            return tv;
        }

        //创建对应组的对应子条目的界面

        /**
         * 参数一：groupPosition 组的索引
         * 参数二：childPosition 对应组的子条目的索引
         * 参数三：isLastChild 是否是最后一个条目
         * 参数四：convertView 复用类
         * 参数五：parent ExpandableListView
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView tv = new TextView(CommonNumberActivity.this);
//            tv.setText("group  ：" + groupPosition + "childPosition:" + childPosition);
            String[] childText = CommonNumberDb.getChildText(CommonNumberActivity.this, groupPosition, childPosition);
            tv.setText(childText[0] + "\n" + childText[1]);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.parseColor("#D6D7D6"));
            return tv;
        }

        //设置子条目是否能够被点击
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
