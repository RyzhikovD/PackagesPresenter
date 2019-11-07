package ru.sberbankmobile.uiabstraction.presentation.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SortingSpinnerAdapter extends BaseAdapter {
    private final List<String> mGroupTypes;

    public SortingSpinnerAdapter(@NonNull List<String> groupTypes) {
        mGroupTypes = groupTypes;
    }

    @Override
    public int getCount() {
        return mGroupTypes.size();
    }

    @Override
    public String getItem(int position) {
        return mGroupTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            SortingSpinnerAdapter.ViewHolder viewHolder = new SortingSpinnerAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        SortingSpinnerAdapter.ViewHolder holder = (SortingSpinnerAdapter.ViewHolder) convertView.getTag();
        holder.mGroupType.setText(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private final TextView mGroupType;

        private ViewHolder(View view) {
            mGroupType = view.findViewById(android.R.id.text1);
        }
    }
}