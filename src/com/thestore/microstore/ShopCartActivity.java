package com.thestore.microstore;

import java.util.HashMap;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.thestore.microstore.adapter.ShopCartDefaultAdapter;
import com.thestore.microstore.adapter.ShopCartOverduetAdapter;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.parser.ShoppingCarParser;
import com.thestore.microstore.parser.ShoppingCartBaseOutputParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.Tracker;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;
import com.thestore.microstore.vo.ShoppingCartBaseOutput;
import com.thestore.microstore.vo.commom.BaseEntity;
import com.thestore.microstore.vo.shoppingcart.CartItem;
import com.thestore.microstore.vo.shoppingcart.ShoppingCart;
import com.thestore.microstore.widget.ListViewForScrollView;

/**
 * 
 * 购物车页面
 * 
 * 2014-9-3 下午9:01:50
 */
public class ShopCartActivity extends BaseWapperActivity {

	private static String TAG = "ShopCartActivity";

	//总金额
	private TextView shopcart_totalprice_text;	
	//结算
	private TextView shopcart_bottom_toPay_text;
	//我也要开店
	private TextView shopcart_will_open_default;
	private TextView shopcart_will_open_overdue;
	//正常商品列表适配器
	private ShopCartDefaultAdapter defaultAdapter;
	//失效商品列表适配器
	private ShopCartOverduetAdapter overdueAdapter;	
	private StringBuilder deleteIds;
	
