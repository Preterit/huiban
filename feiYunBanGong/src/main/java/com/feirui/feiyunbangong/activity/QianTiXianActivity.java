package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.BankInfo;
import com.feirui.feiyunbangong.utils.EditCheckUtil;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

public class QianTiXianActivity extends Activity {

    private ImageButton tianjiayinhangkaBtn;
    private LinearLayout tianxie_layout;
    private LinearLayout tishi_layout;
    private EditText chikaren_name_edt;
    private EditText shenfenzheng_card_edt;
    private EditText type_edt;
    private EditText yinhangka_card_edt;
    private ImageButton lijitianjia_btn;
    private EditText kaihuhang_edt;
    private String tag;
    private LinearLayout leftll;
    private ImageView leftIv;
    private TextView centerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qian_ti_xian);
        Intent intent = getIntent();
        tag= intent.getStringExtra("TAG");

        initViews();
        addCard();
        setListeners();
    }

    private void initViews() {
        leftll=(LinearLayout)findViewById(R.id.leftll);
        leftIv=(ImageView)findViewById(R.id.leftIv);
        centerTv=(TextView)findViewById(R.id.centerTv);
        leftll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        leftIv.setImageResource(R.drawable.arrows_left);
        centerTv.setText("添加银行卡号");

        tianjiayinhangkaBtn=(ImageButton)findViewById(R.id.tianjiayinhangkaBtn);
        tianxie_layout=(LinearLayout)findViewById(R.id.tianxie_layout);
        tishi_layout=(LinearLayout)findViewById(R.id.tishi_layout);
        chikaren_name_edt=(EditText)findViewById(R.id.chikaren_name_edt);
        shenfenzheng_card_edt=(EditText)findViewById(R.id.shenfenzheng_card_edt);

        type_edt=(EditText)findViewById(R.id.type_edt);
        kaihuhang_edt=(EditText)findViewById(R.id.kaihuhang_edt);
        yinhangka_card_edt=(EditText)findViewById(R.id.yinhangka_card_edt);
        lijitianjia_btn=(ImageButton)findViewById(R.id.lijitianjia_btn);

        if("1".equals(tag)){
            tianxie_layout.setVisibility(View.VISIBLE);
            tishi_layout.setVisibility(View.GONE);
        }else if("0".equals(tag)){
            tianxie_layout.setVisibility(View.GONE);
            tishi_layout.setVisibility(View.VISIBLE);
        }
    }


    private void addCard() {

        lijitianjia_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type_edt.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"请填写证件类型",Toast.LENGTH_SHORT).show();
                }else if(shenfenzheng_card_edt.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"请填写身份证号",Toast.LENGTH_SHORT).show();
                }else if(!EditCheckUtil.IDCardValidate(shenfenzheng_card_edt.getText().toString())){
                    Toast.makeText(getApplication(),"请填写正确的身份证号",Toast.LENGTH_SHORT).show();
                }else if(!EditCheckUtil.checkBankCard(yinhangka_card_edt.getText().toString())){
                    Toast.makeText(getApplication(),"请填写正确的银行卡号",Toast.LENGTH_SHORT).show();
                } else if(yinhangka_card_edt.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"请填写银行卡号",Toast.LENGTH_SHORT).show();
                }else if(chikaren_name_edt.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"请填写持卡人姓名",Toast.LENGTH_SHORT).show();
                }else if(kaihuhang_edt.getText().toString().equals("")){
                    Toast.makeText(getApplication(),"请填写开户银行",Toast.LENGTH_SHORT).show();
                }else{
                    addCardPost();
                }
            }

        });
    }

    private void addCardPost() {

            RequestParams params = new RequestParams();
            params.put("button","0");
            params.put("certificate_type",type_edt.getText().toString());
            params.put("id_number",shenfenzheng_card_edt.getText().toString());
            params.put("bank_number",yinhangka_card_edt.getText().toString());
            params.put("card_name",chikaren_name_edt.getText().toString());
            params.put("bank_type",kaihuhang_edt.getText().toString());

            Log.e("tag", "参数"+params);
            String url = UrlTools.url+UrlTools.ADD_BACKCARD;

                Utils.doPost(LoadingDialog.getInstance(QianTiXianActivity.this),
                        QianTiXianActivity.this, url, params, new Utils.HttpCallBack() {

                    @Override
                    public void success(JsonBean bean) {
                        Log.e("tag", "bean---"+bean.toString() );
                        Toast.makeText(QianTiXianActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(QianTiXianActivity.this,XuanZeYinHangKaActivity.class));
                        finish();
                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(QianTiXianActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void finish() {

                    }
                });
    }

    private void setListeners() {


        yinhangka_card_edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 在输入数据时监听
                int huoqu = yinhangka_card_edt.getText().toString().length();
                if (huoqu >= 6) {
                    String huoqucc = yinhangka_card_edt.getText().toString();
                    char[] cardNumber = huoqucc.toCharArray();
                    String name = BankInfo.getNameOfBank(cardNumber, 0);// 获取银行卡的信息
                    kaihuhang_edt.setText(name);
                } else {
                    kaihuhang_edt.setText(null);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 在输入数据前监听

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 在输入数据后监听

            }
        });


        tianjiayinhangkaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tianxie_layout.setVisibility(View.VISIBLE);
                tishi_layout.setVisibility(View.GONE);
            }
        });

    }

}
