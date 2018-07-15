package com.noiprocs.gnik.randomreminder.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noiprocs.gnik.randomreminder.R;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {

    private List<String> mDataSet;
    private OnViewButtonClick mOnButtonClick;

    public DataAdapter(List<String> dataSet){
        this.mDataSet = dataSet;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_data_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final String item = mDataSet.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mLabel.setText(item);
        viewHolder.mImageButton.setOnClickListener((v) -> {
            mOnButtonClick.onClick(item.split(" - "));
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mLabel;
        ImageButton mImageButton;

        ViewHolder(View v){
            super(v);

            this.mLabel = v.findViewById(R.id.display_data_view_item_label);
            this.mImageButton = v.findViewById(R.id.display_data_view_item_delete_button);
        }
    }

    public void setOnButtonClick(OnViewButtonClick onButtonClick) {
        this.mOnButtonClick = onButtonClick;
    }

    public interface OnViewButtonClick {
        void onClick(final String[] data);
    }
}
