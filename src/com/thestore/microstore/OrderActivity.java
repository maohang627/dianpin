package com.thestore.microstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.thestore.microstore.adapter.OrderLoadOthersListener;
import com.thestore.microstore.adapter.OrderProListAdapter;
import com.thestore.microstore.adapter.PaymentAdapter;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.AreaParser;
import com.thestore.microstore.parser.CityParser;
import com.thestore.microstore.parser.InvoiceParser;
import com.thestore.microstore.parser.OrderInitParser;
import com.thestore.microstore.parser.ProvinceParser;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.parser.SummitOrderOutputParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.ProvinceCityCountyManager;
import com.thestore.microstore.util.Tracker;
import com.thestore.microstore.vo.Address;
import com.thestore.microstore.vo.IdValue;
import com.thestore.microstore.vo.Invoice;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;
import com.thestore.microstore.vo.SummitOrderOutput;
import com.thestore.microstore.vo.commom.BaseEntity;
import com.thestore.microstore.vo.coupon.ShoppingPaymentCoupon;
import com.thestore.microstore.vo.order.Delivery;
import com.thestore.microstore.vo.order.InvoiceShowVo;
import com.thestore.microstore.vo.order.InvoicesCommDisplay;
import com.thestore.microstore.vo.order.Order;
import com.thestore.microstore.vo.order.OrderPro;
import com.thestore.microstore.vo.order.Payment;
import com.thestore.microstore.vo.order.ProPackage;
import com.thestore.microstore.vo.proxy.Area;
import com.thestore.microstore.vo.proxy.AreaProxy;
import com.thestore.microstore.vo.proxy.City;
import com.thestore.microstore.vo.proxy.CityProxy;
import com.thestore.microstore.vo.proxy.Province;
import com.thestore.microstore.vo.proxy.ProvinceProxy;
import com.thestore.microstore.vo.shoppingcart.CartItem;
import com.thestore.microstore.widget.ListViewForScrollView;
import com.thestore.microstore.widget.UISwitchButton;

/**
 * 
 * 订单结算
 * 
 * 2014-9-8 上午11:31:03
 */
public class OrderActivity extends BaseWapperActivity implements OnItemSelectedListener {

	private static String TAG = "vdian_OrderActivity";

	/** 省份 */
	private Spinner provinceSpinner;
	private ArrayAdapter<Province> provinceAdapter;

	/** 城市 */
	private Spinner citySpinner;

	/** 区域 */
	private Spinner areaSpinner;

	/** 地址栏layout */
	private LinearLayout orderAddressLayout;

	/** 发票类型 */
	private Spinner invoiceTypeSpinner;
	/** 发票抬头 */
	private EditText oa_invoice_title;
	/** 发票内容 */
	private Spinner oa_invoice_content;
	/** 是否开发票 */
	private boolean isNeedInvoice;
	/** 是否包含普通商品 */
	private boolean isContainComm;
	/** 是否包含强制开票商品 */
	private boolean isContainMust;
	/** 是否开发票 左侧label */
	private TextView oa_is_need_invoice_text;
	private PaymentAdapter paymentAdapter;

	/** 因支付方式选择用的listview实现，初始化是默认为list中的第一个元素，用户选择某一行后，改变其值 */
	private Payment curPayment;

	/** 支付方式 */
	private ListViewForScrollView oa_payments;

	private TextView oa_address_save; // 我的地址保存

	private List<ProPackage> proPackageList;

	/** 订单信息 */
	private Order order;

	/** 是否需要输入收货地址 */
	private boolean isInput;

	/** 是否开具发票 */
	private UISwitchButton oa_is_need_invoice;
	
	private String mProvinceId = "1"; // 默认上海	
	private ProvinceCityCountyManager provinceCityCountyManager;

