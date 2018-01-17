package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.TongXunLuAdapter;
import com.feirui.feiyunbangong.entity.ContactMember;

import java.util.ArrayList;

public class ChooseTxlActivity extends AppCompatActivity {
    private ListView txl_list;
    private TongXunLuAdapter adapter;
    private String name="";
    private String phone="" ;
    private ArrayList<ContactMember> ls;// 存放联系人姓名和手机号的数组；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_txl);
        request();

    }

    private void initView() {
        txl_list=findViewById(R.id.txl_list);
        txl_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new TongXunLuAdapter(ChooseTxlActivity.this,ls);
        txl_list.setAdapter(adapter);

        txl_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    // 获取手机联系人：
    private void request() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                ls = getContact(ChooseTxlActivity.this);
                Log.e("任务单添加通讯录好友", "联系人列表"+ls.toString());
                initView();
            }

        }).start();
    }



    Cursor c;
    public ArrayList<ContactMember> getContact(Activity context) {
        ArrayList<ContactMember> listMembers = new ArrayList<ContactMember>();
        Cursor cursor = null;
        try {

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 这里是获取联系人表的电话里的信息  包括：名字，名字拼音，联系人id,电话号码；
            // 然后在根据"sort-key"排序
            cursor = context.getContentResolver().query(uri,
                    new String[]{"display_name", "sort_key", "contact_id",
                            "data1"}, null, null, "sort_key");
            if (cursor.moveToFirst()) {
                do {
                    ContactMember contact = new ContactMember();
                    String contact_phone = cursor
                            .getString(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(0);
                    String sortKey = getSortKey(cursor.getString(1));
                    int contact_id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    contact.contact_name = name;
                    contact.sortKey = sortKey;
                    contact.contact_phone = contact_phone;
                    contact.setContact_id(contact_id);
                    Log.e("任务单添加通讯录好友", "name: "+name );
                    Log.e("任务单添加通讯录好友", "sortKey: "+sortKey );
                    Log.e("任务单添加通讯录好友", "contact_phone: "+contact_phone );
                    if (name != null)
                        listMembers.add(contact);
                } while (cursor.moveToNext());
                c = cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context = null;
        }
        return listMembers;
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     *
     * @param sortKeyString 数据库中读取出的sort key
     * @return 英文字母或者#
     */
    private static String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    private void addData(ArrayList<ContactMember> list) {
        ArrayList<ContactMember> list1=list;

    }


}
