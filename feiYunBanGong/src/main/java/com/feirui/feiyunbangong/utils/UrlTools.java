package com.feirui.feiyunbangong.utils;

public class UrlTools {

  public static final String ADD_GOOD = "Store/add_goods";
  public static final String GOOD_DETAIL = "Other/goodsdetaile";
  public static final String FRIEND_SHOP = "Store/look_store";
  public static final String TEAM_TASK_LIST = "Task/taskIndex";  //任务列表
  public static final String MY_FORM_LIST = "form/my_form_list";
  public static final String OTHER_FORM_LIST = "form/form_list";
  public static final String FORM_LIST_DETAILS = "form/details";
  public static final String TASK_ADDTASK="Task/add_task";  //添加发布的任务
  public static final String TASK_ACCEPT="task/task_accept"; //接收任务
  public static final String TASK_GETTEAN="Task/get_team";   //获取团队列表
  public static final String TASK_MY_TASK_LIST="task/my_task_list";//自己发布的任务列表
  public static final String TASK_ACCEPT_TASK_LIST="Task/accept_task_list";//自己接收的任务列表
    /**
     * 新的PC端的接口地址
     * */
  public static String pcUrl = "http://123.57.45.74/feiybg1/public/index.php/home_api/";
  /**
   * 链接地址
   */
  public static String url = "http://123.57.45.74/feiybg1/public/index.php/api/";
  public static String url1 = "http://123.57.45.74/feiybg1/public/index.php/index/";
  /**
   * 后台给的图片有的地址差的url
   * */
  public static String url_img = "http://123.57.45.74/feiybg1";
//public static String url = "http://175.25.23.139/feiybg/public/index.php/api/";
// public static String fyUrl = "http://175.25.23.139/feiybg/public/index.php/index/";

  /*
  添加好友接口搜索
   */
  public static String FRIEND_SEARCH = "User/search_stranger";

  /**
   * 注册获取验证码
   */
  public static String ZHUCE_REGIST = "Zhuce/regist";

  /**
   * 修改备注
   */
  public static String XIUGAI_BEIZHU = "User/updatremark";

  /**
   * 登录获取验证码
   */
  public  static String DENGLU_REGIST ="login/logincode";
  /**
   * 快速登录
   */
  public static String QUICK_LOGIN = "login/quicklogin";
  /**
   * 忘记密码--获取验证码
   */
  public static String ZHUCE_FORGET_PWD = "Zhuce/forget_pwd";
  /**
   * 忘记密码--修改密码
   */
  public static String ZHUCE_UPDATE_PWD = "zhuce/update_pwd";
  /**
   * 注册、忘记密码时的验证码的验证
   */
  public static String ZHUCE_CHECK_REGIST = "Zhuce/check_regist";
  /**
   * 登录
   */
  public static String LOGIN_LOGIN = "login/login";

