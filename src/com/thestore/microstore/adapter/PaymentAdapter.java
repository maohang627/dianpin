package com.thestore.microstore.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thestore.microstore.R;
import com.thestore.microstore.vo.order.Payment;

/**
 * 
 * 支付方式 数据适配器
 * 
 * 2014-9-13 下午1:06:22
 */
public class PaymentAdapter extends ArrayAdapter<Payment> {

	private List<Payment> paymentList;
	private Context context;
	private static String TAG = "vidian_PaymentAdapter";
	private int selectedIndex = 0;

	public PaymentAdapter(Context context, int resource, List<Payment> paymentList) {
		super(context, resource, paymentList);
		this.context = context;
		this.paymentList = paymentList;
	}

	static class ViewHolder {
		TextView opi_name;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView == null) {
			view = View.inflate(context, R.layout.order_payment_items_activity, null);

			final ViewHolder viewHolder = new ViewHolder();

			viewHolder.opi_name = (TextView) view.findViewById(R.id.opi_name);

			view.setTag(viewHolder);
		} else {
			view = convertView;
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		Payment payment = paymentList.get(position);

		if (!"无可用支付方式".equals(payment.getName()) && selectedIndex == position) {
			viewHolder.opi_name.setTextColor(context.getResources().getColor(R.color.yhd_red));
		} else {
			viewHolder.opi_name.setTextColor(context.getResources().getColor(R.color.hei_se));
		}

		viewHolder.opi_name.setText(payment.getName());

		return view;
	}

	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}

}
