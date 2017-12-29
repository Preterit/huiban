package com.feirui.feiyunbangong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ChengYuanAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.IPreference;
import com.feirui.feiyunbangong.utils.PreferenceImpl;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 搜索团队成员 从网络请求数据再加载  如果数据太多 会出现SocketTimeoutException:服务器响应超时
 * 解决办法：延长相应超时的时间  但不是最终解决方案  最好让后台写一个搜索的接口
 */
public class SearchMemberActivity extends BaseActivity {
    private TuanDui td;
    private int totleNum = 0;
    private ArrayList<TuanDuiChengYuan> tdcys ; //所有的团队成员
    private List<TuanDuiChengYuan> middle=new ArrayList<>();
    private List<TuanDuiChengYuan> mBackData=new ArrayList<>(); //返回的团队成员
    private ListView lv_search;//承载搜索到的成员
    private SearchView sc_search;
    private TextView tv_search_person;
    private ChengYuanAdapter mSearchAdapter;
    private AVLoadingIndicatorView mAvi;//加载中动画
//    private FlexboxLayout mFlexboxLayout;//流式标签云控件
    private RelativeLayout mLayoutHistory;//历史搜索标签
    private List<String> mHotTitles = new ArrayList<String>();//搜索历史内容
    private List<String> mHistoryTitles;
    private PreferenceImpl mPreUtils;
    private final String HISTORY_SEARCHA = "history_search";
    private String mKeywords;//搜索关键字


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);
        Intent intent = getIntent();
        //传过来的团队
        td = (TuanDui) intent.getSerializableExtra("td");
        initView();
        showLoading();
        getHistoryTitles();
        initData(1,0);
        initListener();
    }

    /*
    展示搜索历史
     */
    private void getHistoryTitles() {
        mPreUtils = (PreferenceImpl) IPreference.prefHolder.getPreference(this, HISTORY_SEARCHA);
        mHistoryTitles = new ArrayList<>();
        if (mPreUtils.getAll(HISTORY_SEARCHA) != null) {
            mHistoryTitles = mPreUtils.getAll(HISTORY_SEARCHA);
            updateShowHotTag(mHistoryTitles);
        }
    }


    private void initView() {
        tdcys = new ArrayList<>();
        initTitle();
        setCenterString("搜索团队成员");
        leftIv.setImageResource(R.drawable.arrows_left);
        setRightVisibility(false);
//        mFlexboxLayout = (FlexboxLayout) findViewById(R.id.flexbox_layout);
        mLayoutHistory = (RelativeLayout) findViewById(R.id.layout_history);
        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
    }

    /**
     *
     * @param num 请求页数
     * @param type 请求类型 0 第一次请求 1 多次请求
     */
    private void initData(int num, final int type) {
        String url = UrlTools.url + UrlTools.DETAIL_TUANDUICHENGYUAN_TEAM;
        RequestParams params = new RequestParams();
        params.put("id", td.getTid());
        params.put("curpage",num + "");

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
                new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        if (type == 0){
                            tdcys.removeAll(tdcys);
                            HashMap<String, Object> hm = infor.get(0);
                            totleNum = Integer.parseInt(hm.get("Allnum") + "");
                            getMember(infor);
                            mHandler.sendEmptyMessage(0);
                        }else if (type == 1){
                            getMember(infor);
                        }
                        hideLoading();
                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(SearchMemberActivity.this, msg,Toast.LENGTH_SHORT)
                                .show();
                        hideLoading();
                    }

                    @Override
                    public void finish() {
                        hideLoading();
                    }
                });
    }

    private void getMember(ArrayList<HashMap<String,Object>> infor){
        for (int i = 0; i < infor.size(); i++) {
            HashMap<String, Object> hm = infor.get(i);
            Log.e("团队成员", "团队成员: "+hm.toString() );
            TuanDuiChengYuan tdcy = new TuanDuiChengYuan(hm
                    .get("id") + "", String.valueOf(hm
                    .get("staff_id")), String.valueOf(hm
                    .get("staff_name")), String.valueOf(hm
                    .get("staff_head")), hm.get("type") + "",
                    String.valueOf(hm.get("staff_mobile")),
                    String.valueOf(hm.get("staff_email")),
                    hm.get("tag_name") + "",
                    String.valueOf(hm.get("introduction")),String.valueOf(hm.get("store_url")),
                    String.valueOf(hm.get("sex")),String.valueOf(hm.get("birthday")),
                    String.valueOf(hm.get("address")),String.valueOf(hm.get("staff_key1")),
                    String.valueOf(hm.get("staff_key2")),String.valueOf(hm.get("staff_key3"))
                    ,String.valueOf(hm.get("staff_key4")),String.valueOf(hm.get("staff_key5")),
                    String.valueOf(hm.get("type_position")),String.valueOf(hm.get("position")),
                    String.valueOf(hm.get("limit_position")));

            tdcy.setTeam_member_list_id(hm.get("team_member_list_id") + "");

            tdcy.setState(Integer.parseInt(hm.get("state") + ""));

            tdcy.setFriendstate((int) hm.get("friendstate"));// 是否是好友；

            tdcy.setRemark(hm.get("remark") + "");// 设置备注；

            tdcy.setT_remark(hm.get("t_remark") + "");// 设置团队备注；

            // 团队设置到团队团长id:
            if ("团长".equals(tdcy.getType())) {
                td.setGuanli_id(tdcy.getStaff_id());
            }
            // 副团长添加到团队副团长id集合中；
            if ("副团长".equals(tdcy.getType())) {
                td.getDcmoes().add(tdcy.getStaff_id());
            }
            tdcys.add(tdcy);
        }
    }

    private void initListener() {
        listenerSearch();
    }

    //搜索团队成员
    private void listenerSearch() {
        sc_search = (SearchView) findViewById(R.id.sc_search);
        lv_search = (ListView) findViewById(R.id.lv_search);
        tv_search_person = (TextView)findViewById(R.id.tv_search_person);
        // 设置该SearchView默认是否自动缩小为图标
        sc_search.setIconifiedByDefault(false);
        if (sc_search != null) {
            try {        //--拿到字节码
                Class<?> argClass = sc_search.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(sc_search);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        mBackData.addAll(tdcys);
        Log.e("123", "listenerSearch: -------------" + mBackData );
        //设置搜索文本监听
        sc_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //搜索内容改变时
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    mBackData.clear();
                    lv_search.setVisibility(View.GONE);
                    tv_search_person.setVisibility(View.GONE);
                }else {//不为空时从联系人中取出
                    lv_search.setVisibility(View.VISIBLE);
                    tv_search_person.setVisibility(View.GONE);
                    //添加到历史搜索记录里
                    mHistoryTitles.add(newText);
                    mPreUtils.putAll(HISTORY_SEARCHA, mHistoryTitles);
                    setFilterText(newText);
                }
                return false;
            }
        });

    }

    public void setFilterText(String text){
        middle.clear();

        for (int i = 0;i < tdcys.size();i++){
            TuanDuiChengYuan bean = tdcys.get(i);
            if (bean.getName().indexOf(text) != -1 || bean.getKey1().indexOf(text) != -1
                    || bean.getKey2().indexOf(text) != -1 || bean.getKey3().indexOf(text) != -1
                    || bean.getKey4().indexOf(text) != -1 || bean.getKey5().indexOf(text) != -1){//是否包含该字
                middle.add(bean);
            }
        }
        if (middle.size() > 0){
            mBackData.clear();
            mSearchAdapter =  new ChengYuanAdapter(getLayoutInflater());
            mBackData.addAll(middle);
            lv_search.setAdapter(mSearchAdapter);
            mSearchAdapter.add(mBackData);
            lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 查看团队某个成员信息：
                    TuanDuiChengYuan tdcy = mBackData.get(position);
                    Intent intent = new Intent(SearchMemberActivity.this, PersonDataActivity.class);
                    intent.putExtra("tdcy", tdcy);
                    intent.putExtra("friend", 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                }
            });
        }else {
            lv_search.setVisibility(View.GONE);
            tv_search_person.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 展示历史记录云效果
     *
     * @param tags
     */
    private void updateShowHotTag(List<String> tags) {
        // 通过代码向FlexboxLayout添加View
        for (int i = 0; i < tags.size(); i++) {
            TextView textView = new TextView(this);
            textView.setBackground(getResources().getDrawable(R.drawable.flexbox_text_bg));
            textView.setText(tags.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(20, 20, 20, 20);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setTextColor(getResources().getColor(Color.parseColor("#333333")));
//            mFlexboxLayout.addView(textView);
            //通过FlexboxLayout.LayoutParams 设置子元素支持的属性
            ViewGroup.LayoutParams params = textView.getLayoutParams();
//            if (params instanceof FlexboxLayout.LayoutParams) {
//                FlexboxLayout.LayoutParams layoutParams = (FlexboxLayout.LayoutParams) params;
//                //layoutParams.setFlexBasisPercent(0.5f);
//                layoutParams.setMargins(10, 10, 20, 10);
//            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v;
                    //保存搜索记录
                    mKeywords = tv.getText().toString().trim();
                    mHistoryTitles.add(mKeywords);
                    mPreUtils.putAll(HISTORY_SEARCHA, mHistoryTitles);
                    setFilterText(mKeywords);
                }
            });
        }

    }


    public void showLoading() {
        mAvi.setVisibility(VISIBLE);
//        mFlexboxLayout.setVisibility(GONE);
        mAvi.smoothToShow();
    }

    public void hideLoading() {
        mAvi.setVisibility(View.GONE);
//        mFlexboxLayout.setVisibility(VISIBLE);
        mAvi.smoothToHide();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    int n = totleNum / 25;
                    Log.e("tuan", "handleMessage:------------- " + n );
                    if (n != 0){
                        for (int i = 0;i < n;i++){
                           initData(i + 2,1);
                        }
                    }
                    break;
            }
        }
    };
}
