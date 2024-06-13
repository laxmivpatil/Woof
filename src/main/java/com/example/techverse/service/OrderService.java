package com.example.techverse.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.Model.Cart;
import com.example.techverse.Model.CartItem;
import com.example.techverse.Model.Order;
import com.example.techverse.Model.OrderItem;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.CartRepository;
import com.example.techverse.Repository.OrderItemRepository;
import com.example.techverse.Repository.OrderRepository;
import com.example.techverse.Repository.UserRepository;
import com.example.techverse.exception.OrderException;
import com.razorpay.RazorpayException;
 


@Service
public class OrderService {
	

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
    private OrderService1 orderService1;
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderItemService orderItemSrvice;
	
	
 
	
	
	
	public Order createOrder(User user, String shippAddress) throws RazorpayException{
 	Cart cart =cartService.findUserCart(user.getId());
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem item:cart.getCartItems())
		{
			OrderItem orderItem=new OrderItem();
			
			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());
			
			
			OrderItem createdOrderItem=orderItemRepository.save(orderItem);
			
			orderItems.add(createdOrderItem);
		}
		
		String order_id=orderService1.createOrder(cart.getTotalPrice(), "INR", RandomStringUtils.randomAlphanumeric(10));
		//money is in mind
		Order createdOrder=new Order();
		createdOrder.setOrderId(order_id);
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setToatalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDicountedPrice());
		createdOrder.setDiscounte(cart.getDiscounte());
		createdOrder.setTotalItem(cart.getTotalItem());
		
		createdOrder.setShippingAddress(shippAddress);
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus("PLACED");
		createdOrder.getPaymentDetails().setStatus("PENDING");
		createdOrder.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder=orderRepository.save(createdOrder);
		
		
		for(OrderItem item: orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}
		
		return savedOrder;
	}

	 
	public Order findOrderById(String orderId) throws OrderException {
		 Optional<Order> opt=orderRepository.findById(orderId);
		 if(opt.isPresent())
		 {
		return opt.get();
		 }
		 throw new OrderException("Order not Exist with id "+orderId);
	}

	 
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders=orderRepository.getUsersOreders(userId);
		return orders;
	}

 
	public Order placedOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setStatus("COMPLETED");
		
		return orderRepository.save(order);
		
	 
	}

	 
	public Order confirmedOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("CONFIRMED");
		 
		return orderRepository.save(order);
	}

	 
	public Order shippedOrder(String  orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("SHIPPED");
		 
		return orderRepository.save(order);
		 
	}

 
	public Order deliveredOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("DELIVERED");
		 
		return orderRepository.save(order);
		 
	}

	 
	public Order cancledOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("CANCELLED");
		 
		return orderRepository.save(order);
		 
	}

 
	public List<Order> getAllOrders() {
		 
		return orderRepository.findAll();
	}

	 
	public void deleteOrder(String orderId) throws OrderException { 
		
		Order order=findOrderById(orderId);
		orderRepository.deleteById(orderId);
		
	}

}

