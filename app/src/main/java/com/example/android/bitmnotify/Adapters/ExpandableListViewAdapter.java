package com.example.android.bitmnotify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.android.bitmnotify.R;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter{

    String[] groupItems = {"Computer Science", "Electronics & Communication", "Civil", "Electrical", "Mechanical"};
    String[] childItems = {"1st Sem", "2nd Sem", "3rd Sem", "4th Sem", "5th Sem", "6th Sem"};

    Context context;

    public ExpandableListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupItems.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItems.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupItems[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItems[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_view_explst, parent, false);
        }

        TextView tvGroupItem = (TextView) convertView.findViewById(R.id.textView_group_explst);
        tvGroupItem.setText(groupItems[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_view_explst, parent, false);
        }

        TextView tvChildItem = (TextView) convertView.findViewById(R.id.textView_child_explst);
        tvChildItem.setText(childItems[childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
