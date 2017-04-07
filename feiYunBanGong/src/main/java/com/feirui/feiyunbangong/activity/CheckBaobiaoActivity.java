package com.feirui.feiyunbangong.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

public class CheckBaobiaoActivity extends ActionBarActivity {

    private TextView today_work;
    private TextView unfinish_work;
    private TextView tomorrow_work;
    private TextView remark_work;
    private ImageView img_remark1;
    private ImageView img_remark2;
    private ImageView img_remark3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_baobiao);
        initUI();
    }

    private void initUI() {
        today_work = (TextView)findViewById(R.id.today_work);
        unfinish_work = (TextView)findViewById(R.id.today_unfinish_work);
        tomorrow_work = (TextView)findViewById(R.id.tomorrow_work);
        remark_work = (TextView)findViewById(R.id.remark_work);
        img_remark1 = (ImageView)findViewById(R.id.img_remark1);
        img_remark2 = (ImageView)findViewById(R.id.img_remark2);
        img_remark3 = (ImageView)findViewById(R.id.img_remark3);


    }
}
