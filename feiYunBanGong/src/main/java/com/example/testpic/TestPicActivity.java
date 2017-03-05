package com.example.testpic;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;

public class TestPicActivity extends Activity {
	// ArrayList<Entity> dataList;//阎剑娼电慭锻版祰閺佺增宓佸┃镒留壸掓銆?
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 阏奉亚鐣炬稊澶屾留褒佞铡ら嵛锟?
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	/**
	 * 壸掓繂颤愰崠镙ㄦ殶阉癸拷
	 */
	private void initData() {
		// /**
		// *
		// 鏉╂莹鍣烽叙灞惧灉娴状创浜ｇ抛榔у嚒缂佸系绮犵纯鎴犵捕阉存牞锟藉懏婀伴崷鎷屝挜弸镒偨娴滃棙罐熼徽显嗙礉阉碉拷娴犮儳娲块幒銉ユ躬鏉╂莹鍣峰Ο鈩富珯娴滐拷10娑擃亚鼎勬担鎾惰阌涘睑娲块幒銉柄鏉╂稑戣悰銊よ厬
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		dataList = helper.getImagesBucketList(false);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 壸掓繂颤愰崠鏉ew鐕椤枣娴?
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 閺嶈宓乸osition壸椤俭罐熼叙灞藉讲娴犮儴骞忓妤勭GridView阎ㄥ嫬鐡橵iew阎╁掼绮︾€规氨娈戠€圭侃缍嬬猾浼櫕礉阎掕泛镇楅韘瑙勫祦鐎瑰啰娈戚sSelected阎椫埖
				 * 锟戒緤绱?閺斋儱戣介弬颛熸Ц壸泛附妯夌粈娲拷澶夎厬閺佸牊鐏夐妴锟?阏峰厖绨柅澶夎厬閺佸牊鐏夐惃鍕潐壸掓瑱绱涩粳瀣桨褒佞铡ら嵛銊ф留娴狅絿鐖沧粳颛濈窗閺埚显╅弰锟?
				 */
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 褒氨镦￠柅鍌炲帐壸ｎ炼绱濈紒鎴濈暰阎ㄥ嫭罐熼徽颜煎絺阎三喍绨￠弨鐕板绑阌涘苯绨茶ぐ鎾冲熙閺傛妈颤嬮崶锟?
				 */
				// adapter.notifyDataSetChanged();
				Intent intent = new Intent(TestPicActivity.this,
						ImageGridActivity.class);
				intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}
		});

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
}
