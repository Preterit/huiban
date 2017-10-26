package com.feirui.feiyunbangong.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.ChuangJianTuanDuiActivity;
import com.feirui.feiyunbangong.activity.GuanLiTuanDuiActivity;
import com.feirui.feiyunbangong.activity.SouSuoTuanDuiActivity;
import com.feirui.feiyunbangong.activity.TuanDui_DetailActivity;
import com.feirui.feiyunbangong.adapter.MyAdapter;
import com.feirui.feiyunbangong.adapter.TuanDuiAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnTeamNoticeNumChanged;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.WordsNavigation;
import com.feirui.feiyunbangong.view.WordsNavigation.onWordsChangeListener;
import com.loopj.android.http.RequestParams;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 我的团队：
 *
 * @author feirui1
 */
public class Fragment4 extends BaseFragment implements OnClickListener,
        OnItemClickListener, onWordsChangeListener, OnScrollListener {

    private ListView lv_tuandui;// 已创建的团队列表；
    private Handler handler;
    private static TuanDuiAdapter adapter;
    private static MyAdapter adapter2;
    private WordsNavigation word;
    private static List<TuanDui> tds0 = new ArrayList<>(); // //从数据库获取的 排序
    private List<TuanDui> tds = new ArrayList<>(); // 从数据库获取到的
    private View header_view;// listview头部；
    private LinearLayout ll_chaungjiantuandui, ll_jiarutuandui;
    private View v;
    private TextView tv_sousuolianxiren, tv_word;// 搜索联系人，中间位置显示的首字母；
    private static int allNoticce;// 所有未读数数量；
    private static OnTeamNoticeNumChanged listener;
    static ArrayList<Activity> activitys = Happlication.getActivities();
    private static Activity activity ;

    @SuppressLint({"NewApi", "ValidFragment"})//横竖屏切换问题
    public Fragment4(OnTeamNoticeNumChanged listener) {
        this.listener = listener;
    }

    public Fragment4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = setContentView(inflater, R.layout.fragment_main4);
        DataSupport.deleteAll(TuanDui.class, "tid = ?", "100144");
        initView();
        setListener();
        setListView();
        return v;
    }

    private void regist() {
        //动态广播
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constant.GET_TEAM_BROADCAST);
//        filter.addAction(Constant.ON_REACEIVE_ADD_TEAM);// 加入某个团队的意图；
//        filter.addAction(Constant.ON_RECEIVE_NEW_MEMBER_ADD);
//        receiver = new MyBroadReceiver();
//        getActivity().registerReceiver(receiver, filter);
        //静态广播
        Intent intent = new Intent();
        intent.setAction(Constant.GET_TEAM_BROADCAST);
        intent.setAction(Constant.ON_REACEIVE_ADD_TEAM);// 加入某个团队的意图；
        intent.setAction(Constant.ON_RECEIVE_NEW_MEMBER_ADD);
        getActivity().sendBroadcast(intent);

    }

    private  static void initData() {  //通知获取数据
        String url = UrlTools.url + UrlTools.TUANDUI_ITEM;
        RequestParams params = new RequestParams();
        params.put("current_page", 1 + "");
        params.put("pagesize", 2000 + "");

        if (activitys.size() == 0 || activitys == null){
            return;
        }else {
            activity = activitys.get(0);
        }
        Utils.doPost(LoadingDialog.getInstance(activity), activity,
                url, params, new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        setData(bean);
                    }

                    @Override
                    public void failure(String msg) {
//                        tds.removeAll(tds);
//                        adapter.add(tds);
//                        getData();
                        T.showShort(activity, "网络请求失败，请检查网络");
//                        T.showShort(getActivity(), msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }

                });
        adapter.add(tds0);
    }

    /**
     * 从网络获取数据保存到数据库
     */
    private void getNetData(){
        String url = UrlTools.url + UrlTools.TUANDUI_ITEM;
        RequestParams params = new RequestParams();
        params.put("current_page", 1 + "");
        params.put("pagesize", 2000 + "");

        Utils.doPost(LoadingDialog.getInstance(getActivity()), getActivity(),
                url, params, new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        setNetData(bean);
                    }

                    @Override
                    public void failure(String msg) {
//                        tds.removeAll(tds);
//                        adapter.add(tds);
                        getData();
                        T.showShort(getActivity(), "网络请求失败，请检查网络");
//                        T.showShort(getActivity(), msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }

                });
        adapter.add(tds0);
    }


    /**
     * 数据获取
     */
    public void getData(){
        tds = DataSupport.findAll(TuanDui.class);
        //获取本地数据库信息
        if (tds == null || tds.size() == 0){
            //如果数据库为空  从网络获取数据
                getNetData();
        } else {
            tds0.removeAll(tds0); //移除集合中的添加入排序好的
            for (int i = 0; i < tds.size(); i++) {
                TuanDui td = new TuanDui(tds.get(i).getTid(),
                        tds.get(i).getName());
                td.setNotice_number(tds.get(i).getNotice_number());
                try {
                    td.setHave(tds.get(i).getNotice_number() > 0);
                    // 首字母是"哦"的有问题：
                    if ("哦".equals(td.getName().charAt(0) + "")) {
                        td.setPinyin("O");
                    } else {
                        td.setPinyin(Utils.getPinyin(td.getName()));
                    }
                    Log.e("TAG", td.getPinyin().substring(0, 1)
                            + "td.getPinyin().substring(0,1)");
                    td.setHeadword(td.getPinyin().charAt(0) + "");
                    Log.e("tds", "setData: ------数据获取-sdsdsd-------" + td);
                    tds0.add(td);
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
            }

            //[TuanDui [id=100149, name=123456], TuanDui [id=100140, name=Android]
            // 对集合排序
            Collections.sort(tds0, new Comparator<TuanDui>() {
                @Override
                public int compare(TuanDui lhs, TuanDui rhs) {
                    // 根据拼音进行排序
                    return lhs.getHeadword().compareTo(rhs.getHeadword());
                }
            });
            Log.e("tds", "setData: ------数据获取-sdsdsd-------" + tds0);
            adapter2.add(tds0);   //从数据库获取的 排序
        }
    }

    @Override
    public void onResume() {
        regist();// 注册广播接收器;
        getData();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setListView() {
        lv_tuandui.addHeaderView(header_view);
        lv_tuandui.setAdapter(adapter2);
        lv_tuandui.setOnScrollListener(this);
    }

    private void setListener() {
        ll_chaungjiantuandui.setOnClickListener(this);
        ll_jiarutuandui.setOnClickListener(this);
        lv_tuandui.setOnItemClickListener(this);
       // tv_sousuolianxiren.setOnClickListener(this);

        // 设置列表点击滑动监听
        handler = new Handler();
        word.setOnWordsChangeListener(this);
    }

    @SuppressLint("InflateParams")
    private void initView() {
        tv_word = (TextView) v.findViewById(R.id.tv);
        word = (WordsNavigation) v.findViewById(R.id.words);
        lv_tuandui = (ListView) v.findViewById(R.id.lv_tuandui);
        adapter = new TuanDuiAdapter(getActivity().getLayoutInflater());
        adapter2 = new MyAdapter(getActivity(), tds);
        tds = new ArrayList<>();
        //创建加入团队
        header_view = getActivity().getLayoutInflater().inflate(
                R.layout.ll_team_header, null);
        ll_chaungjiantuandui = (LinearLayout) header_view
                .findViewById(R.id.ll_add_team);
        ll_jiarutuandui = (LinearLayout) header_view
                .findViewById(R.id.ll_add_to_team);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_guanli:
                toGuanLiTuanDui(v);
                break;
            case R.id.ll_add_team:
                // 创建团队：
                startActivityForResult(new Intent(getActivity(),
                        ChuangJianTuanDuiActivity.class),1000);
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            case R.id.ll_add_to_team:
                // 加入团队：
                startActivityForResult(new Intent(getActivity(),
                        SouSuoTuanDuiActivity.class),1000);
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            /**case R.id.tv_sousuolianxiren:
                startActivity(new Intent(getActivity(), SouSuoTuanDuiActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;*/
        }
    }

    private void toGuanLiTuanDui(View v) {
        String i = (String) v.getTag();
        Intent intent = new Intent(getActivity(), GuanLiTuanDuiActivity.class);
        intent.putExtra("id", i);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.aty_zoomin,
                R.anim.aty_zoomout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // 注意从1开始，因为有header;
        if (position > 0 && position <= tds0.size()){
            TuanDui tuanDui = tds0.get(position - 1);
            Intent intent = new Intent(getActivity(), TuanDui_DetailActivity.class);
            intent.putExtra("tuanDui", tuanDui);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.aty_zoomin,
                    R.anim.aty_zoomout);
        }

    }

    private void setNetData(JsonBean bean){
        ArrayList<HashMap<String, Object>> infor = bean.getInfor();
        tds0.removeAll(tds0);
        // 总消息数：
        allNoticce = (int) infor.get(0).get("allCount");

        listener.changed(allNoticce);// 接口回调改变团队图标上的消息个数指示；

        for (int i = 0; i < infor.size(); i++) {
            TuanDui td = new TuanDui(String.valueOf(infor.get(i).get("id")),
                    String.valueOf(infor.get(i).get("team_name")));
            td.setNotice_number(Integer.parseInt(""
                    + infor.get(i).get("noticecount")));
            try {
                td.setHave(Integer.parseInt(""
                        + infor.get(i).get("noticecount")) > 0);
                // 首字母是"哦"的有问题：
                if ("哦".equals(td.getName().charAt(0) + "")) {
                    td.setPinyin("O");
                } else {
                    td.setPinyin(Utils.getPinyin(td.getName()));
                }
                Log.e("TAG", td.getPinyin().substring(0, 1)
                        + "td.getPinyin().substring(0,1)");
                td.setHeadword(td.getPinyin().charAt(0) + "");

                tds0.add(td);
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }
        }

        //[TuanDui [id=100149, name=123456], TuanDui [id=100140, name=Android]
        // 对集合排序
        Collections.sort(tds0, new Comparator<TuanDui>() {
            @Override
            public int compare(TuanDui lhs, TuanDui rhs) {
                // 根据拼音进行排序
                return lhs.getHeadword().compareTo(rhs.getHeadword());
            }
        });
        Log.e("tds", "setData: ----------------" + tds0 );
        adapter2.notifyDataSetChanged();
        adapter2.add(tds0);

        for (int i = 0; i < tds0.size(); i++){
            TuanDui tuanDui = new TuanDui();
            tuanDui.setTid(tds0.get(i).getTid());
            tuanDui.setName(tds0.get(i).getName());
            tuanDui.save();
            Log.e("tag", "setData: -----tds2-----------" + DataSupport.find(TuanDui.class,i));
        }

    }

    private static void setData(JsonBean bean) { //通知的 从网络重新获取数据
        ArrayList<HashMap<String, Object>> infor = bean.getInfor();
        tds0.removeAll(tds0);
        // 总消息数：
        allNoticce = (int) infor.get(0).get("allCount");

        listener.changed(allNoticce);// 接口回调改变团队图标上的消息个数指示；
        for (int i = 0; i < infor.size(); i++) {
            TuanDui td = new TuanDui(String.valueOf(infor.get(i).get("id")),
                    String.valueOf(infor.get(i).get("team_name")));
            td.setNotice_number(Integer.parseInt(""
                    + infor.get(i).get("noticecount")));
            try {
                td.setHave(Integer.parseInt(""
                        + infor.get(i).get("noticecount")) > 0);
                // 首字母是"哦"的有问题：
                if ("哦".equals(td.getName().charAt(0) + "")) {
                    td.setPinyin("O");
                } else {
                    td.setPinyin(Utils.getPinyin(td.getName()));
                }
                Log.e("TAG", td.getPinyin().substring(0, 1)
                        + "td.getPinyin().substring(0,1)");
                td.setHeadword(td.getPinyin().charAt(0) + "");

                tds0.add(td);
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }
        }

        //[TuanDui [id=100149, name=123456], TuanDui [id=100140, name=Android]
        // 对集合排序
        Collections.sort(tds0, new Comparator<TuanDui>() {
            @Override
            public int compare(TuanDui lhs, TuanDui rhs) {
                // 根据拼音进行排序
                return lhs.getHeadword().compareTo(rhs.getHeadword());
            }
        });
        Log.e("tag", "setData: ----------------" + tds0 );
        adapter2.notifyDataSetChanged();
        adapter2.add(tds0);

        //清除数据库  重新添加
        DataSupport.deleteAll(TuanDui.class);
        for (int i = 0; i < tds0.size(); i++){
            TuanDui tuanDui = new TuanDui();
            tuanDui.setTid(tds0.get(i).getTid());
            tuanDui.setName(tds0.get(i).getName());
            tuanDui.save();
        }


    }
    //动态注册改为动态注册
    public static  class MyBroadReceiver extends BroadcastReceiver {

        public MyBroadReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.GET_TEAM_BROADCAST.equals(intent.getAction())) {
                initData();
            } else if (Constant.ON_REACEIVE_ADD_TEAM.equals(intent.getAction())) {
                initData();
            }else if(Constant.ON_RECEIVE_NEW_MEMBER_ADD.equals(intent.getAction())){
                initData();
            }
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
//        getActivity().unregisterReceiver(receiver);
        super.onDestroyOptionsMenu();
    }

    @Override
    public void wordsChange(String words) {
        updateWord(words);
        updateListView(words);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 当滑动列表的时候，更新右侧字母列表的选中状态
        if (firstVisibleItem > 0 && firstVisibleItem <= tds.size()) {
            word.setTouchIndex(tds.get(firstVisibleItem - 1).getHeadword());
        }
    }

    /**
     * @param words 首字母
     */
    private void updateListView(String words) {
        for (int i = 0; i < tds.size(); i++) {
            String headerWord = tds.get(i).getHeadword();
            // 将手指按下的字母与列表中相同字母开头的项找出来
            if (words.equals(headerWord)) {
                // 将列表选中哪一个
                lv_tuandui.setSelection(i + 1);// 因为有头；
                // 找到开头的一个即可
                return;
            }
        }
    }

    /**
     * 更新中央的字母提示
     *
     * @param words 首字母
     */
    private void updateWord(String words) {
        tv_word.setText(words);
        tv_word.setVisibility(View.VISIBLE);
        // 清空之前的所有消息
        handler.removeCallbacksAndMessages(null);
        // 1s后让tv隐藏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_word.setVisibility(View.GONE);
            }
        }, 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001){
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