    @Override  
    protected void onRestart() {  
        super.onRestart();  
        //Activity重新恢复时刷新数据
        loadViewLayout();
        initAttrs();
        process();
    }  
	
	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.shopcart_main_activity);
		context = this;
		setTitle("购物车");
		setRightTextTo("删除", true, null);
		Tracker.trackPage(Const.PAGE_CODE_VDIAN_CARTINDEX);// BI埋点
	}

	/**
	 * 右侧删除按钮被点击
	 */
	@Override
	protected void onHeadRightButton(View v) {
		deleteIds = new StringBuilder();
		// 正常商品列表
		for (int i = 0; i < defaultAdapter.getCount(); i++) {
			CartItem cartItem = defaultAdapter.getItem(i);
			if (cartItem.isChecked()) {
				if(deleteIds.length() > 0){
					deleteIds.append("," + cartItem.getId());
				}else{
					deleteIds.append(cartItem.getId());
				}
			}
		}
		// 失效商品列表
		for (int i = 0; i < overdueAdapter.getCount(); i++) {
			CartItem cartItem = overdueAdapter.getItem(i);
			if (cartItem.isChecked()) {
				if(deleteIds.length() > 0){
					deleteIds.append("," + cartItem.getId());
				}else{
					deleteIds.append(cartItem.getId());
				}
			}
		}

		if (deleteIds.length() > 0) {
			Builder builder  = new Builder(this);
			builder.setMessage("确定要删除这些商品吗？");
			
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					//删除商品
					if(deleteIds.length() > 0){
						deleteNormalPmInfo(deleteIds.toString());
					}
				}
			});
			
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
				}
			});
			
			Dialog alertDialog = builder.create();
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
	}

	@Override
	public void onClick(View view) {
	}

	
	/**
	 * 初始化一些基本的参数
	 */
	@Override
	protected void initAttrs() {
		shopcart_totalprice_text = (TextView) findViewById(R.id.shopcart_totalprice_text);
		shopcart_bottom_toPay_text = (TextView) findViewById(R.id.shopcart_bottom_toPay_text);
		shopcart_will_open_default = (TextView) findViewById(R.id.shopcart_will_open_default); 
		shopcart_will_open_overdue = (TextView) findViewById(R.id.shopcart_will_open_overdue); 
//		shopcart_will_open_default.setText(Html.fromHtml("<u>我也要开店</u>"));
//		shopcart_will_open_overdue.setText(Html.fromHtml("<u>我也要开店</u>"));
	}

	/**
	 * 异步加载 读取数据以及绘制页面
	 */
	@Override
	protected void process() {
	    RequestVo requestVo = new RequestVo();
	    requestVo.requestUrl = Config.SERVLET_URL_GETCART;
	    requestVo.context = context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("interfaceVersion", Config.INTERFACE_VERSION);
		postParamMap.put("sessionId", CookiesUtil.getVSessionId());
		postParamMap.put("userToken", CookiesUtil.getUT());
		requestVo.requestDataMap = postParamMap;
	    requestVo.jsonParser = new ShoppingCarParser();
		
	    asynForLoadDataAndDraw(requestVo, new DataCallback<ShoppingCart>() {
	    	
			@Override
			public boolean errorLogicAndCall(BaseEntity obj) {

				// 限购商品数量错误
				if (Const.ERROR_CODE_SHOPCART_NUM.equals(obj.getRtnCode())) {
				    
					showCommDialog("", obj.getRtnMsg(), false);
					process();
					
					return false;
				}

				return true;
			}

			@Override
			public void processData(ShoppingCart paramObject) {
				ShoppingCart cart = (ShoppingCart) paramObject;
				if(cart.getCartItems().isEmpty() && cart.getOverDueCartItems().isEmpty()){
					// 没有商品，展示空购物车
					setContentView(R.layout.shopcart_none_activity);
					
					//我也要开店
					((TextView) findViewById(R.id.shopcart_will_open_text)).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// 跳转到H5页面
							Intent intent = new Intent();
							intent.setClass(ShopCartActivity.this, WebViewActivity.class);
							intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/myvdian/createstart.do");							
							startActivity(intent);
						}
					});
					
					//去结算
					((TextView) findViewById(R.id.shopcart_bottom_toPay_text)).setOnClickListener(new View.OnClickListener() {						
						@Override
						public void onClick(View view) {
							showCommDialog("", "购物车中没有商品", false);
						}
					});
					
				}else{
					draw(paramObject);
				}

			}
			
		});
	}

	
	/**
	 * 绘制页面
	 */
	private void draw(Object obj) {
		ShoppingCart cart = (ShoppingCart) obj;
		
		if(cart.getCartItems().size() > 0 && cart.getOverDueCartItems().size() == 0){
			shopcart_will_open_default.setVisibility(View.GONE);
			shopcart_will_open_overdue.setVisibility(View.GONE);
		}else if(cart.getCartItems().size() == 0 && cart.getOverDueCartItems().size() > 0){
			shopcart_will_open_default.setVisibility(View.GONE);
			shopcart_will_open_overdue.setVisibility(View.GONE);
		}else{
			shopcart_will_open_default.setVisibility(View.GONE);
			shopcart_will_open_overdue.setVisibility(View.VISIBLE);
		}

		//正常的商品
		ListViewForScrollView shopcart_default_list = (ListViewForScrollView) findViewById(R.id.shopcart_default_list);
		defaultAdapter = new ShopCartDefaultAdapter(ShopCartActivity.this, R.layout.shopcart_default_listitem, cart.getCartItems());
		shopcart_default_list.setAdapter(defaultAdapter);
		
		//过期的商品
		TextView shopcart_overdue_text = (TextView) findViewById(R.id.shopcart_overdue_text);
		if(cart.getOverDueCartItems().isEmpty()){
			shopcart_overdue_text.setVisibility(View.INVISIBLE);
		}else{
			shopcart_overdue_text.setVisibility(View.VISIBLE);
		}
		
		ListViewForScrollView shopcart_overdue_list = (ListViewForScrollView) findViewById(R.id.shopcart_overdue_list);
		overdueAdapter = new ShopCartOverduetAdapter(ShopCartActivity.this, R.layout.shopcart_overdue_listitem, cart.getOverDueCartItems());
		shopcart_overdue_list.setAdapter(overdueAdapter);
		
		if(!"".equals(cart.getTotalPrice())) {
			shopcart_totalprice_text.setText("￥" + cart.getTotalPrice());
		} else {
			startActivity(new Intent(this, ShopCartNoneActivity.class));
//			finish();
		}
		
		// item 选择事件
		shopcart_default_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CartItem cartPros = (CartItem) parent.getItemAtPosition(position);
				if (cartPros.isChecked()) {
					cartPros.setChecked(false);
				} else {
					cartPros.setChecked(true);
				}
				defaultAdapter.notifyDataSetChanged();
			}

		});
		
		//我也要开店
//		shopcart_will_open_default.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				// 跳转到H5页面
//				Intent intent = new Intent();
//				intent.setClass(ShopCartActivity.this, WebViewActivity.class);
//				intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/myvdian/createstart.do");
//
//				startActivity(intent);
////				CookiesUtil.setNativeURI(context, "toNative-ShopCartActivity");
//			}
//		});
		
