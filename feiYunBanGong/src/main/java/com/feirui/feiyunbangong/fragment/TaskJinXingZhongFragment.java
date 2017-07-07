package com.feirui.feiyunbangong.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feirui.feiyunbangong.R;

/**
 * 任务单-进行中
 */
public class TaskJinXingZhongFragment extends Fragment {


    public TaskJinXingZhongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jin_xing_zhong, container, false);
    }

}
