package com.feirui.feiyunbangong.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

public class AboutUsActivity extends AppCompatActivity {
    TextView tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        tv_about= (TextView) findViewById(R.id.tv_about);
        tv_about.setText("版本号：" + getVersionCode());
    }
    /**
     * 获取版本号改为版本名
     *
     * @return
     */
    public String getVersionCode() {
        PackageManager manager = getApplicationContext().getPackageManager();// 获取包管理器
        try {
            // 通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(),
                    0);// 获取包对象信息
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
