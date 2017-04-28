package com.feirui.feiyunbangong.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feirui.feiyunbangong.R;

/**
 * Created by Administrator on 2017/4/27.
 */

public class ShenPiFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.ll_item_daishenqi, container, false);
//        mListView = (ListView) v.findViewById(R.id.receiveTaskList);
//        receiveTaskList();

        return v;

    }
}
