package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testpic.PublishedActivity;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ListItemAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.ItemEntity;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.PingLunItem;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.RefreshLayout;
import com.feirui.feiyunbangong.view.RefreshLayout.OnLoadListener;
import com.loopj.android.http.RequestParams;
import com.xw.repo.refresh.PullListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.feirui.feiyunbangong.R.id.swipe_container;

/**
 * 工作圈
 *
 * @author Lesgod
 */
public class WorkCircleActivity extends BaseActivity implements
        OnRefreshListener, OnLoadListener, OnTouchListener, OnItemClickListener {

    private RefreshLayout swipeLayout;

    private ArrayList<ItemEntity> itemEntities = new ArrayList<>();//团队圈list
    private PullListView lv_work;
    private View header;

    private View m_listViewFooter;

    private int position = 1;// 当前页数；
    private ListItemAdapter adapter;

    private LinearLayout ll_pinglun_input,rv_work;// 评论布局
    private EditText et_pinglun;
    private Button bt_send;// 发送评论；

    private String team_id;// 团队id,如果未空则表示是朋友圈，不为空则表示团队圈；
    private TextView tvLinked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_circle);
        initView();
        setListener();
        setAdapter();
    }

    private void setAdapter() {
        lv_work.setAdapter(adapter);
    }

    private AlertDialog mAlertDialog;

