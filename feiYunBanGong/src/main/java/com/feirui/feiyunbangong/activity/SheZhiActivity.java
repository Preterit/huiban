package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.utils.T;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SheZhiActivity extends AppCompatActivity {

    private RelativeLayout yijianfankui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_zhi);

        yijianfankui=(RelativeLayout)findViewById(R.id.yijianfankui);
        setListeners();

    }

    private void setListeners() {

        yijianfankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SheZhiActivity.this, YiJianActivity.class));
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);

            }
        });

    }

    public void onClick1(View view){
        startActivity(new Intent(SheZhiActivity.this, ForgetPasswordActivity.class));
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }
    public void onClick2(View view){
        // 清理缓存：
        ImageLoader.getInstance().clearMemoryCache();// 清除内存图片；
        ImageLoader.getInstance().clearDiscCache();// 清除sd卡图片；
        T.showShort(this, "清理完成！");
    }

    public void onClick3(View view){
//        UseMessageDialog dialog1 = new UseMessageDialog(this);
//        dialog1.show();
        startActivity(new Intent(SheZhiActivity.this, AboutUsActivity.class));
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }

    public void onClick4(View view){
        finish();
    }
    public void onClick5(View view){
        startActivity(new Intent(SheZhiActivity.this, AddShopActivity.class));
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }
}
