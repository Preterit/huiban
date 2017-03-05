package com.feirui.feiyunbangong.utils;

import com.loopj.android.http.RequestParams;

public class HttpRequestParamsUtil {
	
	
	public static RequestParams getParams(String stringAPI) {
		
		RequestParams params = new RequestParams();
		
		params.put("sign", Encrypt.getEnc(stringAPI));
		return params;
	}
	

}