//    Intent intent=new Intent();
//                intent.putExtra("uri",urll);
//                intent.putExtra("TAG","1");
//                intent.setClass(getApplicationContext(),WebViewActivity.class);
//    startActivity(intent);

    @SuppressLint("InflateParams")
    private void initView() {
        //
//        tvLinked = (TextView) findViewById(R.id.tv_content_work);
//        tvLinked.setAutoLinkMask(Linkify.ALL);
//        CharSequence content = tvLinked.getText();
//        Log.e("朋友圈textview网址", "textView: " + content);
//        SpannableStringBuilder builder = SpannableStringBuilder.valueOf(content);
//        URLSpan[] spans = builder.getSpans(0, content.length(), URLSpan.class);
//        if (spans != null && spans.length > 0) {
//            int start = 0;
//            int end = 0;
//            for (URLSpan span : spans) {
//                start = builder.getSpanStart(span);
//                end = builder.getSpanEnd(span);
//                // to replace each link span with customized ClickableSpan
//                builder.removeSpan(span);
////                builder.setSpan(new MyURLSpan(WorkCircleActivity.this, span.getURL().toString()),
////                        start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }
//            }


            Intent intent = getIntent();
            team_id = intent.getStringExtra("team_id");
            Log.e("朋友圈", "team_id" + team_id);

            initTitle();
            setLeftDrawable(R.drawable.arrows_left);
            if (team_id != null) {
                setCenterString("团队圈");
            } else {
                setCenterString("朋友圈");
            }
            setRightDrawable(R.drawable.jia);
            rightll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(WorkCircleActivity.this,
                                PublishedActivity.class);
                        intent.putExtra("team_id", team_id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.aty_zoomin,
                                R.anim.aty_zoomout);
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                }
            });

            swipeLayout = (RefreshLayout) findViewById(swipe_container);
            header = this.getLayoutInflater()
                    .inflate(R.layout.ll_header_work, null);
            m_listViewFooter = LayoutInflater.from(this).inflate(
                    R.layout.listview_foot, null, false);

            lv_work = (PullListView) findViewById(R.id.lv_work);

            lv_work.addFooterView(m_listViewFooter);

            lv_work.addHeaderView(header);


            ll_pinglun_input = (LinearLayout) findViewById(R.id.ll_pinglun_input);
            et_pinglun = (EditText) findViewById(R.id.et_pinglun);
            bt_send = (Button) findViewById(R.id.bt_send);
            adapter = new ListItemAdapter(this, this, itemEntities);


            lv_work.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });


            adapter.setMyLongClickListener(new ListItemAdapter.MyLongClickListener() {
                @Override
                public void onLongClick(final ItemEntity itemEntity, final int position) {
                    if (itemEntity.getStaffId().equals(AppStore.myuser.getId())) {
                        mAlertDialog = new AlertDialog.Builder(WorkCircleActivity.this)
                                .setMessage("删除这条朋友圈吗?")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        delete(itemEntity.getId(), position);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create();
                        mAlertDialog.show();
                    }
                }
            });
            rv_work = (LinearLayout) findViewById(R.id.rl_work);

    }


    /**
     * @param id
     */
    private void delete(String id, final int postition) {
        String url = UrlTools.url + UrlTools.DELETE_CIRCLE;
        RequestParams params = new RequestParams();
        params.put("id", id);

        Utils.doPost(LoadingDialog.getInstance(WorkCircleActivity.this), WorkCircleActivity.this, url, params, new HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                if (bean.getCode().equals("200")) {
                    T.showShort(WorkCircleActivity.this, "删除成功");

                    getdata(1, 0);
                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(WorkCircleActivity.this, msg);
            }

            @Override
            public void finish() {

            }
        });
    }

    // 点赞：
    private void dianzan(int position) {
        String url = UrlTools.url + UrlTools.WORK_QUAN_ZAN;
        RequestParams params = new RequestParams();
        final ItemEntity item = (ItemEntity) adapter.getItem(position);

        params.put("id", item.getId());

        Utils.doPost(null, WorkCircleActivity.this, url, params,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        Log.e("orz", "success: " + bean.toString());
                        if (bean.getMsg().equals("点赞成功")) {
                            item.setZan(true);
                            if (item.getZan_name().equals("")) {
                                item.setZan_name(item.getZan_name().concat(AppStore.myuser.getName()));
                            } else {
                                item.setZan_name(item.getZan_name().concat("," + AppStore.myuser.getName()));
                            }
                        } else {
                            item.setZan(false);
                            if (item.getZan_name().equals(AppStore.myuser.getName())) {
                                //没有人点赞,自己取消赞
                                item.setZan_name("");
                            } else {
                                int lastIndexOf = item.getZan_name().lastIndexOf(",");
                                if (lastIndexOf != -1) {
                                    item.setZan_name(item.getZan_name().substring(lastIndexOf, item.getZan_name().length()));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(WorkCircleActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    // 控制点赞和评论的显示与隐藏：
    private void setShowHide(View v) {
        ItemEntity itemEntity = itemEntities.get((int) v.getTag());
        // 通过tag找到对应的控件，对该控件进行相应的操作：
        LinearLayout ll_work = (LinearLayout) lv_work
                .findViewWithTag(itemEntity);
        if (ll_work.isShown()) {
            ll_work.setVisibility(View.GONE);
        } else {
            ll_work.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setListener() {
        bt_send.setOnTouchListener(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
        lv_work.setOnTouchListener(this);
        lv_work.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {

        getdata(1, 0);

        super.onResume();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoad() {
        load();
    }

    @Override
    public void setFooterView(boolean isLoading) {
        if (isLoading) {
            lv_work.removeFooterView(m_listViewFooter);
            lv_work.addFooterView(m_listViewFooter);
        } else {
            lv_work.removeFooterView(m_listViewFooter);
        }
    }

    // 刷新数据：
    private void refresh() {
        getdata(1, 0);
    }

    // 加载更多；
    private void load() {
        getdata(position, 1);
    }

    /**
     * @param page 当前页
     * @param type 请求类型，0代表下拉刷新；1代表上拉加载；
     */
    private void getdata(int page, final int type) {

        String url = UrlTools.url + UrlTools.WORK_QUAN;
        RequestParams params = new RequestParams();
        if(team_id!=null){
            params.put("team_id", "" + team_id);
        }
        params.put("current_page", "" + page);
        params.put("pagesize", 10 + "");
        Log.e("团队圈", "page: "+page );
       // params.put("team_id", team_id);
        Log.e("TAG", params.toString());

        Utils.doPost(null, WorkCircleActivity.this, url, params,
                new HttpCallBack() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeLayout.setLoading(false);
                            }
                        }, 1500);

                        swipeLayout.setRefreshing(false);

                        // 下拉刷新：
                        if (type == 0) {
                            itemEntities.removeAll(itemEntities);
                            position = 2;
                        }
                        // 上拉加载：
                        if (type == 1) {
                            position++;
                        }
                        for (int i = 0; i < infor.size(); i++) {
                            ItemEntity item = new ItemEntity();
                            String head = infor.get(i).get("fabu_pic") + "";// 发布者头像；
                            String name = infor.get(i).get("fabu_name") + "";// 发布者名字；
                            String content = infor.get(i).get("text") + "";// 发布的内容；
                            String id = infor.get(i).get("id") + "";// 发布者id;
                            String staff_id = infor.get(i).get("staff_id") + "";

                            JSONArray pics = (JSONArray) infor.get(i).get(
                                    "fabu_picture");// 发布的图片集合；
                            ArrayList<String> imageUrls = new ArrayList<>();
                            if (pics != null && pics.length() > 0) {
                                for (int j = 0; j < pics.length(); j++) {
                                    try {
                                        imageUrls.add(pics.getString(j));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            String circle_zan_name = infor.get(i).get(
                                    "circle_zan_name")
                                    + "";
                            item.setAvatar(head);
                            item.setContent(content);
                            item.setId(id);
                            item.setImageUrls(imageUrls);
                            item.setTitle(name);
                            item.setZan_name(circle_zan_name);
                            String[] zans = circle_zan_name.split(",");
                            item.setStaffId(staff_id);

                            for (int j = 0; j < zans.length; j++) {
                                if (AppStore.myuser.getName().equals(zans[j])) {
                                    item.setZan(true);// 将点赞置为true证明已经点赞了
                                    break;
                                }
                            }

                            // 评论列表：
                            JSONArray pls = (JSONArray) infor.get(i).get(
                                    "reply");
                            List<PingLunItem> plis = new ArrayList<>();
                            if (pls != null && pls.length() > 0) {
                                for (int j = 0; j < pls.length(); j++) {
                                    try {
                                        JSONObject jo = pls.getJSONObject(j);
                                        PingLunItem pl = new PingLunItem();
                                        pl.setName(jo.getString("pinglun_name"));
                                        pl.setText(jo.getString("reply_text"));
                                        plis.add(pl);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            item.setPlis(plis);
                            itemEntities.add(item);
                        }
                        adapter.add(itemEntities);
                    }

                    @Override
                    public void failure(String msg) {
                        swipeLayout.setRefreshing(false);
                        swipeLayout.setLoading(false);
                        if ("暂无内容".equals(msg) && position > 1) {
                            return;
                        }
                        T.showShort(WorkCircleActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private String pinglun = null;// 评论内容；
    private int pos_pinglun;// 记录下评论的item位置；
    private View show_hide;// 显示与隐藏评论和赞；

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) et_pinglun.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.bt_send:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pinglun = null;
                    pinglun = et_pinglun.getText().toString();
                    if (TextUtils.isEmpty(pinglun)) {
                        T.showShort(WorkCircleActivity.this, "请输入评论内容!");
                        return true;
                    }
                    pinglun(pos_pinglun);
                }
                return true;
            case R.id.tv_zan:
                TextView tv = (TextView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int position = (int) v.getTag();
                    dianzan(position);
                    setShowHide(v);
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return true;
            case R.id.tv_pinglun_click:
                TextView t = (TextView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pos_pinglun = (int) v.getTag();
                    setShowHide(v);
                    // 如果评论输入框显示：
                    if (ll_pinglun_input.isShown()) {
                        ll_pinglun_input.setVisibility(View.GONE);
                    } else {
                        Log.e("tag", "onTouch:------------显示了吗----------- " );
                        ll_pinglun_input.setVisibility(View.VISIBLE);

                        et_pinglun.requestFocus();
                        imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);

                    }
                    t.setTextColor(Color.BLACK);
                } else {
                    t.setTextColor(Color.WHITE);
                }
                return true;
            case R.id.ll_show_hide_zan:

                return true;
            case R.id.iv_xinxi:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setShowHide(v);
                }
                return true;
//            case R.id.lv_work:
//                ll_pinglun_input.setVisibility(View.GONE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
//                return true;
        }
        return false;
    }

    /**
     * 评论：
     */
    private void pinglun(int position) {
        String url = UrlTools.url + UrlTools.WORK_QUAN_PINGLUN;
        RequestParams params = new RequestParams();
        final ItemEntity item = (ItemEntity) adapter.getItem(position);
        params.put("circle_id", item.getId());
        params.put("reply_text", pinglun);

        Utils.doPost(null, WorkCircleActivity.this, url, params,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ll_pinglun_input.setVisibility(View.GONE);
                        List<PingLunItem> plis = new ArrayList<>();
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        for (int i = 0; i < infor.size(); i++) {
                            et_pinglun.setText("");
                            // 得到所有评论：
                            HashMap<String, Object> hm = infor.get(i);
                            PingLunItem item = new PingLunItem();
                            item.setName(hm.get("pinglun_name") + "");
                            item.setText(hm.get("reply_text") + "");
                            plis.add(item);
                        }
                        item.setPlis(plis);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(WorkCircleActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if ((position - 1) != pos_pinglun) {

            ll_pinglun_input.setVisibility(View.GONE);
        }
    }

}
