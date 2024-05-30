package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.techverse.Model.Product;
import com.example.techverse.service.ProductService;
import com.example.techverse.service.StorageService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    
    @Autowired
    private  StorageService storageService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Map<String, List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        Map<String, List<Product>> response = new HashMap<>();
        response.put("products", products);

        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
    	  Map<String, Object> response = new HashMap<>();
          response.put("product", productService.getProductById(id));

          return response;
          
    }

    @GetMapping("/search")
    public Map<String, List<Product>> searchProducts(@RequestParam String keyword) {
    	 List<Product> products= productService.searchProducts(keyword);
        
        Map<String, List<Product>> response = new HashMap<>();
        response.put("products", products);

        return response;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,Object> > addProduct(
            @RequestPart("photo") MultipartFile photo,
            @RequestPart("name") String name,
            @RequestPart("price") String price,
            @RequestPart("description") String description,
            @RequestPart("isVeg") String isVeg,
            @RequestPart("petCategory") String petCategory,
            @RequestPart("productCategory") String productCategory,
            @RequestPart("manufacturingCompany") String manufacturingCompany,
            @RequestPart("availableOffers") String combineOffers
             
            
    ) {
    	
   	 Map<String, Object> response = new HashMap<>();
        // Construct the Product object and save it using ProductService
        Product product = new Product();
        List<String> availableOffers = Arrays.asList(combineOffers.split(","));

        if(photo!=null|| !photo.isEmpty()) {
        	 product.setPhotoUrl(storageService.uploadFileOnAzure(photo)); // Custom method to upload and get URL
        }
       
        product.setProductName(name);
        product.setPrice(Double.parseDouble(price));
        product.setDescription(description);
        product.setVeg( Boolean.parseBoolean(isVeg));
        product.setPetCategory(petCategory);
        product.setProductCategory(productCategory);
        product.setManufacturingCompany(manufacturingCompany);
        product.setAvailableOffers(availableOffers);
        // Add reviews and ratings handling as needed

       Product p1= productService.saveProduct(product);
        
        response.put("message","Product added successfully");
        response.put("product", p1);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public Map<String, Product>  updateProduct(@PathVariable Long id, @RequestBody Product product) {
    	 Map<String, Product> response = new HashMap<>();
         
    	
        if (productService.getProductById(id).isPresent()) {
            product.setId(id);
          Product p= productService.saveProduct(product);
          response.put("products", p);
          return response;
        } else {
            // Handle error, product not found
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    
    @GetMapping("/byPetCategory")
    public Map<String, List<Product>> getProductsByPetCategory(@RequestParam String petCategory) {
    	List<Product> products=  productService.getProductsByPetCategory(petCategory);
        
        Map<String, List<Product>> response = new HashMap<>();
        response.put("products", products);

        return response;
    }
}
