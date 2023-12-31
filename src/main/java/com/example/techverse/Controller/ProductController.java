package com.example.techverse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.techverse.Model.Product;
import com.example.techverse.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

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
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public Map<String, List<Product>> searchProducts(@RequestParam String keyword) {
    	 List<Product> products= productService.searchProducts(keyword);
        
        Map<String, List<Product>> response = new HashMap<>();
        response.put("products", products);

        return response;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (productService.getProductById(id).isPresent()) {
            product.setId(id);
            return productService.saveProduct(product);
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
    public List<Product> getProductsByPetCategory(@RequestParam String petCategory) {
        return productService.getProductsByPetCategory(petCategory);
    }
}
