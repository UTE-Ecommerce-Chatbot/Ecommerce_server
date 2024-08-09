package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.entity.product.Brand;
import com.example.demo.entity.product.Product;
import javax.persistence.LockModeType;


import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	public Page<Product> findAllByBrand(Brand brand, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    <T> Optional<T> findByIdForUpdate(@Param("id") Long productId);
}
