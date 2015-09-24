package com.thestore.microstore.adapter;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.thestore.microstore.R;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.vo.order.OrderPro;

/**
 * 
 * 订单 商品 展现更多按钮 被点击 处理类
 *
 * 2014-9-17 上午8:50:15
 */
public class OrderLoadOthersListener implements OnClickListener {

	private OrderProListAdapter adapter;
	Button currentButton;
	private int count; // 商品记录数
	private List<OrderPro> orderProList;

	public OrderLoadOthersListener(Button currentButton, OrderProListAdapter adapter, int count, List<OrderPro> orderProList) {
		this.currentButton = currentButton;
		this.adapter = adapter;
		this.count = count;
		this.orderProList = orderProList;
		int quantity = 0;
		for (int i = adapter.getCount(); i < orderProList.size(); i++) {
			OrderPro orderPro = orderProList.get(i);
			quantity += Integer.valueOf(orderPro.getCount());
		}
		currentButton.setText("还有更多" + (quantity) + "件商品");
	}

	@Override
	public void onClick(View view) {
		if (adapter.getCount() == Const.ORDER_PRO_DEFAULT_PAGE_SIZE) {
			adapter.setCount(count);
			currentButton.setBackgroundResource(R.drawable.spinner_bg_up);
			currentButton.setText("收起包裹清单");
		} else {
			adapter.setCount(Const.ORDER_PRO_DEFAULT_PAGE_SIZE);
			currentButton.setBackgroundResource(R.drawable.spinner_bg_down);
			int quantity = 0;
			for (int i = adapter.getCount(); i < orderProList.size(); i++) {
				OrderPro orderPro = orderProList.get(i);
				quantity += Integer.valueOf(orderPro.getCount());
			}
			currentButton.setText("还有更多" + (quantity) + "件商品");
		}
		adapter.notifyDataSetChanged();
	}

}