	@Override
	public void onClick(View view) {

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.order_activity);
		context = this;
		super.setTitle("订单确认");
		super.setRightTextTo("", false, null);
		provinceCityCountyManager = ProvinceCityCountyManager.getInstance(context);
		Tracker.trackPage(Const.PAGE_CODE_VDIAN_INITORDER);// BI埋点
	}

	/**
	 * 异步加载 读取数据以及绘制页面
	 */
	@Override
	protected void process() {

		// 参数准备
		Intent intent = getIntent();
		String resource = intent.getStringExtra("resource");
		String shoppingBizType = intent.getStringExtra("shoppingBizType");

		// 如果不是从购物车跳转过来的，而是从 我的地址列表或者订单页
		if (!"ShopCartActivity".equals(resource)) {
			draw(intent.getSerializableExtra("order"));
		} else {
			String checkboxStr = intent.getStringExtra("checkboxStr");
			RequestVo requestVo = RequestVo.setCommontParam(context);
			HashMap<String, String> postParamMap = requestVo.requestDataMap;
			if("FAST_VDIAN_MOBILE".equals(shoppingBizType)){//1键购
				postParamMap.put("shoppingBizType", "FAST_VDIAN_MOBILE");
			}else{
				postParamMap.put("checkboxStr", checkboxStr);
			}
			requestVo.jsonParser = new OrderInitParser();
			requestVo.requestUrl = Config.SERVLET_URL_INIT_CHECKOUT;
			requestVo.testObject = null; // loadData();// 测试数据

			// 请求调用
			asynForLoadDataAndDraw(requestVo, new DataCallback<Order>() {
				@Override
				public void processData(Order paramObject) {
					draw(paramObject);
				}

			});
		}

	}

	@Override
	protected void initAttrs() {

	}

	private void draw(Object obj) {

		ScrollView sv = (ScrollView) findViewById(R.id.order_scroll_view);
		sv.smoothScrollTo(0, 0);

		order = (Order) obj;

		// -- 收货地址

		// 设置收货地址的显示layout
		orderAddressLayout = (LinearLayout) findViewById(R.id.order_address);

		// 前提条件: 用户收获地址为空, 如果当前收货地址不为空，则还需要判断当前省份下收货地址是否为空。
		if (order.getSelectedAddress() == null) {
			isInput = true;
		} else {
			List<Address> addressList = order.getAddressList();
			for (Address addr : addressList) {
				if (addr.getProvinceId().equals(CookiesUtil.getProvinceId())) {
					isInput = false;

					break;
				}
			}
		}
		isInput = (order.getSelectedAddress() == null);

		if (isInput) {
			orderAddressInputActivity();
		} else {
			orderAddressShowActivity(order.getSelectedAddress(), order.getAddressList());
		}
		
		// -- 抵用券
		ShoppingPaymentCoupon paymentCoupon = order.getPaymentCoupon();			
		if(paymentCoupon!=null && paymentCoupon.getUseableCouponNum()>0){
			
			findViewById(R.id.oa_coupon_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.oa_coupon_line).setVisibility(View.VISIBLE);
			TextView oa_coupon_num = (TextView)findViewById(R.id.oa_coupon_num);
			oa_coupon_num.setText(paymentCoupon.getUseableCouponNum()+"张未使用");
			View coupon_layout = (View)findViewById(R.id.oa_coupon);			
			coupon_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 跳转到选择抵用券列表页
					Intent intent = getIntent();
					intent.setClass(OrderActivity.this.context, CouponListActivity.class);			
					intent.putExtra("paymentCoupon", order.getPaymentCoupon());
					startActivity(intent);
					
				}
			});
			
			
		}
		
		

		// -- 支付方式
		List<Payment> paymentList = order.getPaymentList();
		oa_payments = (ListViewForScrollView) findViewById(R.id.oa_payments);
		if (paymentList == null || paymentList.isEmpty()) { // 如果支付方式信息为空 则隐藏支付方式信息
			Payment payment = new Payment();
			payment.setId("0");
			payment.setName("无可用支付方式");
			paymentList.add(payment);
			paymentAdapter = new PaymentAdapter(OrderActivity.this, R.layout.order_payment_items_activity, paymentList);
			oa_payments.setAdapter(paymentAdapter);
		} else {
			// 默认为第一个
			curPayment = paymentList.get(0);
			paymentAdapter = new PaymentAdapter(OrderActivity.this, R.layout.order_payment_items_activity, paymentList);
			oa_payments.setAdapter(paymentAdapter);
			oa_payments.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {

					// 动态改变支付方式的值
					curPayment = paymentAdapter.getItem(position);

					paymentAdapter.setSelectedIndex(position);
					paymentAdapter.notifyDataSetChanged();
				}
			});

		}

		// -- 发票信息

		oa_invoice_title = (EditText) findViewById(R.id.oa_invoice_title); // 获取发票抬头文本框
		oa_is_need_invoice_text = (TextView) findViewById(R.id.oa_is_need_invoice_text); // 是否开具发票 左侧label

		// 是否开具发票
		oa_is_need_invoice = (UISwitchButton) findViewById(R.id.oa_is_need_invoice);
		oa_is_need_invoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 如果需要收货地址， 同时收货地址右侧的保存按钮，为显示状态 则提示用户先保存后货地址
				if (isInput && ((TextView) findViewById(R.id.oa_address_save)).getVisibility() == View.VISIBLE && isChecked) {
					oa_is_need_invoice.setChecked(false);
					// 弹出层提示用户
					Builder builder = new Builder(OrderActivity.this);
					builder.setTitle("");
					builder.setMessage("请填写收货地址！");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							// 关闭弹出框
							dialog.dismiss();
						}
					});
					Dialog alertDialog = builder.create();
					alertDialog.setCancelable(false);
					alertDialog.setCanceledOnTouchOutside(false); // 点击外部不会消失
					alertDialog.show();

				} else {
					isNeedInvoice = isChecked;

					if (isChecked) {
						// Toast.makeText(OrderActivity.this, "开启", Toast.LENGTH_SHORT).show();

						findViewById(R.id.oa_invoice_layout).setVisibility(View.VISIBLE);
						oa_is_need_invoice_text.setText("需要发票");

						// 设置发票类型
						invoiceTypeSpinner = (Spinner) findViewById(R.id.oa_invoice_type);
						List<IdValue> typeList = order.getInvoiceTypeList();
						if (typeList != null && !typeList.isEmpty()) {
							ArrayAdapter<IdValue> invoiceTypeAdapter = new ArrayAdapter<IdValue>(OrderActivity.this, R.layout.spinner_item_self, order.getInvoiceTypeList());
							invoiceTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
							invoiceTypeSpinner.setAdapter(invoiceTypeAdapter);
							invoiceTypeSpinner.setPrompt("发票类型");
							invoiceTypeSpinner.setOnItemSelectedListener(OrderActivity.this);
						}

						// 设置发票内容
						oa_invoice_content = (Spinner) findViewById(R.id.oa_invoice_content);
						List<String> invoiceContentList = order.getInvoiceContentList();
						if (invoiceContentList != null && !invoiceContentList.isEmpty()) {
							ArrayAdapter<String> contentAdapter = new ArrayAdapter<String>(OrderActivity.this, R.layout.spinner_item_self, order.getInvoiceContentList());
							contentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
							oa_invoice_content.setAdapter(contentAdapter);
							oa_invoice_content.setPrompt("发票内容");
						}
						
						// 设置发票类型和内容的默认值
						int invoiceTypePosition = 0; // 默认：0 个人
						String invoiceContent = "日用品";
						int invoiceContentPosition = 0; // 
						// 上一次开的发票内容
						InvoicesCommDisplay invoicesComm = order.getInvoicesCommDisplay();
						if(invoicesComm != null){
							if(!TextUtils.isEmpty(invoicesComm.getTitleType())){
								// 发票类型的默认值
								invoiceTypePosition = Integer.valueOf(invoicesComm.getTitleType());
								// 发票抬头的默认值
								if("1".equals(invoicesComm.getTitleType()) && !TextUtils.isEmpty(invoicesComm.getTitle())){
									oa_invoice_title.setText(invoicesComm.getTitle());
								}								
								// 发票内容的默认值								
								if(!TextUtils.isEmpty(invoicesComm.getContent())){
									invoiceContent = invoicesComm.getContent();
									if (invoiceContentList != null && !invoiceContentList.isEmpty()) {
										int position = invoiceContentList.indexOf(invoiceContent);
										if(position >= 0){
											invoiceContentPosition = position;
										}
									}
								}
							}
						}
						invoiceTypeSpinner.setSelection(invoiceTypePosition);
						oa_invoice_content.setSelection(invoiceContentPosition);
						
					} else {
						// Toast.makeText(OrderActivity.this, "关闭", Toast.LENGTH_SHORT).show();

						oa_is_need_invoice_text.setText("不需要发票");
						findViewById(R.id.oa_invoice_layout).setVisibility(View.GONE);
					}
				}

			}
		});
		
		InvoiceShowVo invoiceShow = order.getInvoiceShowVo();
		if(invoiceShow != null){
			// 包含强制开票商品
			if("true".equals(invoiceShow.getIsContainMust())){
				isContainMust = true;
				oa_is_need_invoice.setChecked(true);
				oa_is_need_invoice.setVisibility(View.INVISIBLE);
				
				// 包含普通商品
				if("true".equals(invoiceShow.getIsContainComm())){
					isContainComm = true;
					oa_invoice_content.setVisibility(View.VISIBLE);
				}else{
					isContainComm = false;
					oa_invoice_content.setVisibility(View.GONE);
				}
				
			}else{
				isContainMust = false;
			}
		}

		// -- 商品列表

		proPackageList = order.getProPackageList();

		LinearLayout order_pro_package_layout = (LinearLayout) findViewById(R.id.order_pro_package_layout);
		order_pro_package_layout.removeAllViews();
		if (proPackageList != null && !proPackageList.isEmpty()) {
			for (ProPackage pro : proPackageList) {
				LinearLayout prosDefault = (LinearLayout) getLayoutInflater().inflate(R.layout.order_pros_default_activity, null);

				TextView order_pro_name = (TextView) prosDefault.findViewById(R.id.order_pack_name);
				TextView order_pro_desc = (TextView) prosDefault.findViewById(R.id.order_pro_desc);
				TextView order_pro_priceDesc = (TextView) prosDefault.findViewById(R.id.order_pack_price_desc);

				order_pro_name.setText(pro.getName());
				order_pro_desc.setText(pro.getDesc());
				// 无收货地址时，商品清单上“运费”需要隐藏
				if (isInput && ((TextView) findViewById(R.id.oa_address_save)).getVisibility() == View.VISIBLE){
					order_pro_priceDesc.setVisibility(View.INVISIBLE);
				}else{
					order_pro_priceDesc.setVisibility(View.VISIBLE);
					order_pro_priceDesc.setText(Html.fromHtml("运费　<font color=\"#FF0000\">" + pro.getPriceDesc() + "</font>"));
				}

				ListView listView = (ListView) prosDefault.findViewById(R.id.order_pros_list);
				int totalCount = pro.getProList().size();
				OrderProListAdapter orderProListAdapter = new OrderProListAdapter(OrderActivity.this, R.layout.order_pros_items_activity, pro.getProList());
				LinearLayout load_others_layout = (LinearLayout) prosDefault.findViewById(R.id.load_others_layout);
				Button button = (Button) prosDefault.findViewById(R.id.load_others);

				// 如果数量小于2个，则不展现 显示更多按钮.
				if (totalCount <= Const.ORDER_PRO_DEFAULT_PAGE_SIZE) {
					orderProListAdapter.setCount(totalCount);
					load_others_layout.setVisibility(View.GONE);
				} else {
					orderProListAdapter.setCount(Const.ORDER_PRO_DEFAULT_PAGE_SIZE);
				}

				listView.setAdapter(orderProListAdapter);

				button.setOnClickListener(new OrderLoadOthersListener(button, orderProListAdapter, pro.getProList().size(), pro.getProList() ));

				order_pro_package_layout.addView(prosDefault);
			}
		}

		// 商品金额
		((TextView) findViewById(R.id.oa_amt)).setText("￥" + order.getAmt());

		// 运费
		((TextView) findViewById(R.id.oa_tran_amt)).setText("￥" + order.getTranAmt());

		// 还需要支付
		((TextView) findViewById(R.id.oa_need_pay)).setText("￥" + order.getNeedPay());

		// 结算
		findViewById(R.id.oa_sub_order).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String type = "0";
				if (invoiceTypeSpinner != null) {
					type = ((IdValue) invoiceTypeSpinner.getSelectedItem()).getId();
				}				
				String invoiceTitle = ((TextView) findViewById(R.id.oa_invoice_title)).getText().toString();
				
				// 如果需要收货地址， 同时收货地址右侧的保存按钮，为显示状态 则提示用户先保存后货地址
				if (isInput && ((TextView) findViewById(R.id.oa_address_save)).getVisibility() == View.VISIBLE) {
					showCommDialog("","请填写收货地址！",false);
				}else if(Const.INVOICE_UNIT.equals(type) && TextUtils.isEmpty(invoiceTitle)){
					// 选择单位，抬头不能为空
					showCommDialog("","发票抬头不能为空！",false);
				}else {
					// 提交订单
					findViewById(R.id.oa_sub_order).setEnabled(false);// 提交后禁用
					sub(isNeedInvoice, order.getSelectedAddress().getName(), order.getProPackageList());
				}
			}
		});
		
		//注册广播
		IntentFilter filter = new IntentFilter(CouponListActivity.action); 
        registerReceiver(broadcastReceiver, filter); 
				  
	}
	
	 BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {//收到广播消息，更新抵用券信息
				String paidByCoupon = intent.getExtras().getString("paidByCoupon");
				int useableCouponNum = 0;
				String amountNeed2Pay = intent.getExtras().getString("amountNeed2Pay");
								
				final ShoppingPaymentCoupon coupon =(ShoppingPaymentCoupon ) intent.getSerializableExtra("paymentCoupon");
				if(coupon!=null){
					useableCouponNum = coupon.getUseableCouponNum();
					View coupon_layout = (View)findViewById(R.id.oa_coupon);			
					coupon_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// 跳转到选择抵用券列表页
							Intent intent = getIntent();
							intent.setClass(OrderActivity.this.context, CouponListActivity.class);			
							intent.putExtra("paymentCoupon", coupon);
							startActivity(intent);
							
						}
					});
				}
				
				Double paidByCouponDouble = 0d;
				if(paidByCoupon!=null && !"".equals(paidByCoupon) ){
					 paidByCouponDouble = Double.parseDouble(paidByCoupon);
					
				}				
				TextView oa_coupon_num = (TextView)findViewById(R.id.oa_coupon_num);
				TextView oa_coupon_amount = (TextView)findViewById(R.id.oa_coupon_amount);
				if(paidByCouponDouble>0){						
					oa_coupon_amount.setText("￥"+paidByCouponDouble);
					oa_coupon_num.setText("已抵用");
				}else{
					oa_coupon_amount.setText("");
					oa_coupon_num.setText(useableCouponNum+"张未使用");
				}
				// 还需要支付
				((TextView) findViewById(R.id.oa_need_pay)).setText("￥" + amountNeed2Pay);
				
				
				
				
			} 			   
	   };


	// 收货地址 -- 输入layout
	private void orderAddressInputActivity() {

		// 设置layout
		orderAddressLayout.removeAllViews();
		orderAddressLayout.addView(getLayoutInflater().inflate(R.layout.order_address_input_activity, null));

		// 默认填写用户注册时的手机号
		((TextView) findViewById(R.id.oaia_tel)).setText(CookiesUtil.getUserMobileNumFrom());

		// 初始化省份
		provinceSpinner = (Spinner) findViewById(R.id.oaia_address_province);
		// 初始化城市
		citySpinner = (Spinner) findViewById(R.id.oaia_address_city);
		// 初始化区域
		areaSpinner = (Spinner) findViewById(R.id.oaia_address_area);

		// 显示收货地址保存按钮
		oa_address_save = (TextView) findViewById(R.id.oa_address_save);
		oa_address_save.setVisibility(View.VISIBLE);
		oa_address_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Province curProvince = (Province) provinceSpinner.getSelectedItem();
				String provinceId = curProvince.getId();

				// 判断用户选择的省份和系统中的省份, 如果不相同 则以页面传过来的省份为准，同时，不保存收货地址，直接跳转到购物车页面
				if (!provinceId.equals(CookiesUtil.getProvinceId())) {

					// 弹出层提示用户
					showDialog(CookiesUtil.getProvinceId(), curProvince.getValue(), provinceId);

				} else {

					// 保存收货地址
					saveReceiverInfo();
				}

			}
		});

		// 省份