  /**
   * 个人注册
   */
  public static String ZHUCE_USERREGISTER = "Zhuce/userregister";
  /**
   * 企业注册
   */
  public static String ZHUCE_COMPANY_ADD = "Zhuce/company_add";
  /**
   * 首页--打卡--上班签到
   */
  public static String HOME_WORK_START = "Home/work_start";
  /**
   * 首页--打卡--下班签退
   */
  public static String HOME_WORK_END = "Home/work_end";
  /**
   * 首页--点击打卡显示上下班打卡的时间
   */
  public static String HOME_ATTENDANCE = "Home/attendance";
  /**
   * 打卡数据--带搜索
   */
  public static String HOME_ATTENDANCE_LIST = "Home/attendance_list";
  /**
   * 项目进行中详情页
   */
  public static String PROJECT_PROJECT_LIST = "Project/project_list";
  /**
   * 项目已完成详情页
   */
  public static String PROJECT_PROJECT_END = "Project/project_end";
  /**
   * 新增项目信息
   */
  public static String PROJECT_ADD_PROJECT = "Project/add_project";
  /**
   * 显示拜访客户信息
   */
  public static String CUSTOMER_CUSTOMER_LIST = "Customer/customer_list";
  /**
   * 签退后显示拜访客户信息
   */
  public static String CUSTOMER_SIGN_LIST = "Customer/sign_list";
  /**
   * 拜访客户后反馈信息页
   */
  public static String CUSTOMER_FEEDBACK_SHOW = "Customer/feedback_show";
  /**
   * 拜访客户后反馈信息详情页
   */
  public static String CUSTOMER_FEEDBACK_LIST = "Customer/feedback_list";
  /**
   * 创建拜访客户反馈信息
   */
  public static String CUSTOMER_CUSTOMER_FEEDBACK = "Customer/customer_feedback";
  /**
   * 创建拜访客户信息
   */
  public static String CUSTOMER_ADD_CUSTOMER = "Customer/add_customer";
  /**
   * 报表显示接口
   */
  public static String FORM_REPORT_LIST = "form/report_list";
  /**
   * 上传报表接口
   */
  public static String FORM_REPORT = "form/report";
  /**
   * 报表详细接口
   */
  public static String FORM_DETAILS = "form/details";
  /**
   * 拜访客户签到按钮触及的事件
   */
  public static String CUSTOMER_CUSTOMER_SIGN = "Customer/customer_sign";
  /**
   * 拜访客户签退按钮触及的事件
   */
  public static String CUSTOMER_CUSTOMER_BACK = "Customer/customer_back";
  /**
   * 显示拜访客户信息(签到的时候用)
   */
  public static String CUSTOMER_SHOW_LIST = "Customer/show_list";
  /**
   * 首页显示公司是否认证
   */
  public static String HOME_AUTHENTICATION = "Home/authentication";
  /**
   * 首页--公司须知
   */
  public static String HOME_COMPANY_PROFILE = "Home/company_profile";
  /**
   * 新增请假信息
   */
  public static String LEAVE_APP_LEAVE = "leave/app_leave";
    /**
     * 新增请假信息,带抄送人
     */
    public static String LEAVE_ADD_LEAVE1 = "leave/add_leave1";
  /**
   * 新增报销信息
   */
  public static String EXPENSE_ADD_EXPENSE = "Expense/add_expense";
  /**
   * 新增报销信息
   */
  public static String EXPENSE_APP_EXPENSE = "Expense/app_expense";

  /**
   * 新增外出信息
   */
  public static String OUT_APP_OUT = "Out/app_out";
  /**
   * 新增外出信息(添加抄送人)
   */
  public static String OUT_ADD_OUT1 = "Out/add_out111";
  /**
   * 付款新增
   */
  public static String BUY_ADD_BUY = "buy/add_buy";

  /**
   * 付款新接口
   */
  public static String BUY_APP_BUY = "buy/app_payment";
  /**
   * 新增采购信息：
   */
  public static final String ADD_CAIGOU_XINXI = "Purchase/add_purchase";
  /**
   * 新增采购信息：http://123.57.45.74/feiybg/public/index.php/index/Purchase/add_purchase1
   */
  public static final String ADD_CAIGOU_XINXI1 = "Purchase/app_purchase";
  /**
   * 待审批其他新增
   */
  public static String OTHER_OTHER_ADD = "Other/other_add";
  /**
   * 待审批其他新增,带抄送人
   */
  public static String OTHER_APP_OTHER = "other/app_other";
  /**
   * 审批里我发起点进去显示所有的信息(我提交的)
   */
  public static String APPROVAL_APPROVAL_ALL = "Approval/approval_all";
  /**
   * 审批流程中发给我，需要我审批的
   * */
  public static String APPROVAL_MY_APPROVAL = "Approval/my_approval";//旧
    /**
     * 操作记录-我提交的
     * */
  public static String APP_MY_APPROVAL = "approval/app_my_post";//新
  /**
   * 操作记录-我审批的
   * */
  public static String APPROVAL_MY_APPROVAL_OLD = "approval/app_my_approval";

