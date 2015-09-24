package com.thestore.microstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;






import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thestore.microstore.adapter.CouponListAdapter;
import com.thestore.microstore.parser.OrderCouponParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.coupon.CouponResult;
import com.thestore.microstore.vo.coupon.CouponVo;
import com.thestore.microstore.vo.coupon.ShoppingCouponGroup;
import com.thestore.microstore.vo.coupon.ShoppingCouponType;
import com.thestore.microstore.vo.coupon.ShoppingCouponVo;
import com.thestore.microstore.vo.coupon.ShoppingPaymentCoupon;
import com.thestore.microstore.widget.ListViewForScrollView;

/**
 * 
 * 结算页-抵用券列表
 * 
 * 
 */
public class CouponListActivity extends BaseWapperActivity {
	
	public static final String action = "com.thestore.microstore.broadcast.action";

	private static String TAG = "vdian_CouponListActivity";

	private CouponListAdapter adapter;

	private List<CouponVo> couponList;
	
	//验证码dialog
	private Dialog dialog;
	
	private View view;
	
	//确定
	private TextView ccd_save;
	//取消
	private TextView ccd_cancel;
	//定时器
	private TimeCount time;
	
	private TextView ccd_check_code_again;
	private TextView ccd_check_code_time;
	

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.coupon_save_button :
			finish();
			break;
		default:
			Log.i(TAG, "错误的被点击了..");
			break;
		
		}
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.coupon_list_activity);
		context = this;
		setTitle("抵用券");
		setRightTextTo("确定", true, null);
	}

	/**
	 * 右侧按钮被点击时到达新建页面
	 */
	@Override
	protected void onHeadRightButton(View v) {
		finish();
	}

	@Override
	protected void process() {
		draw(null);
	}

	@Override
	protected void initAttrs() {
	}

	private void draw(ShoppingPaymentCoupon obj) {
				
		findViewById(R.id.coupon_save_button).setOnClickListener(this);	
		
		ShoppingPaymentCoupon coupon =null;
		Intent intent = getIntent();
		if(obj==null){			
			coupon = (ShoppingPaymentCoupon ) intent.getSerializableExtra("paymentCoupon");
		}else{
			coupon = obj;			
		}
		
		couponList = new ArrayList<CouponVo>();
		transferCouponList(coupon,couponList);
		
		ListViewForScrollView counpon_list = (ListViewForScrollView) findViewById(R.id.coupon_list);
		adapter = new CouponListAdapter(CouponListActivity.this, R.layout.coupon_listitem_activity, couponList);
		counpon_list.setAdapter(adapter);
		
		counpon_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {

				CheckBox checkBox = (CheckBox) adapterview.getChildAt(position).findViewById(R.id.coupon_checkbox);
									
					CouponVo couponVo = (CouponVo) checkBox.getTag();
					if(couponVo!=null && couponVo.isCanUse()){
						reDraw(couponVo, checkBox);						
					}												
			}
		});
		
	}
	
	
	/**
	 * 将coupon转换成list
	 * @param coupon
	 * @param couponList
	 */
	private void transferCouponList(ShoppingPaymentCoupon coupon,List<CouponVo> couponList){		
		if(coupon!=null){			
			List<ShoppingCouponGroup> couponGroups = coupon.getCouponGroups();
			for (ShoppingCouponGroup shoppingCouponGroup : couponGroups) {
				
				int cardNum =0;
				if(shoppingCouponGroup.getMultipleCouponList()!=null){
					cardNum = shoppingCouponGroup.getMultipleCouponList().size();
				}
				if(shoppingCouponGroup.getMutexCouponList()!=null){
					cardNum += shoppingCouponGroup.getMutexCouponList().size();
				}
				int i=0;
				for (ShoppingCouponVo shoppingCouponVo : shoppingCouponGroup.getMultipleCouponList()) {
					CouponVo couponVo = new CouponVo();
					couponVo.setAmount(shoppingCouponVo.getAmount());
					couponVo.setBeginTime(shoppingCouponVo.getBeginTime());
					couponVo.setCanUse(shoppingCouponVo.isCanUse());
					couponVo.setCardNum(cardNum);
					couponVo.setCouponNumber(shoppingCouponVo.getCouponNumber());
					couponVo.setCouponTypeDesc(getCouponTypeDesc(shoppingCouponGroup.getCouponType()));					
					couponVo.setDescription(shoppingCouponVo.getDescription());										
					couponVo.setDefineType(shoppingCouponVo.getDefineType());
					couponVo.setExpiredTime(shoppingCouponVo.getExpiredTime());
					couponVo.setPrefix(shoppingCouponVo.getPrefix());
					if(i==0){
						couponVo.setShowHead(true);
					}					
					couponVo.setType(shoppingCouponVo.getType());
					couponVo.setUsed(shoppingCouponVo.isUsed());
					couponList.add(couponVo);
					i++;
				}
				
				i=0;
				for (ShoppingCouponVo shoppingCouponVo : shoppingCouponGroup.getMutexCouponList()) {
					CouponVo couponVo = new CouponVo();
					couponVo.setAmount(shoppingCouponVo.getAmount());
					couponVo.setBeginTime(shoppingCouponVo.getBeginTime());
					couponVo.setCanUse(shoppingCouponVo.isCanUse());
					couponVo.setCardNum(cardNum);
					couponVo.setCouponNumber(shoppingCouponVo.getCouponNumber());
					couponVo.setCouponTypeDesc(getCouponTypeDesc(shoppingCouponGroup.getCouponType()));
					couponVo.setDescription(shoppingCouponVo.getDescription());
					couponVo.setDefineType(shoppingCouponVo.getDefineType());
					couponVo.setExpiredTime(shoppingCouponVo.getExpiredTime());
					couponVo.setPrefix(shoppingCouponVo.getPrefix());
					if(i==0){
						couponVo.setShowHead(true);
					}					
					couponVo.setType(shoppingCouponVo.getType());
					couponVo.setUsed(shoppingCouponVo.isUsed());
					couponList.add(couponVo);
					i++;
				}
			}
			
		}
	}

	/**
	 * 重新绘制我的抵用券列表页
	 * 
	 * @param address
	 */
	public void reDraw(CouponVo couponVo,CheckBox checkBox) {
								
			RequestVo requestVo = RequestVo.setCommontParam(CouponListActivity.this.context);
			
			if(!checkBox.isChecked()){
				requestVo.requestUrl = Config.SERVLET_URL_ADD_COUPON;
			}else{				
				requestVo.requestUrl = Config.SERVLET_URL_REMOVE_COUPON;
			}
			requestVo.context = CouponListActivity.this.context;
			HashMap<String, String> postParamMap = requestVo.requestDataMap;
			postParamMap.put("couponNumber", couponVo.getCouponNumber());
			
			requestVo.jsonParser = new OrderCouponParser();
			final String couponNumber = couponVo.getCouponNumber();
			
			asynForLoadDataAndDraw(requestVo, new DataCallback<CouponResult>() {

				@Override
				public void processData(CouponResult paramObject) {
					if(paramObject!=null){
						if("0".equals(paramObject.getCode())&& paramObject.getShoppingPaymentCoupon()!=null){//添加或删除抵用券成功
							draw(paramObject.getShoppingPaymentCoupon());
							
							//发送广播更新结算页面ui
							Intent intent = new Intent(action); 							
			                intent.putExtra("paidByCoupon", paramObject.getPaidByCoupon()); 
			                intent.putExtra("amountNeed2Pay", paramObject.getAmountNeed2Pay()); 			              
			                intent.putExtra("paymentCoupon",paramObject.getShoppingPaymentCoupon());
			                sendBroadcast(intent);
							
						}else if("3".equals(paramObject.getCode())){//需要手机验证
							sendCouponSms(couponNumber,paramObject.getMobile(),true);
						}else{
							Toast.makeText(context, paramObject.getMsg(), Toast.LENGTH_SHORT).show();
						}						
					}
					
				}

			});		
	}

	
	/**
	 * 发送验证码
	 */
	private void sendCouponSms(final String couponNumber,final String mobile,final boolean isShowMsgDialog){
		RequestVo requestVo = RequestVo.setCommontParam(CouponListActivity.this.context);				
		requestVo.requestUrl = Config.SERVLET_URL_SEND_COUPON_SMS;		
		requestVo.context = CouponListActivity.this.context;	
		requestVo.jsonParser = new OrderCouponParser();		
		asynForLoadDataAndDraw(requestVo, new DataCallback<CouponResult>() {

			@Override
			public void processData(CouponResult paramObject) {
				if(paramObject!=null){
					if("0".equals(paramObject.getCode())){//发送验证码成功
						if(isShowMsgDialog){
							showSendMsgDialog(couponNumber,mobile);
						}else{
							time =null;
							time = new TimeCount(60000, 1000);
					        time.start();
							Toast.makeText(context, paramObject.getMsg(), Toast.LENGTH_SHORT).show();
						}						
					}else{
						Toast.makeText(context, paramObject.getMsg(), Toast.LENGTH_SHORT).show();
					}					
				}				
			}
		});		
	}
	
	/**
	 * 验证验证码
	 */
	private void verifyCouponSms(String couponNumber,String couponSms){
		RequestVo requestVo = RequestVo.setCommontParam(CouponListActivity.this.context);				
		requestVo.requestUrl = Config.SERVLET_URL_VERIFY_COUPON_SMS;	
		requestVo.context = CouponListActivity.this.context;
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("couponNumber",couponNumber);	
		postParamMap.put("couponSms", couponSms);		
		requestVo.jsonParser = new OrderCouponParser();		
		asynForLoadDataAndDraw(requestVo, new DataCallback<CouponResult>() {

			@Override
			public void processData(CouponResult paramObject) {
				if(paramObject!=null){
					if("0".equals(paramObject.getCode())&& paramObject.getShoppingPaymentCoupon()!=null){// 验证验证码成功
						if(dialog!=null){
							dialog.dismiss();
						}
						if(time!=null){
							time.cancel();
						}
						draw(paramObject.getShoppingPaymentCoupon());
						
						//发送广播更新结算页面ui
						Intent intent = new Intent(action); 							
		                intent.putExtra("paidByCoupon", paramObject.getPaidByCoupon()); 
		                intent.putExtra("amountNeed2Pay", paramObject.getAmountNeed2Pay()); 	
		                intent.putExtra("paymentCoupon",paramObject.getShoppingPaymentCoupon());
		                sendBroadcast(intent);
					}else{
						Toast.makeText(context, paramObject.getMsg(), Toast.LENGTH_SHORT).show();
					}						
				}
				
			}

		});		
	}
	
	/**
	 * 展示发送验证码窗口
	 */
	private void showSendMsgDialog(final String couponNumber,final String mobile){
		  view=LayoutInflater.from(CouponListActivity.this).inflate(R.layout.coupon_check_dialog,null);
		  dialog = new Dialog(CouponListActivity.this, R.style.dialog);//自定义dialog		  
		  dialog.setContentView(view);
          dialog.show();	
          
          ccd_save = (TextView) view.findViewById(R.id.ccd_save);
          ccd_save.setOnClickListener(new View.OnClickListener() {
  			
  			@Override
  			public void onClick(View arg0) {
  				EditText ccd_check_code = (EditText) view.findViewById(R.id.ccd_check_code);
  				String check_code = ccd_check_code.getText().toString();
  				if("".equals(check_code))	{ 					
  					Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
  				}else{
  					verifyCouponSms(couponNumber,check_code);
  				}			
  			}
  		  });
          if(mobile!=null){
        	  TextView ccd_mobile = (TextView) view.findViewById(R.id.ccd_mobile);
        	  ccd_mobile.setText("验证码已发送至"+mobile);
          }
          ccd_cancel = (TextView) view.findViewById(R.id.ccd_cancel);
          ccd_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(time!=null){
					time.cancel();
				}
				dialog.dismiss();				
			}
		  });
          
          time = new TimeCount(60000, 1000);
          time.start();
          ccd_check_code_again = (TextView) view.findViewById(R.id.ccd_check_code_again);
          ccd_check_code_again.setClickable(false);
          ccd_check_code_time = (TextView) view.findViewById(R.id.ccd_check_code_time);
          ccd_check_code_again.setOnClickListener(new View.OnClickListener() {
  			
  			@Override
  			public void onClick(View arg0) {
  				sendCouponSms(couponNumber,null,false);			
  			}
  		  });
          
	}
	
	public String getCouponTypeDesc(String type){
		String desc ="";
		if (type.equals( ShoppingCouponType.YHD_COUPON_TYPE_BRAND.getType())) {
			desc = ShoppingCouponType.YHD_COUPON_TYPE_BRAND.getDesc();
		} else if (type.equals( ShoppingCouponType.YHD_COUPON_TYPE_CATEGORY.getType())) {
			desc = ShoppingCouponType.YHD_COUPON_TYPE_CATEGORY.getDesc();
		} else if (type.equals( ShoppingCouponType.YHD_COUPON_TYPE_DELIVERY_FEE.getType())) {
			desc = ShoppingCouponType.YHD_COUPON_TYPE_DELIVERY_FEE.getDesc();
		} else if (type.equals(ShoppingCouponType.YHD_COUPON_TYPE_DELIVERY_ON_TIME_FEE.getType())) {
			desc = ShoppingCouponType.YHD_COUPON_TYPE_DELIVERY_ON_TIME_FEE.getDesc();
		} else if (type.equals(ShoppingCouponType.YHD_COUPON_TYPE_PRODUCT.getType())) {
			desc = ShoppingCouponType.YHD_COUPON_TYPE_PRODUCT.getDesc();
		}else if (type.equals(ShoppingCouponType.YMALL_COUPON_TYPE_MERCHANT.getType())) {
			desc = ShoppingCouponType.YMALL_COUPON_TYPE_MERCHANT.getDesc();
		}else if (type.equals(ShoppingCouponType.YMALL_COUPON_TYPE_THRESHOLD.getType())) {
			desc = ShoppingCouponType.YMALL_COUPON_TYPE_THRESHOLD.getDesc();
		}
		return desc;
	}
	
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			ccd_check_code_again.setTextColor(getResources().getColor(R.color.blue));
			ccd_check_code_again.setClickable(true);
			ccd_check_code_time.setVisibility(View.GONE);			
		}

		@Override
		public void onTick(long millisUntilFinished) {	
			ccd_check_code_again.setTextColor(getResources().getColor(R.color.gray_B8B8B8));
			ccd_check_code_again.setClickable(false);
			ccd_check_code_time.setText("("+millisUntilFinished/1000+"S)");			
		}
		
	}
}
