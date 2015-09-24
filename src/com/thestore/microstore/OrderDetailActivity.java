package com.thestore.microstore;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.adapter.OrderLoadOthersListener;
import com.thestore.microstore.adapter.OrderProListAdapter;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.IsCanceOrderParser;
import com.thestore.microstore.parser.OrderDetailParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.DensityUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.Tracker;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.order.CanceOrder;
import com.thestore.microstore.vo.order.OrderDetail;
import com.thestore.microstore.vo.order.ProPackage;
import com.thestore.microstore.vo.proxy.Bank;


/**
 * 
 *  订单详情
 *
 * 2014-9-16 上午9:27:15
 */
public class OrderDetailActivity extends BaseWapperActivity {
	
	private static String TAG = "OrderDetailActivity";
	
	// 订单编号
	private String orderCode;
	// 包裹及商品列表
	private List<ProPackage> proPackageList;
	// 支付方式
	private Spinner odpa_online_pay;
	// 立即支付
	private Button odpa_pay_immediately_button;
	//取消订单原因map
	private JSONObject jsonReason;
	//取消订单原因ids
	private String reasonIds[];
	//取消订单dialog
	private Dialog dialog;
	//取消订单RadioGroup
	private  RadioGroup radioGroup;
	private View view;
	//对话框上的取消订单按钮
	private TextView  cancel_order_btn1;
    @Override  
    protected void onRestart() {  
        super.onRestart();  
        //Activity重新恢复时刷新数据
        loadViewLayout();
        initAttrs();
        process();
    }  

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.order_detail_activity);
		context = this;
		super.setTitle("订单详情");
		super.setRightTextTo("", false, null);
		Tracker.trackPage(Const.PAGE_CODE_VDIAN_ORDERDOFINISH);// BI埋点
	}

	@Override
	protected void process() {
		Intent intent = getIntent();
		orderCode = intent.getStringExtra("orderCode");
		
		// TODO:临时测试 begin
//		orderCode = "221808353589";
		// TODO:临时测试 end

		Logger.d(TAG, "订单详情页，订单code：" + orderCode);
		
	    RequestVo requestVo = new RequestVo();
	    requestVo.requestUrl = Config.SERVLET_URL_FIND_ORDERDETAIL_BYCODE;
	    requestVo.context = context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("ordercode", orderCode);
		postParamMap.put("userToken", CookiesUtil.getUT());
		requestVo.requestDataMap = postParamMap;
	    requestVo.jsonParser = new OrderDetailParser();
		
	    asynForLoadDataAndDraw(requestVo, new DataCallback<OrderDetail>() {

			@Override
			public void processData(OrderDetail paramObject) {
				draw(paramObject);
				
				    RequestVo requestVo2 = new RequestVo();
				    requestVo2.requestUrl = Config.SERVLET_URL_IS_CAN_CANCEL_ORDER;
				    requestVo2.context = context;
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("soId", orderCode);
					
					requestVo2.requestDataMap = map;
					requestVo2.jsonParser = new IsCanceOrderParser();
					
					asynForLoadDataAndDrawWithOutProgress(requestVo2, new DataCallback<CanceOrder>() {

						@Override
						public void processData(CanceOrder paramObject) {
							if(paramObject!=null&&"1".equals(paramObject.getIsCancel())){
								drawCancelOrder(paramObject);
							}							
						}
						
					});
			}
			
		});
	    
	    
	    
	   
	    
	}

	@Override
	protected void initAttrs() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 取消订单按钮显示
	 */
	
	private void drawCancelOrder(Object obj){
		CanceOrder result = (CanceOrder)obj;
		if(result!=null && result.getReason() != null && !TextUtils.isEmpty(result.getReasonIds())){
		    jsonReason = result.getReason();	
		    reasonIds = result.getReasonIds().split(",");
			TextView order_detail_status = (TextView) findViewById(R.id.order_detail_status);	
			order_detail_status.setTextColor(getResources().getColor(R.color.shen_hui_se));				 
			order_detail_status.setText("取消订单");
			order_detail_status.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					//AlertDialog.Builder builder=new AlertDialog.Builder(OrderDetailActivity.this);
	                view=LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.order_cancel_reason,null);
	                radioGroup = (RadioGroup) view.findViewById(R.id.cancelOrderRadioGroup);
	               
	                	                
	                dialog = new Dialog(OrderDetailActivity.this, R.style.dialog);//自定义dialog
	                		                    	                		                   	                
	                RadioButton rbn ;
	                View lineView;	               
	                int i=0;
	                for (String reasonId : reasonIds) {
						if(!TextUtils.isEmpty(reasonId)){
							i++;	    				
		    				rbn = new RadioButton(OrderDetailActivity.this);
		    				rbn.setId(i);
							Drawable btn_radio=getResources().getDrawable(android.R.drawable.btn_radio);
							btn_radio.setBounds(0, 0, btn_radio.getMinimumWidth(), btn_radio.getMinimumHeight());
							rbn.setCompoundDrawables(null, null, btn_radio, null);
							rbn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		    				rbn.setText(jsonReason.getString(reasonId));
		    				int dp_15 =DensityUtil.dip2px(context, 15);
		    				rbn.setPadding(dp_15, 0, dp_15, 0);
		    				rbn.setTag(reasonId);
		    				radioGroup.addView(rbn, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    				
		    				if(i<jsonReason.entrySet().size()){	    					
		    					LinearLayout ly = new LinearLayout(OrderDetailActivity.this);
	                            lineView = new View(OrderDetailActivity.this);
	                            lineView.setBackgroundColor(getResources().getColor(R.color.liang_hui_se));
	                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 1));
	                            lp.leftMargin = dp_15;
	                            lp.rightMargin = dp_15;
	                            ly.addView(lineView, lp);                          
	                            radioGroup.addView(ly);
		    				}	    				
						}
					}
	                	                
	               // builder.setView(view);
	               // alertDialog =builder.show();
	                dialog.setContentView(view);
	                dialog.show();	               
	                
	                
	                TextView  cancel_close_btn1 = (TextView) view.findViewById(R.id.cancel_close_btn1);
		            cancel_close_btn1.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v) {
								dialog.dismiss();							
							}
		            	   
		            });
		            
		            cancel_order_btn1 = (TextView) view.findViewById(R.id.cancel_order_btn1);
		            cancel_order_btn1.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v) {
																
								RadioButton radioButton = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());	
								if(radioButton==null){
									Toast toast = Toast.makeText(context, "请选择取消原因", Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
									return;
								}
								String reasonId = (String)radioButton.getTag();
								
								RequestVo requestVo3 = new RequestVo();
							    requestVo3.requestUrl = Config.SERVLET_URL_CANCEL_ORDER;
							    requestVo3.context = context;
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("soId", orderCode);
								map.put("reasonId", reasonId);
								map.put("userToken", CookiesUtil.getUT());
								//"soId", "reasonId", "userToken"
								requestVo3.requestDataMap = map;
								requestVo3.jsonParser = new IsCanceOrderParser();
								
								asynForLoadDataAndDraw(requestVo3, new DataCallback<CanceOrder>() {

									@Override
									public void processData(CanceOrder paramObject) {
										if(paramObject!=null&&"1".equals(paramObject.getCode())){
											 dialog.dismiss();
											 loadViewLayout();
											 initAttrs();
										     process();
										}else{
											 Toast toast = Toast.makeText(context, "取消订单失败", Toast.LENGTH_SHORT);
											 toast.setGravity(Gravity.CENTER, 0, 0);
											 toast.show();											 
											 dialog.dismiss();
										}	
									}
									
								});
							}
		            	   
		            });
					
		            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							cancel_order_btn1.setTextColor(getResources().getColor(R.color.yhd_red));							
						}
					});
				}
			});
		}
		
	}
	
	/**
	 * 绘制页面
	 */
	private void draw(Object obj) {	
		ScrollView sv = (ScrollView) findViewById(R.id.order_detail_scroll_view);
		sv.smoothScrollTo(0, 0);
		
		OrderDetail order = (OrderDetail)obj;
		// 基本信息
		TextView order_detail_status = (TextView) findViewById(R.id.order_detail_status);
		LinearLayout order_detail_layout = (LinearLayout) findViewById(R.id.order_detail_layout);
		LinearLayout orderDetailPay = (LinearLayout) getLayoutInflater().inflate(R.layout.order_detail_pay_activity, null);
		TextView odpa_orderno = (TextView) orderDetailPay.findViewById(R.id.odpa_orderno);
		TextView odpa_createtime = (TextView) orderDetailPay.findViewById(R.id.odpa_createtime);
		TextView odpa_name = (TextView) orderDetailPay.findViewById(R.id.odpa_name);
		TextView odpa_address = (TextView) orderDetailPay.findViewById(R.id.odpa_address);
		TextView odpa_detailed_address = (TextView) orderDetailPay.findViewById(R.id.odpa_detailed_address);
		TextView odpa_tel = (TextView) orderDetailPay.findViewById(R.id.odpa_tel);
		TextView odpa_count = (TextView) orderDetailPay.findViewById(R.id.odpa_count);
		TextView odpa_count_has = (TextView) orderDetailPay.findViewById(R.id.odpa_count_has);
		TextView odpa_other_pay = (TextView) orderDetailPay.findViewById(R.id.odpa_other_pay);
		TextView odpa_other_tran_pay = (TextView) orderDetailPay.findViewById(R.id.odpa_other_tran_pay);
		TextView odpa_pay_service_type = (TextView) orderDetailPay.findViewById(R.id.odpa_pay_service_type);
		odpa_online_pay = (Spinner) orderDetailPay.findViewById(R.id.odpa_online_pay);
		TextView odpa_no_pay_service_type = (TextView) orderDetailPay.findViewById(R.id.odpa_no_pay_service_type);
		TextView odpa_no_pay_service_type_name = (TextView) orderDetailPay.findViewById(R.id.odpa_no_pay_service_type_name);
		View odpa_pay_service_type_line = (View) orderDetailPay.findViewById(R.id.odpa_pay_service_type_line);
		RelativeLayout odpa_pay_service_type_layout = (RelativeLayout) orderDetailPay.findViewById(R.id.odpa_pay_service_type_layout);
		LinearLayout odpa_no_pay_service_type_layout = (LinearLayout) orderDetailPay.findViewById(R.id.odpa_no_pay_service_type_layout);
		View odpa_other_pay_line = (View) orderDetailPay.findViewById(R.id.odpa_other_pay_line);
		RelativeLayout odpa_other_pay_layout = (RelativeLayout) orderDetailPay.findViewById(R.id.odpa_other_pay_layout);
		LinearLayout odpa_pay_immediately_layout = (LinearLayout) orderDetailPay.findViewById(R.id.odpa_pay_immediately_layout);
		odpa_pay_immediately_button = (Button) orderDetailPay.findViewById(R.id.odpa_pay_immediately_button);
		
		// 基本信息赋值		
		if("34".equals(order.getOrderStatus())){
			// 34 交易取消，红字
			order_detail_status.setTextColor(Color.parseColor("#FF0000"));
		}else if("35".equals(order.getOrderStatus())){
			// 35 交易完成，绿字
			order_detail_status.setTextColor(Color.parseColor("#24AD3B"));
		}
		order_detail_status.setText(order.getOrderStatusString());
		odpa_orderno.setText(order.getOrderCode());
		odpa_createtime.setText(order.getOrderTime());
		odpa_name.setText(order.getGoodReceiverName());
		// 收货人地址
		String address = "";
		if(!TextUtils.isEmpty(order.getGoodReceiverProvince())){
			address += order.getGoodReceiverProvince() + " ";
		}
		if(!TextUtils.isEmpty(order.getGoodReceiverCity())){
			address += order.getGoodReceiverCity() + " ";
		}
		if(!TextUtils.isEmpty(order.getGoodReceiverCounty())){
			address += order.getGoodReceiverCounty() + " ";
		}
		odpa_address.setText(address);
		odpa_detailed_address.setText(order.getGoodReceiverAddress());
		odpa_tel.setText(order.getGoodReceiverMobile());
		odpa_count.setText(order.getChildNum()+ "个");
		odpa_count_has.setText("(含"+order.getProductCount()+"件商品)");
		odpa_other_pay.setText("￥"+order.getPayableAmount());
		odpa_other_tran_pay.setText("(含￥"+order.getOrderDeliveryFee()+"运费)");
		// 支付方式
		// 0：账户支付 1：网上支付 2：货到付款 3：邮局汇款 4：银行转账 5：pos机 6：万里通 7：分期付款 8：合同账期 9：货到转账 10：货到付支票, 目前微店只有1 2 5
		if("1".equals(order.getPayServiceType())){
			// 网上支付
			//3 等待支付， 4 已支付，20 已发货， 24已发货，34 交易取消，35 交易完成 ，36 订单待审核， 37 已发货， 38 待发货
			if("3".equals(order.getOrderStatus())){
				// 等待支付
				ArrayAdapter<Bank> payAdapter = new ArrayAdapter<Bank>(OrderDetailActivity.this, R.layout.spinner_item_self, order.getBankList());
				payAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
				odpa_online_pay.setAdapter(payAdapter);
				String paymethod = CookiesUtil.getPaymethod(); // 历史支付方式
				String bankCode = "";
				for(int i=0; i < order.getBankList().size(); i++){
					bankCode = order.getBankList().get(i).getCode();
					if (TextUtils.isEmpty(paymethod)) {
						// 无历史支付方式，默认：支付宝网页支付
						if ("alipay".equals(bankCode)) {
							odpa_online_pay.setSelection(i);
							break;
						}
					} else {
						if (paymethod.equals(bankCode)) {
							odpa_online_pay.setSelection(i);
							break;
						}
					}
				}
				odpa_online_pay.setPrompt("支付方式");
				odpa_pay_service_type_layout.setVisibility(View.VISIBLE);
				odpa_no_pay_service_type_layout.setVisibility(View.GONE);
				odpa_other_pay_layout.setVisibility(View.VISIBLE);				
				odpa_pay_immediately_layout.setVisibility(View.VISIBLE);
			}else{
				// 非等待支付				
				if("34".equals(order.getOrderStatus())){
					// 交易取消
					odpa_no_pay_service_type_name.setText("网上支付");
				}else{
					// 非交易取消
					if(order.getGateway()==null ||"".equals(order.getGateway())){
						odpa_no_pay_service_type_name.setText("网上支付");
					}else{
						odpa_no_pay_service_type_name.setText(order.getGateway());
					}											
				}				
				odpa_pay_service_type_line.setVisibility(View.GONE);
				odpa_pay_service_type_layout.setVisibility(View.GONE);
				odpa_no_pay_service_type_layout.setVisibility(View.VISIBLE);
				odpa_other_pay_line.setVisibility(View.GONE);	
				odpa_other_pay_layout.setVisibility(View.GONE);				
				odpa_pay_immediately_layout.setVisibility(View.GONE);
			}			
		}else if("2".equals(order.getPayServiceType()) || "5".equals(order.getPayServiceType())){
			if("3".equals(order.getOrderStatus())){
				// 等待支付
				if("2".equals(order.getPayServiceType())) {
					odpa_no_pay_service_type_name.setText("货到付款");	
				}else{
					odpa_no_pay_service_type_name.setText("货到刷卡");	
				}				
				odpa_pay_service_type_line.setVisibility(View.GONE);
				odpa_pay_service_type_layout.setVisibility(View.GONE);
				odpa_online_pay.setVisibility(View.GONE);
				odpa_no_pay_service_type_layout.setVisibility(View.VISIBLE);
				odpa_other_pay_layout.setVisibility(View.VISIBLE);				
				odpa_pay_immediately_layout.setVisibility(View.GONE);
			}else{
				// 非等待支付
				// 目前支持：货到刷卡
//				if("2".equals(order.getPayServiceType())){
//					odpa_no_pay_service_type_name.setText("货到付现金");
//				}else if("5".equals(order.getPayServiceType())){
//					odpa_no_pay_service_type_name.setText("货到刷卡");
//				}
				if("2".equals(order.getPayServiceType())) {
					odpa_no_pay_service_type_name.setText("货到付款");	
				}else{
					odpa_no_pay_service_type_name.setText("货到刷卡");	
				}
				odpa_pay_service_type_line.setVisibility(View.GONE);
				odpa_pay_service_type_layout.setVisibility(View.GONE);
				odpa_no_pay_service_type_layout.setVisibility(View.VISIBLE);
				odpa_other_pay_line.setVisibility(View.GONE);	
				odpa_other_pay_layout.setVisibility(View.GONE);				
				odpa_pay_immediately_layout.setVisibility(View.GONE);
			}
		}
		
		order_detail_layout.addView(orderDetailPay);
		
		// 微店卖家信息
		ImageView od_store_logo_img = (ImageView) findViewById(R.id.od_store_logo_img);
		TextView od_store_name = (TextView) findViewById(R.id.od_store_name);
		TextView od_weixinno = (TextView) findViewById(R.id.od_weixinno);
		TextView od_phoneno = (TextView) findViewById(R.id.od_phoneno);
		// 微店卖家信息赋值
		new DownImage(od_store_logo_img).execute(order.getStoreLogoUrl());
		od_store_name.setText(order.getStoreName());
		od_weixinno.setText(order.getWechatId());
		od_phoneno.setText(order.getPhone());		
		// 拨打电话
		od_phoneno.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {				
				if (!TextUtils.isEmpty(((TextView)view).getText())) {
					try {
						context.startActivity(new Intent(Intent.ACTION_DIAL,
								Uri.parse("tel:" + ((TextView) view).getText())));
					} catch (Exception e) {
						Logger.d(TAG, e.getMessage());
					}
				}
			}
		});
		
		// 发票信息		
		LinearLayout od_invoice_layout = (LinearLayout) findViewById(R.id.od_invoice_layout);
		LinearLayout orderDetailInvoice = (LinearLayout) getLayoutInflater().inflate(R.layout.order_detail_invoice_per_com_activity, null);
		TextView odipca_invoice_type = (TextView) orderDetailInvoice.findViewById(R.id.odipca_invoice_type);
		LinearLayout odipca_invoice_title_layout = (LinearLayout) orderDetailInvoice.findViewById(R.id.odipca_invoice_title_layout);
		TextView odipca_invoice_title = (TextView) orderDetailInvoice.findViewById(R.id.odipca_invoice_title);
		TextView odipca_invoice_content = (TextView) orderDetailInvoice.findViewById(R.id.odipca_invoice_content);
		
		LinearLayout orderDetailNoInvoice = (LinearLayout) getLayoutInflater().inflate(R.layout.order_detail_no_invoice_activity, null);
		// 发票信息赋值
		if(order.getInvoiceType() != null){
			// 需要发票
			// 抬头类型 0个人 1单位
			if("0".equals(order.getInvoiceType())){
				odipca_invoice_type.setText(order.getInvoiceTitle());
				odipca_invoice_title_layout.setVisibility(View.GONE);
				odipca_invoice_content.setText(order.getInvoiceContent());				
			}else if("1".equals(order.getInvoiceType())){
				odipca_invoice_type.setText("单位");
				odipca_invoice_title_layout.setVisibility(View.VISIBLE);
				odipca_invoice_title.setText(order.getInvoiceTitle());
				odipca_invoice_content.setText(order.getInvoiceContent());
			}
			od_invoice_layout.addView(orderDetailInvoice);
		}else{
			// 不需要发票
			od_invoice_layout.addView(orderDetailNoInvoice);
		}
		
		// 包裹及配送信息	
		LinearLayout od_package_layout = (LinearLayout) findViewById(R.id.od_package_layout);
		// 包裹及配送信息赋值
		// 商品列表
		proPackageList = order.getProPackageList();
		od_package_layout.removeAllViews();
		if (proPackageList != null && !proPackageList.isEmpty()) {
			Integer idx = 1;// 包裹序号
			for (ProPackage pro : proPackageList) {
				LinearLayout prosDefault = (LinearLayout) getLayoutInflater().inflate(R.layout.order_pros_default_activity, null);

				RelativeLayout order_pack_layout = (RelativeLayout) prosDefault.findViewById(R.id.order_pack_layout);
				TextView order_pro_name = (TextView) prosDefault.findViewById(R.id.order_pack_name);
				TextView order_pro_desc = (TextView) prosDefault.findViewById(R.id.order_pro_desc);
				TextView order_pro_priceDesc = (TextView) prosDefault.findViewById(R.id.order_pack_price_desc);

				order_pack_layout.setTag(idx);
				order_pro_name.setText(pro.getName());
				order_pro_desc.setText(pro.getDesc());
				order_pro_priceDesc.setText(pro.getExpectReceiveDate());

				// 物流查询
				order_pack_layout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// 跳转到H5页面
						Intent intent = new Intent();
						intent.setClass(OrderDetailActivity.this, WebViewActivity.class);
						Integer index = (Integer)view.getTag();
						intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/mycenter/orderStatusTrack.do?orderCode="+orderCode+"&index="+index);
						
						startActivity(intent);
					}
					
				});

				ListView listView = (ListView) prosDefault.findViewById(R.id.order_pros_list);
				int totalCount = pro.getProList().size();
				OrderProListAdapter orderProListAdapter = new OrderProListAdapter(OrderDetailActivity.this, R.layout.order_pros_items_activity, pro.getProList());
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

				od_package_layout.addView(prosDefault);
				
				idx++;
			}
		}
		
		// 价格清单
		TextView od_order_amount = (TextView) findViewById(R.id.od_order_amount);
		TextView od_order_delivery_fee = (TextView) findViewById(R.id.od_order_delivery_fee);
		TextView od_payable_amount = (TextView) findViewById(R.id.od_payable_amount);
		TextView od_order_coupon_fee = (TextView) findViewById(R.id.od_order_coupon_fee);
		// 价格清单赋值
		od_order_amount.setText("￥"+order.getOrderAmount());
		od_order_delivery_fee.setText("￥"+order.getOrderDeliveryFee());
		od_payable_amount.setText("￥"+order.getPayableAmount());
		if(order.getOrderPaidByCoupon()!=null && !"0".equals(order.getOrderPaidByCoupon())){
			od_order_coupon_fee.setText("-￥"+order.getOrderPaidByCoupon());
		}else{			
			View coupon_fee_layout = (View)findViewById(R.id.od_order_coupon_fee_layout);
			coupon_fee_layout.setVisibility(View.GONE);
			View coupon_fee_view = (View)findViewById(R.id.od_order_coupon_fee_view);
			coupon_fee_view.setVisibility(View.GONE);
		}
		
		
		// 立即支付
		odpa_pay_immediately_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				Bank bank = (Bank) odpa_online_pay.getSelectedItem();

				// 保存
				CookiesUtil.setPayment(context, bank.getCode());

				// 跳转到H5页面
				Intent intent = new Intent();
				intent.setClass(OrderDetailActivity.this, WebViewActivity.class);
				intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/mycenter/gopay.do?orderCode=" + orderCode + "&paymethod=" + bank.getCode());
				intent.putExtra("showTitle", "true");
				intent.putExtra("headTitle", "在线支付");
				
				startActivity(intent);
//				finish();
//				CookiesUtil.setNativeURI(context, "toNative-OrderDetailActivity?orderCode="+orderCode);
			}

		});

	}
	
	/**
	 * 异步加载图片
	 * @author wangliqun
	 *
	 * 2014-9-6 下午10:10:00
	 */
	class DownImage extends AsyncTask<String, Void, Bitmap> {
		private ImageView image;
		private String url;

		public DownImage(ImageView iamgeView) {
			this.image = iamgeView;
		}

		protected Bitmap doInBackground(String... utls) {
			url = utls[0];
			Bitmap bitmap = null;

			try {
				InputStream is = new URL(url).openStream();
				bitmap = BitmapFactory.decodeStream(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return bitmap;
		}

		protected void onPostExecute(Bitmap result) {

			image.setImageBitmap(result);
		
		}

	}
	
	

}