  /**
   * 操作记录-抄送我的
   * */
  public static String APPROVAL_MY_APPROVAL_SEND = "approval/carbon_copy";

  /**
   * 待审批详情查看没有审批的 --分页+查询
   */
  public static String APPROVAL_APPROVAL = "Approval/app_approval";
  /**
   * 待审批--根据id查看审批条目详细数据
   */
  public static String APPROVAL_DETAIL = "Approval/approval_detaile";//旧
  public static String APP_DETAIL = "approval/app_alldetail";//新

    /**
     * 待审批的个数显示
     * */
    public static String APPROVAL_SHOWAPPCOUNT = "Approval/showappcount";
  /**
   * 待审批的个数显示
   * */
  public static String APPROVAL_TASK = "remind/task_subscript";
  /**
   * 推广-团队工作圈
   */
  public static String CIRCLE_ADDTEAMCIRCLE = "Circle/addteamcircle";
  /**
   * 邀请码--显示邀请码
   */
  public static String APPLICATION_SHOW_CODE = "Application/show_code";
  /**
   * 邀请码--点击生成邀请码
   */
  public static String APPLICATION_INVITATION = "Application/invitation";
  /**
   * 邀请码--统计用户总费用
   */
  public static String APPLICATION_STATISTICS = "Application/statistics";
  /**
   * 邀请码--详细统计费用
   */
  public static String APPLICATION_STATISLIST = "Application/statislist";
  // //////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * 修改头像
   */
  public static String modhead = "business/modhead";
  /**
   * 轮播图
   */
  public static final String wheelpic = "http://www.weikecn.net/goodsers/api.php/operation/wheelpic";
  /**
   * 轮播图1
   */
  public static final String wheelpic1 = "operation/wheelpic";

  /**
   * 审批人列表：
   */
  public static final String SHENPIREN_LIEBIAO = "Approval/sp_select";

  /**
   * 联系人同事：
   */
  public static final String LIANXIREN_TONGSHI = "User/show_staff";
  /**
   * 联系人供应商：
   */
  public static final String LIANXIREN_GONGYINGSHANG = "User/show_supplier";
  /**
   * 联系人客户：
   */
  public static final String LIANXIREN_KEHU = "User/show_person";

  /**
   * 联系人好友：
   */
  public static final String LIANXIREN_HAOYOU = "User/show_friend";

  /**
   * 上传手机联系人并返回已经注册的联系人信息：
   */
  public static final String SHOUJILIANXIREN = "Addfriend/contacts";

  /**
   * 添加好友；
   */
  public static final String TIANJIAHAOYOU = "user/addfriendapp1";

  /**
   * 搜索手机号加好友：
   */
  public static final String SOUSUO_JIAHAOYOU = "User/search_mobile";

  /**
   * 同意好友申请：
   */
  public static final String TONGYI_SHENQING = "user/accept1";

  /**
   * 拒绝好友申请：
   */
  public static final String JVJUE_SHENQING = "User/refuse";

  /**
   * 申请列表：
   */
  public static final String SHENQING_LIEBIAO = "User/showlist1";

  /**
   * 根据手机号搜索联系人
   */
  public static final String SOUSUO_LIANXIREN = "User/search_mobile";

  /**
   * 删除好友：
   */
  public static final String DELETE_FRIEND = "User/del_staff";

  /**
   * 退出登录：
   */
  public static final String OUT = "Home/login_out";

  /**
   * 更新apk:
   */
  public static String UPDATE_URL = "apk/apk_android";

  /**
   * 新增员工：
   */
  public static String ADD_YUANGONG = "Addyg/add_person";

  /**
   * 显示个人信息：
   */
  public static String DETAIL_ME = "Person/person_detail1";

  /**
   * 获取他人信息：
   */
  public static String DETAIL_OTHER = "Person/person_detail2";

  /**
   * 工作圈：
   */
  public static String WORK_QUAN = "Circle/circle_list";

