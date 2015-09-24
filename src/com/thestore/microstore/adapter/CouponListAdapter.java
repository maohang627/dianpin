package com.thestore.microstore.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.thestore.microstore.CouponListActivity;
import com.thestore.microstore.R;
import com.thestore.microstore.vo.coupon.CouponVo;



public class CouponListAdapter extends ArrayAdapter<CouponVo> {

	private List<CouponVo> couponList;
	private final CouponListActivity context;
	private static String TAG = "vdian_couponListAdapter";
	

	public CouponListAdapter(CouponListActivity context, int resource, List<CouponVo> couponList) {
		super(context, resource, couponList);
		this.context = context;
		this.couponList = couponList;
	}

	static class ViewHolder {
		View coupon_head;
		CheckBox coupon_checkbox;
		TextView coupon_type_desc;
		TextView coupon_card_num;
		TextView coupon_amount;
		TextView coupon_description1;
		TextView coupon_description2;
		TextView coupon_description3;
		TextView coupon_description4;
		TextView coupon_time;
		ImageView coupon_img;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;				
		if (convertView == null) {
			view = View.inflate(context, R.layout.coupon_listitem_activity, null);

			final ViewHolder viewHolder = new ViewHolder();

			viewHolder.coupon_head = (View) view.findViewById(R.id.coupon_head); 
			viewHolder.coupon_checkbox = (CheckBox) view.findViewById(R.id.coupon_checkbox);
			viewHolder.coupon_type_desc = (TextView) view.findViewById(R.id.coupon_type_desc);
			viewHolder.coupon_card_num = (TextView) view.findViewById(R.id.coupon_card_num);
			viewHolder.coupon_amount = (TextView) view.findViewById(R.id.coupon_amount);
			viewHolder.coupon_description1 = (TextView) view.findViewById(R.id.coupon_description1);
			viewHolder.coupon_description2 = (TextView) view.findViewById(R.id.coupon_description2);
			viewHolder.coupon_description3 = (TextView) view.findViewById(R.id.coupon_description3);
			viewHolder.coupon_description4 = (TextView) view.findViewById(R.id.coupon_description4);
		
			viewHolder.coupon_time = (TextView) view.findViewById(R.id.coupon_time);
			viewHolder.coupon_img =(ImageView) view.findViewById(R.id.coupon_img);
			
			view.setTag(viewHolder);
			viewHolder.coupon_checkbox.setTag(couponList.get(position));
			
			
		} else {
			view = convertView;
			((ViewHolder)view.getTag()).coupon_checkbox.setTag(couponList.get(position));
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();			
						
		CouponVo couponVo = couponList.get(position);
		if(couponVo.isShowHead()==true){
			viewHolder.coupon_head.setVisibility(View.VISIBLE);
		}		
		
		if(!couponVo.isCanUse()){
			viewHolder.coupon_img.setBackgroundResource(R.drawable.coupon_grey);
		}		
		if(couponVo.getDescription()!=null && couponVo.getDescription().size()>0){
			
			String description = couponVo.getDescription().get(0);
			if(description!=null){
				if(description.indexOf("满") !=-1 && description.indexOf("抵用") !=-1){	
					
					viewHolder.coupon_description1.setText("满");										
					viewHolder.coupon_description2.setText(description.substring(1, description.indexOf("抵用")));														
					viewHolder.coupon_description3.setText("抵用");					
					viewHolder.coupon_description4.setText(description.substring(description.indexOf("抵用")+2));															
				}else{					
					viewHolder.coupon_description1.setText(description);					
				}
			}
		}		
		viewHolder.coupon_checkbox.setChecked(couponVo.isUsed());
		viewHolder.coupon_type_desc.setText(couponVo.getCouponTypeDesc());
		viewHolder.coupon_card_num.setText(couponVo.getCardNum()+"");
		viewHolder.coupon_amount.setText("￥"+couponVo.getAmount());
		if(couponVo.getBeginTime()!=null && !"".equals(couponVo.getBeginTime()) && couponVo.getExpiredTime()!=null && !"".equals(couponVo.getExpiredTime())){
			viewHolder.coupon_time.setText(couponVo.getBeginTime()+"至"+couponVo.getExpiredTime());
		}
		
		return view;
	}

}
