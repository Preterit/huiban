package com.feirui.feiyunbangong.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.utils.T;

/**
 * 我提交的
 */
public class CommitFragment extends Fragment {
    TextView text;
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_commit,null);
        return view;
    }

    public void showMessageFromActivity(String message){
        text= (TextView) view.findViewById(R.id.commit_text);
        text.setText(message);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        T.showShort(getContext(),"我提交的——————————");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
