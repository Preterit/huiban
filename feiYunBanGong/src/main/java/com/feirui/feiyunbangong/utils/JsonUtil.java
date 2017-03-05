package com.feirui.feiyunbangong.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.feirui.feiyunbangong.entity.JsonB;

public class JsonUtil {

	public static JsonB getMessage(String json) {
		JsonB bean = new JsonB();
		try {
			JSONObject jo = new JSONObject(json);

			bean.setCode(jo.getString("code"));
			bean.setMsg(jo.getString("msg"));

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			if (jo.getString("code").equals("200")) {
				try {
					JSONObject job = jo.getJSONObject("data");
					System.out.println("---------");
					HashMap<String, Object> map = new HashMap<String, Object>();
					Iterator<String> it = job.keys();
					while (it.hasNext()) {
						String key = it.next();
						map.put(key, job.getString(key));
					}
					list.add(map);
					System.out.println("objject");
				} catch (Exception e) {
					JSONArray js = jo.getJSONArray("data");
					for (int i = 0; i < js.length(); i++) {
						JSONObject job = js.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						Iterator<String> it = job.keys();
						while (it.hasNext()) {
							String key = it.next();
							map.put(key, job.getString(key));
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
	};

}
