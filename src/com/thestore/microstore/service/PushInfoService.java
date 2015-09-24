package com.thestore.microstore.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.yihaodian.mobile.vo.push.PushInformationVO;

public class PushInfoService extends Service {

	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent != null) {
			PushInformationVO vo = (PushInformationVO) intent
					.getSerializableExtra("push_info_vo");
			if (vo != null) {
				try {
					String promotionId = vo.getPromotionId();
					String promotionType = vo.getPromotionType();
					Long pId = null;
					Integer pType = null;
					if (!TextUtils.isEmpty(promotionId)) {
						pId = Long.valueOf(promotionId.replaceAll("[a-zA-Z]",
								""));
					}
					if (!TextUtils.isEmpty(promotionType)) {
						pType = Integer.valueOf(promotionType.replaceAll(
								"[a-zA-Z]", ""));
					}
					String pageId = vo.getPageId();
					Long pageIdForlong = null;
					if (!TextUtils.isEmpty(pageId)) {
						pageIdForlong = Long.valueOf(pageId.replaceAll(
								"[a-zA-Z]", ""));
					}

					messageOpen(intent, pageIdForlong, pId, pType);
				} catch (NumberFormatException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	private void messageOpen(final Intent intent, Long pageId,
			Long promotionId, Integer promotionType) {/*
													 * MainAsyncTask task = new
													 * MainAsyncTask
													 * (MainAsyncTask
													 * .PUSH_MESSAGEOPEN, new
													 * MainAsyncTask
													 * .AsyncTaskCallback() {
													 * public void
													 * callBack(Object result) {
													 * String clazz =
													 * intent.getStringExtra
													 * ("for_word_action");
													 * Lg.d(clazz); try { Intent
													 * i = new
													 * Intent(PushInfoService
													 * .this, Class
													 * .forName(clazz)); if
													 * (intent.getExtras() !=
													 * null) {
													 * i.putExtras(intent
													 * .getExtras()); } Bundle b
													 * = i.getExtras(); if (b !=
													 * null) { for (String key :
													 * b.keySet()) { Lg.d(key +
													 * ":" + b.get(key)); } }
													 * i.setFlags(Intent.
													 * FLAG_ACTIVITY_NEW_TASK |
													 * Intent.
													 * FLAG_ACTIVITY_BROUGHT_TO_FRONT
													 * | Intent.
													 * FLAG_ACTIVITY_CLEAR_TOP);
													 * startActivity(i); } catch
													 * (ClassNotFoundException
													 * e) { e.printStackTrace();
													 * } } }, null);
													 * task.execute
													 * (DBHelper.getTrader(),
													 * new Date().getTime(),
													 * pageId, promotionId,
													 * promotionType,
													 * UserInfo.getInstance
													 * ().getToken());
													 */
	}

}
