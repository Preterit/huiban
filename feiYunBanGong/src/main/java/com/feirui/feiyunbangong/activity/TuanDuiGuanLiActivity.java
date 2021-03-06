package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.tribe.YWTribeCreationParam;
import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.tribe.TribeConstants;
import com.feirui.feiyunbangong.activity.tribe.TribeInfoActivity;
import com.feirui.feiyunbangong.adapter.GuanLiChengYuanAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog.AlertCallBack;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * 团队管理：
 * 
 * @author feirui1
 *
 */
public class TuanDuiGuanLiActivity extends BaseActivity implements
		OnClickListener {

	private TuanDui td;
	private GuanLiChengYuanAdapter adapter;
	private List<TuanDuiChengYuan> tdcys;
	private ListView lv_chengyuan;
	private int pos;// 点击的item位置；
	private Button bt_dissolve_team,bt_set_team;// 解散团队按钮；创建团聊
	//群聊的
	private YWIMKit mIMKit;
	YWTribe tribe;
	private IYWTribeService mTribeService;
	private long mTribeId; //团聊的ID
	private String mTbID;
	private TuanDuiChengYuan mTuanDui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuan_dui_guan_li);
		mIMKit = AppStore.mIMKit;
		mTribeService = mIMKit.getTribeService();  //获取群管理器
		initView();
		setListener();
		setListView();
		initData();
	}

	private void setListView() {
		lv_chengyuan.setAdapter(adapter);
	}

	private void initData() {

		String url = UrlTools.url + UrlTools.CHENGYUAN_WU_GUANLIYUAN;
		RequestParams params = new RequestParams();
		params.put("team_id", td.getTid());
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						JsonBean bean = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(bean.getCode())) {
							Message msg = handler.obtainMessage(0);
							msg.obj = bean;
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(1);
							msg.obj = bean;
							handler.sendMessage(msg);
						}
						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);
					}

				});
		//获取团队群Id
		getTuanLiaoId();
	}

	private void setListener() {
		bt_dissolve_team.setOnClickListener(this);
        bt_set_team.setOnClickListener(this);
		lv_chengyuan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				parent.getChildAt(position).setBackgroundColor(Color.WHITE);
			}
		});
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("团队管理");
		setRightVisibility(false);
		Intent intent = getIntent();
		td = (TuanDui) intent.getSerializableExtra("td");
		adapter = new GuanLiChengYuanAdapter(getLayoutInflater(), this);
		tdcys = new ArrayList<>();
		lv_chengyuan = (ListView) findViewById(R.id.lv_chengyuan);
		bt_dissolve_team = (Button) findViewById(R.id.bt_dissolve_team);
		bt_set_team = (Button) findViewById(R.id.bt_set_team);

	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.iv_set_manager:

			MyAleartDialog dialog = new MyAleartDialog("移交团长", "确定移交团长吗？",
					this, new AlertCallBack() {
						@Override
						public void onOK() {
							setManager(v);
						}

						@Override
						public void onCancel() {
						}
					});

			dialog.show();

			break;
		case R.id.bt_dissolve_team:
			MyAleartDialog dissolve_dialog = new MyAleartDialog("解散团队",
					"确定解散团队吗？", this, new AlertCallBack() {
						@Override
						public void onOK() {
							dissolve();
						}

						@Override
						public void onCancel() {
						}
					});
			dissolve_dialog.show();

			break;
		case R.id.bt_set_team: //创建群聊的
			createTribe(YWTribeType.CHATTING_GROUP);
			break;
		default:
			final MyAleartDialog delete_dialog = new MyAleartDialog("删除团队成员",
					"确定删除团队该成员吗？", this, new AlertCallBack() {
						@Override
						public void onOK() {
							delete(v);
						}

						@Override
						public void onCancel() {
						}
					});
			delete_dialog.show();
			break;
		}
	}

	/**
	 * 创建团聊
	 */
	private void createTribe(final YWTribeType type) {

		YWTribeCreationParam tribeCreationParam = new YWTribeCreationParam();
		tribeCreationParam.setTribeName(td.getName());
		tribeCreationParam.setTribeType(type);
		if (type == YWTribeType.CHATTING_GROUP){
			//讨论组需要指定用户
			final List<String> userList = new ArrayList<String>();
			final YWIMCore core = mIMKit.getIMCore();

			userList.add(core.getLoginUserId());// 当前登录的用户ID，这个必须要传
			Log.e("tag","获取到的群成员--------");
			for (int i = 0;i < tdcys.size();i++){
				userList.add(tdcys.get(i).getPhone());
				Log.e("tag","获取到的群成员--------"+tdcys.get(i).getPhone());
			}

			tribeCreationParam.setUsers(userList);
		}

		mTribeService.createTribe(new MyCallback() {
			@Override
			public void onSuccess(Object... result) {
				// 返回值为刚刚成功创建的群
				tribe = (YWTribe) result[0];
				tribe.getTribeId();// 群ID，用于唯一标识一个群
				//        跳转到群名片
				Intent intent = new Intent(TuanDuiGuanLiActivity.this, TribeInfoActivity.class);
				intent.putExtra("postId","postId");
				intent.putExtra("code",100);
				intent.putExtra("id",td.getTid());
				intent.putExtra(TribeConstants.TRIBE_ID, tribe.getTribeId());
				startActivity(intent);
				finish();
			}
		},tribeCreationParam);

	}

	/**
	 * 请求回调
	 *
	 * @author zhaoxu
	 *
	 */
	private static abstract class MyCallback implements IWxCallback {

		@Override
		public void onError(int arg0, String arg1) {
			YWLog.e("TribeSampleHelper", "code=" + arg0 + " errInfo=" + arg1);
		}

		@Override
		public void onProgress(int arg0) {

		}
	}

	/**
	 * 解散团队之后解散团队所在的团聊
	 */
	private void dissolve() {
		String url = UrlTools.url + UrlTools.DISSOLVE_TEAM;
		RequestParams params = new RequestParams();
		params.put("team_id", td.getTid());

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						//移除本地缓存的团队
						DataSupport.deleteAll(TuanDui.class, "tid = ?", td.getTid());
						T.showShort(TuanDuiGuanLiActivity.this, "团队已解散！");
						TuanDuiGuanLiActivity.this.finish();
						for (int i = 0; i < AppStore.acts.size(); i++) {
							AppStore.acts.get(i).finish();
						}
						if (mTbID!=null){//!"".equals(mTbID)
							jieSanQun();
						}
					}

					@Override
					public void failure(String msg) {
						T.showShort(TuanDuiGuanLiActivity.this, msg);
					}

					@Override
					public void finish() {

					}
				});

	}

	/**
	 * 解散团聊
	 */
	public void jieSanQun(){
		mTribeService.disbandTribe(new IWxCallback() {
			@Override
			public void onSuccess(Object... objects) {

			}

			@Override
			public void onError(int i, String s) {
                T.showShort(TuanDuiGuanLiActivity.this,"解散团聊失败--" + s);
			}

			@Override
			public void onProgress(int i) {

			}
		},Long.parseLong(mTbID));

	}

	// 移交管理员：
	private void setManager(final View v) {

		TuanDuiChengYuan tdcy = (TuanDuiChengYuan) v.getTag();
		String url = UrlTools.url + UrlTools.SET_TEEM_MANAGER;
		RequestParams params = new RequestParams();

		params.put("team_id", td.getTid());
		params.put("staff_id", tdcy.getStaff_id());
		params.put("oldstaff_id", td.getGuanli_id());

		Log.e("TAG", params.toString());

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						Intent intent = new Intent(TuanDuiGuanLiActivity.this,
								TuanDuiJiaActivity.class);
						setResult(100, intent);
						if (mTbID!=null){//!"".equals(mTbID)
							jieSanQun();
						}
						TuanDuiGuanLiActivity.this.finish();
						Toast.makeText(TuanDuiGuanLiActivity.this, "移交管理员成功", 0)
								.show();
					}

					@Override
					public void failure(String msg) {
						T.showShort(TuanDuiGuanLiActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
					}
				});

		//转移团长后群聊解散  需要新团长重新建群
		String url2 = UrlTools.url + UrlTools.JIESAN_QUN;
		RequestParams params2 = new RequestParams();
		params2.put("id",td.getTid());
		Log.e("yag", "团聊的群解散群失败！--td.getTid()--------" + td.getTid());
		Utils.doPost(LoadingDialog.getInstance(this), this, url2, params2, new Utils.HttpCallBack() {
			@Override
			public void success(JsonBean bean) {
				if ("200".equals(bean.getCode())){
					Log.e("yag", "团聊的群解散群成功！");
				}
			}

			@Override
			public void failure(String msg) {
				Log.e("yag", "团聊的群解散群失败！----------" + msg);
			}

			@Override
			public void finish() {

			}
		});

	}

	private void delete(View v) {

		final TuanDuiChengYuan  tdcy = (TuanDuiChengYuan) v.getTag();

		String url = UrlTools.url + UrlTools.DELETE_TUANDUI_CHENGYUAN;
		RequestParams params = new RequestParams();
		params.put("id", tdcy.getCId());
		mTuanDui = tdcy;
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						JsonBean bean = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(bean.getCode())) {
							Message msg = handler.obtainMessage(3);
							msg.obj = tdcy;
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(4);
							msg.obj = bean;
							handler.sendMessage(msg);
						}
						;
						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Message msg = handler.obtainMessage(5);
						handler.sendMessage(msg);
						super.onFailure(arg0, arg1, arg2, arg3);
					}
				});

	}

	/**
	 * 团队团聊Id
	 */
	public void getTuanLiaoId(){
		String url = UrlTools.url + UrlTools.GET_TUANLIAOID;
		RequestParams params = new RequestParams();
		params.put("team_id",td.getTid());
		AsyncHttpServiceHelper.post(url,params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				JsonBean bean = JsonUtils.getMessage(new String(arg2));
				if ("200".equals(bean.getCode()) ) {
					if ((bean.getInfor().get(0).get("team_talk") + "").equals("")){
						Log.e("chengyuan", "handleMessage: -----------------" + bean.getInfor().get(0).get("team_talk") );
						bt_set_team.setVisibility(View.VISIBLE);
					}else {
						mTbID = bean.getInfor().get(0).get("team_talk") + "" ;
						bt_set_team.setVisibility(View.GONE);
					}

				} else {
					T.showShort(TuanDuiGuanLiActivity.this,bean.getMsg());
				}
				super.onSuccess(arg0, arg1, arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
								  Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
			}
		});

	}
	/**
	 * 团队聊天自动删除人
	 * @param
	 * @param item
	 */
	public void  deleteTuanLiao(final TuanDuiChengYuan item){
		mTribeId = Long.parseLong(mTbID);
		Log.e("团队成员自动删除人", "mTribeId: -----------------" + mTribeId + "----------" + mTuanDui.getPhone());
		Log.e("团队成员自动删除人", "td: -----------------" + td );

		IYWContact iywContact =  YWContactFactory.createAPPContact(mTuanDui.getPhone(), Happlication.APP_KEY);
		Log.e("团队成员自动删除人", "iywContact: -----------------" + iywContact.toString() + "------" + mTribeService);
        mTribeService.expelMember(mTribeId, iywContact, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                Log.e("chengyuan", "iywContact: -------移除团聊----------");
				// 团队删除成员发出的广播：
				Intent i = new Intent();
				i.setAction(Constant.ON_RECEIVE_NEW_MEMBER_DELETE);
				i.putExtra("guangbo","删除");
				i.putExtra("id", item.getStaff_id());
				i.putExtra("teamId",td.getTid());
				Log.e("tdcys", "handleMessage: ==============" + item.getStaff_id() + "---" + td.getTid() );
				sendBroadcast(i);
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i) {

            }
        });
