package com.thestore.microstore.adapter;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.thestore.microstore.R;
import com.thestore.microstore.ShopCartActivity;
import com.thestore.microstore.vo.shoppingcart.CartItem;

/**
 * 
 * 购物车 数据适配器
 * 
 * 2014-9-5 上午9:13:44
 */
public class ShopCartOverduetAdapter extends ArrayAdapter<CartItem> {

	private final List<CartItem> cartPros;
	private final ShopCartActivity context;
	private static String TAG = "ShopCarOverduetAdapter";

	public ShopCartOverduetAdapter(ShopCartActivity context, int resource, List<CartItem> cartPros) {
		super(context, resource, cartPros);

		this.context = context;
		this.cartPros = cartPros;
	}

	static class ViewHolder {
		protected CheckBox shopcart_item_del_checkbox;
		protected ImageView shopcart_item_prodImage_img;
		protected TextView shopcart_item_desc_text;
		protected TextView shopcart_item_color_text;
		protected TextView shopcart_item_size_text;
		protected TextView shopcart_item_count_text;
		protected TextView shopcart_item_price_text;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final ViewHolder viewHolder;
		View view = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(context, R.layout.shopcart_overdue_listitem, null);
			
			viewHolder.shopcart_item_del_checkbox = (CheckBox) view.findViewById(R.id.shopcart_item_del_checkbox_due); // 商品checkbox
			viewHolder.shopcart_item_prodImage_img = (ImageView) view.findViewById(R.id.shopcart_item_prodImage_img_due); // 商品图片

			viewHolder.shopcart_item_desc_text = (TextView) view.findViewById(R.id.shopcart_item_desc_text_due); // 商品简介
 
			viewHolder.shopcart_item_color_text = (TextView) view.findViewById(R.id.shopcart_item_color_text_due); // 商品颜色
			viewHolder.shopcart_item_size_text = (TextView) view.findViewById(R.id.shopcart_item_size_text_due); // 商品大小
			viewHolder.shopcart_item_count_text = (TextView) view.findViewById(R.id.shopcart_item_count_text_due); // 商品数量
			viewHolder.shopcart_item_price_text = (TextView) view.findViewById(R.id.shopcart_item_price_text_due); // 商品价格

			view.setTag(viewHolder);
			viewHolder.shopcart_item_del_checkbox.setTag(cartPros.get(position));
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
			view = convertView;
			((ViewHolder) view.getTag()).shopcart_item_del_checkbox.setTag(cartPros.get(position));
		}

		CartItem cartPro = cartPros.get(position);		
		viewHolder.shopcart_item_del_checkbox.setChecked(cartPro.isChecked());
        // 给 ImageView 设置一个 tag
		viewHolder.shopcart_item_prodImage_img.setTag(cartPro.getImageUrl());
		new DownImage(viewHolder.shopcart_item_prodImage_img).execute(cartPro.getImageUrl());

		viewHolder.shopcart_item_desc_text.setText(cartPro.getDesc());
		viewHolder.shopcart_item_color_text.setText(cartPro.getColor());
		viewHolder.shopcart_item_size_text.setText(cartPro.getSize());
		viewHolder.shopcart_item_count_text.setText(cartPro.getCount());
		viewHolder.shopcart_item_price_text.setText("￥" + cartPro.getPrice());		
		
		viewHolder.shopcart_item_del_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				CartItem element = (CartItem) viewHolder.shopcart_item_del_checkbox.getTag();
				element.setChecked(buttonView.isChecked());

			}
		});
		
//		viewHolder.shopcart_item_prodImage_img.setOnClickListener(new View.OnClickListener(){//跳转到H5商品详情页    
//            public void onClick(View v) {
//            	CartItem cartItem = (CartItem)viewHolder.shopcart_item_del_checkbox.getTag();
//            	context.toH5DetailIndex(cartItem.getvPmId(), cartItem.getPmId(), cartItem.getvMerchantId());
//            }	  
//        }); 
//		
//		viewHolder.shopcart_item_desc_text.setOnClickListener(new View.OnClickListener(){//跳转到H5商品详情页    
//            public void onClick(View v) {
//            	CartItem cartItem = (CartItem)viewHolder.shopcart_item_del_checkbox.getTag();
//            	context.toH5DetailIndex(cartItem.getvPmId(), cartItem.getPmId(), cartItem.getvMerchantId());
//            }	  
//        }); 

		return view;
	}

	
	/**
	 * 异步加载图片
	 * @author wangliqun
	 *
	 * 2014-9-6 下午10:10:00
	 */
	class DownImage extends AsyncTask<String, Void, Bitmap> {
		private ImageView image;
		private String url;

		public DownImage(ImageView iamgeView) {
			this.image = iamgeView;
		}

		protected Bitmap doInBackground(String... utls) {
			url = utls[0];
			Bitmap bitmap = null;
			// 通过 tag 来防止图片错位
			if(image.getTag() != null && image.getTag().equals(url)){
				try {
					InputStream is = new URL(url).openStream();
					bitmap = BitmapFactory.decodeStream(is);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return bitmap;
		}

		protected void onPostExecute(Bitmap result) {
			// 通过 tag 来防止图片错位
			if(image.getTag() != null && image.getTag().equals(url)){
				image.setImageBitmap(result);
			}
		}

	}

}
