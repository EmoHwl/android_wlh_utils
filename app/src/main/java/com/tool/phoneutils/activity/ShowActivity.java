package com.tool.phoneutils.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.phoneutils.R;
import com.tool.phoneutils.utils.L;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    void initRecyclerView(){
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initStringArr();
        itemAdapter = new ItemAdapter(this,stringArrayList);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                L.i("newState="+newState);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                L.i("dx="+dx+"| dy="+dy);
                int[] location = new int[2];
                itemAdapter.getTextView(0).getLocationOnScreen(location);
                L.i("location = "+location[0]);
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initStringArr() {
        stringArrayList.add("test1");
        stringArrayList.add("test2");
        stringArrayList.add("test3");
        stringArrayList.add("test4");

        stringArrayList.add("test1");
        stringArrayList.add("test2");
        stringArrayList.add("test3");
        stringArrayList.add("test4");
        stringArrayList.add("test1");
        stringArrayList.add("test2");
        stringArrayList.add("test3");
        stringArrayList.add("test4");

        stringArrayList.add("test1");
        stringArrayList.add("test2");
        stringArrayList.add("test3");
        stringArrayList.add("test4");

        stringArrayList.add("test1");
        stringArrayList.add("test2");
        stringArrayList.add("test3");
        stringArrayList.add("test4");


    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        Context context;
        ArrayList<String> stringArrayList;
        ArrayList<TextView> textViews = new ArrayList<>();
        public ItemAdapter(Context context,ArrayList<String> stringArrayList) {
            this.context = context;
            this.stringArrayList = stringArrayList;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(context);
            textView.setPadding(120,20,120,20);
            ItemHolder holder = new ItemHolder(textView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, final int position) {
            holder.textView.setText(stringArrayList.get(position));
            holder.textView.setTextColor(Color.WHITE);
            textViews.add(position,holder.textView);
            int[] location = new int[2];
            holder.textView.getLocationOnScreen(location);
            L.i(position+"=location = "+location[0]);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    L.i("stringArrayList.get(position)="+stringArrayList.get(position));
                }
            });
        }

        public TextView getTextView(int position){
            return textViews.get(position);
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public ItemHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }

            public TextView getTextView() {
                return textView;
            }
        }
    }
}
