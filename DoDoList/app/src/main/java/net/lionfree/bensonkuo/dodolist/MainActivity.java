package net.lionfree.bensonkuo.dodolist;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), R.id.container);

        tabHost.addTab(tabHost.newTabSpec("Unsolved")
                .setIndicator("1"), UnsolvedActivity.class, null);

        tabHost.addTab(tabHost.newTabSpec("Focus")
                .setIndicator("2"), FocusActivity.class, null);

        tabHost.addTab(tabHost.newTabSpec("Finish")
                .setIndicator("3"), FinishActivity.class, null);

    }

    public String getUnsolved(){
        return "test success!";
    }

    public String getFocus(){
        return "Focus!";
    }

    public String getFinish(){
        return "consider it done.";
    }
}
