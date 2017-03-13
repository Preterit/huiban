package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testpic.PublishedActivity;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ListItemAdapter;
import com.feirui.feiyunbangong.entity.ItemEntity;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.PingLunItem;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.RefreshLayout;
import com.feirui.feiyunbangong.view.RefreshLayout.OnLoadListener;
import com.loopj.android.http.RequestParams;

import static com.feirui.feiyunbangong.R.id.swipe_container;

/**
 * 工作圈
 *
 * @author Lesgod
 */
public class WorkCircleActivity extends BaseActivity implements
        OnRefreshListener, OnLoadListener, OnTouchListener, OnItemClickListener {

    private RefreshLayout swipeLayout;

    private ArrayList<ItemEntity> itemEntities = new ArrayList<>();
    private ListView lv_work;
    private View header;

    private View m_listViewFooter;

    private int position = 1;// 当前页数；
    private ListItemAdapter adapter;

    private LinearLayout ll_pinglun_input;// 评论布局
    private EditText et_pinglun;
    private Button bt_send;// 发送评论；

    private String team_id;// 团队id,如果未空则表示是朋友圈，不为空则表示团队圈；

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

    @SuppressLint("InflateParams")
    private void initView() {

        Intent intent = getIntent();
        team_id = intent.getStringExtra("team_id");
        Log.e("TAG", team_id + "team_id");

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
        lv_work = (ListView) findViewById(R.id.lv_work);
        lv_work.addFooterView(m_listViewFooter);
        lv_work.addHeaderView(header);
        ll_pinglun_input = (LinearLayout) findViewById(R.id.ll_pinglun_input);
        et_pinglun = (EditText) findViewById(R.id.et_pinglun);
        bt_send = (Button) findViewById(R.id.bt_send);
        adapter = new ListItemAdapter(this, this, itemEntities);
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
                        item.setZan(!item.isZan());
                        // 获得数据后：
                        item.setZan_name(""
                                + bean.getInfor().get(0).get("name"));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(WorkCircleActivity.this, msg, 0).show();
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
        swipeLayout.setColorScheme(
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
        params.put("current_page", "" + page);
        params.put("pagesize", 10 + "");
        params.put("team_id", team_id);
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
                        Toast.makeText(WorkCircleActivity.this, msg, 0).show();
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
        switch (v.getId()) {
            case R.id.bt_send:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pinglun = null;
                    pinglun = et_pinglun.getText().toString();
                    if (TextUtils.isEmpty(pinglun)) {
                        Toast.makeText(WorkCircleActivity.this, "请输入评论内容！", 0)
                                .show();
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
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return true;
            case R.id.tv_pinglun_click:
                TextView t = (TextView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pos_pinglun = (int) v.getTag();
                    // 如果评论输入框显示：
                    if (ll_pinglun_input.isShown()) {
                        ll_pinglun_input.setVisibility(View.GONE);
                    } else {
                        ll_pinglun_input.setVisibility(View.VISIBLE);
                    }
                    t.setTextColor(Color.BLACK);
                } else {
                    t.setTextColor(Color.WHITE);
                }
                return true;
            case R.id.ll_show_hide_zan:
                Toast.makeText(this, "ll_show_hide_zan", 0).show();
                return true;
            case R.id.iv_xinxi:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setShowHide(v);
                }
                return true;
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
                        Toast.makeText(WorkCircleActivity.this, msg, 0).show();
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
