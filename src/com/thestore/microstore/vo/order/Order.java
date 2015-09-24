package com.thestore.microstore.vo.order;

import java.util.List;

import com.thestore.microstore.vo.Address;
import com.thestore.microstore.vo.IdValue;
import com.thestore.microstore.vo.commom.BaseEntity;
import com.thestore.microstore.vo.coupon.ShoppingPaymentCoupon;

/**
 * 
 * 订单初始化实体类
 * 
 * 2014-9-16 下午3:46:47
 */
public class Order extends BaseEntity {
	
	private Address selectedAddress;
	
	// 收货地址 receiverDTOList 收货地址 receiverDTOList->receiverDTO
	private List<Address> addressList;

	// 支付方式 paymentList
	private List<Payment> paymentList;
	
	// 发票显示
	private InvoiceShowVo invoiceShowVo;

	// 发票类型 invoiceDTO
	private List<IdValue> invoiceTypeList;

	// 发票类型 invoiceContentStr
	private List<String> invoiceContentList;
	
	// 上一次开的发票内容
	private InvoicesCommDisplay invoicesCommDisplay;

	// 商品清单 merchantList->merchants->deliveryGroups->packages
	private List<ProPackage> proPackageList;

	// 金额
	private String amt;

	// 运费
	private String tranAmt;

	// 共需支付
	private String needPay;
		
    //抵用券支付   
    private ShoppingPaymentCoupon paymentCoupon;

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getTranAmt() {
		return tranAmt;
	}

	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}

	public String getNeedPay() {
		return needPay;
	}

	public void setNeedPay(String needPay) {
		this.needPay = needPay;
	}

	public List<ProPackage> getProPackageList() {
		return proPackageList;
	}

	public void setProPackageList(List<ProPackage> proPackageList) {
		this.proPackageList = proPackageList;
	}

	public InvoiceShowVo getInvoiceShowVo() {
		return invoiceShowVo;
	}

	public void setInvoiceShowVo(InvoiceShowVo invoiceShowVo) {
		this.invoiceShowVo = invoiceShowVo;
	}

	public List<IdValue> getInvoiceTypeList() {
		return invoiceTypeList;
	}

	public void setInvoiceTypeList(List<IdValue> invoiceTypeList) {
		this.invoiceTypeList = invoiceTypeList;
	}

	public List<String> getInvoiceContentList() {
		return invoiceContentList;
	}

	public void setInvoiceContentList(List<String> invoiceContentList) {
		this.invoiceContentList = invoiceContentList;
	}

	public InvoicesCommDisplay getInvoicesCommDisplay() {
		return invoicesCommDisplay;
	}

	public void setInvoicesCommDisplay(InvoicesCommDisplay invoicesCommDisplay) {
		this.invoicesCommDisplay = invoicesCommDisplay;
	}

	public List<Payment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<Payment> paymentList) {
		this.paymentList = paymentList;
	}

	public Address getSelectedAddress() {
		return selectedAddress;
	}

	public void setSelectedAddress(Address selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	public ShoppingPaymentCoupon getPaymentCoupon() {
		return paymentCoupon;
	}

	public void setPaymentCoupon(ShoppingPaymentCoupon paymentCoupon) {
		this.paymentCoupon = paymentCoupon;
	}
	
	

}
