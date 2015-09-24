package com.thestore.microstore.vo.shoppingcart;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;



/**
 * 
 * 购物车VO
 * 
 * 2014-9-6 上午11:24:48
 */
public class ShoppingCart extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//正常的商品
	private List<CartItem> cartItems;
	
	//过期的商品
	private List<CartItem> overDueCartItems;
	
	private String totalPrice;

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<CartItem> getOverDueCartItems() {
		return overDueCartItems;
	}

	public void setOverDueCartItems(List<CartItem> overDueCartItems) {
		this.overDueCartItems = overDueCartItems;
	}

}
