package com.example.android.timemanagement.activities;
import java.util.HashMap;
import java.util.Set;
import com.example.android.timemanagement.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class HomeTabActivity extends TabActivity  {

    private static final String TAB_1= "Days";
    private static final String TAB_2 = "Total";
    private static final String TAB_3 = "Graph";
    private TabHost mTabHost;
    private int currentSelectIndex = 0;
    
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_tab_main);
        setupView();
    }

    private void setupView() {

        mTabHost = (TabHost) this.findViewById(android.R.id.tabhost);
        mTabHost.setup();

        Intent i = new Intent(this, MainActivity.class);
        TabSpec tab1 = mTabHost
                .newTabSpec(TAB_1)
                .setIndicator(
                        createTabView(getApplicationContext(), TAB_1))
                .setContent(i);
        mTabHost.addTab(tab1);

        TabSpec tab2 = mTabHost
                .newTabSpec(TAB_2)
                .setIndicator(
                        createTabView(getApplicationContext(), TAB_2))
                .setContent(new Intent(this, TotalActivity.class));
        mTabHost.addTab(tab2);


        TabSpec tab3 = mTabHost
                .newTabSpec(TAB_3)
                .setIndicator(
                        createTabView(getApplicationContext(), TAB_3))
                .setContent(new Intent(this, TotalActivity.class));
        mTabHost.addTab(tab3);

        mTabHost.setCurrentTabByTag(TAB_1);
        updateTab(mTabHost);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                mTabHost.setCurrentTabByTag(s);
               if(s.equals(TAB_1)){
                   currentSelectIndex = 0;
               }else if(s.equals(TAB_2)){
                   currentSelectIndex = 1;
               }else if(s.equals(TAB_3)){
                   currentSelectIndex = 2;
               }
                updateTab(mTabHost);
            }
        });

    }

    /**
     *
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost){
        for (int i= 0; i<tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_widget_content);
            if(currentSelectIndex == i)
                tv.setTextColor(Color.RED);
            else
                tv.setTextColor(Color.WHITE);
        }
    }

    private View createTabView(Context context, String text) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.activity_home_tab_view, null);
        TextView textview = (TextView) view1.findViewById(R.id.tab_widget_content);
        textview.setText(text);
        return view1;
    }
}
