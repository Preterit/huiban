package com.feirui.feiyunbangong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.ClockInActivity;
import com.feirui.feiyunbangong.activity.CustomerActivity;
import com.feirui.feiyunbangong.activity.ExamineActivity;
import com.feirui.feiyunbangong.activity.ProjectActivity;
import com.feirui.feiyunbangong.activity.ReadFormActivity;
import com.feirui.feiyunbangong.activity.StatementActivity;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.GlideImageLoader;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.LooperPicture;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;
import java.util.ArrayList;
import org.apache.http.Header;

/**
 * 会办
 *
 * @author Lesgod
 */
public class Fragment1 extends BaseFragment {
    private static final String MainActivity = null;
    View view;
    //	@PView
//	ViewPager vp;
//	// 轮播图片底部加载圆点的布局
    @PView
    LinearLayout fragment_vp_ll;
    @PView(click = "onClick")
    TextView tv_renzheng, tv_1, tv_2, tv_3;// 认证,电话会议，视频会议，公司须知
    // 打卡，报表，项目，审批，客户管理，数据报表
    @PView(click = "onClick")
    LinearLayout ll_clockIn, ll_statement, ll_project, ll_examine, ll_customer,
            ll_data;

    public Fragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = setContentView(inflater, R.layout.fragment_main1);
            initData();
            // 添加轮播图片
//			LooperPicture.addLooperPicture(vp, fragment_vp_ll, getActivity());

            ArrayList<String> images = new ArrayList<>();
            images.add("drawable://" + R.drawable.ban1);
            images.add("drawable://" + R.drawable.ban2);
            images.add("drawable://" + R.drawable.ban3);


            Banner banner = (Banner) view.findViewById(R.id.banner);
            banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 是否认证
     */
    private void initData() {
        RequestParams params = new RequestParams();

        String url = UrlTools.url + UrlTools.HOME_AUTHENTICATION;
        L.e("首页显示是否认证url" + url + " params" + params);
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        final JsonBean json = JsonUtils.getMessage(new String(
                                arg2));

                        if ("200".equals(json.getCode())) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    tv_renzheng.setText(json.getMsg());
                                }
                            });

                        } else {
                            T.showShort(getActivity(), json.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        super.onFailure(arg0, arg1, arg2, arg3);

                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1: // 电话会议
                T.showShort(getActivity(), "正在疯狂开发中...");
                break;
            case R.id.tv_2: // 视频会议
                T.showShort(getActivity(), "正在疯狂开发中...");
                break;
            case R.id.tv_3: // 公司须知
                RequestParams params = new RequestParams();

                String url = UrlTools.url + UrlTools.HOME_COMPANY_PROFILE;
                L.e("公司须知url" + url + " params" + params);
                AsyncHttpServiceHelper.post(url, params,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                super.onSuccess(arg0, arg1, arg2);
                                final JsonBean json = JsonUtils
                                        .getMessage(new String(arg2));
                                if ("200".equals(json.getCode())) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            T.showShort(getActivity(),
                                                    (String) json.getInfor().get(0)
                                                            .get("notice"));
                                        }
                                    });

                                } else {
                                    T.showShort(getActivity(), json.getMsg());
                                }
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                                super.onFailure(arg0, arg1, arg2, arg3);

                            }
                        });
                break;
            case R.id.ll_clockIn: // 打卡
                startActivity(new Intent(getActivity(), ClockInActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            case R.id.ll_statement: // 报表
                startActivity(new Intent(getActivity(), StatementActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            case R.id.ll_project: // 项目
                startActivity(new Intent(getActivity(), ProjectActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            case R.id.ll_examine: // 审批
                startActivity(new Intent(getActivity(), ExamineActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            case R.id.ll_customer: // 客户管理
                startActivity(new Intent(getActivity(), CustomerActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
            case R.id.ll_data: // 数据报表
                startActivity(new Intent(getActivity(), ReadFormActivity.class));
                getActivity().overridePendingTransition(R.anim.aty_zoomin,
                        R.anim.aty_zoomout);
                break;
        }
    }

    @Override
    public void onDestroy() {
        LooperPicture.stopTimerTask();
        super.onDestroy();
    }

}
