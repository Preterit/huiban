package com.feirui.feiyunbangong.utils;

import android.util.Log;

import com.feirui.feiyunbangong.entity.JsonBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonUtils {

	public static JsonBean getMessage(String json) {
		JsonBean bean = new JsonBean();
		try {
			JSONObject jo = new JSONObject(json);

			bean.setCode(jo.getString("code"));
			bean.setMsg(jo.getString("msg"));

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			Log.e("tag", "getMessage: ---------------" + jo.getString("code") );
			if (jo.getString("code").equals("200")) {
				try {
					JSONObject job = jo.getJSONObject("infor");
					System.out.println("---------");
					HashMap<String, Object> map = new HashMap<String, Object>();
					Iterator<String> it = job.keys();
					while (it.hasNext()) {
						String key = it.next();
						map.put(key, job.get(key));
					}
					list.add(map);
					System.out.println("objject");
				} catch (Exception e) {
					JSONArray js = jo.getJSONArray("infor");
					for (int i = 0; i < js.length(); i++) {
						JSONObject job = js.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						Iterator<String> it = job.keys();
						while (it.hasNext()) {
							String key = it.next();
							map.put(key, job.get(key));
						}
						list.add(map);
					}

				}

			}
			bean.setInfor(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bean;
	}

}
