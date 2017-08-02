package com.example.android.timemanagement.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.timemanagement.R;
import com.example.android.timemanagement.data.Item;

import android.widget.RadioGroup.OnCheckedChangeListener;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TotalActivity extends Activity implements OnCheckedChangeListener {

    SegmentedRadioGroup segmentText;
    private RecyclerView rv_tasks;
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_HEAD = 1;

    private List<ShowBean> list = new ArrayList<ShowBean>();
    private MyAdapter adapter;

    private int curIndex = 0;
    DbOptDao db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_total);
        Log.e("xxx","xxxx1");
        db = new DbOptDao(TotalActivity.this);

        segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
        segmentText.setOnCheckedChangeListener(this);
        rv_tasks = (RecyclerView)findViewById(R.id.rv_tasks);
        adapter = new MyAdapter();
        rv_tasks.setLayoutManager(new LinearLayoutManager(this));
        rv_tasks.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        updateDataShow();
        super.onResume();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.e("xxx","xxxx2");
        if (group == segmentText) {
            if (checkedId == R.id.button_one) {
                curIndex = 0;
            } else if (checkedId == R.id.button_two) {
                curIndex = 1;
            } else if (checkedId == R.id.button_three) {
                curIndex = 2;
            }
        }
        updateDataShow();
    }

    public void updateDataShow(){
        switch(curIndex){
            case 0:
                list.clear();
                list.add(db.getDayAll());
                Log.e("xxx","getDayAll()="+db.getDayAll());
                list.add(db.getDayAllSubjects());
                Log.e("xxx","getDayAllSubjects()="+db.getDayAllSubjects());
                list.add(db.getDayAllProjects());
                Log.e("xxx","getDayAllProjects()="+db.getDayAllProjects());


                Log.e("xxx","list getDayAll()="+list.size());
                adapter.notifyDataSetChanged();
                break;
            case 1:
                list.clear();
                list.add(db.getWeekAll());
                Log.e("xxx","getWeekAll()="+db.getWeekAll());
                list.add(db.getWeekAllSubjects());
                Log.e("xxx","getWeekAllSubjects()="+db.getWeekAllSubjects());
                list.add(db.getWeekAllProjects());
                Log.e("xxx","getWeekAllProjects()="+db.getWeekAllProjects());

                Log.e("xxx","list getWeekAll()="+list.size());
                adapter.notifyDataSetChanged();
                break;
            case 2:
                list.clear();
                list.add(db.getMonthAll());
                Log.e("xxx","getMonthAll()="+db.getMonthAll());
                list.add(db.getMonthAllSubjects());
                Log.e("xxx","getMonthAllSubjects()="+db.getMonthAllSubjects());
                list.add(db.getMonthAllProjects());
                Log.e("xxx","getMonthAllProjects()="+db.getMonthAllProjects());

                Log.e("xxx","list getMonthAll()="+list.size());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private TextView textView_content;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater mInflater = LayoutInflater.from(TotalActivity.this);
            switch (viewType) {
                case TYPE_CONTENT:
                    ViewGroup vImage = (ViewGroup) mInflater.inflate(R.layout.layout_content, parent, false);
                    ViewContentHolder vhImage = new ViewContentHolder(vImage);
                    return vhImage;
                case TYPE_HEAD:
                    ViewGroup vGroup = (ViewGroup) mInflater.inflate(R.layout.layout_head, parent, false);
                    ViewHeadHolder vhGroup = new ViewHeadHolder(vGroup);
                    return vhGroup;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                ShowBean bean = list.get(i);
                List<Item> dataList = bean.getData();
                if (position == count) {
                    ViewHeadHolder vGroup = (ViewHeadHolder) holder;
                    vGroup.textView_head.setText(bean.getTitle());
                }
                count++;

                for (int j = 0; j < dataList.size(); j++) {
                    if (position == count) {
                        ViewContentHolder vhContext = (ViewContentHolder) holder;

                        vhContext.textView_content.setText(dataList.get(j).getName());
                        vhContext.tv_finished.setText(dataList.get(j).getFinished());
                        Double finished = Double.parseDouble(dataList.get(j).getFinished());
                        vhContext.tv_rate.setText((int)(finished/(8*60) * 100)+"%");
                        vhContext.pb.setProgress((int)(finished/(8*60) * 100));
                        Log.e("xxx","bean.getTitle().toLowerCase()--"+bean.getTitle().toLowerCase());
                        if(!bean.getTitle().toLowerCase().contains("/")){
                            vhContext.ll_target.setVisibility(View.GONE);
                        }else{
                            vhContext.ll_target.setVisibility(View.VISIBLE);
                        }
                    }
                    count++;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                ShowBean bean = list.get(i);
                List<Item> dataList = bean.getData();
                if (position == count) {
                    return TYPE_HEAD;
                }
                count++;
                for (int j = 0; j < dataList.size(); j++) {
                    if (position == count) {
                        return TYPE_CONTENT;
                    }
                    count++;
                }
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            int count = list.size();
            for (int i = 0; i < list.size(); i++) {
                ShowBean bean = list.get(i);
                List<Item> dataList = bean.getData();
                count += dataList.size();
            }
            return count;
        }

        public class ViewHeadHolder extends RecyclerView.ViewHolder {
            public View rootView;
            public TextView textView_head;

            public ViewHeadHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.textView_head = (TextView) rootView.findViewById(R.id.textView_head);
            }

        }

        public class ViewContentHolder extends RecyclerView.ViewHolder {
            public View rootView;
            public TextView textView_content;
            public TextView tv_finished;
            public TextView tv_rate;
            private ProgressBar pb;
            private View ll_target;

            public ViewContentHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.textView_content = (TextView) rootView.findViewById(R.id.textView_content);
                this.tv_finished = (TextView) rootView.findViewById(R.id.tv_finished);
                this.tv_rate = (TextView) rootView.findViewById(R.id.tv_rate);
                this.pb = (ProgressBar) rootView.findViewById(R.id.pb);
                this.ll_target =  rootView.findViewById(R.id.ll_target);
            }

        }
    }
}
