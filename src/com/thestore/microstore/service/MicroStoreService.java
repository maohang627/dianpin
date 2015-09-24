package com.thestore.microstore.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thestore.main.core.app.AppContext;
import com.thestore.main.core.datastorage.SpManager;
import com.thestore.main.core.net.bean.ResultVO;
import com.thestore.main.core.net.request.ParamHelper;
import com.thestore.main.core.net.request.Request;
import com.thestore.main.core.net.request.UrlHelper;
import com.thestore.microstore.MainActivity;
import com.thestore.microstore.R;
import com.thestore.microstore.data.ApiConst;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.PushInformationParser;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.MsgContent;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;
import com.thestore.microstore.vo.UserLevel;
import com.thestore.microstore.vo.proxy.MsgContentProxy;
import com.yihaodian.mobile.vo.push.PushInformationVO;

public class MicroStoreService extends Service {

	public static final String NOTIFICATION_INTENT = "NOTIFICATION_INTENT";
	public static final String NOTIFICATION_DELAYTIME = "NOTIFICATION_DELAYTIME";
	public static final String SHUTDOWN_TIME = "SHUTDOWN_TIME";
	public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
	public static final String NOTIFICATION_TEXT = "NOTIFICATION_TEXT";
	public static final String NOTIFICATION_ARG1 = "NOTIFICATION_ARG1";
	public static final String NOTIFICATION_ARG2 = "NOTIFICATION_ARG2";
	public static final String NOTIFICATION_ARG3 = "NOTIFICATION_ARG3";
	public static final String NOTIFICATION_ARG4 = "NOTIFICATION_ARG4";

	public static String MODE = "mode";
	public static String MODE_ORDER_NOTIFY = "MODE_ORDER_NOTIFY"; // 订单提醒
	public static String MODE_DAILTY_NOTIFY = "MODE_DAILTY_NOTIFY"; // 每日惠提醒
	public static String MODE_PUSH = "MODE_PUSH"; // 推送
	public static String MODE_UPLOAD_LOG = "MODE_UPLOAD_LOG"; // 上传日志
	public static String MODE_REGISTER = "MODE_REGISTER";
	public static String MODE_GROUPON_NOTIFY = "MODE_GROUPON_NOTIFY";
	public static String MODE_CANCEL_GROUPON_NOTIFY = "MODE_CANCEL_GROUPON_NOTIFY";
	public static String MODE_SEND_NOTIFICATION_IMMEDIATELY = "MODE_SEND_NOTIFICATION_IMMEDIATELY";
	public static String MODE_UPGRADE = "MODE_UPGRADE";

	private Handler pushHandler; // 处理消息的Handler

	private Runnable pushRunnable; // 处理推送消息

	private NotificationManager mNotificationManager;

	private SharedPreferences sharedPreferences; // 存储对像

	private static final int PUSHING_DISABLED = -1;

	private static final String TAG = "PushInformationService";

	private boolean isPushBegined = false;

	private AlarmManager alarmMgr = null;

