package com.thestore.microstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.thestore.microstore.adapter.AddressListAdapter;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.OrderInitParser;
import com.thestore.microstore.parser.ProvinceParser;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.Address;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;
import com.thestore.microstore.vo.order.Order;
import com.thestore.microstore.vo.proxy.Province;
import com.thestore.microstore.vo.proxy.ProvinceProxy;
import com.thestore.microstore.widget.ListViewForScrollView;

/**
 * 
 * 我的地址列表
 * 
 * 2014-9-13 下午12:43:08
 */
public class AddressListActivity extends BaseWapperActivity {

	private static String TAG = "vdian_AddressListActivity";

	private AddressListAdapter adapter;

	private List<Address> addressList;

	/** 默认的收货地址ID */
	private String defaultAddressId;

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.address_save_button: // 新建收货地址

			createAddress();

			break;

		case R.id.address_update_button: // 更新按钮

			Intent intent = getIntent();
			intent.setClass(context, AddressActivity.class);
			intent.putExtra("address", (Address) view.getTag());
			intent.putExtra("addressOpr", "update");

			startActivity(intent);
		default:
			Log.i(TAG, "错误的被点击了..");
			break;
		}

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.address_list_activity);
		context = this;
		setTitle("选择收货地址");
		setRightTextTo("新建", true, null);
	}

	/**
	 * 右侧按钮被点击时到达新建页面
	 */
	@Override
	protected void onHeadRightButton(View v) {
		createAddress();
	}

	@Override
	protected void process() {

		draw(null);
	}

	@Override
	protected void initAttrs() {
	}

	private void draw(Object obj) {

		Intent intent = getIntent();
		addressList = (List<Address>) intent.getSerializableExtra("addressList");
		defaultAddressId = intent.getStringExtra("defaultAddressId");

		ListViewForScrollView address_list = (ListViewForScrollView) findViewById(R.id.address_list);
		adapter = new AddressListAdapter(AddressListActivity.this, R.layout.address_listitem_activity, addressList);
		address_list.setAdapter(adapter);
		address_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {

				CheckBox checkBox = (CheckBox) adapterview.getChildAt(position).findViewById(R.id.address_checkbox);
				Address address = (Address) checkBox.getTag();

				reDraw(address, position);

				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();
			}
		});

		Button address_save_button = (Button) findViewById(R.id.address_save_button);
		address_save_button.setOnClickListener(this);

	}

	/**
	 * 重新绘制我的地址列表页
	 * 
	 * @param address
	 */
	public void reDraw(Address address, int position) {

		// 如果本来就是选中的
		if (address.isSelected()) {

			this.finish();

			return;
		}

		String provinceId = address.getProvinceId();
		String curProvinceId = CookiesUtil.getProvinceId();

		// 如果是同一个address被点击了，则直接跳转到订单页
		if (defaultAddressId.equals(address.getId())) {

			finish();

			return;
		}

		// 如果省份相同 则调用saveReceiverInfo刷新初始化的订单
		if (provinceId.equals(curProvinceId)) {

			// 编辑收货地址
			RequestVo requestVo = RequestVo.setCommontParam(AddressListActivity.this.context);
			requestVo.context = AddressListActivity.this.context;
			HashMap<String, String> postParamMap = requestVo.requestDataMap;
			postParamMap.put("deviceCode", Config.getDeviceCode(AddressListActivity.this.context));
			postParamMap.put("id", address.getId()); // 收货地址Id 编辑保存时需要传入编辑的id 如果没有当做新增地址
			postParamMap.put("receiverName", address.getName()); // 收货人姓名 必填
			postParamMap.put("address1", address.getAddressDetail()); // 详细地址 必填
			postParamMap.put("mobile", address.getTel());
			postParamMap.put("provinceID", address.getProvinceId()); // 一级id 必填
			postParamMap.put("cityID", address.getCityId()); // 二级id 必填
			postParamMap.put("countyID", address.getAreaId()); // 三级id 必填
			postParamMap.put("defaultReceiver", address.getDefaultReceiver()); // 是否默认（1：默认 0：非默认) 必填
			requestVo.jsonParser = new OrderInitParser();
			requestVo.requestUrl = Config.SERVLET_URL_SAVE_RECEIVERINFO;
			asynForLoadDataAndDraw(requestVo, new DataCallback<Order>() {

				@Override
				public void processData(Order paramObject) {
					Intent intent = getIntent();
					intent.setClass(AddressListActivity.this, OrderActivity.class);
					intent.putExtra("resource", "AddressListActivity");
					intent.putExtra("order", paramObject);
					AddressListActivity.this.startActivity(intent);
				}

			});

		} else {
			showDialog(curProvinceId, address.getProvinceName(), provinceId);
		}

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
				Builder builder = new Builder(AddressListActivity.this);
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
						CookiesUtil.setProvinceId(AddressListActivity.this.context, provinceId);

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
		RequestVo requestVo = RequestVo.setCommontParam(AddressListActivity.this.context);
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
				intent.setClass(AddressListActivity.this, ShopCartActivity.class);
				AddressListActivity.this.startActivity(intent);
				AddressListActivity.this.finish();

			}
		});
	}

	/**
	 * 到达创建地址信息页
	 */
	private void createAddress() {
		
		SharedPreferences sp = this.getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
		// 收货地址个数上限（默认10）
		int goodReceiverMaxNum= sp.getInt("mGoodReceiverMaxNum", 0) == 0 ? 10 : sp.getInt("mGoodReceiverMaxNum", 0);
		if(addressList.size() >= goodReceiverMaxNum){
			// 收货地址个数已达上限
			showCommDialog("", "收货地址个数已达上限！", false);
		}else{
			Intent intent = getIntent();
			intent.setClass(this, AddressActivity.class);
			intent.putExtra("addressOpr", "create");
			intent.putExtra("defaultAddressId", defaultAddressId);
			startActivity(intent);
		}

	}

}
