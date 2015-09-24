package com.thestore.microstore;

import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * 空购物车页面
 * 
 * 2014-9-3 下午9:01:50
 */
public class ShopCartNoneActivity extends BaseWapperActivity {

	private static String TAG = "ShopCartNoneActivity";
	//我也要开店
	private TextView shopcart_will_open_text;
	//去结算
	private TextView shopcart_bottom_toPay_text;

	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.shopcart_none_activity);
		context = this;
		setTitle("购物车");
		setRightTextTo("删除", false, null);
	}

	@Override
	protected void process() {
		draw(null);
	}

	@Override
	protected void initAttrs() {
		shopcart_will_open_text = (TextView) findViewById(R.id.shopcart_will_open_text); 	
		shopcart_bottom_toPay_text = (TextView) findViewById(R.id.shopcart_bottom_toPay_text);
	}

	private void draw(Object obj) {
		//我也要开店
		shopcart_will_open_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// 跳转到H5页面
				Intent intent = new Intent();
				intent.setClass(ShopCartNoneActivity.this, WebViewActivity.class);
				intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/myvdian/createstart.do");
                // TODO：临时测试 begin
//				intent.putExtra("toH5Url", "file:///android_asset/test2.html");
				// TODO：临时测试 end
				
				startActivity(intent);
//				CookiesUtil.setNativeURI(context, "toNative-ShopCartActivity");
			}
		});
		
		//去结算
		shopcart_bottom_toPay_text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				showCommDialog("", "购物车中没有商品", false);
			}
		});
		
	}

	private Object loadData() {
		// TODO Auto-generated method stub
		return null;
	}
	




}
