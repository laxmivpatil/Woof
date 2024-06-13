package com.example.techverse.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	/*
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShippingAddressRepository shippingAddressRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	
	
	
	@Autowired
    private OrderService1 orderService1;

    @PostMapping("/generate")
    public String generateOrder(@RequestParam double amount,
                                @RequestParam String currency,
                                @RequestParam String receipt) {
        try {
            return orderService1.createOrder(amount, currency, receipt  );
        } catch (RazorpayException e) {
            e.printStackTrace();
            return "Error creating order: " + e.getMessage();
        }
    }
	
	
    @PostMapping("/addshippingaddress")
    public ResponseEntity<Map<String, Object>> addShippingAddress(@RequestBody ShippingAddress shippingAddress,@RequestHeader("Authorization") String jwt)throws UserException {
        // Retrieve the user by ID (you may adjust this based on your authentication mechanism)
    	User user =userService.findUserProfileByJwt(jwt).get();
         

        // Set the user for the shipping address
        shippingAddress.setUser(user);
        shippingAddressRepository.save(shippingAddress);
        // Add the shipping address to the user's list of shipping addresses
        user.getShippingAddresses().forEach(address -> address.setSetDefaultAddress(false));

        user.getShippingAddresses().add(shippingAddress);
        userRepository.save(user);
        
        Map<String,Object> response = new HashMap<>();
        response.put("ShippingAddress", shippingAddress);

        response.put("status", true);
        response.put("message", "shipping Address added successfully");
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }
    @GetMapping("/setshippingaddress")
    public ResponseEntity<Map<String, Object>> setShippingAddress(@RequestParam Long addressId,@RequestHeader("Authorization") String jwt)throws UserException {
        // Retrieve the user by ID (you may adjust this based on your authentication mechanism)
    	User user =userService.findUserProfileByJwt(jwt).get();
         

System.out.println("fkdgjkhdfkjghkdfjhg");
        // Fetch the shipping address by ID
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            // Handle case where address ID is not found
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found for ID: " + addressId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress shippingAddress = optionalShippingAddress.get();

        // Set the user for the shipping address
       // shippingAddress.setUser(user);

        // Set default address for the selected address
        shippingAddress.setSetDefaultAddress(true);

        // Set all other addresses of the user to non-default
        user.getShippingAddresses().forEach(address -> {
            if (!address.getId().equals(addressId)) {
                address.setSetDefaultAddress(false);
            }
        });

        // Save the updated shipping address and user
        shippingAddressRepository.save(shippingAddress);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("ShippingAddress", shippingAddress);
        response.put("status", true);
        response.put("message", "Default shipping address set successfully");

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getshippingaddresses")
    public ResponseEntity<Map<String, Object>> getShippingAddressesByUser(@RequestHeader("Authorization") String jwt)throws UserException {
    	User user =userService.findUserProfileByJwt(jwt).get();
        
 
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses();

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping addresses retrieved successfully");
        response.put("shippingAddresses", shippingAddresses);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getshippingaddress/default")
    public ResponseEntity<Map<String, Object>> getDefaultShippingAddressesByUser(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt).get();

        // Filter shipping addresses where setDefaultAddress is true
        List<ShippingAddress> shippingAddresses = user.getShippingAddresses().stream()
                .filter(ShippingAddress::isSetDefaultAddress)
                .collect(Collectors.toList());

        if (shippingAddresses.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Default shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Default shipping address retrieved successfully");
        response.put("shippingAddresses", shippingAddresses);

        return ResponseEntity.ok(response);
    }
    @PutMapping("/editshippingaddress/{addressId}")
    public ResponseEntity<Map<String, Object>> editShippingAddress(
            @PathVariable("addressId") Long addressId,
            @RequestBody ShippingAddress updatedAddress,
            @RequestHeader("Authorization") String jwt)throws UserException {
    	User user =userService.findUserProfileByJwt(jwt).get();
        

        
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress existingAddress = optionalShippingAddress.get();
        // Check if the existing address belongs to the user
        if (!existingAddress.getUser().equals(user)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address does not belong to the user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Update the existing address with the new data
        existingAddress.setStreet1(updatedAddress.getStreet1());
        existingAddress.setStreet2(updatedAddress.getStreet2());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setPincode(updatedAddress.getPincode());
        existingAddress.setMobile(updatedAddress.getMobile());
        existingAddress.setAlternateMobile(updatedAddress.getAlternateMobile());
        existingAddress.setLandmark(updatedAddress.getLandmark());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setAddressType(updatedAddress.getAddressType());
        existingAddress.setSetDefaultAddress(updatedAddress.isSetDefaultAddress());

        shippingAddressRepository.save(existingAddress);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping address updated successfully");
        response.put("shippingAddress", existingAddress);

        return ResponseEntity.ok(response);
    }
    
    
    @DeleteMapping("/deleteshippingaddress/{addressId}")
    public ResponseEntity<Map<String, Object>> deleteShippingAddress(
            @PathVariable("addressId") Long addressId,
            @RequestHeader("Authorization") String jwt) throws UserException {
    	User user =userService.findUserProfileByJwt(jwt).get();
        

        
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress shippingAddress = optionalShippingAddress.get();
        // Check if the shipping address belongs to the user
        if (!shippingAddress.getUser().equals(user)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address does not belong to the user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        shippingAddressRepository.delete(shippingAddress);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping address deleted successfully");

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getshippingaddress/{addressId}")
    public ResponseEntity<Map<String, Object>> getShippingAddressById(
            @PathVariable("addressId") Long addressId,
            @RequestHeader("Authorization") String jwt) {
        Optional<ShippingAddress> optionalShippingAddress = shippingAddressRepository.findById(addressId);
        if (optionalShippingAddress.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("message", "Shipping address not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ShippingAddress shippingAddress = optionalShippingAddress.get();

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Shipping address retrieved successfully");
        response.put("shippingAddress", shippingAddress);

        return ResponseEntity.ok(response);
    }
	@PostMapping("/")
	public ResponseEntity<Order> createOrder(@RequestBody String shippingAddress,@RequestHeader("Authorization") String jwt)throws  RazorpayException,UserException{
		
		User user =userService.findUserProfileByJwt(jwt).get();
		System.out.println(user.getId());
		Order order=orderService.createOrder(user, shippingAddress);
 		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	
	
	@PutMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody CheckoutRequest checkoutRequest)throws OrderException {
        String paymentSuccess = checkoutRequest.getPaymentStatus();

        // Update order status and payment details if payment is successful
        if (paymentSuccess.equalsIgnoreCase("success")) {
           Order order= orderService.findOrderById(checkoutRequest.getOrderId());
           
           order.getPaymentDetails().setPaymentId(checkoutRequest.getPaymentId());
           order.getPaymentDetails().setPaymentMethod(checkoutRequest.getPaymentMethod());
           order.getPaymentDetails().setRazorpayPaymentId(checkoutRequest.getRazorpayPaymentId());
           order.getPaymentDetails().setRazorpayPaymentLinkId(checkoutRequest.getRazorpayPaymentLinkId());
           order.getPaymentDetails().setRazorpayPaymentLinkReferenceId(checkoutRequest.getRazorpayPaymentLinkReferenceId());
           order.getPaymentDetails().setRazorpayPaymentLinkStatus(checkoutRequest.getRazorpayPaymentLinkStatus());
           order.getPaymentDetails().setStatus(paymentSuccess);
           order.setOrderStatus("CONFIRMED");
           List<OrderItem> orderitems=order.getOrderItems();
           for(OrderItem o:orderitems) {
        	   
        	   o.setDeliveryDate(checkoutRequest.getDeliveryDate());
        	   orderItemRepository.save(o);
           }
           
           
          Order savedOrder= orderRepository.save(order);
          
          Map<String,Object> response = new HashMap<>();
          response.put("Order", savedOrder);

          response.put("status", true);
          response.put("message", "order checkout successfully");
          
           return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
        } else {
        	Map<String,Object> response = new HashMap<>();
          

            response.put("status", false);
            response.put("message", "order checkout failed");
        	 return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
        }
    }
	
	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> userOrderHistory(@RequestHeader("Authorization") String jwt)throws UserException{
 		User user =userService.findUserProfileByJwt(jwt).get();
		List<Order> order=orderService.usersOrderHistory(user.getId());
		 Map<String,Object> response = new HashMap<>();
         response.put("Order", order);

         response.put("status", true);
         response.put("message", "order history  get successfully");
         return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
 	}
	
	
	@GetMapping("/{Id}")
	public ResponseEntity<Map<String, Object>> findOrderById(@PathVariable("Id") String orderId,
			@RequestHeader("Authorization") String jwt)throws UserException,OrderException{
		
		User user =userService.findUserProfileByJwt(jwt).get();
		
		Order order=orderService.findOrderById(orderId);
		
		Map<String,Object> response = new HashMap<>();
        response.put("Order", order);

        response.put("status", true);
        response.put("message", "order by id  get successfully");
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
		
		
	}
	@DeleteMapping("/{Id}")
	public ResponseEntity<ApiResponse> deleteOrderById(@PathVariable("Id") String orderId,
			@RequestHeader("Authorization") String jwt)throws UserException,OrderException{
		
		User user =userService.findUserProfileByJwt(jwt).get();
		
		 orderService.deleteOrder(orderId);
		
		 ApiResponse res=new ApiResponse();
		 res.setMessage("order deleted successfully");
		 res.setStatus(true);
		 return new ResponseEntity<>(res,HttpStatus.OK);
		
		
	}
	*/
	
	 

}

