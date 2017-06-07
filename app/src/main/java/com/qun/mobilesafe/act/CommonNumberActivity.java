package com.qun.mobilesafe.act;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
    private int previousExpandedPosition = -1;//记录前一个被展开的组的索引

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

        //给组设置点击事件
        mElv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                //当点击时，当前展开的索引不是-1并且，我们点击的组的索引与记录下来的前一个展开的索引不同时，将前一个展开的组给关闭
                if (previousExpandedPosition != -1 && previousExpandedPosition != groupPosition) {
                    mElv.collapseGroup(previousExpandedPosition);
                }

                //根据当前组的状态，将组的打开与关闭进行逻辑操作
                if (mElv.isGroupExpanded(groupPosition)) {//当前是打开状态
                    mElv.collapseGroup(groupPosition);//关闭对应的组
                } else {//当前是关闭状态
                    mElv.expandGroup(groupPosition);//展开对应的组
                    previousExpandedPosition = groupPosition;
                }
                mElv.setSelection(groupPosition);//能够将点击的组的条目直接显示到第一个可见条目上(可以实现回到顶部的效果)

//                return false;//组的点击事件不由我们自己处理，由ExpandableListView源码处理
                return true;//组的点击事件由我们自己处理
            }
        });

        //子条目的点击事件
        mElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //获取对应条目的号码，并拨打出去
                String[] childText = CommonNumberDb.getChildText(CommonNumberActivity.this, groupPosition, childPosition);
                String number = childText[1];
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);//进入拨号界面，但是不进行拨打
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                return true;
            }
        });
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
