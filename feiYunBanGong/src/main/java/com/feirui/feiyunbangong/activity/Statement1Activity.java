package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.testpic.Bimp;
import com.example.testpic.FileUtils;
import com.example.testpic.PhotoActivity;
import com.example.testpic.TestPicActivity;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.adapter.ShenPiRecAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.AddShenHe;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.ImageUtil;
import com.feirui.feiyunbangong.utils.PictureUtil;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;
import java.util.ArrayList;


/**
 * 报表-日报
 *
 * @author admina
 */
public class Statement1Activity extends BaseActivity implements OnClickListener {

  @PView
  public EditText et_1, et_2, et_3, et_beizhu;// 完成，未完成，明天计划,备注
  private SelectPicPopupWindow window;// 弹出图片选择框；

  AddShenHeAdapter mAdapter;
  public boolean shenpi =false;
  private Button mBtnCommit;
  private RecyclerView mRecShenPi;

  private GridView noScrollgridview;
  private GridAdapter adapter;
  private String path = "";// 拍照返回的图片路径；


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statement1);
    initView();
  }

  private HeaderViewRecyclerAdapter mShenpiFooterAdapter;
  public ShenPiRecAdapter mShenPiRecAdapter;
  private ImageView addShenPiRen;

  public void initView() {
    View footerShenpi = LayoutInflater.from(this).inflate(R.layout.add_pic_footer_shenpi, null);
    mBtnCommit = (Button) findViewById(R.id.btn_submit_dialy);

    mShenPiRecAdapter = new ShenPiRecAdapter(new ArrayList<ShenPiRen>());
    mShenpiFooterAdapter = new HeaderViewRecyclerAdapter(mShenPiRecAdapter);
    mShenpiFooterAdapter.addFooterView(footerShenpi);

    addShenPiRen = (ImageView) footerShenpi.findViewById(R.id.iv_add_shenpi_ren_footer);


    noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
    noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
    adapter = new GridAdapter(this);
    adapter.update();
    noScrollgridview.setAdapter(adapter);

    noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                              long arg3) {
        if (arg2 == Bimp.bmp.size()) {
          // 弹出选择图片的相册：
          //隐藏键盘
          Log.e("tag","zouleba-------------------");
          ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                  hideSoftInputFromWindow(Statement1Activity.this.getCurrentFocus().getWindowToken(),
                          InputMethodManager.HIDE_NOT_ALWAYS);

          window = PictureUtil.toastChoicePic(Statement1Activity.this,
                  Statement1Activity.this, noScrollgridview);
        } else {
          //查看图片
          Intent intent = new Intent(Statement1Activity.this,
                  PhotoActivity.class);
          intent.putExtra("ID", arg2);
          startActivity(intent);
        }
      }
    });

    addShenPiRen.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        shenpi =true;
        Intent intent = new Intent(Statement1Activity.this, ShenPiRenActivity.class);
        startActivityForResult(intent, 102);// 请求码；

      }
    });

    initTitle();
    setLeftDrawable(R.drawable.arrows_left);
    setCenterString("写日报");
    setRightVisibility(false);
    mAdapter = new AddShenHeAdapter(getLayoutInflater(),
        Statement1Activity.this);
    mRecShenPi = (RecyclerView) findViewById(R.id.rec_add_shenpiren);

      //给审批人RecyclerView设置layoutManager
    mRecShenPi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    mRecShenPi.setAdapter(mShenpiFooterAdapter);

    mBtnCommit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        commit();
      }
    });
  }


  public void commit() {
    if (TextUtils.isEmpty(et_1.getText().toString().trim())) {
      T.showShort(this, "请输入今日完成工作的内容");
      return;
    }
    if (TextUtils.isEmpty(et_3.getText().toString().trim())) {
      T.showShort(this, "请输入明日计划");
      return;
    }
    if(shenpi==false){
        T.showShort(this,"请选择审批人");
        return;
    }
    RequestParams params = new RequestParams();

    params.put("type_id", "1");
    params.put("option_one", et_1.getText().toString().trim());
    params.put("option_two", et_2.getText().toString().trim());
    params.put("option_three", et_3.getText().toString().trim());
    params.put("remarks", et_beizhu.getText().toString().trim());

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < Bimp.bmp.size(); i++) {
      Bitmap bt = Bimp.bmp.get(i);
      sb.append(BitmapToBase64.bitmapToBase64(bt));
      sb.append(",");
    }

    if (sb.length() > 0) {
      sb.delete(sb.length() - 1, sb.length());
    }
    params.put("picture", sb.toString());
    ArrayList<ShenPiRen> shenPiRenList = mShenPiRecAdapter.getDataSet();
    StringBuffer sb_id = new StringBuffer();
    for (int i = 0; i < shenPiRenList.size(); i++) {
      sb_id.append(shenPiRenList.get(i).getId() + ",");
    }
    params.put("form_check", sb_id.deleteCharAt(sb_id.length() - 1)
            .toString());
    Log.e("日报审批人---------------", "日报审批人---------------"  + params.toString());
    String url = UrlTools.url + UrlTools.FORM_REPORT;
    Utils.doPost(LoadingDialog.getInstance(Statement1Activity.this), Statement1Activity.this, url, params, new Utils.HttpCallBack() {
      @Override
      public void success(final JsonBean bean) {
        if ("200".equals(bean.getCode())) {
          runOnUiThread(new Runnable() {
            public void run() {
              T.showShort(Statement1Activity.this,
                      bean.getMsg());

              Statement1Activity.this.finish();
              overridePendingTransition(
                      R.anim.aty_zoomclosein,
                      R.anim.aty_zoomcloseout);
            }
          });

        } else {
          T.showShort(Statement1Activity.this,
                  bean.getMsg());
        }
      }

      @Override
      public void failure(String msg) {
        T.showShort(Statement1Activity.this, msg);
      }

      @Override
      public void finish() {

      }
    });
  }


  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case 102:
        ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
        if (spr.getId() == 0) {
          return;
        }
        mShenPiRecAdapter.addShenPiRen(spr);
        break;
      case 2:  //拍照的返回结果
        if (resultCode == Activity.RESULT_OK) {
          Bitmap bitmap = ImageUtil.getimage(path);
          if (Bimp.drr.size() < 9) {
            Bimp.drr.add(path);
          }
        }
        break;
    }
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.view_camero_rl_takephoto:  //拍照
        window.dismiss();
        path = PictureUtil.paiZhao(window, this);
        break;
      case R.id.view_camero_rl_selectphoto:  //选择照片
        window.dismiss();
        Intent intent = new Intent(Statement1Activity.this,
                TestPicActivity.class);
        startActivity(intent);
        break;
      case R.id.iv_01:
        mAdapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
        break;
      case R.id.iv_add:

        break;

    }
  }


  @SuppressLint("HandlerLeak")
  public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater; //
    private int selectedPosition = -1;//
    private boolean shape;

    public boolean isShape() {
      return shape;
    }

    public void setShape(boolean shape) {
      this.shape = shape;
    }

    public GridAdapter(Context context) {
      inflater = LayoutInflater.from(context);
    }

    public void update() {
      loading();
    }

    public int getCount() {
      return (Bimp.bmp.size() + 1);
    }

    public Object getItem(int arg0) {
      return null;
    }

    public long getItemId(int arg0) {
      return 0;
    }

    public void setSelectedPosition(int position) {
      selectedPosition = position;
    }

    public int getSelectedPosition() {
      return selectedPosition;
    }

    /**
     * ListView Item
     */
    public View getView(int position, View convertView, ViewGroup parent) {
      final int coord = position;
     ViewHolder holder = null;
      if (convertView == null) {
        convertView = inflater.inflate(R.layout.item_published_grida,
                parent, false);
        holder = new ViewHolder();
        holder.image = (ImageView) convertView
                .findViewById(R.id.item_grida_image);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }
      if (position == Bimp.bmp.size()) {
        holder.image.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.icon_addpic_unfocused));
        if (position == 9) {
          holder.image.setVisibility(View.GONE);
        }
      } else {
        try {
          holder.image.setImageBitmap(Bimp.bmp.get(position));
        } catch (Exception e) {
          Log.e("TAg", e.getMessage());
        }
      }
      return convertView;
    }

    public class ViewHolder {
      public ImageView image;
    }

    public void loading() {
      new Thread(new Runnable() {
        public void run() {
          try {
            for (int i = Bimp.bmp.size(); i < Bimp.drr.size(); i++) {
              String path = Bimp.drr.get(i);
              // Bitmap bm = Bimp.revitionImageSize(path);
              // 注意：必须得压缩，否则极有可能报oom;上传服务器也比较慢；
              Bitmap bm = ImageUtil.getimage(path);
              Bimp.bmp.add(bm);
              Bimp.max += 1;
            }
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                adapter.notifyDataSetChanged();
              }
            });
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    adapter.update();
  }

  @Override
  protected void onDestroy() {
    initBimp();
    Log.e("TAG", "清除！！！！");
    super.onDestroy();
  }

  private void initBimp() {
    FileUtils.deleteDir();
    Bimp.drr.clear();
    Bimp.bmp.removeAll(Bimp.bmp);
    Bimp.act_bool = true;
    Bimp.max = 0;
  }

}