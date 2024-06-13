package com.example.techverse.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

 
@Entity
public class CartItem {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	@JsonIgnore
	@ManyToOne
	private Cart cart;
	
	
	@ManyToOne
	private Product product;
	
	private String size;
	
	private int quantity;
	
	private Long price;
	
	private Long discountedPrice;
	
	private Long userId;
	
	public CartItem() {
		// TODO Auto-generated constructor stub
	}

	public CartItem(Long id, Cart cart, Product product, String size, int quantity, Long price,
			Long  discountedPrice, Long userId) {
		super();
		this.id = id;
		this.cart = cart;
		this.product = product;
		this.size = size;
		this.quantity = quantity;
		this.price = price;
		this.discountedPrice = discountedPrice;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(long l) {
		this.price = l;
	}

	public Long  getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(Long  discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
	
	

}