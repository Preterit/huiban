package com.feirui.feiyunbangong.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.CaoZuoJiLuActivity;
import com.feirui.feiyunbangong.activity.MyCaiGouDetailActivity;
import com.feirui.feiyunbangong.activity.MyFuKuanDetailActivity;
import com.feirui.feiyunbangong.activity.MyQiTaDetailActivity;
import com.feirui.feiyunbangong.activity.MyShenPiBaoXiaoDetailActivity;
import com.feirui.feiyunbangong.activity.MyShenPiQingJaDetailActivity;
import com.feirui.feiyunbangong.activity.MyWaiChuDetailActivity;
import com.feirui.feiyunbangong.adapter.ChaoSongAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wang.avi.AVLoadingIndicatorView;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 抄送我的
 */
public class SendFragment extends Fragment implements ChaoSongAdapter.OnChakanClickListener{
    private static final int ON_REFRESH = 1;  //刷新常量
    private static final int ON_LOAD_MORE = 2;  //加载常量
    private AVLoadingIndicatorView mAvi;
    private LinearLayout mRy_loading_more; //显示加载动画
    private PullToRefreshLayout mPull_more; //实现上拉加载  下拉刷新
    private AVLoadingIndicatorView mAvi_loadingmore;//加载更多的动画
    private PullListView mListView;
    private ChaoSongAdapter chaoAdapter;
    private View mView;
    private String chaosongurl; //请求的地址
    private String type = "";
    private int currentPage = 1;//当前页数
    CallBackValue callBackValue;

    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()){
            initView();
            initListener();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_send, container, false);
        if (getUserVisibleHint()){
            initView();
            initListener();
        }
        return mView;
    }


    public void showMessageFromActivity(String message){
        type = message;
        initView();
        initListener();
    }

    public void initView(){
        mAvi =  mView.findViewById(R.id.avi);
        mRy_loading_more = mView.findViewById(R.id.ry_loading_more);
        mPull_more = mView.findViewById(R.id.pull_to_refresh);
        mAvi_loadingmore = mView.findViewById(R.id.avi_loadingmore);
        chaosongurl = UrlTools.url + UrlTools.APPROVAL_MY_APPROVAL_SEND;
        mListView =  mView.findViewById(R.id.haveget);
        chaoAdapter = new ChaoSongAdapter(getActivity(), new ArrayList<HashMap<String, Object>>());
        mListView.setAdapter(chaoAdapter);
        // 显示加载等待动画
        mAvi.smoothToShow();
        currentPage = 1;
        loadData(currentPage, ON_REFRESH, chaosongurl);
    }

    private void initListener() {
        //下拉刷新  上拉加载更多
        mPull_more.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentPage = 1;
                loadData(currentPage, ON_REFRESH, chaosongurl);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                currentPage += 1;
                loadData(currentPage,ON_LOAD_MORE,chaosongurl);
                mRy_loading_more.setVisibility(View.VISIBLE);
                mPull_more.loadMoreFinish(true);
                mAvi_loadingmore.smoothToShow();
            }
        });

        //查看每一项的详情
        chaoAdapter.setOnChakanClickListener(this);

    }

    public void loadData(int page, final int onRefreshOrLoadMore,String url) {
        RequestParams params = new RequestParams();
            if (!TextUtils.isEmpty(type)) {
                Log.e("操作记录-抄送我的", "------" + type);
                switch (type) {
                    case "请假":
                        params.put("type", "1");
                        break;
                    case "外出":
                        params.put("type", "2");
                        break;
                    case "报销":
                        params.put("type", "3");
                        break;
                    case "付款":
                        params.put("type", "4");
                        break;
                    case "采购":
                        params.put("type", "5");
                        break;
                    case "其他":
                        params.put("type", "6");
                        break;

                }
            } else {
                params.put("type", "");
            }
            params.put("current_page", page + "");
            params.put("pagesize", "15");
            Log.e("操作记录-抄送我的", "params: " + chaosongurl + params.toString());
            //将类型传递给activity
            callBackValue.SendMessageValue("send",type);
            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);
                    JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    Log.e("操作记录-抄送我的", "jsonBean: " + jsonBean.toString());
                    if (onRefreshOrLoadMore == ON_REFRESH){
                        // 隐藏加载等待动画
                        mAvi.smoothToHide();
                        mPull_more.refreshFinish(true);
                    }else if (onRefreshOrLoadMore == ON_LOAD_MORE){
                        mRy_loading_more.setVisibility(View.GONE);
                        mAvi_loadingmore.smoothToHide();
                    }

                    if (jsonBean.getCode().equals("200")) {
                        if (jsonBean.getInfor().size() == 0) {
                            if (onRefreshOrLoadMore == ON_REFRESH) {
                                T.showShort(getActivity(), "暂无该数据~");
                            } else {
                                T.showShort(getActivity(), "没有更多数据啦~");
                            }
                            return;
                        }

                        if (onRefreshOrLoadMore == ON_REFRESH) {
                            chaoAdapter.addAll(jsonBean.getInfor());
                        } else {
                            chaoAdapter.add(jsonBean.getInfor());
                        }
                    } else if (jsonBean.getCode().equals("-400")) {
                        T.showShort(getActivity(), jsonBean.getMsg());
                    } else {
                        chaoAdapter.addAll(jsonBean.getInfor());
                    }
                }
            });
}

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onChakanClick(HashMap<String, Object> data, int position) {
        String approval_type = (String) data.get("approval_type");
        switch (approval_type){
            case "请假":
                Intent qingjia = new Intent(getActivity(),MyShenPiQingJaDetailActivity.class);
                qingjia.putExtra("data",data);
                startActivity(qingjia);
                break;
            case "报销":
                Intent baoxiao = new Intent(getActivity(),MyShenPiBaoXiaoDetailActivity.class);
                baoxiao.putExtra("data",data);
                startActivity(baoxiao);
                break;
            case "外出":
                Intent waichu = new Intent(getActivity(),MyWaiChuDetailActivity.class);
                waichu.putExtra("data",data);
                startActivity(waichu);
                break;
            case "付款":
                Intent fukuan = new Intent(getActivity(),MyFuKuanDetailActivity.class);
                fukuan.putExtra("data",data);
                startActivity(fukuan);
                break;
            case "采购":
                Intent caigou = new Intent(getActivity(),MyCaiGouDetailActivity.class);
                caigou.putExtra("data",data);
                startActivity(caigou);
                break;
            case "其他":
                Intent qita = new Intent(getActivity(),MyQiTaDetailActivity.class);
                qita.putExtra("data",data);
                startActivity(qita);
                break;
        }
    }

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }

    //定义一个回调接口
    public interface CallBackValue{
         void SendMessageValue(String type, String strValue);
    }
}
