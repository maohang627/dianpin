package com.thestore.microstore;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.AreaParser;
import com.thestore.microstore.parser.CityParser;
import com.thestore.microstore.parser.OrderInitParser;
import com.thestore.microstore.parser.ProvinceParser;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.ProvinceCityCountyManager;
import com.thestore.microstore.util.Tracker;
import com.thestore.microstore.vo.Address;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;
import com.thestore.microstore.vo.commom.BaseEntity;
import com.thestore.microstore.vo.order.Order;
import com.thestore.microstore.vo.proxy.Area;
import com.thestore.microstore.vo.proxy.AreaProxy;
import com.thestore.microstore.vo.proxy.City;
import com.thestore.microstore.vo.proxy.CityProxy;
import com.thestore.microstore.vo.proxy.Province;
import com.thestore.microstore.vo.proxy.ProvinceProxy;
import com.thestore.microstore.widget.UISwitchButton;

/**
 * 
 * 我的地址
 * 
 * 2014-9-12 上午11:23:05
 */
public class AddressActivity extends BaseWapperActivity implements OnItemSelectedListener {

	private static String TAG = "vdian_AddressActivity";

	/** 省份 */
	private Spinner provinceSpinner;
	private ArrayAdapter<Province> provinceAdapter;

	/** 城市 */
	private Spinner citySpinner;

	/** 区域 */
	private Spinner areaSpinner;

	/** 我的地址实体 */
	private Address address;

	private TextView address_name;
	private TextView address_tel;
	private TextView address_detail;
	private UISwitchButton address_is_default;

	/** 创建(create) or 更新 (update) */
	private String addressOpr;

	/** 是否是默认地址 是否默认（1：默认 0：非默认) */
	private String defaultReceiver = "0";
	private String defaultAddressId;	//仅修改的时候判断是否是默认
	
	private String mProvinceId = "1"; // 默认上海	
	private ProvinceCityCountyManager provinceCityCountyManager;

