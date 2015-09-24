package com.thestore.microstore;

import java.math.BigDecimal;
import java.util.HashMap;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.BankParser;
import com.thestore.microstore.parser.OrderCreateParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.Tracker;
import com.thestore.microstore.vo.OrderCreate;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.proxy.Bank;
import com.thestore.microstore.vo.proxy.BankProxy;

/**
 * 
 * 订单创建页  订单完成页
 * 
 * 2014-9-16 上午10:22:37
 */
public class OrderCreateActivity extends BaseWapperActivity {

	private static String TAG = "vdian_OrderCreateActivity";

	// 订单编号
	private String orderCode;

	private TextView oca_desc_text;
	// 立即支付
	private Button pay_immediately_button;

	private String hourStr;

	/** 支付方式 */
	private Spinner oca_online_pay;

	@Override
	public void onClick(View arg0) {

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.order_create_activity);
		context = this;
		setLeftTextTo("", false, null);// 隐藏左边返回按钮
		setTitle("订单已生成");
		setRightTextTo("查看订单", true, null);
		Tracker.trackPage(Const.PAGE_CODE_VDIAN_ORDERDOFINISH);// BI埋点
	}

	@Override
	protected void process() {

		Intent intent = getIntent();
		orderCode = intent.getStringExtra("orderCode");

		Log.d(TAG, "订单完成页，订单code：" + orderCode);
		// TODO:临时测试 begin
//		 orderCode = "221802634589";
		// TODO:临时测试 end

		RequestVo requestVo = new RequestVo();
		HashMap<String, String> requestDataMap = new HashMap<String, String>();
		requestDataMap.put("ordercode", orderCode);
		requestVo.context = this;
		requestVo.requestUrl = Config.SERVLET_URL_ORDERDETAIL_BYCODE;
		requestVo.requestDataMap = requestDataMap;
		requestVo.jsonParser = new OrderCreateParser();
		requestVo.testObject = null; // loadData();// 测试数据

		asynForLoadDataAndDraw(requestVo, new DataCallback<OrderCreate>() {

			@Override
			public void processData(OrderCreate paramObject) {
				draw(paramObject);
			}

		});

	}

	@Override
	protected void initAttrs() {

	}

	/**
	 * 右侧查看订单按钮被点击
	 */
	@Override
	protected void onHeadRightButton(View v) {
		gotoOrderDetail();
	}

	private void draw(OrderCreate orderCreate) {

		pay_immediately_button = (Button) findViewById(R.id.pay_immediately_button); // 立即支付按钮
		TextView oca_online_pay_text = (TextView) findViewById(R.id.oca_online_pay_text); // 支付部分左侧文字说明，默认为在线支付
		oca_online_pay = (Spinner) findViewById(R.id.oca_online_pay); // 支付部分 右侧下拉支付方式列表
		oca_desc_text = (TextView) findViewById(R.id.oca_desc); // 订单描述

		// 如果订单出错 或者订单状态并非是 等待支付(","orderStatus":3,"orderStatusString":"等待支付",") 则，隐藏支付方式和立即支付按钮
		if (!"0".equals(orderCreate.getRtnCode()) || !"3".equals(orderCreate.getOrderStatus())) {

			// 正常情况下，这部分内容存在几率较少

			oca_desc_text.setText("订单已生成，祝您购物愉快！");

			oca_online_pay_text.setVisibility(View.INVISIBLE);
			oca_online_pay.setVisibility(View.INVISIBLE);
			pay_immediately_button.setVisibility(View.INVISIBLE);

			return;
		}

		orderCode = orderCreate.getOrderCode();
		((TextView) findViewById(R.id.oca_orderCode)).setText(orderCreate.getOrderCode()); // 订单编号
		((TextView) findViewById(R.id.oca_childNum)).setText(orderCreate.getChildNum()); // 包裹数
		((TextView) findViewById(R.id.oca_productCount)).setText(orderCreate.getProductCount()); // 商品数
		((TextView) findViewById(R.id.oca_orderAmount)).setText("￥" + orderCreate.getOrderAmount()); // 金额

		// 只有网上支付类型才会有倒计时, 如果此处的 1 不懂，请查看 OrderCreate 类中的字段说明
		if (orderCreate.getPayServiceType().equals("1")) {

			oca_online_pay_text.setText("在线支付");
			oca_online_pay.setVisibility(View.VISIBLE);

			// 如果订单取消时间和剩余时间都不为空才会有倒计时
			if (!TextUtils.isEmpty(orderCreate.getCancelTime()) && !TextUtils.isEmpty(orderCreate.getLefttime())) {
				hourStr = new BigDecimal(orderCreate.getCancelTime()).divide(new BigDecimal(60)).toString(); // 获取取消到期时间

				// 定时器
				BigDecimal allTimes = new BigDecimal(orderCreate.getLefttime()).multiply(new BigDecimal(1000)); // 秒转毫秒
				new CountDownTimer(allTimes.longValue(), 1000) {

					@Override
					public void onTick(long millisUntilFinished) {
						String showMsg = "我们将保留订单hour小时，请在 time 内完成支付，祝您购物愉快！";
						showMsg = showMsg.replace("hour", hourStr);
						showMsg = showMsg.replace("time", "<font color=\"#FF0000\">" + formatTime(millisUntilFinished) + "</font>");
						oca_desc_text.setText(Html.fromHtml(showMsg));
					}

					@Override
					public void onFinish() {
						String showMsg = "我们将保留订单hour小时，请在 time 内完成支付，祝您购物愉快！";
						showMsg = showMsg.replace("hour", hourStr);
						showMsg = showMsg.replace("time", "<font color=\"#FF0000\">00:00:00</font>");
						oca_desc_text.setText(Html.fromHtml(showMsg));

						// 订单已生成页面，如果订单倒计时为0时，这个时候订单会自动取消，进入订单详情页
						gotoOrderDetail();

					}
				}.start();
			} else {
				oca_desc_text.setText("订单已生成，请尽快完成支付，祝您购物愉快！");
			}
		} else if (orderCreate.getPayServiceType().equals("2") || orderCreate.getPayServiceType().equals("5")) {
			
			oca_desc_text.setText("订单已生成，祝您购物愉快！");
			
			oca_online_pay_text.setText("货到刷卡");
			oca_online_pay.setVisibility(View.INVISIBLE);
			pay_immediately_button.setVisibility(View.INVISIBLE);
		} else {

			// 正常情况下，这部分内容是不存在的。
			oca_desc_text.setText("订单已生成，祝您购物愉快！");

			oca_online_pay_text.setVisibility(View.INVISIBLE);
			oca_online_pay.setVisibility(View.INVISIBLE);
			pay_immediately_button.setVisibility(View.INVISIBLE);
		}

		// -- 订单详情
		RelativeLayout oca_order_detail_layout = (RelativeLayout) findViewById(R.id.oca_order_detail_layout);
		oca_order_detail_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				gotoOrderDetail();
			}
		});

		// -- 支付方式
		RequestVo requestVo = new RequestVo();
		HashMap<String, String> requestDataMap = new HashMap<String, String>();
		requestDataMap.put("osType", Config.OS_TYPE);
		requestDataMap.put("ordertype", orderCreate.getOrderType()); // 订单类型
		requestDataMap.put("businessType", orderCreate.getBusinessType()); // 业务类型 14 为手机订单
		requestVo.context = this;
		requestVo.requestUrl = Config.SERVLET_URL_GET_BANKLIST;
		requestVo.requestDataMap = requestDataMap;
		requestVo.jsonParser = new BankParser(CookiesUtil.getPaymethod());
		requestVo.testObject = null;

		asynForLoadDataAndDraw(requestVo, new DataCallback<BankProxy>() {

			@Override
			public void processData(BankProxy paramObject) {
				ArrayAdapter<Bank> payAdapter = new ArrayAdapter<Bank>(OrderCreateActivity.this, R.layout.spinner_item_self, paramObject.getBankList());
				payAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
				oca_online_pay.setAdapter(payAdapter);
				oca_online_pay.setSelection(paramObject.getPosition());
				oca_online_pay.setPrompt("支付方式");
			}

		});

		// -- 立即支付
		pay_immediately_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				Bank bank = (Bank) oca_online_pay.getSelectedItem();

				// 保存
				CookiesUtil.setPayment(context, bank.getCode());

				// 跳转到H5页面
				Intent intent = new Intent();
				intent.setClass(OrderCreateActivity.this, WebViewActivity.class);
				intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/mycenter/gopay.do?orderCode=" + orderCode + "&paymethod=" + bank.getCode());
				intent.putExtra("showTitle", "true");
				intent.putExtra("headTitle", "在线支付");
				
				startActivity(intent);
//				CookiesUtil.setNativeURI(context, "toNative-OrderCreateActivity?orderCode="+orderCode);
//				finish();
			}

		});

	}

	// -- 工具方法

	/*
	 * 毫秒转化时分秒毫秒
	 */
	public String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		// Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + ":");
		}
		if (hour >= 0) {
			sb.append(String.format("%02d", hour) + ":");
		}
		if (minute >= 0) {
			sb.append(String.format("%02d", minute) + ":");
		}
		if (second >= 0) {
			sb.append(String.format("%02d", second) + "");
		}
		// if (milliSecond > 0) {
		// sb.append(milliSecond + "毫秒");
		// }
		return sb.toString();
	}

	/**
	 * 跳转到订单详情界面
	 */
	private void gotoOrderDetail() {
		Intent intent = new Intent();
//		intent.setClass(OrderCreateActivity.this, MainActivity.class);
//		intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/mycenter/orderDetail.do?orderCode=" + orderCode+"&returnUrl=toNative-OrderCreateActivity");
		intent.setClass(OrderCreateActivity.this, OrderDetailActivity.class);
		intent.putExtra("orderCode", orderCode);
				
		startActivity(intent);
//		finish();
	}

}
