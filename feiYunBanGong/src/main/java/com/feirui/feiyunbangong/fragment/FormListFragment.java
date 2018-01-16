package com.feirui.feiyunbangong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.CheckBaobiaoActivity;
import com.feirui.feiyunbangong.activity.ReadFormActivity;
import com.feirui.feiyunbangong.adapter.FormAdapter;
import com.feirui.feiyunbangong.entity.ReadFormEntity;
import com.feirui.feiyunbangong.entity.ReadFormEntity.InforBean;
import com.feirui.feiyunbangong.fragment.dummy.DummyContent.DummyItem;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FormListFragment extends Fragment {

  private static final String FORM_TYPE = "formType";
  private OnListFragmentInteractionListener mListener;
  private int mFormType = 1;
  private FormAdapter mFormAdapter;
  private RecyclerView list;
  public FormListFragment() {
  }

  @SuppressWarnings("unused")
  public static FormListFragment newInstance(int type) {
    FormListFragment fragment = new FormListFragment();
    Bundle args = new Bundle();
    args.putInt(FORM_TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /**
   * 读取其自己的列表数据
   */
  private void loadMyForm() {
    String url = UrlTools.pcUrl + UrlTools.MY_FORM_LIST;
    RequestParams requestParams = new RequestParams();
    AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();
        ReadFormEntity readFormEntity = gson.fromJson(new String(responseBody), ReadFormEntity.class);
        mFormAdapter.setData(readFormEntity.getInfor());
      }
    });
  }
  /**
   * 读取其他人的列表数据
   */
  private void loadOtherForm() {
    String url = UrlTools.pcUrl + UrlTools.OTHER_FORM_LIST;
    RequestParams requestParams = new RequestParams();
    AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();
        ReadFormEntity readFormEntity = gson.fromJson(new String(responseBody), ReadFormEntity.class);
          mFormAdapter.setData(readFormEntity.getInfor());

//        Log.e("查看报表页面","其他人的列表数据-------------------" + read FormEntity.getInfor().toString());
      }
    });
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_item_list, container, false);
    initView(view);
    return view;
  }


  @Override
  public void onDetach() {
    super.onDetach();
   // mListener = null;
  }

  private void initView(View view) {
    final ArrayList<Integer> value = new ArrayList<>();
    mFormAdapter = new FormAdapter(new ArrayList<InforBean>(), getActivity());

    list = (RecyclerView) view.findViewById(R.id.list);
    list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    list.setAdapter(mFormAdapter);

    if (getArguments() != null) {
      mFormType = getArguments().getInt(FORM_TYPE);
      if (mFormType == ReadFormActivity.MY_FORM) {
        loadMyForm();
        //查看报表
        mFormAdapter.setItemClickListener(new FormAdapter.MyItemClickListener(){

          @Override
          public void onItemClick(View view, int position) {
            Intent intent = new Intent(getActivity(), CheckBaobiaoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            bundle.putInt("id",mFormAdapter.getId(position));
            Log.e("mBeanList的值", "onSuccess: "+position);
            bundle.putInt("type",mFormType);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        });
      }
      if (mFormType == ReadFormActivity.OTHER_FORM) {
        loadOtherForm();
        //查看报表
        mFormAdapter.setItemClickListener(new FormAdapter.MyItemClickListener(){

          @Override
          public void onItemClick(View view, int position) {
            view.getId();
            Intent intent = new Intent(getActivity(), CheckBaobiaoActivity.class);
            Bundle bundle = new Bundle();
//            value.add(mFormType);  //传递报表类型
//            value.add(position);   //传递报表所在的位置
            bundle.putInt("position",position);
            bundle.putInt("id",mFormAdapter.getId(position));
            Log.d("mBeanList的值", "onSuccess: "+position);
            bundle.putInt("type",mFormType);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        });
      }
    }

  }

  public interface OnListFragmentInteractionListener {

    // TODO: Update argument type and name
    void onListFragmentInteraction(DummyItem item);
  }
}