//
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				Log.e("TAG", "成功！！！！！！！！！！！！！");
				JsonBean bean = (JsonBean) msg.obj;
				ArrayList<HashMap<String, Object>> infor = bean.getInfor();
				for (int i = 0; i < infor.size(); i++) {
					HashMap<String, Object> hm = infor.get(i);
					TuanDuiChengYuan tdcy = new TuanDuiChengYuan(
							String.valueOf(hm.get("id")), String.valueOf(hm
									.get("staff_name")), String.valueOf(hm
									.get("staff_head")),String.valueOf(hm.get("staff_mobile")));
					tdcy.setStaff_id(hm.get("staff_id") + "");
					tdcys.add(tdcy);
					Log.e("tag", "handleMessage:----------------- " + tdcys.get(0));
				}
				if (tdcys != null && tdcys.size() != 0) {
					adapter.add(tdcys);
				}
				break;
			case 1:
				JsonBean bean02 = (JsonBean) msg.obj;
				Toast.makeText(TuanDuiGuanLiActivity.this, bean02.getMsg(), 0)
						.show();
				break;
			case 2:

				break;
			case 3:
				// 删除成功！
				TuanDuiChengYuan item = (TuanDuiChengYuan) msg.obj;
				adapter.reduce(item);
				//删除团聊成员
				if (!TextUtils.isEmpty(mTbID)){
					deleteTuanLiao(item);
				}
				break;
			case 4:
				// 删除失败：
				Toast.makeText(TuanDuiGuanLiActivity.this,
						((JsonBean) msg.obj).getMsg(), 0).show();
				break;
			case 5:
				// 删除失败：
				Toast.makeText(TuanDuiGuanLiActivity.this, "删除失败！", 0).show();
				break;
			case 6:
				JsonBean bean6 = (JsonBean) msg.obj;
				Log.e("chengyuan", "JsonBean: -----------------" + bean6.getInfor().get(0).get("team_talk") );
				break;
			}

		};
	};


}
