package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.Province;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class AddShopActivity extends BaseActivity {

	private Spinner mSpPro;
	private Spinner mSpCity;
	private Button mBtnCommit;
	private EditText mEtShopName;
	private EditText mEtShopDesc;
	private EditText weidianUrl;
	private Button weidianBtn;
	private String Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_shop);
		//新添加的
		weidianUrl=(EditText)findViewById(R.id.weidianUrl);   //微店的地址
		weidianBtn=(Button)findViewById(R.id.weidianBtn);     //按钮

//		Url=weidianUrl.getText().toString().trim();  //获取微店地址并去掉空格


		mSpPro = (Spinner) findViewById(R.id.spPro);
		mSpCity = (Spinner) findViewById(R.id.spCity);
		mBtnCommit = (Button) findViewById(R.id.add_shop_commit_btn);
		mEtShopDesc = (EditText) findViewById(R.id.et_shop_description);
		mEtShopName = (EditText) findViewById(R.id.et_shop_name);

		mBtnCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(mEtShopName.getText().toString().trim())) {
					T.showShort(AddShopActivity.this, "请输入店铺名");
					return;
				}
				if (TextUtils.isEmpty(mEtShopDesc.getText().toString().trim())) {
					T.showShort(AddShopActivity.this, "请输入店铺描述");
					return;
				}
				commitData();
			}
		});
		initView();
		setListeners();
		initSpinner();
	}

	private void setListeners() {
		weidianBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Url=weidianUrl.getText().toString().trim();  //获取微店地址并去掉空格
				Intent intent=new Intent();
				intent.putExtra("uri",Url);
				intent.putExtra("TAG","1");
				intent.setClass(AddShopActivity.this, WebViewActivity.class);

				if(Url.isEmpty()){
					Toast.makeText(AddShopActivity.this,"您还没有输入微店地址",Toast.LENGTH_SHORT).show();
				}else{
					startActivity(intent);
					addpostWD();
					finish();
				}

			}
		});

	}

	private void addpostWD() {
		//Toast.makeText(getApplication(),"请求成功",Toast.LENGTH_SHORT).show();
		String postUrl="http://123.57.45.74/feiybg/public/index.php/api/store/edit_info";

		RequestParams requestParams = new RequestParams();
		requestParams.put("store_url",Url);
		Utils.doPost(LoadingDialog.getInstance(this), this, postUrl, requestParams, new HttpCallBack() {
			@Override
			public void success(JsonBean bean) {
            Toast.makeText(getApplication(),"创建微店成功",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void failure(String msg) {
				//Toast.makeText(getApplication(),msg.toString(),Toast.LENGTH_SHORT).show();
			}

			@Override
			public void finish() {

			}
		});

	}

	private void commitData() {
		// TODO Auto-generated method stub
		String desc = mEtShopDesc.getText().toString().trim();
		String name = mEtShopName.getText().toString().trim();
		String province = (String) mSpPro.getSelectedItem();
		String city = (String) mSpCity.getSelectedItem();

		String url = UrlTools.url + UrlTools.XIAODIAN_ADD_SHOP;
		RequestParams params = new RequestParams();
		params.put("store_name", name);
		params.put("content", desc);
		params.put("provice_id", province);
		params.put("city_id", city);
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new HttpCallBack() {

			@Override
			public void success(JsonBean bean) {
				// TODO Auto-generated method stub
				if (bean.getCode().equals("200")) {
					T.showShort(AddShopActivity.this, "小店创建成功");
					finish();
				}

			}

			@Override
			public void finish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void failure(String msg) {
				// TODO Auto-generated method stub
				T.showShort(AddShopActivity.this, "创建失败");
			}
		});

	}

	private void initView() {
		// TODO Auto-generated method stub
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("我的小店");
		setRightVisibility(false);

	}

	private void initSpinner() {
		// TODO Auto-generated method stub

		final ArrayList<Province> provinces = pullParser();
		ArrayList<String> provinceName = new ArrayList<>();
		for (Province province : provinces) {
			provinceName.add(province.getProvinceName());
		}
		Log.e("orz", provinceName.toString());

		ArrayAdapter<String> mProvinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
				provinceName);

		mSpPro.setAdapter(mProvinceAdapter);
		mSpPro.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				Spinner spinner = (Spinner) parent;
				String provinceName = (String) spinner.getItemAtPosition(position);

				ArrayList<String> cityList = new ArrayList<>();
				for (Province province : provinces) {
					if (province.getProvinceName().equals(provinceName)) {
						cityList = province.getCityList();
						break;
					}
				}

				ArrayAdapter<String> spCityAdapter = new ArrayAdapter<>(AddShopActivity.this,
						android.R.layout.simple_spinner_item, cityList);

				mSpCity.setAdapter(spCityAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	private ArrayList<Province> pullParser() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Resources res = getResources();
		XmlResourceParser xrp = res.getXml(R.xml.province_city);

		ArrayList<Province> provinces = new ArrayList<>();

		Province province = null;
		ArrayList<String> cityList = null;
		try {
			int eventCode = xrp.getEventType();

			while (eventCode != XmlResourceParser.END_DOCUMENT) {
				switch (eventCode) {

					case XmlResourceParser.START_DOCUMENT:// start read xml
						break;
					case XmlResourceParser.START_TAG:// start read tag

						if (xrp.getName().equals("province")) {
							province = new Province();
							cityList = new ArrayList<String>();

							String provinceName = xrp.getAttributeValue(null, "name");
							province.setProvinceName(provinceName);
						}
						if (xrp.getName().equals("item")) {

							cityList.add(xrp.nextText());
						}
						break;
					case XmlResourceParser.END_TAG:

						if (xrp.getName().equals("province")) {
							provinces.add(province);
							province.setCityList(cityList);
						}

						break;
				}
				eventCode = xrp.next();
			}
		} catch (XmlPullParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return provinces;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_shop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
