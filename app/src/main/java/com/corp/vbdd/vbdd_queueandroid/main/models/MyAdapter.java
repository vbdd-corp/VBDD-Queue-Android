package com.corp.vbdd.vbdd_queueandroid.main.models;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corp.vbdd.vbdd_queueandroid.R;

import java.util.ArrayList;

class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Queue> queues = new ArrayList<>();
    public MyAdapter(ArrayList<Queue> qz){
        queues =qz;
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

        private  TextView qInfos;

        private Queue currentQueue;

        public MyViewHolder(final View itemView) {
            super(itemView);

            qInfos = ((TextView) itemView.findViewById(R.id.queueInfos));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentQueue.getQueueId())
                            .setMessage(currentQueue.toString())
                            .show();
                }
            });
        }

        public void display(Queue queue) {
            currentQueue = queue;
            qInfos.setText(queue.toString());
        }
    }

}
