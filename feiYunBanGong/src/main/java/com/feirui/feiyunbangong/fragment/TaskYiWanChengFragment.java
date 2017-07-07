package com.feirui.feiyunbangong.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feirui.feiyunbangong.R;

/**
 * 任务单-已完成
 */
public class TaskYiWanChengFragment extends Fragment {

    public TaskYiWanChengFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_yi_wan_cheng, container, false);
    }

}