	private final List<String> notificationMsgs = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// init handler and runnable
		alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		sharedPreferences = getSharedPreferences(Const.STORE_NAME,
				Context.MODE_PRIVATE);
		isPushBegined = false;
		pushRunnable = new PushRunnable();
		pushHandler = new PushHandler();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		final Intent finalIntent = intent;
		String mode = finalIntent.getStringExtra("mode");
		Log.i("", "==========onStartCommand mode is " + mode);
		if (mode != null && mode.equals(MODE_ORDER_NOTIFY)) {

		} else if (mode != null && mode.equals(MODE_DAILTY_NOTIFY)) {

		} else if (mode != null && mode.equals(MODE_PUSH)) {
			startPush();
		} else if (mode != null && mode.equals(MODE_UPLOAD_LOG)) {

		} else if (mode != null && mode.equals(MODE_REGISTER)) {
			// registerTimer();
		} else if (mode != null
				&& mode.equals(MODE_SEND_NOTIFICATION_IMMEDIATELY)) {
			Intent notificationIntnet = (Intent) finalIntent
					.getParcelableExtra(NOTIFICATION_INTENT);
			sendNotificationImmedately(notificationIntnet);
		} else if (mode != null && mode.equals(MODE_UPGRADE)) {

		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void startPush() {
		if (!isPushBegined) {
			isPushBegined = true;
			pushHandler.post(pushRunnable);
		}
	}

	public static int getID(Intent intent) {
		String uri = intent.toURI();
		String[] args = uri.split(";");
		List<String> list = Arrays.asList(args);
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (String string : list) {
			sb.append(string).append(";");
		}
		return sb.toString().hashCode();
	}

	private void sendNotificationImmedately(final Intent intent) {
		final int id = getID(intent);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.tickerText = intent.getStringExtra(NOTIFICATION_TITLE);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				MicroStoreService.this, id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(MicroStoreService.this,
				intent.getStringExtra(NOTIFICATION_TITLE),
				intent.getStringExtra(NOTIFICATION_TEXT), pendingIntent);

		mNotificationManager.notify(id, notification);
		SpManager.getInstance().remove(String.valueOf(id));
	}

	// private void registerTimer() {
	// timer.scheduleAtFixedRate(new TimerTask() {
	//
	// @Override
	// public void run() {
	// count++;
	// if (count == 30) {
	// timer.cancel();
	// return;
	// }
	// Log.e("StartService", "Start Schedule");
	// Context context = MicroStoreService.this
	// .getApplicationContext();
	// if (context != null) {
	// try {
	// ConnectivityManager connectivityManager = (ConnectivityManager) context
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	// NetworkInfo activeNetInfo = connectivityManager
	// .getActiveNetworkInfo();
	// if (activeNetInfo != null) {
	// Log.e("StartService", "Have Active Network");
	// if (MicroStoreService.this.register()) {
	// Log.e("StartService", "StopService");
	// timer.cancel();
	// }
	// }
	// } catch (Exception e) {
	// // Log.error("StartService", e.getMessage());
	// }
	// } else {
	// // Log.debug("StartService", "No Active Network");
	// }
	// }
	// }, 0, 1000 * 30);
	//
	// String sDStateString = android.os.Environment.getExternalStorageState();
	// Log.e("StartService", sDStateString);
	// }

	private class PushRunnable implements Runnable {
		@Override
		public void run() {
			// 判断推送开关是否开启,并当前的时间是否在时间段内, 满足条件才调用接口
			getPushInformation();
		}
	}

	private class PushHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO 处理推送接口的消息
			switch (msg.what) {
			case R.id.push_getpushinformation:
				ResultVO<List<PushInformationVO>> resultVO = (ResultVO<List<PushInformationVO>>) msg.obj;
				if (resultVO != null) {
					// 获取推送信息
					List<PushInformationVO> results = resultVO.getData();
					if (results != null) {
						boolean hasData = false; // 标记是否有推送数据
						long nextPullTime = -1;
						long sendDate = -1;
						for (int i = 0; i < results.size(); i++) {
							PushInformationVO vo = results.get(i);
							if (i == 0) {
								// 第一条数据取nexttime和sendTime
								if (vo != null && vo.getNextTimePull() != null) {
									nextPullTime = vo.getNextTimePull();// 秒,不是毫秒数
								}
								if (vo != null && vo.getSendDate() != null) {
									sendDate = vo.getSendDate();
								}
								if (vo != null
										&& !TextUtils.isEmpty(vo
												.getMsgContent())) {// 说明有数据
									hasData = true;
								}
							}
						}
						if (hasData) {
							addNotification(results);
							Log.i(TAG, "LogReporting called, hasData");
							// 上报接口调用
						}
						if (nextPullTime != -1l) {
							Log.i(TAG, "after " + nextPullTime
									+ " seconds  get push information again");
							postDelayed(pushRunnable, nextPullTime * 1000);
						} else {
							Log.i(TAG, "nextPullTime error, it is "
									+ nextPullTime);
							postDelayed(pushRunnable, 10 * 60 * 1000);
						}
					} else {
						Log.i(TAG, "may be your net is busy");
						postDelayed(pushRunnable, 10 * 60 * 1000);
					}
				}

				break;
			case PUSHING_DISABLED:
				Log.i(TAG, "push is disabled or not on time");
				postDelayed(pushRunnable, 10 * 60 * 1000);
				break;
			}
		};
	}

	/**
	 * 获取推送信息
	 */
	private void getPushInformation() {
		Log.i(TAG, "getPushInformation called");
		if (isInPushingTime()) {
//			Request reqTask = AppContext.newRequest();
//
//			HashMap<String, Object> param = ParamHelper.jsonParam(
//					ApiConst.GETPUSHINFORMATION, null);
//
//			reqTask.applyParam(Request.GET,
//					UrlHelper.getUrl(ApiConst.getPushInformationPath), param,
//					new TypeToken<ResultVO<List<PushInformationVO>>>() {
//					}.getType());
//
//			reqTask.setCallBack(pushHandler, R.id.push_getpushinformation);
//			reqTask.execute();
			
			new Thread(new Runnable() {
				public void run() {
					try {
				    	RequestVo requestVo = new RequestVo();
						requestVo.requestUrl = Config.SERVLET_URL_GET_PUSHINFORMATION;
						requestVo.context = MicroStoreService.this;
						requestVo.jsonParser = new PushInformationParser();
						ResultVO<List<PushInformationVO>> result = (ResultVO<List<PushInformationVO>>) HttpUtil.post(requestVo);
						Message msg = Message.obtain();
						msg.what = R.id.push_getpushinformation;
						msg.obj = result;
						pushHandler.sendMessage(msg);
					} catch (Exception e) {
						Logger.d(TAG, e.getMessage());
					}
				}
			}).start();			

		} else {
			// Handler 发消息, 60秒之后再来
			Log.i(TAG, "not on time");
			pushHandler.sendEmptyMessage(PUSHING_DISABLED);
		}
	}

	/**
	 * 把接口返回的推送信息以通知栏的形式提示用户
	 * 
	 * @param results
	 *            推送信息
	 */
	private void addNotification(List<PushInformationVO> results) {
		for (PushInformationVO push : results) {
			String msgContentJson = push.getMsgContent();

			MsgContentProxy msgContentProxy = new Gson().fromJson(msgContentJson,
					new TypeToken<MsgContentProxy>() {
					}.getType());
			MsgContent msgContent = null;
			String pId = "";
			int pType = 0;
			String title = Const.NOTIFICATION_COMMON_TITLE;
			String msg = "";
			
			if(msgContentProxy != null){
				msgContent = msgContentProxy.getMsgContent();
				UserLevel userLevel = null;
				if(msgContent != null){
					pId = msgContent.getpId();
					pType = msgContent.getpType();
					if(pType == 3){
						// 商品下架提醒
						title = Const.NOTIFICATION_OFF_TITLE;
					}else if(pType == 4){
						// 商品无库存提醒
						title = Const.NOSTOCK_TITLE;
					}
					
					userLevel = msgContent.getUserLevel();
					if (userLevel != null) {
						msg = userLevel.getMsg();
					}
				}
			}

			if (notificationMsgs.contains(push.getMsgContent())) {
				// 如果是重复的消息就不要添加通知了
				continue;
			}
			notificationMsgs.add(push.getMsgContent());

			Notification notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			notification.defaults = Notification.DEFAULT_SOUND;
			notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.tickerText = msg;
			Intent i = new Intent(MicroStoreService.this, MainActivity.class);
			// Intent intent = getForwardIntentFromPageId(push);
			// if (intent.getExtras() != null) {
			// i.putExtras(intent.getExtras());
			// }
			// i.putExtra("for_word_action",
			// intent.getComponent().getClassName());
			i.putExtra("push_info_vo", push);
			/*
			 * PendingIntent pendingIntent = PendingIntent.getActivity(
			 * MicroStoreService.this, 0, intent,
			 * PendingIntent.FLAG_UPDATE_CURRENT);
			 */
			PendingIntent pendingIntent = PendingIntent.getActivity(
					MicroStoreService.this, 0, i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(MicroStoreService.this, title, msg,
					pendingIntent);
			// 跟订单相关的, 用orderId作为通知栏的Id,其他的用push的hashCode
			boolean aboutOrderId = aboutOrderId(push.getPageId());
			int notifyId = aboutOrderId ? Long.valueOf(push.getOrderCode())
					.intValue() : push.hashCode();
			mNotificationManager.notify(notifyId, notification);
		}
	}

	/**
	 * 判断该pageId对应的推送消息是否与订单相关
	 * 
	 * @param pId
	 *            pageId
	 * @return true是相关
	 */
	private boolean aboutOrderId(String pId) {
		PageIdEnum pe = PageIdEnum.valueFromPId(pId);
		return pe == PageIdEnum.PACKAGE_TRACK_STORE
				|| pe == PageIdEnum.PACKAGE_TRACK_MALL || pe == PageIdEnum.PAY;
	}

	/**
	 * 判断是否是获取推送时间的时间段
	 * 
	 * @return true表示推送开关是on并且当前时间在推送时间段之内
	 */
	private boolean isInPushingTime() {
		boolean isPushingOpened = sharedPreferences.getBoolean(
				Const.STORE_IS_PUSHING, true);
		String startTime = sharedPreferences.getString(
				Const.STORE_PUSH_START_TIME, "9");
		String endTime = sharedPreferences.getString(Const.STORE_PUSH_END_TIME,
				"23");
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int startTimeForInt = Integer.valueOf(startTime);
		int endTimeForInt = Integer.valueOf(endTime);
		if (startTimeForInt < endTimeForInt) {
			// 当天时间之内推送
			return isPushingOpened
					&& (currentHour >= startTimeForInt && currentHour < endTimeForInt);
		} else {
			// 跨日的推送, 如果是两个相同的时间, 则第二个条件判断是恒true的
			return isPushingOpened
					&& (currentHour >= startTimeForInt || currentHour < endTimeForInt);
		}
	}

	/**
	 * pageId的枚举
	 * 
	 * @author Xu Hui 2013-08-12
	 * 
	 */
	private enum PageIdEnum {
		/* 物流推送 */
		PACKAGE_TRACK_STORE, PACKAGE_TRACK_MALL,
		/* 抵用券推送,返利余额发放 */
		COUPON_OR_RETURNBALANCE,
		/* 支付推送 */
		PAY,
		/* 活动推送 */
		PROMOTIOM,
		/* 闪购 */
		FLASH_SALE,
		/* 闪购支付完成 */
		FLASH_SALE_PAY,
		/* 团购详情 */
		GROUPON_DETAIL,
		/* 品牌团详情 */
		BRAND_DETAIL;

		static PageIdEnum valueFromPId(String pId) {
			if ("1y".equals(pId)) {
				return PACKAGE_TRACK_STORE;
			} else if ("1m".equals(pId)) {
				return PACKAGE_TRACK_MALL;
			} else if ("2".equals(pId)) {
				return PROMOTIOM;
			} else if ("3".equals(pId)) {
				return PAY;
			} else if ("4".equals(pId)) {
				return COUPON_OR_RETURNBALANCE;
			} else if ("5".equals(pId)) {
				return FLASH_SALE_PAY;
			} else if ("6".equals(pId)) {
				return FLASH_SALE;
			} else if ("7".equals(pId)) {
				return GROUPON_DETAIL;
			} else if ("8".equals(pId)) {
				return BRAND_DETAIL;
			}
			return null;
		}
	}

	/**
	 * PromotionType
	 * 
	 * @author Xu Hui 2013-8-12
	 * 
	 */
	private enum PromotionTypeEnum {
		/* CMS */
		CMS,
		/* 满减 */
		CASH_OFF,
		/* 赠品 */
		GIFT,
		/* n元n件, promotionId传来的是promotionLevelId */
		N_YUAN_N_JIAN,
		/* 抵用券 */
		COUPON,
		// Native cms
		NATIVE_CMS,
		// 摇一摇
		ROCK;

		static PromotionTypeEnum valueFromPromotionType(String pType) {
			if ("1y".equals(pType) || "1m".equals(pType)) {
				return CMS;
			} else if ("2y".equals(pType) || "2m".equals(pType)) {
				return CASH_OFF;
			} else if ("3y".equals(pType) || "3m".equals(pType)) {
				return GIFT;
			} else if ("4y".equals(pType) || "4m".equals(pType)) {
				return N_YUAN_N_JIAN;
			} else if ("5y".equals(pType) || "5m".equals(pType)) {
				return COUPON;
			} else if ("13".equals(pType)) {
				return ROCK;
			} else if ("12".equals(pType)) {
				return NATIVE_CMS;
			}
			return null;
		}
	}

}
