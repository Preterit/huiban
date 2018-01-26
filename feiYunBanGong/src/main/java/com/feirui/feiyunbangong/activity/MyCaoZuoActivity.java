package com.feirui.feiyunbangong.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.MyFragmentPagerAdapter;
import com.feirui.feiyunbangong.fragment.CommitFragment;
import com.feirui.feiyunbangong.fragment.ReviseFragment;
import com.feirui.feiyunbangong.fragment.SendFragment;
import com.feirui.feiyunbangong.view.TypePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * create by xy
 * 这个anctiviy包含的fragment最好用接口的形式 使代码更清晰简洁
 * 由于viewpager的与缓存效果 不能使activity及时更新 所以把预缓存屏蔽掉了
 * 但是这样的话每次请求数据会浪费流量
 *
 */
public class MyCaoZuoActivity extends BaseActivity implements View.OnClickListener,
        SendFragment.CallBackValue,CommitFragment.CallBackCommitValue,ReviseFragment.CallBackReviseValue{
    private TabLayout mHandle_tab;
    private TextView righttv;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private ViewPager mViewPager;
    private TypePopupWindow mPopupWindow;
    private List mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cao_zuo);
        mList=new ArrayList();
        mList.add(CommitFragment.newInstance());
        mList.add(ReviseFragment.newInstance());
        mList.add(SendFragment.newInstance());
        initView();
        initListener();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("操作记录");
        righttv = findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        //右上角类型
        righttv.setText("类型");
        //TabLayaout
        mHandle_tab = findViewById(R.id.handle_tab);
        //ViewPager
        mViewPager = findViewById(R.id.view_pager);
        //初始化MyFragmentPagerAdapter
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mList);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mViewPager.setCurrentItem(0);
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        mHandle_tab.setupWithViewPager(mViewPager);
        mHandle_tab.setVerticalScrollbarPosition(0);

    }

    private void initListener() {
        righttv.setOnClickListener(this);
        mPopupWindow = new TypePopupWindow(MyCaoZuoActivity.this,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.righttv:
                //显示窗口
                mPopupWindow.showAtLocation(findViewById(R.id.righttv),Gravity.TOP | Gravity.RIGHT,righttv.getWidth() / 2,righttv.getHeight() * 2);
                break;
            case R.id.tv_all: //全部
                loadData(mHandle_tab.getSelectedTabPosition(),"");
                righttv.setText("全部");
                mPopupWindow.dismiss();
                break;
            case R.id.tv_leave: //请假
                loadData(mHandle_tab.getSelectedTabPosition(),"请假");
                righttv.setText("请假");
                mPopupWindow.dismiss();
                break;
            case R.id.tv_submit://报销
                loadData(mHandle_tab.getSelectedTabPosition(),"报销");
                righttv.setText("报销");
                mPopupWindow.dismiss();
                break;
            case R.id.tv_out://外出
                loadData(mHandle_tab.getSelectedTabPosition(),"外出");
                righttv.setText("外出");
                mPopupWindow.dismiss();
                break;
            case R.id.tv_spend://付款
                loadData(mHandle_tab.getSelectedTabPosition(),"付款");
                righttv.setText("付款");
                mPopupWindow.dismiss();
                break;
            case R.id.tv_purchase://采购
                loadData(mHandle_tab.getSelectedTabPosition(),"采购");
                righttv.setText("采购");
                mPopupWindow.dismiss();
                break;
            case R.id.tv_else://其他
                loadData(mHandle_tab.getSelectedTabPosition(),"其他");
                righttv.setText("其他");
                mPopupWindow.dismiss();
                break;


        }
    }

    /**
     * 从Activity传递类型到Fragment 加载数据
     * @param selectedTabPosition 选中的tab位置
     * @param type 查看的类型
     */
    private void loadData(int selectedTabPosition, String type) {
        if ( selectedTabPosition == 0) {
            //我提交的
            CommitFragment fragment0= (CommitFragment) myFragmentPagerAdapter.getItem(0);
            fragment0.showMessageFromActivity(type);
        }else if (selectedTabPosition == 1){
            //我审批的
            ReviseFragment fragment1 = (ReviseFragment) myFragmentPagerAdapter.getItem(1);
            fragment1.showMessageFromActivity(type);
        }else if (selectedTabPosition == 2){
            //抄送我的
            SendFragment fragment2 = (SendFragment) myFragmentPagerAdapter.getItem(2);
            fragment2.showMessageFromActivity(type);
        }
    }

    /**
     * 以下两个方法缓存数据 防止出现加载不出来的情况
     */
    public static final String POSITION = "POSITION";
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION,mHandle_tab.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    /**
     * 获取的类型
     * @param strValue
     */
    @Override
    public void SendMessageValue(String type, String strValue) {

            if (TextUtils.isEmpty(strValue)){
                righttv.setText("全部");
            }else {
                righttv.setText(strValue);
            }

    }

}
