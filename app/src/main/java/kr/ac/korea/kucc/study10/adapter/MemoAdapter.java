package kr.ac.korea.kucc.study10.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kr.ac.korea.kucc.study10.R;
import kr.ac.korea.kucc.study10.activity.MemoInputActivity;
import kr.ac.korea.kucc.study10.activity.MemoListActivity;
import kr.ac.korea.kucc.study10.data.Memo;

/**
 * Created by ffaass on 2017-08-21.
 */

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private List<Memo> memoList;
    private Activity activity;

    public MemoAdapter(List<Memo> memoList, Activity activity) {
        this.memoList = memoList;
        this.activity = activity;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_memo, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        final String id = memoList.get(position).getId();
        final String title = memoList.get(position).getTitle();
        final String content = memoList.get(position).getContent();


        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MemoInputActivity.class);
                intent.putExtra(MemoInputActivity.KEY_MODIFY, true);
                intent.putExtra(MemoInputActivity.KEY_ID, Integer.parseInt(id));
                intent.putExtra(MemoInputActivity.KEY_TITLE, title);
                intent.putExtra(MemoInputActivity.KEY_CONTENT, content);
                activity.startActivityForResult(intent, MemoListActivity.REQ_CODE_INPUT);
            }
        });
        holder.titleView.setText(title);
        holder.contentView.setText(content);
    }


    @Override
    public int getItemCount() {
        return memoList.size();
    }

    static class MemoViewHolder extends RecyclerView.ViewHolder {
        ViewGroup body;
        TextView titleView;
        TextView contentView;

        MemoViewHolder(View itemView) {
            super(itemView);
            this.body = (ViewGroup) itemView.findViewById(R.id.item_body);
            this.titleView = (TextView) itemView.findViewById(R.id.item_title);
            this.contentView = (TextView) itemView.findViewById(R.id.item_content);
        }
    }
}
