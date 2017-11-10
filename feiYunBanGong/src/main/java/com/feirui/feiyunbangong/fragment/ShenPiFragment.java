package com.feirui.feiyunbangong.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ShenPiAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * 好像没用上,看情况删了
 */

public class ShenPiFragment extends BaseFragment {

    private ListView mListView;
    private ShenPiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_shen_pi, container, false);
        mListView = (ListView) v.findViewById(R.id.receiveTaskList_shenpi);
        requestData();

        return v;

    }

    private void requestData() {
        Bundle bundle =   getArguments();
        String string = bundle.getString("data");
        Log.d("tag","传过来的---"+string);

        RequestParams params = new RequestParams();
        String url = UrlTools.pcUrl  + UrlTools.APPROVAL_MY_APPROVAL;
        if (!"选择审批类型".equals(string)) {
            params.put("type", string);

        }
        AsyncHttpServiceHelper.post(url, params,    new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                Log.d("我审批fragment的json","JsonBean----"+jsonBean.getInfor());
                if (jsonBean.getCode().equals("200")) {
                    adapter=new ShenPiAdapter(getActivity(),jsonBean.getInfor());
   //                     adapter.addAll(jsonBean.getInfor());
                    mListView.setAdapter(adapter);
                }
            }
        });
    }
}
