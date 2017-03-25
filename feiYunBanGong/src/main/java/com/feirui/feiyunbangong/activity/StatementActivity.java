package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.PView;

/**
 * 报表
 *
 * @author admina
 */
public class StatementActivity extends BaseActivity {

  // 日报，周报，月报，业绩报表
  @PView(click = "onClick")
  LinearLayout ll_daily, ll_weekly, ll_monthly, ll_performance;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statement);
    initView();
  }

  private void initView() {
    initTitle();
    setLeftDrawable(R.drawable.arrows_left);
    setCenterString("报表");
    setRightVisibility(false);
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ll_daily: // 日报
        startActivity(new Intent(this, Statement1Activity.class));
        break;
      case R.id.ll_weekly: // 周报
        startActivity(new Intent(this, Statement2Activity.class));
        break;
      case R.id.ll_monthly: // 月报
        startActivity(new Intent(this, Statement3Activity.class));
        break;
      case R.id.ll_performance: // 业绩报表
        startActivity(new Intent(this, Statement4Activity.class));
        break;
    }
    overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
  }
}