//		shopcart_will_open_overdue.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				// 跳转到H5页面
//				Intent intent = new Intent();
//				intent.setClass(ShopCartActivity.this, WebViewActivity.class);
//				intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/myvdian/createstart.do");
//
//				startActivity(intent);
////				CookiesUtil.setNativeURI(context, "toNative-ShopCartActivity");
//			}
//		});		
		
		//去结算
		shopcart_bottom_toPay_text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(checkShopCart()){
					
					new Thread(new Runnable() {
						public void run() {
							try {
								// 去结算，要先登录
								String ut = CookiesUtil.getUT();
								String vut = CookiesUtil.getVUT();
								boolean isLogin = true;
								// vut无效，要先登录
								// "rtn_code" : "055000000016"
						    	RequestVo requestVo = new RequestVo();
								requestVo.requestUrl = Config.SERVLET_URL_ISLOGIN;
								requestVo.context = context;
								HashMap<String, String> postParamMap = new HashMap<String, String>();
								postParamMap.put("ut", vut);
								requestVo.requestDataMap = postParamMap;
								requestVo.jsonParser = new ResultParser();
								Result result = (Result) HttpUtil.post(requestVo);
								if(result != null && Boolean.FALSE.equals(result.getData())){
									isLogin = false;
								}
								
								if(TextUtils.isEmpty(ut) || !isLogin){
									toH5GoLogin(context, "toNative-ShopCartActivity");
								}else{
									//跳转到订单确认页
									Intent intent = new Intent();
									intent.setClass(ShopCartActivity.this, OrderActivity.class);
									intent.putExtra("resource", "ShopCartActivity");
									
									//checkboxStr格式：id=选中状态（1：选择 0：未选中）中间用逗号隔开   例如 102809_0=1,102808_0=0
									StringBuffer checkboxStr = new StringBuffer("");
									for (int i = 0; i < defaultAdapter.getCount(); i++) {
										CartItem cartItem = defaultAdapter.getItem(i);
										String isChecked = "0";
										if (cartItem.isChecked()) {
											isChecked = "1";
										}
										checkboxStr.append(cartItem.getId()).append("=").append(isChecked).append(",");
									}
									
									intent.putExtra("checkboxStr", checkboxStr.toString());
									
									startActivity(intent);
//									finish();
								}		
								
							} catch (Exception e) {
								Logger.e(TAG, e.getMessage());
							}
						}
					}).start();
					
				}// end if
				
			}
		});
	}	
	
	/**
	 * 检查购物车
	 * @return
	 */
	private boolean checkShopCart(){	
		// 选中的商品中存在无效商品，请先清理后再结算
		for (int i = 0; i < overdueAdapter.getCount(); i++) {
			CartItem cartItem = overdueAdapter.getItem(i);
			if (cartItem.isChecked()) {
				showCommDialog("", "选中的商品中存在无效商品，请先清理后再结算", false);
				return false;
			}
		}
		
		// 请选择商品
		int num = 0;
		for (int i = 0; i < defaultAdapter.getCount(); i++) {
			CartItem cartItem = defaultAdapter.getItem(i);
			if (cartItem.isChecked()) {
				num++;
			}
		}
		if (num == 0) {
			showCommDialog("", "请选择商品", false);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 修改普通商品数量
	 * @param num
	 * @param vMerchantId
	 * @param vPmId
	 */
    public void editNormalPmInfoNum(String num, String vMerchantId, String vPmId){
	    RequestVo requestVo = new RequestVo();
	    requestVo.requestUrl = Config.SERVLET_URL_EDIT_NORMALPMINFO_NUM;
	    requestVo.context = context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("interfaceVersion", Config.INTERFACE_VERSION);
		postParamMap.put("sessionId", CookiesUtil.getVSessionId());
		postParamMap.put("userToken", CookiesUtil.getUT());
		postParamMap.put("num", num);
		postParamMap.put("provinceId", CookiesUtil.getProvinceId());
		postParamMap.put("vmerchantId", vMerchantId);
		postParamMap.put("vpmId", vPmId);
		requestVo.requestDataMap = postParamMap;
	    requestVo.jsonParser = new ShoppingCartBaseOutputParser();
	    
	    asynForLoadDataAndDraw(requestVo, new DataCallback<ShoppingCartBaseOutput>() {
	    	
			@Override
			public boolean errorLogicAndCall(BaseEntity obj) {

				// 限购商品数量错误
				if (Const.ERROR_CODE_SHOPCART_NUM.equals(obj.getRtnCode())) {
				    
					showCommDialog("", obj.getRtnMsg(), false);
					process();
					
					return false;
				}

				return true;
			}

			@Override
			public void processData(ShoppingCartBaseOutput paramObject) {	
				Logger.d(TAG, paramObject.getRtnMsg());	
				// 重新加载购物车
				process();

			}
			
		});
	
	}
    
    /**
     * 删除商品
     * @param deleteId
     */
    public void deleteNormalPmInfo(String deleteIds){
	    RequestVo requestVo = new RequestVo();
	    requestVo.requestUrl = Config.SERVLET_URL_DELETE_NORMALPMINFO;
	    requestVo.context = context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("interfaceVersion", Config.INTERFACE_VERSION);
		postParamMap.put("sessionId", CookiesUtil.getVSessionId());
		postParamMap.put("userToken", CookiesUtil.getUT());
		postParamMap.put("ids", deleteIds);
		postParamMap.put("provinceId", CookiesUtil.getProvinceId());
		requestVo.requestDataMap = postParamMap;
	    requestVo.jsonParser = new ShoppingCartBaseOutputParser();
	    
	    asynForLoadDataAndDraw(requestVo, new DataCallback<ShoppingCartBaseOutput>() {
	    	
			@Override
			public boolean errorLogicAndCall(BaseEntity obj) {

				// 限购商品数量错误
				if (Const.ERROR_CODE_SHOPCART_NUM.equals(obj.getRtnCode())) {
				    
					showCommDialog("", obj.getRtnMsg(), false);
					process();
					
					return false;
				}

				return true;
			}

			@Override
			public void processData(ShoppingCartBaseOutput paramObject) {	
				Logger.d(TAG, paramObject.getRtnMsg());	
				// 重新加载购物车
				process();

			}
			
		});
    }
    
    /**
     * 更新购物车选择状态
     * @param checkboxStr
     */
    public void getCart(){
		// checkboxStr格式：id=选中状态（1：选择 0：未选中）中间用逗号隔开   例如 102809_0=1,102808_0=0
		StringBuffer checkboxStr = new StringBuffer("");
		// 正常商品列表
		for (int i = 0; i < defaultAdapter.getCount(); i++) {
			CartItem cartItem = defaultAdapter.getItem(i);
			String isChecked = "0";
			if (cartItem.isChecked()) {
				isChecked = "1";
			}
			checkboxStr.append(cartItem.getId()).append("=").append(isChecked).append(",");
		}		
		
	    RequestVo requestVo = new RequestVo();
	    requestVo.requestUrl = Config.SERVLET_URL_GETCART;
	    requestVo.context = context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("interfaceVersion", Config.INTERFACE_VERSION);
		postParamMap.put("sessionId", CookiesUtil.getVSessionId());
		postParamMap.put("userToken", CookiesUtil.getUT());
		postParamMap.put("checkboxStr", checkboxStr.toString());
		requestVo.requestDataMap = postParamMap;
	    requestVo.jsonParser = new ShoppingCarParser();
		
	    asynForLoadDataAndDraw(requestVo, new DataCallback<ShoppingCart>() {
	    	
			@Override
			public boolean errorLogicAndCall(BaseEntity obj) {

				// 限购商品数量错误
				if (Const.ERROR_CODE_SHOPCART_NUM.equals(obj.getRtnCode())) {
				    
					showCommDialog("", obj.getRtnMsg(), false);
					process();
					
					return false;
				}

				return true;
			}

			@Override
			public void processData(ShoppingCart paramObject) {
				draw(paramObject);
			}
			
		});
    }
    
    /**
     * 跳转到H5商品详情页 
     * @param vDPmInfoId
     * @param yhdPmInfoId
     * @param vDMerchantId
     */
    public void toH5DetailIndex(String vDPmInfoId, String yhdPmInfoId, String vDMerchantId){
		// 跳转到H5页面
		Intent intent = new Intent();
		intent.setClass(context, WebViewActivity.class);
		intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/detail/detailIndex.do?vDPmInfoId="+vDPmInfoId+"&yhdPmInfoId="+yhdPmInfoId+"&vDMerchantId="+vDMerchantId);

		startActivity(intent);  
//		finish();
    }

}
