package com.example.techverse.Model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

 

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	private User user;


	@OneToMany(mappedBy="cart",cascade=CascadeType.ALL,orphanRemoval=true)
	private Set<CartItem> cartItems=new HashSet<>();

	@Column(name="total_price")
	private double totalPrice;

	@Column(name="total_item")
	private int totalItem;


	private Long totalDicountedPrice;

	private Long discounte;
	public Cart() {
		// TODO Auto-generated constructor stub
	}
	public Cart(Long id, User user, Set<CartItem> cartItems, double totalPrice, int totalItem, Long totalDicountedPrice,
			Long discounte) {
		super();
		this.id = id;
		this.user = user;
		this.cartItems = cartItems;
		this.totalPrice = totalPrice;
		this.totalItem = totalItem;
		this.totalDicountedPrice = totalDicountedPrice;
		this.discounte = discounte;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}
	public Long getTotalDicountedPrice() {
		return totalDicountedPrice;
	}
	public void setTotalDicountedPrice(Long totalDicountedPrice) {
		this.totalDicountedPrice = totalDicountedPrice;
	}
	public Long getDiscounte() {
		return discounte;
	}
	public void setDiscounte(Long discounte) {
		this.discounte = discounte;
	}





}
