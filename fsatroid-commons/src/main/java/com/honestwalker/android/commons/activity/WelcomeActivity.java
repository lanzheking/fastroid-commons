package com.honestwalker.android.commons.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.honestwalker.android.KCCommons.R;
import com.honestwalker.android.commons.adapter.WelcomeViewFlowAdapter;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.views.CircleFlowIndicator;
import com.honestwalker.androidutils.views.ViewFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkyy on 16-8-8.
 */
public class WelcomeActivity extends Activity {
    private WelcomeViewFlowAdapter welcomeViewFlowAdapter;

    private ViewFlow showNavVP;
    private CircleFlowIndicator mIndicator;
    private Button skipBTN;
    private List<Integer> showNavimgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String mainClassName = intent.getStringExtra("MainActivity");
        if(!StringUtil.isEmptyOrNull(mainClassName)){
            initWelcome(mainClassName);
        }
    }

    private void initWelcome(final String mainClassName){
        setContentView(R.layout.welcome_layout);
        showNavVP = (ViewFlow) findViewById(R.id.viewflow_login);
        mIndicator = (CircleFlowIndicator) findViewById(R.id.indicator_login);
        skipBTN = (Button) findViewById(R.id.button1);

        skipBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(mainClassName);
            }
        });

        initDataSource();
        welcomeViewFlowAdapter = new WelcomeViewFlowAdapter(this,showNavimgs);
        showNavVP.setAdapter(welcomeViewFlowAdapter);
        showNavVP.setOnViewSwitchListener(new ViewFlow.ViewSwitchListener() {
            @Override
            public void onSwitched(View view, int position) {
                if (position == showNavimgs.size() - 1) {
                    skipBTN.setText("点击进入");
                } else {}
            }
        });
        mIndicator.bringToFront();
        mIndicator.setColors(Color.parseColor("#3390c0"), Color.parseColor("#E0E0E0"), 8, 4);
        showNavVP.setFlowIndicator(mIndicator);
    }

    private void initDataSource(){
        showNavimgs = new ArrayList<Integer>();
        showNavimgs.add(R.drawable.image_p1);
        showNavimgs.add(R.drawable.image_p2);
        showNavimgs.add(R.drawable.image_p3);
        showNavimgs.add(R.drawable.image_p4);
    }

    private void openActivity(String mainClassName){
        Intent intent = null;
        try {
            intent = new Intent(this, Class.forName(mainClassName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferencesLoader.putBoolean("isfirstopen", false);
    }
}