  /**
   * 工作圈发表文章：
   */
  public static String FABIAO_WENZHANG = "Circle/circle_add";

  /**
   * 工作圈点赞：
   */
  public static String WORK_QUAN_ZAN = "Circle/circle_zan";

  /**
   * 工作圈评论：
   */
  public static String WORK_QUAN_PINGLUN = "Circle/circle_reply_add";

  /**
   * 反馈：
   */
  public static String FANKUI = "Feedback/add_feedback";

  /**
   * 修改个人资料：
   */
  public static String XIUGAI_GERENZILIAO = "Person/person_update";

  /**
   * 申请好友个数：
   */
  public static String HAOYOU_NUM = "Addfriend/add_num";

  /**
   * 创建团队：
   */
  public static String CHUANGIJAN_TUANDUI = "Team/add_team";

  /**
   * 显示所有团队列表：
   */
  public static String TUANDUI_ITEM = "Team/team_list";

  /**
   * 查看团队中的成员：
   */
  public static String DETAIL_TUANDUICHENGYUAN = "Team/team_detail";
  /**
   * 查看团队中的成员 新接口：
   */
  public static String DETAIL_TUANDUICHENGYUAN_TEAM = "Team/team_detail_page";

  /**
   * 修改邮箱：
   */
  public static String XIUGAI_YOUXIANG = "Team/team_yx_update";
  /**
   * 修改成员信息页面获取成员信息：
   */
  public static String XIUGAI_ChengYuan = "Team/team_update";

  /**
   * 没有管理员的团队成员，删除成员用；
   */
  public static String CHENGYUAN_WU_GUANLIYUAN = "Team/team_member_list";
  /**
   * 团队创建团聊用的  群ID
   */
  public static String QUN_ID = "Team/team_talk";

  /**
   * 解散团队群聊
   */
  public  static String JIESAN_QUN = "Team/dissolutionGroup";
  /**
   * 删除团队成员：
   */
  public static String DELETE_TUANDUI_CHENGYUAN = "Team/team_member_del";

  /**
   * 退出团队：
   */
  public static String OUT_TEAM = "Team/team_out";

  /**
   * 添加成员：
   */
  public static String ADD_CHENGYUAN = "Team/team_member_add";
  /**
   * 获取团队对应的团聊id
   */
  public static String GET_TUANLIAOID = "Team/teamtalk";
  /**
   * 获取团聊对应的团队id
   */
  public static String GET_TEAMID = "Team/teamid";
  /**
   * 加入团队显示团队信息：
   */
  public static String JIARU_TUANDUI = "Team/team_get1";

  /**
   * 解散团队：
   */
  public static String DISSOLVE_TEAM = "team/dissolve";

  /**
   * 加入团队：
   */
  public static String JIARU = "Team/team_getin";

  /**
   * 搜索团队id返回团队信息；
   */
  public static String TUANDUI_XINXI = "Team/team_insert";

  /**
   * 设置管理员：
   */
  public static String SET_TEEM_MANAGER = "team/transfer";

  /**
   * 公告列表：
   */
  public static String GONGGAO_TUANDUI = "Circle/showteamnotice";

  /**
   * 发布公告：
   */
  public static String FABU_GONGGAO = "Circle/addteamnotice";

  /**
   * 工作圈发表文章：
   */
  public static String SEND_WENZHANG = "Circle/circle_add";

  /**
   * 团队通告未读消息个数
   */
  public static String TEAM_MESSAGE_NUM = "Circle/unreadinfor";

  /**
   * 团队公告已读及未读的成员：
   */
  public static String TEAM_MESSAGE_READ_UNREAD = "Circle/readNotice";

  /**
   * 团队公告未读总数：
   */
  public static String TEAM_MESSAGE_UNREAD_COUNT = "Team/Allnote";

  /**
   * 删除公告：
   */
  public static String TEAM_NOTICE_DELETE = "Circle/delNotice";

  /**
   * 获取好友分组：
   */
  public static String GET_FRIEND_GROUP = "user/getfriendgroup";

