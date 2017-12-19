package com.feirui.feiyunbangong.state;

public class Constant {
    public static final String SP_ALREADYUSED = "ALREADYUSED"; // 是否已经登陆过
    // 加密常量
    public static final String ENCRYPT = "2016goodsersabcdef06240925qwertg";
    // 登录时间
    public static final String SP_STARTTIME = "starttime";
    // 密码失效时间
    public static final String SP_ENDTIME = "endtime";
    // 失效的时间间隔
    public static final int SP_PWS_NO_USE = 1000 * 60 * 60 * 24 * 7;
    // 应用缓存文件
    // public static final File CACHE_FILE_PATH = StorageUtils
    // .getOwnCacheDirectory(Happlication.getInstance().getAppContext(),
    // "RWT/Cache");
    // 公用标记
    public static final int PUBLIC_TYPE_ONE = 1;
    // 刷新autolist
    public static final int REFRESH = -101;
    // 加载autolist
    public static final int LOAD = -102;
    public static final String SP_PASSWORD = "PASSWORD";// 密码
    public static final String SP_USERNAME = "USERNAME";// 用户名

    // 接受到好友申请信息发出的广播：
    public static final String GET_BROADCAST_ABOUT_FRIEND = "com.feirui.feiyunbangong.friend.mes";

    // 有新的公告推送时，发出获取团队列表的广播：
    public static final String GET_TEAM_BROADCAST = "com.feirui.feiyunbangong.getteam";

    // 登录阿里百川成功后发出的广播：
    public static final String ON_LOGIN_ALI_SUCCESS = "com.feirui.feiyunbangong.login.ali.success";

    // 手机联系人已注册的广播：

    public static final String IS_REGIST_CONTACT = "com.feirui.feiyunbangong.phone.regist";

    // 手机联系人未注册的广播：

    public static final String NOT_REGIST_CONTACT = "com.feirui.feiyunbangong.phone.regist";

    // 接收到您被邀请加入某团队消息推送后发出的广播：
    public static final String ON_REACEIVE_ADD_TEAM = "com.feirui.feiyunbangong.onadd.team";

    // 有新成员加入推送后发出的广播：
    public static final String ON_RECEIVE_NEW_MEMBER_ADD = "com.feirui.feiyunbangong.onadd.team.member";
    // 删除成员发出的广播：
    public static final String ON_RECEIVE_NEW_MEMBER_DELETE = "com.feirui.feiyunbangong.ondelete.team.member";

    //接收到需要审核的广播

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String NEED_TO_SHEN_HE = "com.feirui.feiyunbangong.need.to.shenhe";
    public static final String INTENT_SERIALIZABLE_DATA = "intent_serializable_data";

    public static class JPUSH {
        public static final String QINGJIA_SHENPI = "add_leave";
    }
}
