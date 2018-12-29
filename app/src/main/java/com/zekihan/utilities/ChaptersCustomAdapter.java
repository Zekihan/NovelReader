package com.zekihan.utilities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zekihan.novelreader.R;

import java.util.List;

public class ChaptersCustomAdapter extends RecyclerView.Adapter<ChaptersCustomAdapter.MyViewHolder> {

    private final List<String> chapterList;

    public ChaptersCustomAdapter(List<String> chapterList) {
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapters_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String chNum = chapterList.get(i);
        myViewHolder.chNum.setText(chNum);
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView chNum;

        MyViewHolder(@NonNull View view) {
            super(view);
            chNum = view.findViewById(R.id.chNum);

        }
    }
}
