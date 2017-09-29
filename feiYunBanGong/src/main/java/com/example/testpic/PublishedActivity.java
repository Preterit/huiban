package com.example.testpic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.BitmapFileSetting;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.ButtonUtils;
import com.feirui.feiyunbangong.utils.PictureUtil;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 发布动态：
 */
public class PublishedActivity extends Activity implements OnClickListener {

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private TextView activity_selectimg_send, tv_content;
    private SelectPicPopupWindow window;// 弹出图片选择框；
    private String path = "";// 拍照返回的图片路径；
    private ImageView iv_back;
    private String team_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectimg);
        Init();
        setListener();
    }

    private void setListener() {
        iv_back.setOnClickListener(this);
    }

    public void Init() {

        team_id = getIntent().getStringExtra("team_id");
        Log.e("TAG", team_id + "发布team_id");

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_content = (TextView) findViewById(R.id.tv_content);

        if (team_id != null) {
            tv_content.setHint("发布团队圈动态......");
        } else {
            tv_content.setHint("发布朋友圈动态......");
        }

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);

        noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    // 弹出选择图片：
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(PublishedActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    window = PictureUtil.toastChoicePic(PublishedActivity.this,
                            PublishedActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(PublishedActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

        activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);

        activity_selectimg_send.setOnClickListener(this);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        for (int i = Bimp.bmp.size(); i < Bimp.drr.size(); i++) {
                            final String path = Bimp.drr.get(i);
//                             注意：必须得压缩，否则极有可能报oom;上传服务器也比较慢；
//                            Bitmap bm = ImageUtil.getimage(path);
//                            Bimp.bmp.add(bm);
//                            Bimp.max += 1;

                            File file = new File(path);
                            //鲁班压缩
                            Luban.with(PublishedActivity.this)
                                    .load(file)
                                    .setCompressListener(new OnCompressListener() {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            // TODO 压缩成功后调用，返回压缩后的图片文件
                                            Bitmap bm = null;
                                            try {
                                                bm = BitmapFileSetting.decodeFile(file);
                                                Bimp.bmp.add(bm);
                                                Bimp.max += 1;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    }).launch();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("path", "onActivityResult:-------------------------- " + path );
//                    Bitmap bitmap = ImageUtil.getimage(path);
                    File file = new File(path);
                    //鲁班压缩
                    Luban.with(PublishedActivity.this)
                            .load(file)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    // TODO 压缩成功后调用，返回压缩后的图片文件
                                    Bitmap bm = null;
                                    try {
                                        bm = BitmapFileSetting.decodeFile(file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            }).launch();
                    if (Bimp.drr.size() < 9) {
                        Bimp.drr.add(path);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 拍照；
            case R.id.view_camero_rl_takephoto:
                window.dismiss();
                path = PictureUtil.paiZhao(window, this);
                Log.e("path", "onActivityResult:-------------------------- " + path );
                break;
            // 从相册选择：
            case R.id.view_camero_rl_selectphoto:
                window.dismiss();
                Intent intent = new Intent(PublishedActivity.this,
                        TestPicActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case  R.id.activity_selectimg_send:  //发送
                if (ButtonUtils.isFastDoubleClick(R.id.activity_selectimg_send)){//防止多次点击
                    Toast.makeText(this,"请勿重复发布~",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (TextUtils.isEmpty(tv_content.getText().toString().trim())) {
                        Toast.makeText(PublishedActivity.this, "请输入内容！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < Bimp.bmp.size(); i++) {
                        Bitmap bt = Bimp.bmp.get(i);
                        Log.e("tag", "Bimp.bmp.size():-------------------------- " + bt);
                        sb.append(BitmapToBase64.bitmapToBase64(bt));
                        sb.append(",");
                    }

                    if (sb.length() > 0) {
                        sb.delete(sb.length() - 1, sb.length());
                    }

                    String url = UrlTools.url + UrlTools.FABIAO_WENZHANG;
                    RequestParams params = new RequestParams();
                    params.put("picture", sb.toString());
                    params.put("text", tv_content.getText().toString());
                    params.put("type", "公开");
                    params.put("team_id", team_id);

                    Utils.doPost(LoadingDialog.getInstance(PublishedActivity.this),
                            PublishedActivity.this, url, params,
                            new HttpCallBack() {

                                @Override
                                public void success(JsonBean bean) {
                                    Toast.makeText(PublishedActivity.this, "发表成功！",
                                            Toast.LENGTH_SHORT).show();

                                    PublishedActivity.this.finish();
                                }

                                @Override
                                public void failure(String msg) {
                                    Log.e("tag", "failure:-------------------------- " + msg);
                                    Toast.makeText(PublishedActivity.this, msg, Toast.LENGTH_SHORT)
                                            .show();
                                }

                                @Override
                                public void finish() {
                                    // TODO Auto-generated method stub

                                }
                            });

                }
                break;

         }

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
