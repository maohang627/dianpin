package com.thestore.microstore.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thestore.microstore.R;
import com.thestore.microstore.vo.order.OrderPro;

/**
 * 
 * 订单 商品列表部分
 * 
 * 2014-9-14 下午10:42:56
 */
public class OrderProListAdapter extends ArrayAdapter<OrderPro> {

	private static String TAG = "vdian_OrderProListAdapter";

	private int count;

	private Context context;

	private List<OrderPro> orderProList;

	public OrderProListAdapter(Context context, int resource, List<OrderPro> objects) {
		super(context, resource, objects);

		this.context = context;
		count = objects.size();
		orderProList = objects;
	}

	@Override
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	static class ViewHolder {
		TextView opia_name;
		TextView opia_price;
		TextView opia_count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "当前索引: " + position);
		View view = null;
		if (convertView == null) {
			view = View.inflate(context, R.layout.order_pros_items_activity, null);

			final ViewHolder viewHolder = new ViewHolder();

			viewHolder.opia_name = (TextView) view.findViewById(R.id.opia_name);
			viewHolder.opia_price = (TextView) view.findViewById(R.id.opia_price);
			viewHolder.opia_count = (TextView) view.findViewById(R.id.opia_count);
			view.setTag(viewHolder);

		} else {
			view = convertView;
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		OrderPro pro = orderProList.get(position);

		viewHolder.opia_name.setText(pro.getName());
		viewHolder.opia_price.setText("￥" + pro.getPrice());
		viewHolder.opia_count.setText("x" + pro.getCount());

		return view;
	}

}