//		RequestVo requestVo = new RequestVo();
//		requestVo.context = this.context;
//		requestVo.requestUrl = Config.SERVLET_URL_GET_ALLPROVINCE;
//		requestVo.jsonParser = new ProvinceParser();
//		setShowProcessDialog(Boolean.FALSE); // 设置不显示过程弹出层
//
//		asynForLoadDataAndDraw(requestVo, new DataCallback<ProvinceProxy>() {
//
//			@Override
//			public void processData(ProvinceProxy paramObject) {
//				Logger.d(TAG, "加载省份列表");
//				setShowProcessDialog(Boolean.TRUE); // 恢复弹出层显示
//				provinceAdapter = new ArrayAdapter<Province>(OrderActivity.this, R.layout.spinner_item_self, paramObject.getProvinceList());
//				provinceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
//				provinceSpinner.setAdapter(provinceAdapter);
//				provinceSpinner.setPrompt("省份");
//				// 读取Cookie中的provinceId作为初始值
//				for (int i = 0; i < provinceAdapter.getCount(); i++) {
//					Province province = provinceAdapter.getItem(i);
//					if(province != null && province.getId().equals(CookiesUtil.getProvinceId())){
//						provinceSpinner.setSelection(i);
//						break;
//					}
//				}
//				provinceSpinner.setOnItemSelectedListener(OrderActivity.this);
//
//			}
//
//		});
		
		Logger.d(TAG, "加载省份列表");
		mProvinceId = CookiesUtil.getProvinceId();
		ProvinceProxy provinces = provinceCityCountyManager.getProvinces(mProvinceId);
		provinceAdapter = new ArrayAdapter<Province>(OrderActivity.this, R.layout.spinner_item_self, provinces.getProvinceList());
		provinceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
		provinceSpinner.setAdapter(provinceAdapter);
		provinceSpinner.setPrompt("省份");
		provinceSpinner.setSelection(provinces.getPosition());
		provinceSpinner.setOnItemSelectedListener(OrderActivity.this);

	}

	// 收货地址 -- 展示layout
	private void orderAddressShowActivity(final Address defaultAddress, final List<Address> addressList) {
		// 设置layout
		orderAddressLayout.removeAllViews();
		orderAddressLayout.addView(getLayoutInflater().inflate(R.layout.order_address_show_activity, null));

		((TextView) findViewById(R.id.oasa_name)).setText(defaultAddress.getName()); // 设置收货人
		((TextView) findViewById(R.id.oasa_address)).setText(defaultAddress.getAddress()); // 设置收货地址
		((TextView) findViewById(R.id.oasa_address_detail)).setText(defaultAddress.getAddressDetail()); // 设置收货详细地址
		((TextView) findViewById(R.id.oasa_tel)).setText(defaultAddress.getTel()); // 设置收货人电话

		// 当整个收货地址平面被点击时，跳转到我的地址列表.
		RelativeLayout oasa_address_show_layout = (RelativeLayout) findViewById(R.id.oasa_address_show_layout);
		oasa_address_show_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				// 跳转到我的地址列表页
				Intent intent = getIntent();
				intent.setClass(OrderActivity.this.context, AddressListActivity.class);
				intent.putExtra("addressList", (Serializable) addressList);
				intent.putExtra("defaultAddressId", defaultAddress.getId());
				startActivity(intent);
			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int postion, long i) {
		RequestVo requestVo = new RequestVo();
		requestVo.context = this.context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();

		switch (parent.getId()) {
		case R.id.oaia_address_province:

			Province province = (Province) parent.getSelectedItem();
			mProvinceId = province.getId();

			Logger.d(TAG, "省份ID是：" + province.getId());

			// 城市
//			postParamMap.clear();
//			postParamMap.put("provinceId", province.getId());
//			requestVo.requestDataMap = postParamMap;
//			requestVo.requestUrl = Config.SERVLET_URL_GET_CITY_BYPROVINCEID;
//			requestVo.jsonParser = new CityParser();
//			setShowProcessDialog(Boolean.FALSE); // 设置不显示过程弹出层
//
//			asynForLoadDataAndDraw(requestVo, new DataCallback<CityProxy>() {
//
//				@Override
//				public void processData(CityProxy paramObject) {
//					Logger.d(TAG, "加载城市列表");
//					setShowProcessDialog(Boolean.TRUE); // 恢复弹出层显示
//					ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(OrderActivity.this, R.layout.spinner_item_self, paramObject.getCityList());
//					cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
//					citySpinner.setAdapter(cityAdapter);
//					citySpinner.setPrompt("城市");
//					citySpinner.setOnItemSelectedListener(OrderActivity.this);
//
//				}
//
//			});
			
			Logger.d(TAG, "加载城市列表");
			String cityId = null;
			CityProxy citys = provinceCityCountyManager.getCitys(province.getId(), cityId);
			ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(OrderActivity.this, R.layout.spinner_item_self, citys.getCityList());
			cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
			citySpinner.setAdapter(cityAdapter);
			citySpinner.setPrompt("城市");
			citySpinner.setSelection(citys.getPosition());
			citySpinner.setOnItemSelectedListener(OrderActivity.this);

			break;
		case R.id.oaia_address_city:

			City city = (City) parent.getSelectedItem();

			// 区域
//			postParamMap.clear();
//			postParamMap.put("cityId", city.getId());
//			requestVo.requestDataMap = postParamMap;
//			requestVo.requestUrl = Config.SERVLET_URL_GET_COUNTY_BYCITYID;
//			requestVo.jsonParser = new AreaParser();
//			setShowProcessDialog(Boolean.FALSE); // 设置不显示过程弹出层
//			asynForLoadDataAndDraw(requestVo, new DataCallback<AreaProxy>() {
//
//				@Override
//				public void processData(AreaProxy paramObject) {
//					Logger.d(TAG, "加载区域列表");
//					setShowProcessDialog(Boolean.TRUE); // 恢复弹出层显示
//					ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(OrderActivity.this, R.layout.spinner_item_self, paramObject.getAreaList());
//					areaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
//					areaSpinner.setAdapter(areaAdapter);
//					areaSpinner.setPrompt("区域");
//					areaSpinner.setOnItemSelectedListener(OrderActivity.this);
//
//				}
//
//			});
			
			Logger.d(TAG, "加载区域列表");
			String areaId = null;
			AreaProxy areas = provinceCityCountyManager.getAreas(mProvinceId, city.getId(), areaId);
			ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(OrderActivity.this, R.layout.spinner_item_self, areas.getAreaList());
			areaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
			areaSpinner.setAdapter(areaAdapter);
			areaSpinner.setPrompt("区域");
			areaSpinner.setSelection(areas.getPosition());
			areaSpinner.setOnItemSelectedListener(OrderActivity.this);

			break;
		case R.id.oa_invoice_type: // 发票类型
			IdValue keyValue = (IdValue) parent.getSelectedItem();
			if (Const.INVOICE_PERSION.equals(keyValue.getId())) { // 如果是个人
				oa_invoice_title.setVisibility(View.INVISIBLE);
			} else { // 如果是单位，就显示
				oa_invoice_title.setVisibility(View.VISIBLE);
			}
//			oa_invoice_title.setText("");
			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	// 公共处理

	/**
	 * 保存收获地址
	 */
	private void saveReceiverInfo() {
		// 保存收获地址
		RequestVo requestVo = RequestVo.setCommontParam(OrderActivity.this.context);
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("receiverName", ((TextView) findViewById(R.id.oaia_name)).getText().toString()); // 获取收货人名称
		postParamMap.put("address1", ((TextView) findViewById(R.id.oaia_detail_address)).getText().toString()); // 收货人详细地址
		postParamMap.put("provinceID", ((Province) provinceSpinner.getSelectedItem()).getId()); // 省份ID
		postParamMap.put("cityID", ((City) citySpinner.getSelectedItem()).getId()); // 城市ID
		postParamMap.put("countyID", ((Area) areaSpinner.getSelectedItem()).getId()); // 区域ID
		postParamMap.put("mobile", ((TextView) findViewById(R.id.oaia_tel)).getText().toString()); // 收货人手机
		postParamMap.put("defaultReceiver", "1"); // defaultReceiver 是否默认（1：默认 0：非默认) 必填
		requestVo.requestUrl = Config.SERVLET_URL_SAVE_RECEIVERINFO;
		requestVo.jsonParser = new OrderInitParser();
		asynForLoadDataAndDraw(requestVo, new DataCallback<Order>() {

			@Override
			public boolean errorLogicAndCall(BaseEntity obj) {

				// 省份不一样
				if ("003003400009".equals(obj.getRtnCode())) {

					Province selectProvince = (Province) provinceSpinner.getSelectedItem();

					// 弹出对话框
					showDialog(CookiesUtil.getProvinceId(), selectProvince.getValue(), selectProvince.getId());

					return false;
				}

				return true;
			}

			@Override
			public void processData(Order paramObject) {
				Logger.d(TAG, "保存收获地址" + paramObject.toString());
				
				String mobile = ((TextView) findViewById(R.id.oaia_tel)).getText().toString();
				if(!TextUtils.isEmpty(mobile)){
					CookiesUtil.setUserMobileNum(context, mobile);
				}

				// 设置保存收货地址的按钮为隐藏
				oa_address_save.setVisibility(View.INVISIBLE);

				// 是否开具发票设置为默认情况
				if (oa_is_need_invoice != null) {
					oa_is_need_invoice.setChecked(Boolean.FALSE);
				}

				draw(paramObject);

			}

		});

	}

	/**
	 * 切换省份, 为 用户选择的省份
	 */
	private void switchProvince(String provinceId) {
		// 切换省份
		RequestVo requestVo = RequestVo.setCommontParam(OrderActivity.this.context);
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("deviceCode", Config.getDeviceCode(context)); // 设备唯一标识 不可以是null
		postParamMap.put("mobileSiteType", Const.MOBILE_SITE_TYPE_1); // 客户端site区分 1：一号店 2：1mall 3:合规后一号店 所有接口都必须传入
		postParamMap.put("provinceId", provinceId);
		requestVo.requestUrl = Config.SERVLET_URL_SWITCH_PROVINCE;
		requestVo.jsonParser = new ResultParser();
		asynForLoadDataAndDraw(requestVo, new DataCallback<Result>() {

			@Override
			public void processData(Result paramObject) {
				// 跳转到购物车页面
				Intent intent = new Intent();
				intent.setClass(OrderActivity.this, ShopCartActivity.class);
				OrderActivity.this.startActivity(intent);
				OrderActivity.this.finish();

			}
		});
	}

	/**
	 * 提交订单
	 */
	public void sub(final boolean isNeedInvoice, final String addressName, final List<ProPackage> proList) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					
					Looper.prepare();

					// 1.保存配送方式
					saveDelivery(proList);

					// 2.保存支付方式
					savePaymentInfo();

					// 3.保存发票信息
					Invoice invoice = saveInvoiceInfo(isNeedInvoice, addressName);

					// 4.提交订单
					SummitOrderOutput output = submitOrderInfo(invoice.getOrderRundomString());

					// 5.跳转到订单创建完成页面
					Intent intent = getIntent();
					intent.setClass(OrderActivity.this, OrderCreateActivity.class);
					intent.putExtra("orderCode", output.getOrderCode());
					OrderActivity.this.startActivity(intent);
					OrderActivity.this.finish();
					
					Looper.loop();
					
				} catch (Exception e) {
					Logger.e(TAG, e.getMessage());
				}

			}
		}).start();
	}

	/**
	 * 保存支付方式
	 */
	private void savePaymentInfo() {

		RequestVo requestVo = RequestVo.setCommontParam(OrderActivity.this.context);
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("deviceCode", Config.getDeviceCode(context)); // 设备唯一标识 不可以是null
		postParamMap.put("methodId", curPayment.getId()); // 支付方式, 此处 不可以为null
		requestVo.requestUrl = Config.SERVLET_URL_SAVE_PAYMENTINFO;
		requestVo.jsonParser = new ResultParser();

		Logger.d(TAG, "保存支付方式, ");
		HttpUtil.post(requestVo);
	}

	/**
	 * 保存发票
	 */
	private Invoice saveInvoiceInfo(boolean isNeedInvoice, String addressName) {
		RequestVo requestVo = RequestVo.setCommontParam(OrderActivity.this.context);
		HashMap<String, String> postParamMap = requestVo.requestDataMap;

		String needInvoice = "0";
		String needCommInvoice = "0";
		if (isNeedInvoice) {
			needInvoice = "1";
			needCommInvoice = "1";
		}

		String invoiceTitle = ((TextView) findViewById(R.id.oa_invoice_title)).getText().toString();
		String type = "0"; // 发票抬头类型 0是个人抬头 1是单位抬头 必填字段
		if (invoiceTypeSpinner != null) {
			type = ((IdValue) invoiceTypeSpinner.getSelectedItem()).getId();
			if (type.equals(Const.INVOICE_PERSION)) { // 如果是个人类型的.
				invoiceTitle = addressName;
			}
		} else {
			invoiceTitle = addressName;
		}

		String invoiceContent = "详见发票明细";
		// 强制开票，发票内容：详见发票明细
		if(!isContainMust || isContainComm){
			if (oa_invoice_content != null) {
				invoiceContent = (String) oa_invoice_content.getSelectedItem();
			} else {
				invoiceContent = "日用品";
			}
		}
		
		postParamMap.put("needInvoice", needInvoice); // 是否需要发票 0:不要1：要 必填字段
		postParamMap.put("needCommInvoice", needCommInvoice); // 是否需要普通发票 0:不要1：要 必填字段， 此处获取 默认都是 普通发票.
		postParamMap.put("invoiceType", "2"); // 发票类型 2：普票 3：增票 必填字段
		postParamMap.put("invoiceTitleType", type); // 发票抬头配型 0是个人抬头 1是单位抬头 必填字段
		postParamMap.put("invoiceTitle", TextUtils.htmlEncode(invoiceTitle)); // 发票抬头, 如果是个人的，则填写收货人姓名（使用HTML编码）
		postParamMap.put("invoiceContent", TextUtils.htmlEncode(invoiceContent)); // 发票内容 必填字段，则填写收货人姓名（使用HTML编码）

		requestVo.requestUrl = Config.SERVLET_URL_SAVE_INVOICEINFO;
		requestVo.jsonParser = new InvoiceParser();

		Logger.d(TAG, "保存发票, ");
		return (Invoice) HttpUtil.post(requestVo);
	}

	/**
	 * 保存配送方式, 如果 微信号和留言用户都不填写，则不调用该方法.
	 */
	private void saveDelivery(List<ProPackage> proList) {

		String oa_weixinno = ((TextView) findViewById(R.id.oa_weixinno)).getText().toString();
		String oa_msg = ((TextView) findViewById(R.id.oa_msg)).getText().toString();

		// 如果 微信号和留言用户都不填写，则不保存配送方式
		if ("".equals(oa_weixinno) && "".equals(oa_msg)) {
			return;
		}

		RequestVo requestVo = RequestVo.setCommontParam(OrderActivity.this.context);
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("deviceCode", Config.getDeviceCode(context)); // 设备唯一标识 不可以是null

		// [{"deliveryMethodId":10001,"fee":0,"orderMark":"1_1_1","remarks":"78900056787|liuyan liuyan"},{}]
		List<Delivery> list = new ArrayList<Delivery>();
		for (ProPackage pp : proList) {
			Delivery deliver = pp.getDelivery();
			deliver.setRemarks(TextUtils.htmlEncode(oa_weixinno) + "|" + TextUtils.htmlEncode(oa_msg));
			list.add(pp.getDelivery());
		}
		postParamMap.put("deliveryList", JSON.toJSONString(list));

		requestVo.requestUrl = Config.SERVLET_URL_SAVE_DELIVERY;
		requestVo.jsonParser = new ResultParser();

		Logger.d(TAG, "保存配送方式");
		HttpUtil.post(requestVo);

	}

	/**
	 * 提交订单
	 */
	public SummitOrderOutput submitOrderInfo(String rdCheck) {
		RequestVo requestVo = RequestVo.setCommontParam(OrderActivity.this.context);
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("deviceCode", Config.getDeviceCode(context)); // 设备唯一标识 不可以是null
		postParamMap.put("rdCheck", rdCheck); // 随机校验码 必填字段
		requestVo.requestUrl = Config.SERVLET_URL_SUBMIT_ORDERINFO;
		requestVo.jsonParser = new SummitOrderOutputParser();

		Logger.d(TAG, "保存支付方式, ");
		return (SummitOrderOutput) HttpUtil.post(requestVo);

	}

	/**
	 * 弹出对话框
	 * 
	 * @param curProvinceId
	 *            当前省份
	 * @param destination
	 *            目的地站
	 */
	private void showDialog(final String curProvinceId, final String destination, final String provinceId) {
		// 省份
		RequestVo requestVo = new RequestVo();
		requestVo.context = this.context;
		requestVo.requestUrl = Config.SERVLET_URL_GET_ALLPROVINCE;
		requestVo.jsonParser = new ProvinceParser();

		asynForLoadDataAndDraw(requestVo, new DataCallback<ProvinceProxy>() {

			@Override
			public void processData(ProvinceProxy paramObject) {
				Logger.d(TAG, "加载省份列表");
				List<Province> list = paramObject.getProvinceList();
				String cur = "";
				for (Province pro : list) {
					if (pro.getId().equals(curProvinceId)) {
						cur = pro.getValue();

						break;
					}
				}

				// 弹出层提示用户
				Builder builder = new Builder(OrderActivity.this);
				builder.setTitle("");
				
				TextView alertText = new TextView(context);
				alertText.setText("您目前是" + cur + "站，选择" + destination + "地址后，您购买的商品及价格会发生变化，点击\"切换城市\"，将自动为您跳转回购物车查看商品及价格。");
				alertText.setGravity(Gravity.CENTER);
				alertText.setTextColor(Color.parseColor("#FFFFFF"));
				alertText.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
				builder.setView(alertText);

				builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {

						dialog.dismiss();

					}

				});

				builder.setNegativeButton("切换城市", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// 关闭弹出框
						dialog.dismiss();

						// 修改cookie中的值。。
						CookiesUtil.setProvinceId(OrderActivity.this.context, provinceId);

						// 切换省份
						switchProvince(provinceId);
					}

				});
				Dialog alertDialog = builder.create();
				alertDialog.setCancelable(false);
				alertDialog.setCanceledOnTouchOutside(false); // 点击外部不会消失
				alertDialog.show();

			}
		});

	}
	
	 protected void onDestroy() { 
		   super.onDestroy();
	       unregisterReceiver(broadcastReceiver); 
	  }; 
}
