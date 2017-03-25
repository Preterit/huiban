package com.feirui.feiyunbangong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.feirui.feiyunbangong.R;
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
import java.util.ArrayList;
import org.apache.http.Header;

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

    if (getArguments() != null) {
      mFormType = getArguments().getInt(FORM_TYPE);
      if (mFormType == ReadFormActivity.MY_FORM) {
        loadMyForm();
      }
      if (mFormType == ReadFormActivity.OTHER_FORM) {
        loadOtherForm();
      }
    }
  }

  private void loadMyForm() {
    String url = UrlTools.pcUrl + UrlTools.MY_FORM_LIST;
    RequestParams requestParams = new RequestParams();
    AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();
        ReadFormEntity readFormEntity = gson
            .fromJson(new String(responseBody), ReadFormEntity.class);
        mFormAdapter.setData(readFormEntity.getInfor());
      }
    });
  }

  private void loadOtherForm() {
    String url = UrlTools.pcUrl + UrlTools.OTHER_FORM_LIST;
    RequestParams requestParams = new RequestParams();
    AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Gson gson = new Gson();
        ReadFormEntity readFormEntity = gson
            .fromJson(new String(responseBody), ReadFormEntity.class);
        mFormAdapter.setData(readFormEntity.getInfor());
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
    mListener = null;
  }

  private void initView(View view) {
    mFormAdapter = new FormAdapter(new ArrayList<InforBean>(), getContext());

    list = (RecyclerView) view.findViewById(R.id.list);
    list.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    list.setAdapter(mFormAdapter);
  }

  public interface OnListFragmentInteractionListener {

    // TODO: Update argument type and name
    void onListFragmentInteraction(DummyItem item);
  }
}
