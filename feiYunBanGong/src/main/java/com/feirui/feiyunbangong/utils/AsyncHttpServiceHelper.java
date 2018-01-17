package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.util.Log;

import com.feirui.feiyunbangong.Happlication;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.client.HttpClient;

public class AsyncHttpServiceHelper {
	public Context context;
	private static Context con = Happlication.getInstance();

	public static AsyncHttpClient client = new AsyncHttpClient();

	public static Gson gson = new Gson();

	/**
	 * GET方式请求 不带参数
	 * 
	 * @param url
	 * @param responseHandler
	 */
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {

		if (!NetUtils.isConnected(con)) {

			T.showShort(con, "当前设备无网络！");

			return;
		}
		client.setTimeout(30 * 1000);
		client.get(url, responseHandler);

	}

	// get Url带参数，获取json对象或者数组
	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		if (!NetUtils.isConnected(con)) {
			T.showShort(con, "当前设备无网络！");
			return;
		}
		client.setTimeout(30 * 1000);
		client.get(urlString, params, res);
	}

	/**
	 * POST方式请求 不带参数
	 *  @param url
	 * @param params   com.lidroid.xutils.http.RequestParams params,
     * @param responseHandler
     */
	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		if (!NetUtils.isConnected(con)) {
			T.showShort(con, "当前设备无网络！");

			return;
		}
		client.setTimeout(30 * 1000);
		client.post(url, responseHandler);
	}

	/**
	 * POST方式请求 带参数
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {

		if (!NetUtils.isConnected(con)) {
			T.showShort(con, "请检查网络设置！");
			return;
		}
		client.setTimeout(30 * 1000);
		client.post(url, params, responseHandler);
	}

	/**
	 * PUT方式请求 不带参数
	 * 
	 * @param url
	 * @param responseHandler
	 */
	public static void put(String url, AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(30 * 1000);
		client.put(url, responseHandler);
	}

	/**
	 * PUT方式请求 带参数
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void put(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(30 * 1000);
		client.put(url, params, responseHandler);
	}

	/**
	 * DELETE方式请求 不带参数
	 * 
	 * @param url
	 * @param
	 * @param responseHandler
	 */
	public static void delete(String url,
			AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(30 * 1000);
		client.delete(url, responseHandler);
	}
}
