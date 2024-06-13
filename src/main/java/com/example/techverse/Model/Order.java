package com.example.techverse.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

 

@Entity
@Table(name="orders")
public class Order {
	
	@Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-long-id")
   // @GenericGenerator(name = "custom-long-id", strategy = "com.techverse.config.CustomLongIDGenerator")
    private String orderId;
	
	 
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy="order",cascade=CascadeType.ALL)
	private List<OrderItem> orderItems=new ArrayList<>();
	
	private LocalDateTime orderDate;
	
	private LocalDateTime deliveryDate;
	
 
	private String shippingAddress;
	
	@javax.persistence.Embedded
	private PaymentDetails paymentDetails=new PaymentDetails();
	
	private double toatalPrice;
	
	private Long totalDiscountedPrice;
	
	private Long discounte;
	
	private String orderStatus;
	
	private int totalItem;
	
	private LocalDateTime createdAt;
	
	
	
	

	public Order() {
 
		// TODO Auto-generated constructor stub
	}

	public Order(String orderId,  User user, List<OrderItem> orderItems, LocalDateTime orderDate,
			LocalDateTime deliveryDate, String shippingAddress, PaymentDetails paymentDetails, double toatalPrice,
			Long  totalDiscountedPrice, Long  discounte, String orderStatus, int totalItem,
			LocalDateTime createdAt) {
		super();
		this.orderId = orderId; 
		this.user = user;
		this.orderItems = orderItems;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.shippingAddress = shippingAddress;
		this.paymentDetails = paymentDetails;
		this.toatalPrice = toatalPrice;
		this.totalDiscountedPrice = totalDiscountedPrice;
		this.discounte = discounte;
		this.orderStatus = orderStatus;
		this.totalItem = totalItem;
		this.createdAt = createdAt;
	}

	 
	 

	 

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
 	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	 

	public PaymentDetails getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public double getToatalPrice() {
		return toatalPrice;
	}

	public void setToatalPrice(double toatalPrice) {
		this.toatalPrice = toatalPrice;
	}

	public Long  getTotalDiscountedPrice() {
		return totalDiscountedPrice;
	}

	public void setTotalDiscountedPrice(Long  totalDiscountedPrice) {
		this.totalDiscountedPrice = totalDiscountedPrice;
	}

	public Long  getDiscounte() {
		return discounte;
	}

	public void setDiscounte(Long  discounte) {
		this.discounte = discounte;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
	
	
	
	

}
