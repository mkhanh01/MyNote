package com.example.notemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemanagement.R;
import com.example.notemanagement.model.Detail;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{

    private List<Detail> detailList;
    private Context context;

    public DetailAdapter(Context context) {
        this.context = context;
    }

    public List<Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Detail> detailList) {
        this.detailList = detailList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.ViewHolder holder, int position) {
        Detail detail = detailList.get(position);

        if (detail == null)
            return;
//
//        Calendar calendar = Calendar.getInstance();
//        String currentDate = DateFormat.getDateInstance(DateFormat.AM_PM_FIELD).format(calendar.getTime());

        holder.tvName.setText("Name: " + detail.getName());
        holder.tvCreatedDate.setText("Created Date: " + detail.getCreatedDate());
    }

    @Override
    public int getItemCount() {
        if (detailList != null) {
            return detailList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvCreatedDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.item_detail_tv_name);
            tvCreatedDate = itemView.findViewById(R.id.item_detail_tv_createdDate);
        }
    }


}
