package com.example.techverse.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.techverse.Model.Product;
import com.example.techverse.Model.User;
import com.example.techverse.Repository.ProductRepository;
import com.example.techverse.exception.ProductException;
 

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public Product findProductById(Long id) throws  ProductException {
  		// TODO Auto-generated method stub
  		Optional<Product> product=productRepository.findById(id);
  		if(product.isPresent())
  		{
  			return product.get();
  		}
  		throw new ProductException("Product Not Found with id "+id);
  	}

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> getProductsByPetCategory(String petCategory) {
        return productRepository.findByPetCategoryIgnoreCase(petCategory);
    }
    public List<Product> getProductsByProductCategory(String productCategory) {
        return productRepository.findByProductCategoryIgnoreCase(productCategory);
    }
    
    public List<Product> setfavouriteStatus(User user,List<Product> products)
    {
    	 
    	Set<Long> favoriteProductIds = user.getFavoriteProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    	products.forEach(product -> product.setFavorite(favoriteProductIds.contains(product.getId())));
    	return products;

    }
    
    
}

