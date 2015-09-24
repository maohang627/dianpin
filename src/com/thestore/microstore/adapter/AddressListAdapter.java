package com.thestore.microstore.adapter;

import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.thestore.microstore.AddressListActivity;
import com.thestore.microstore.R;
import com.thestore.microstore.vo.Address;

/**
 * 
 * 我的地址 数据适配器
 * 
 * 2014-9-13 下午1:06:22
 */
public class AddressListAdapter extends ArrayAdapter<Address> {

	private List<Address> addressList;
	private final AddressListActivity context;
	private static String TAG = "vidian_AddressListAdapter";
	private int selectedIndex = -1;

	public AddressListAdapter(AddressListActivity context, int resource, List<Address> addressList) {
		super(context, resource, addressList);
		this.context = context;
		this.addressList = addressList;
	}

	static class ViewHolder {
		CheckBox address_checkbox;
		TextView address_name;
		TextView address_address;
		TextView address_address_detail;
		TextView address_tel;
		ImageView address_update_button;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if (convertView == null) {
			view = View.inflate(context, R.layout.address_listitem_activity, null);

			final ViewHolder viewHolder = new ViewHolder();

			viewHolder.address_checkbox = (CheckBox) view.findViewById(R.id.address_checkbox); // 我checkbox
			viewHolder.address_name = (TextView) view.findViewById(R.id.address_name);
			viewHolder.address_address = (TextView) view.findViewById(R.id.address_address);
			viewHolder.address_address_detail = (TextView) view.findViewById(R.id.address_address_detail);
			viewHolder.address_tel = (TextView) view.findViewById(R.id.address_tel);
			viewHolder.address_update_button = (ImageView) view.findViewById(R.id.address_update_button);

			viewHolder.address_update_button.setOnClickListener(this.context);

			view.setTag(viewHolder);
			viewHolder.address_checkbox.setTag(addressList.get(position));
			viewHolder.address_update_button.setTag(addressList.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).address_checkbox.setTag(addressList.get(position));
			((ViewHolder) view.getTag()).address_update_button.setTag(addressList.get(position));
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		Address address = addressList.get(position);
		if (selectedIndex != -1) {
			address.setSelected(selectedIndex == position);
		}
		viewHolder.address_checkbox.setChecked(address.isSelected());
		viewHolder.address_name.setText(address.getName());
		viewHolder.address_address.setText(address.getAddress());
		viewHolder.address_address_detail.setText(address.getAddressDetail());
		viewHolder.address_tel.setText(address.getTel());

		return view;
	}

	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}

}
