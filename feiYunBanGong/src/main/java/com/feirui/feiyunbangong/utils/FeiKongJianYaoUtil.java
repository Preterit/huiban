package com.feirui.feiyunbangong.utils;

import java.util.List;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

public class FeiKongJianYaoUtil {

	
	public static int jianYan(List<EditText> ets,List<TextView> tvs) {

		if(ets!=null && ets.size()>0){
			for (int i = 0; i < ets.size(); i++) {
				if (TextUtils.isEmpty(ets.get(i).getText().toString())) {
					return i;
				}
			}
		}
		
		if(tvs!=null && tvs.size()>0){
			for(int i=0;i<tvs.size();i++){
				if(TextUtils.isEmpty(tvs.get(i).getText().toString())){
					return 1000+i;
				}
			}
		}
		
		return -1;
	}

}