  /**
   * 获取分组下的好友：
   */
  public static String GET_CHILD_OF_GROUP = "user/getgroup";

  /**
   * 修改好友分组
   */
  public static String USER_UPDATEFRIENDGROUP = "user/updatefriendgroup";
  /**
   * 增加好友分组
   */
  public static String USER_ADDFRIENDGROUP = "user/addfriendgroup";
  /**
   * 删除好友分组
   */
  public static String USER_DELETEFRIENDGROUP = "user/deletefriendgroup";

  /**
   * 发送短信邀请好友：
   */
  public static String SEND_MSG = "application/message";
  /**
   * 邀请经理、联系人里的短信邀请
   */
  public static String APPLICATION_INVITATIONMESSAGE = "Application/invitationmessage";
/**
 * 短信邀请后再发送
 */
public static String APPLICATION_MESSAGE = "zhuce/sms_registration";//http://123.57.45.74/feiybg1/public/index.php/api/zhuce/sms_registration
  /**
   * 转移分组：
   */
  public static String CHANGE_GROUP = "user/transfergroups";
  /**
   * 修改备注
   */
  public static String USER_UPDATREMARK = "User/updatremark";

  /**
   * 获取好友备注信息：
   */
  public static String REMARK_FRIEND = "User/getinfor";

  /**
   * 搜索手机号显示添加好友信息
   */
  public static String USER_SEARCH_MOBILE = "User/search_mobile";

  /**
   * 更改新团员状态：
   */
  public static String UPDATE_TEAM_STATE = "Team/updatemember";
  /**
   * 可用提现额度
   */
  public static String APPLICATION_WITHDRAWCASH = "application/withdrawCash";
  /**
   * 提现记录列表
   */
  public static String APPLICATION_WITHDRAWCASHLIST = "application/withdrawCashList";

  /**
   * 添加小店
   */
  public static String XIAODIAN_ADD_SHOP = "Store/create_store";
  /**
   * 我的小店的信息
   */
  public static String XIAODIAN_MY_SHOP = "Other/shopcont";
  public static String XIAO_DIAN_GOODS = "Other/goodsList";
  //待审批-通过审批
  public static String APPROVAL_UPDATE = "approval/app_adopt";
  //删除商品
  public static String DELETE_GOOD = "news/delete";
  //删除朋友圈
  public static String DELETE_CIRCLE = "circle/delCircle";


  /**
   * 新任务单-全部任务接口
   * */
  public static String RENWU_QB="Task/alltask";
  /**
   * 任务单信息task/my_task_detaile
   */
  public static String RENWU_DETAIL="task/task_detial";
  /**
   * 新任务单-待接单任务接口
   * */
  public static String RENWU_DJD="task/wait";
  /**
   * 新任务单-进行中任务接口
   * */
  public static String RENWU_JXZ="task/task_ing";
  /**
  * 新任务单-已完成任务接口
  **/
  public static String RENWU_YWC="task/taskend";//接口不对
  /**
   * 新任务单-发布人
   **/
  public static String RENWU_FBR="Task/task_detial_out";
  /**
   * 新任务单-接单人
   **/
  public static String RENWU_JDR="Task/task_detial_in";
  /**
   * 新任务单-任务信息
   **/
  public static String RENWU_RWXX="Task/task_detial";
    /**
     * 新任务单-确认接单
     **/
    public static String RENWU_QRJD="task/task_accept";
  /**
   * 新任务单-生成支付订单
   **/
  public static String RENWU_ORDER="task/task_order";
  /**
   * 新任务单-生成支付订单
   **/
  public static String RENWU_WX_NOTIFY="notify/wx_notify";
  /***
   * 获取下单信息
   * */
  public static final String WX_GETINFO_WXPAY = "Wxpays/wxPayAction";
  /***** 微信appkey ******/
  public static final Object API_KEY_WX = "12WSxfWExfWOSLEKRDwexidrWDERGY31";
}
