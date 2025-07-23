package com.example.serviceImp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;

@Service
public class ServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Product createProduct(Product product) {
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        return productRepository.save(product);
    }

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		 return productRepository.findAll();
	}

	@Override
	public Product getProductById(String id) {
		// TODO Auto-generated method stub
		  return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
	

	@Override
	public Product updateProduct(String id, Product product) {
		// TODO Auto-generated method stub
		 Product existingProduct = getProductById(id);
		 
	        existingProduct.setName(product.getName());
	        existingProduct.setDescription(product.getDescription());
	        existingProduct.setPrice(product.getPrice());
	        existingProduct.setBrand(product.getBrand());
	        existingProduct.setCategory(product.getCategory());
	        existingProduct.setStock(product.getStock());
	        existingProduct.setRatings(product.getRatings());

	        return productRepository.save(existingProduct);
	}

	@Override
	public void deleteProduct(String id) {
		// TODO Auto-generated method stub
		 Product existingProduct = getProductById(id);
	        productRepository.delete(existingProduct);
	}

	 public Page<Product> getAllProducts(int page, int size, String category, String brand, String search, String sort) {
	        Query query = new Query();

	        // ✅ Filtering
	        if (category != null && !category.isEmpty()) {
	            query.addCriteria(Criteria.where("category").is(category));
	        }
	        if (brand != null && !brand.isEmpty()) {
	            query.addCriteria(Criteria.where("brand").is(brand));
	        }

	        // ✅ Search by keyword
	        if (search != null && !search.isEmpty()) {
	            query.addCriteria(new Criteria().orOperator(
	                    Criteria.where("name").regex(search, "i"),
	                    Criteria.where("description").regex(search, "i")
	            ));
	        }

	        // ✅ Sorting
	        Sort sorting = Sort.by("createdAt").descending(); // default
	        if (sort != null) {
	            switch (sort.toLowerCase()) {
	                case "price_asc":
	                    sorting = Sort.by("price").ascending();
	                    break;
	                case "price_desc":
	                    sorting = Sort.by("price").descending();
	                    break;
	                case "ratings_asc":
	                    sorting = Sort.by("ratings").ascending();
	                    break;
	                case "ratings_desc":
	                    sorting = Sort.by("ratings").descending();
	                    break;
	            }
	        }

	        // ✅ Apply pagination & sorting
	        Pageable pageable = PageRequest.of(page, size, sorting);
	        query.with(pageable);

	        // ✅ Execute query
	        List<Product> products = mongoTemplate.find(query, Product.class);
	        long total = mongoTemplate.count(query.skip(-1).limit(-1), Product.class);

	        return new PageImpl<>(products, pageable, total);
}
}