	@Override
	public void onClick(View view) {

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.address_input_activity);
		context = this;
		provinceCityCountyManager = ProvinceCityCountyManager.getInstance(context);
		Tracker.trackPage(Const.PAGE_CODE_VDIAN_ORDERADDRESS);// BI埋点
	}

	@Override
	protected void process() {

		Intent intent = getIntent();

		addressOpr = intent.getStringExtra("addressOpr");

		if (addressOpr.equals("create")) {
			setTitle("新建收货地址");
			setRightTextTo("保存", true, null);

			draw(null);
		} else {
			setTitle("编辑收货地址");
			setRightTextTo("保存", true, null);
			
			defaultAddressId = intent.getStringExtra("defaultAddressId");			
			draw(intent.getSerializableExtra("address"));
		}

	}

	@Override
	protected void initAttrs() {

	}

	private void draw(Object obj) {

		address_name = (TextView) findViewById(R.id.address_name); // 收货人名称
		address_tel = (TextView) findViewById(R.id.address_tel); // 收货人电话
		address_detail = (TextView) findViewById(R.id.address_detail); // 收货人详细地址

		mProvinceId = CookiesUtil.getProvinceId();
		if (addressOpr.equals("update")) {
			address = (Address) obj;
			address_name.setText(address.getName());
			address_tel.setText(address.getTel());
			address_detail.setText(address.getAddressDetail());
			mProvinceId = address.getProvinceId();
			
//			if(!TextUtils.isEmpty(defaultAddressId) && defaultAddressId.equals(address.getId())) {
//				defaultReceiver = "1";
//			}
			defaultReceiver = address.getDefaultReceiver();
			
		} else {

			// 新建页面中手机号默认填写为当前省份上一次编辑时保存的手机号码
			String prevMobile = CookiesUtil.getPrevUserMobileNum();
			if (TextUtils.isEmpty(prevMobile)) {
				prevMobile = CookiesUtil.getUserMobileNumFrom();
				
				if(!TextUtils.isEmpty(prevMobile)) {
					CookiesUtil.setUserMobileNum(context, prevMobile);
				}
			}
			
			address_tel.setText(prevMobile);

		}

		// 初始化省份
		provinceSpinner = (Spinner) findViewById(R.id.address_province_spinner);
		// 初始化城市
		citySpinner = (Spinner) findViewById(R.id.address_city_spinner);
		// 初始化区域
		areaSpinner = (Spinner) findViewById(R.id.address_area_spinner);

		// 省份
//		RequestVo requestVo = new RequestVo();
//		requestVo.context = this.context;
//		requestVo.requestUrl = Config.SERVLET_URL_GET_ALLPROVINCE;
//		requestVo.jsonParser = new ProvinceParser(provinceId);
//		setShowProcessDialog(Boolean.FALSE); // 设置不显示过程弹出层
//
//		asynForLoadDataAndDraw(requestVo, new DataCallback<ProvinceProxy>() {
//
//			@Override
//			public void processData(ProvinceProxy paramObject) {
//				Logger.d(TAG, "加载省份列表");
//				setShowProcessDialog(Boolean.TRUE); // 恢复弹出层显示
//				provinceAdapter = new ArrayAdapter<Province>(AddressActivity.this, R.layout.spinner_item_self, paramObject.getProvinceList());
//				provinceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
//				provinceSpinner.setAdapter(provinceAdapter);
//				provinceSpinner.setPrompt("省份");
//				provinceSpinner.setSelection(paramObject.getPosition());
//				provinceSpinner.setOnItemSelectedListener(AddressActivity.this);
//
//			}
//
//		});
		
		Logger.d(TAG, "加载省份列表");
		ProvinceProxy provinces = provinceCityCountyManager.getProvinces(mProvinceId);
		provinceAdapter = new ArrayAdapter<Province>(AddressActivity.this, R.layout.spinner_item_self, provinces.getProvinceList());
		provinceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
		provinceSpinner.setAdapter(provinceAdapter);
		provinceSpinner.setPrompt("省份");
		provinceSpinner.setSelection(provinces.getPosition());
		provinceSpinner.setOnItemSelectedListener(AddressActivity.this);
		
		//
		// // -- 删除
		// Button aia_del_button = (Button) findViewById(R.id.aia_del_button);
		// aia_del_button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		//
		// RequestVo requestVo = RequestVo.setCommontParam(AddressActivity.this.context);
		// requestVo.context = AddressActivity.this.context;
		// HashMap<String, String> postParamMap = requestVo.requestDataMap;
		// postParamMap.put("deviceCode", Config.getDeviceCode(AddressActivity.this.context));
		// postParamMap.put("receiverId", address.getId()); // 地址的ID
		// requestVo.jsonParser = new ResultParser();
		// requestVo.requestUrl = Config.delReceiverInfo; //"http://vd.yhd.com/service/1/vdianCartService/delReceiverInfo";
		//
		// asynForLoadDataAndDraw(requestVo, new DataCallback<Result>() {
		//
		// @Override
		// public void processData(Result paramObject, boolean paramBoolean) {
		//
		// Log.i(TAG, "删除收获地址;" + paramObject.toString());
		//
		// // 跳转到地址列表页
		// Intent intent = getIntent();
		// intent.setClass(AddressActivity.this.context, AddressListActivity.class);
		// AddressActivity.this.startActivity(intent);
		// AddressActivity.this.finish();
		// }
		//
		// });
		//
		// }
		// });

		// -- 保存
		address_is_default = (UISwitchButton) findViewById(R.id.address_is_default);
		
		//初始化
		if("1".equals(defaultReceiver)) {
			address_is_default.setChecked(Boolean.TRUE);
		}
		
		address_is_default.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
				if (flag) {
					defaultReceiver = "1";
				} else {
					defaultReceiver = "0";
				}
			}
		});
		
		Button aia_save_button = (Button) findViewById(R.id.aia_save_button);
		aia_save_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				saveAddress();
			}
		});

	}
	
	/**
	 * 保存收货地址
	 */
	private void saveAddress(){

		// 获取地址的ID
		RequestVo requestVo = RequestVo.setCommontParam(AddressActivity.this.context);
		requestVo.context = AddressActivity.this.context;
		HashMap<String, String> postParamMap = requestVo.requestDataMap;
		postParamMap.put("deviceCode", Config.getDeviceCode(AddressActivity.this.context));
		if (addressOpr.equals("update")) {
			postParamMap.put("id", address.getId()); // 收货地址Id 编辑保存时需要传入编辑的id 如果没有当做新增地址
		}
		postParamMap.put("receiverName", TextUtils.htmlEncode(address_name.getText().toString())); // 收货人姓名 必填（使用HTML编码）
		postParamMap.put("address1", TextUtils.htmlEncode(address_detail.getText().toString())); // 详细地址 必填（使用HTML编码）
		postParamMap.put("mobile", TextUtils.htmlEncode(address_tel.getText().toString()));// （使用HTML编码）
		postParamMap.put("provinceID", ((Province) provinceSpinner.getSelectedItem()).getId()); // 一级id 必填
		postParamMap.put("cityID", ((City) citySpinner.getSelectedItem()).getId()); // 二级id 必填
		postParamMap.put("countyID", ((Area) areaSpinner.getSelectedItem()).getId()); // 三级id 必填
		postParamMap.put("defaultReceiver", defaultReceiver); // 是否默认（1：默认 0：非默认) 必填
		requestVo.jsonParser = new OrderInitParser();
		requestVo.requestUrl = Config.SERVLET_URL_SAVE_RECEIVERINFO;

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

				Log.i(TAG, "保存收货地址;" + paramObject.toString());

				// 设置cookie中的手机号码为当前手机号码,: 新建页面中手机号默认填写为当前省份上一次编辑时保存的手机号码
				CookiesUtil.setUserMobileNum(AddressActivity.this.context, address_tel.getText().toString());

				// 跳转到订单确认页
				Intent intent = getIntent();
				intent.setClass(AddressActivity.this.context, OrderActivity.class);
				intent.putExtra("order", paramObject);
				AddressActivity.this.startActivity(intent);
				AddressActivity.this.finish();

			}

		});
	
	}

	/**
	 * 下拉框选择事件
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int postion, long i) {
		RequestVo requestVo = new RequestVo();
		requestVo.context = this.context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();

		switch (parent.getId()) {
		case R.id.address_province_spinner:

			Province province = (Province) parent.getSelectedItem();
			mProvinceId = province.getId();

			Log.i(TAG, "省份ID是：" + province.getId());

			// 先清空城市、区域
			ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(AddressActivity.this, R.layout.spinner_item_self);
			cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
			citySpinner.setAdapter(cityAdapter);
			citySpinner.setPrompt("城市");
			ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(AddressActivity.this, R.layout.spinner_item_self);
			areaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
			areaSpinner.setAdapter(areaAdapter);
			areaSpinner.setPrompt("区域");
			
			// 城市
//			postParamMap.clear();
//			postParamMap.put("provinceId", province.getId());
//			requestVo.requestDataMap = postParamMap;
//			requestVo.requestUrl = Config.SERVLET_URL_GET_CITY_BYPROVINCEID;
			String cityId = null;
			if (addressOpr.equals("update")) {
				cityId = address.getCityId();
				address.setCityId("");
			}
//			requestVo.jsonParser = new CityParser(cityId);
//			setShowProcessDialog(Boolean.FALSE); // 设置不显示过程弹出层
//
//			asynForLoadDataAndDraw(requestVo, new DataCallback<CityProxy>() {
//
//				@Override
//				public void processData(CityProxy paramObject) {
//					Logger.d(TAG, "加载城市列表");
//					setShowProcessDialog(Boolean.TRUE); // 恢复弹出层显示
//					ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(AddressActivity.this, R.layout.spinner_item_self, paramObject.getCityList());
//					cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
//					citySpinner.setAdapter(cityAdapter);
//					citySpinner.setPrompt("城市");
//					citySpinner.setSelection(paramObject.getPosition());
//					citySpinner.setOnItemSelectedListener(AddressActivity.this);
//				}
//
//			});
			
			Logger.d(TAG, "加载城市列表");
			CityProxy citys = provinceCityCountyManager.getCitys(province.getId(), cityId);
			cityAdapter = new ArrayAdapter<City>(AddressActivity.this, R.layout.spinner_item_self, citys.getCityList());
			cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
			citySpinner.setAdapter(cityAdapter);
			citySpinner.setPrompt("城市");
			citySpinner.setSelection(citys.getPosition());
			citySpinner.setOnItemSelectedListener(AddressActivity.this);

			break;
		case R.id.address_city_spinner:

			City city = (City) parent.getSelectedItem();

			// 区域			
//			postParamMap.clear();
//			postParamMap.put("cityId", city.getId());
//			requestVo.requestDataMap = postParamMap;
//			requestVo.requestUrl = Config.SERVLET_URL_GET_COUNTY_BYCITYID;
			String areaId = null;
			if (addressOpr.equals("update")) {
				areaId = address.getAreaId();
				address.setAreaId("");
			}
//			requestVo.jsonParser = new AreaParser(areaId);
//			setShowProcessDialog(Boolean.FALSE); // 设置不显示过程弹出层
//
//			asynForLoadDataAndDraw(requestVo, new DataCallback<AreaProxy>() {
//
//				@Override
//				public void processData(AreaProxy paramObject) {
//					Logger.d(TAG, "加载区域列表");
//					setShowProcessDialog(Boolean.TRUE); // 恢复弹出层显示
//					ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(AddressActivity.this, R.layout.spinner_item_self, paramObject.getAreaList());
//					areaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
//					areaSpinner.setAdapter(areaAdapter);
//					areaSpinner.setPrompt("区域");
//					areaSpinner.setSelection(paramObject.getPosition());
//					areaSpinner.setOnItemSelectedListener(AddressActivity.this);
//
//				}
//
//			});
			
			Logger.d(TAG, "加载区域列表");
			AreaProxy areas = provinceCityCountyManager.getAreas(mProvinceId, city.getId(), areaId);
			areaAdapter = new ArrayAdapter<Area>(AddressActivity.this, R.layout.spinner_item_self, areas.getAreaList());
			areaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_self);
			areaSpinner.setAdapter(areaAdapter);
			areaSpinner.setPrompt("区域");
			areaSpinner.setSelection(areas.getPosition());
			areaSpinner.setOnItemSelectedListener(AddressActivity.this);

			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}
	
	/**
	 * 右侧保存按钮被点击
	 */
	@Override
	protected void onHeadRightButton(View v) {
		saveAddress();
	}

	// / 工具方法

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
				Builder builder = new Builder(AddressActivity.this);
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
						CookiesUtil.setProvinceId(AddressActivity.this.context, provinceId);

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

	/**
	 * 切换省份, 为 用户选择的省份
	 */
	private void switchProvince(String provinceId) {
		// 切换省份
		RequestVo requestVo = RequestVo.setCommontParam(AddressActivity.this.context);
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
				intent.setClass(AddressActivity.this, ShopCartActivity.class);
				AddressActivity.this.startActivity(intent);
				AddressActivity.this.finish();

			}
		});
	}

}
