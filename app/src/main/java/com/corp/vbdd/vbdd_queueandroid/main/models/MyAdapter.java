package com.corp.vbdd.vbdd_queueandroid.main.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corp.vbdd.vbdd_queueandroid.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Queue> queues;

    public MyAdapter(List<Queue> qz) {
        queues = qz;

    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Queue queue = queues.get(position);
        holder.display(queue);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView qID;
        private TextView qVisitors;
        private TextView qCurrentVisitor;

        public MyViewHolder(final View itemView) {
            super(itemView);
            qID = itemView.findViewById(R.id.qID);
            qVisitors = itemView.findViewById(R.id.qVisitors);
            qCurrentVisitor = itemView.findViewById(R.id.qCurrentVisitor);
        }

        public void display(Queue queue) {
            qID.setText(String.valueOf(queue.getQueueId()));
            qVisitors.setText(queue.toString());
            qCurrentVisitor.setText(String.valueOf(queue.getCurrentVisitor()));
        }
    }
}
