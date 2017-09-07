package com.feirui.feiyunbangong.im;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.aop.model.ReplyBarItem;
import com.alibaba.mobileim.aop.model.YWChattingPlugin;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWGeoMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageBody;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.AboutFriendActivity;
import com.feirui.feiyunbangong.activity.AddChengYuanActivity;
import com.feirui.feiyunbangong.activity.DetailMapActivity;
import com.feirui.feiyunbangong.activity.WebViewActivity;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.myinterface.AllInterface.ISelectContactListener;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnSendCardListener;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面的输入框所需内容
 */
public class MyIMChattingPageOperateion extends IMChattingPageOperateion
        implements OnSendCardListener {

    YWIMKit mIMKit = AppStore.mIMKit;
    public static Activity activity;
    YWIMCore core = mIMKit.getIMCore();

    public MyIMChattingPageOperateion(Pointcut pointcut) {
        super(pointcut);
    }

    private static boolean haveCheckedShortVideoLibrary = false;// 已经检查了是否集成了段视频SDK
    private static boolean compiledShortVideoLibrary = false;

    /**
     * 检查是否集成了集成了短视频的SDK
     *
     * @return
     */
    private boolean haveShortVideoLibrary() {
        if (!haveCheckedShortVideoLibrary) {
            try {
                Class.forName("com.im.IMRecordVideoActivity");
                compiledShortVideoLibrary = true;
                haveCheckedShortVideoLibrary = true;
            } catch (ClassNotFoundException e) {
                compiledShortVideoLibrary = false;
                haveCheckedShortVideoLibrary = true;
                e.printStackTrace();
            }
        }
        return compiledShortVideoLibrary;

    }

    /**
     * 请注意不要和内部的ID重合 {@link YWChattingPlugin.ReplyBarItem#ID_CAMERA}
     * {@link YWChattingPlugin.ReplyBarItem#ID_ALBUM}
     * {@link YWChattingPlugin.ReplyBarItem#ID_SHORT_VIDEO}
     */
    private static int ITEM_ID_1 = 0x1;
    private static int ITEM_ID_2 = 0x2;
    private static int ITEM_ID_3 = 0X3;

    /**
     * 用于增加聊天窗口 下方回复栏的操作区的item
     * <p>
     * ReplyBarItem itemId:唯一标识 建议从1开始 ItemImageRes：显示的图片 ItemLabel：文字
     * needHide:是否隐藏 默认: false , 显示：false ， 隐藏：true OnClickListener: 自定义点击事件,
     * null则使用默认的点击事件 参照示例返回List<ReplyBarItem>用于操作区显示item列表，可以自定义顺序和添加item
     *
     * @param pointcut         聊天窗口fragment
     * @param conversation     当前会话，通过conversation.getConversationType() 区分个人单聊，与群聊天
     * @param replyBarItemList 默认的replyBarItemList，如拍照、选择照片、短视频等
     * @return
     */
    @Override
    public List<ReplyBarItem> getCustomReplyBarItemList(
            final Fragment pointcut, final YWConversation conversation,
            List<ReplyBarItem> replyBarItemList) {

        this.activity = pointcut.getActivity();

        List<ReplyBarItem> replyBarItems = new ArrayList<ReplyBarItem>();

        for (ReplyBarItem replyBarItem : replyBarItemList) {
            if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_CAMERA) {
                // 是否隐藏ReplyBarItem中的拍照选项
                replyBarItem.setNeedHide(false);
                // 不自定义ReplyBarItem中的拍照的点击事件,设置OnClicklistener(null);
                replyBarItem.setOnClicklistener(null);
                // 自定义ReplyBarItem中的拍照的点击事件,设置OnClicklistener
                // 开发者在自己实现拍照逻辑时，可以在{@link #onActivityResult(int, int, Intent,
                // List<YWMessage>)}中处理拍照完成后的操作
                // replyBarItem.setOnClicklistener(new View.OnClickListener() {
                // @Override
                // public void onClick(View v) {
                //
                // }
                // });
            } else if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_ALBUM) {
                // 是否隐藏ReplyBarItem中的选择照片选项
                replyBarItem.setNeedHide(false);
                // 不自定义ReplyBarItem中的相册的点击事件,设置OnClicklistener（null）
                replyBarItem.setOnClicklistener(null);
                // 自定义ReplyBarItem中的相册的点击事件,设置OnClicklistener
                // replyBarItem.setOnClicklistener(new View.OnClickListener() {
                // @Override
                // public void onClick(View v) {
                // Notification.showToastMsgLong(pointcut.getActivity(),
                // "用户点击了选择照片");
                // }
                // });
            } else if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_SHORT_VIDEO) {

                // 检查是否集成了短视频SDK，短视频SDK集成文档请访问网页http://open.taobao.com/doc2/detail?&docType=1&articleId=104689
                if (!haveShortVideoLibrary()) {
                    // 是否隐藏ReplyBarItem中的短视频选项
                    replyBarItem.setNeedHide(true);
                } else {
                    // 默认配置是群聊时隐藏短视频按钮。这里是为了设置显示群聊短视频item
                    if (conversation.getConversationType() == YWConversationType.Tribe) {
                        replyBarItem.setNeedHide(false);
                    }
                }
            }
            replyBarItems.add(replyBarItem);
        }

        if (conversation.getConversationType() == YWConversationType.P2P) {
            ReplyBarItem replyBarItem = new ReplyBarItem();
            replyBarItem.setItemId(ITEM_ID_1);
            replyBarItem.setItemImageRes(R.drawable.demo_reply_bar_location);
            replyBarItem.setItemLabel("位置");
            replyBarItem.setOnClicklistener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConversation = conversation;
                    sendGeoMessage(conversation);
                }
            });

            replyBarItems.add(0, replyBarItem);

            ReplyBarItem replyBarItem2 = new ReplyBarItem();
            replyBarItem2.setItemId(ITEM_ID_2);
            replyBarItem2
                    .setItemImageRes(R.drawable.demo_reply_bar_profile_card);
            replyBarItem2.setItemLabel("名片");

            replyBarItem2.setOnClicklistener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mConversation = conversation;
                    // 发送名片：
                    sendCard(conversation);
                }

            });

            replyBarItems.add(replyBarItem2);
        }

        return replyBarItems;

    }

    // 发送名片：
    private void sendCard(final YWConversation conversation) {

        AppStore.listener = this;

        Intent intent = new Intent(activity, AddChengYuanActivity.class);

        intent.putExtra("type", "sendCard");

        activity.startActivity(intent);

    }


    /**
     * 发送单聊地理位置消息
     */
    public static void sendGeoMessage(final YWConversation conversation) {

        // 开启定位：
        BaiDuUtil.initLocation(activity, new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {

                // TODO Auto-generated method stub
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    StringBuffer sb = new StringBuffer(256);
                    sb.append("time : ");
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
                    sb.append(location.getTime());
                    sb.append("\nlocType : ");// 定位类型
                    sb.append(location.getLocType());
                    sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                    sb.append(location.getLocTypeDescription());
                    sb.append("\nlatitude : ");// 纬度
                    sb.append(location.getLatitude());
                    sb.append("\nlontitude : ");// 经度
                    sb.append(location.getLongitude());
                    sb.append("\nradius : ");// 半径
                    sb.append(location.getRadius());
                    sb.append("\nCountryCode : ");// 国家码
                    sb.append(location.getCountryCode());
                    sb.append("\nCountry : ");// 国家名称
                    sb.append(location.getCountry());
                    sb.append("\ncitycode : ");// 城市编码
                    sb.append(location.getCityCode());
                    sb.append("\ncity : ");// 城市
                    sb.append(location.getCity());
                    sb.append("\nDistrict : ");// 区
                    sb.append(location.getDistrict());
                    sb.append("\nStreet : ");// 街道
                    sb.append(location.getStreet());
                    sb.append("\naddr : ");// 地址信息
                    sb.append(location.getAddrStr());
                    sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                    sb.append(location.getUserIndoorState());
                    sb.append("\nDirection(not all devices have value): ");
                    sb.append(location.getDirection());// 方向
                    sb.append("\nlocationdescribe: ");
                    sb.append(location.getLocationDescribe());// 位置语义化信息
                    sb.append("\nPoi: ");// POI信息
                    if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                        for (int i = 0; i < location.getPoiList().size(); i++) {
                            Poi poi = (Poi) location.getPoiList().get(i);
                            sb.append(poi.getName() + ";");
                        }
                    }
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                        sb.append("\nspeed : ");
                        sb.append(location.getSpeed());// 速度 单位：km/h
                        sb.append("\nsatellite : ");
                        sb.append(location.getSatelliteNumber());// 卫星数目
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 海拔高度 单位：米
                        sb.append("\ngps status : ");
                        sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                        sb.append("\ndescribe : ");
                        sb.append("gps定位成功");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                        // 运营商信息
                        if (location.hasAltitude()) {// *****如果有海拔高度*****
                            sb.append("\nheight : ");
                            sb.append(location.getAltitude());// 单位：米
                        }
                        sb.append("\noperationers : ");// 运营商信息
                        sb.append(location.getOperators());
                        sb.append("\ndescribe : ");
                        sb.append("网络定位成功");
                    } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                        sb.append("\ndescribe : ");
                        sb.append("离线定位成功，离线定位结果也是有效的");
                    } else if (location.getLocType() == BDLocation.TypeServerError) {
                        sb.append("\ndescribe : ");
                        sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                        sb.append("\ndescribe : ");
                        sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                        sb.append("\ndescribe : ");
                        sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    }
                    Log.e("tag", "onReceiveLocation: -------------------" + sb.toString() );

                }

                Log.e("tag", "onReceiveLocation: -------------------"  );

                // 这个方法会回调多次的问题：这样的话就会发送很多：

                Object[] result = BaiDuUtil.getResult(location);

                final YWMessage  geomessage = YWMessageChannel.createGeoMessage(
                        (double) result[1], (double) result[2],
                        (String) result[0]);
                Log.e("tag", "geomessage: -------------------" +  geomessage.toString() );

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        conversation.getMessageSender().sendMessage(geomessage, 120,
                        null);
                    }
                });

            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }


        });
    }

    /**
     * 定制点击消息事件, 每一条消息的点击事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的点击事件
     *
     * @param fragment 聊天窗口fragment对象
     * @param message  被点击的消息
     * @return true:使用用户自定义的消息点击事件，false：使用默认的消息点击事件
     */
    @Override
    public boolean onMessageClick(Fragment fragment, YWMessage message) {

        activity = fragment.getActivity();
        Log.e("TAG", message.getSubType() + "message.getSubType()");
        Log.e("TAG", message.getContent() + "-----------------message-------");
        Log.e("TAG", YWMessage.SUB_MSG_TYPE.IM_CARD
                + "YWMessage.SUB_MSG_TYPE.IM_CARD");

        // 位置消息：
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {

            // 注意：需要强转成YWGeoMessageBody;
            YWGeoMessageBody messageBody = (YWGeoMessageBody) message
                    .getMessageBody();
            Log.e("TAG", messageBody.getLatitude()
                    + "messageBody.getLatitude()");
            Log.e("TAG", messageBody.getLongitude()
                    + "messageBody.getLatitude()");

            String address = messageBody.getAddress();
            double latitude = messageBody.getLatitude();
            double longitude = messageBody.getLongitude();

            Intent intent = new Intent(activity, DetailMapActivity.class);

            intent.putExtra("address", address);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);

            // 打开百度地图：
            activity.startActivity(intent);

            return true;

            // 如果点击的是名片：

        } else if (message.getMessageBody() != null
                && "[名片]".equals(message.getMessageBody().getSummary())) {

            YWMessageBody messageBody = message.getMessageBody();

            String jsonStr = messageBody.getContent();
            try {

                JSONObject obj = new JSONObject(jsonStr);
                String name = obj.getString("nickName");
                String personId = obj.getString("personId");
                String headURL = obj.getString("headURL");
                Friend friend = new Friend(name, personId, null, headURL);
                Intent intent = new Intent(activity, AboutFriendActivity.class);
                intent.putExtra("friend", friend);
                activity.startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;

        }else if (message.getMessageBody() != null &&
                message.getMessageBody().getContent().contains("ib3mbd6s") == false && //当前用户的id
                Patterns.WEB_URL.matcher(message.getMessageBody().getContent()).matches()){
            try {
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("uri",message.getMessageBody().getContent());
//                intent.setData(Uri.parse(message.getMessageBody().getContent()));//Url 就是你要打开的网址
//                intent.setAction(Intent.ACTION_VIEW);
                activity.startActivity(intent); //启动浏览器
            } catch (Exception e){
                Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            return true;

        }
        // 其他情况返回false:
        return false;
    }

    public static ISelectContactListener selectContactListener = new ISelectContactListener() {
        @Override
        public void onSelectCompleted(List<IYWContact> contacts) {
            if (contacts != null && contacts.size() > 0) {
                for (IYWContact contact : contacts) {
                    // sendP2PCustomMessage(contact.getUserId());
                }
            }
        }
    };

    private static YWConversation mConversation;

    public class CustomMessageType {
        private static final String GREETING = "Greeting";
        private static final String CARD = "CallingCard";
        private static final String IMAGE = "PrivateImage";
        public static final String READ_STATUS = "PrivateImageRecvRead";
        public static final String LOCATION = "MYBAIDULOCATION";// 百度定位；
    }

    // 自定义地理位置消息的显示
    @Override
    public View getCustomGeoMessageView(Fragment fragment, YWMessage msg) {
        YWGeoMessageBody messageBody = (YWGeoMessageBody) msg.getMessageBody();
        String content = "地址: " + messageBody.getAddress();
        LinearLayout layout = (LinearLayout) View.inflate(
                fragment.getActivity(), R.layout.ll_message_geo_item, null);
        TextView textView = (TextView) layout.findViewById(R.id.tv_location);
        // 判断发送消息的是不是登录用户，如果是的话则字体为宝色，否则为黑色；
        String id = msg.getAuthorId().substring(
                msg.getAuthorId().length() - 11, msg.getAuthorId().length());
        if (id.equals(AppStore.myuser.getPhone())) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.BLACK);
        }
        textView.setText(content);
        return layout;
    }

    // 自定义自定义消息的显示：
    @Override
    public View getCustomMessageView(Fragment fragment, YWMessage msg) {

        LinearLayout layout = null;
        if (!"[名片]".equals(msg.getMessageBody().getSummary())) {
            return null;
        }
        layout = sendCard(fragment, msg);
        return layout;
    }

    // 名片消息自定义：
    private LinearLayout sendCard(Fragment fragment, YWMessage msg) {

        LinearLayout layout;

        String name = "";
        String personId = "";
        String headURL = "";

        YWMessageBody messageBody = msg.getMessageBody();
        try {
            JSONObject jObj = new JSONObject(messageBody.getContent());
            name = jObj.getString("nickName");
            Log.e("TAG", name + "name");
            personId = jObj.getString("personId");
            headURL = jObj.getString("headURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        layout = (LinearLayout) View.inflate(fragment.getActivity(),
                R.layout.ll_card_item_im, null);

        TextView tvName = (TextView) layout.findViewById(R.id.tv_name_identity_card);
        ImageView ivHead = (ImageView) layout.findViewById(R.id.iv_head_identity_card);
        TextView tvPhone = (TextView) layout.findViewById(R.id.tv_phone_identity_card);

        tvName.setText(name);
        ImageLoader.getInstance().displayImage(headURL, ivHead, ImageLoaderUtils.getSimpleOptions());
        tvPhone.setText(personId);

        return layout;
    }

    @Override
    public void onSendCard(List<ChildItem> items) {

        for (int i = 0; i < items.size(); i++) {

            @SuppressWarnings("unchecked")
            ChildItem item = items.get(i);
            Log.e("TAG", item.toString());

            String phone = item.getPhone();
            String nickName = item.getTitle();
            String head = item.getMarkerImgId();

            YWMessageBody body = new YWMessageBody();
            JSONObject obj = new JSONObject();

            try {

                obj.put("customizeMessageType", CustomMessageType.CARD);
                obj.put("personId", phone);// 电话号码
                obj.put("headURL", head);// 头像
                obj.put("nickName", nickName);// 昵称

            } catch (JSONException e) {
                Log.e("TAG", e.getMessage());
            }

            Log.e("TAG", obj.toString());

            body.setContent(obj.toString());
            body.setSummary("[名片]");
            YWMessage msg = YWMessageChannel.createCustomMessage(body);
            mConversation.getMessageSender().sendMessage(msg, 120, null);

        }
    }

}
