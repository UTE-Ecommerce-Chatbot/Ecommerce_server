package com.example.demo.repository;

import java.util.List;

import com.example.demo.dto.product.ProductDto;
import com.example.demo.dto.product.ProductListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.product.Product;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.ViewedProduct;

@Repository
public interface ViewedProductRepository extends JpaRepository<ViewedProduct, Long> {
	public List<ViewedProduct> getAllByUser(User user, Sort sort);

	public Boolean deleteByUserAndProduct(User user, Product product);

	public ViewedProduct getOneByUserAndProduct(User user, Product product);

	@Query("SELECT new com.example.demo.dto.product.ProductListDto(p) " +
			"FROM ViewedProduct pv JOIN pv.product p " +
			"WHERE p.id <> :currentProductId " +
			"GROUP BY p.id " +
			"ORDER BY MAX(pv.createdDate) DESC")
	List<ProductListDto> findTop10DistinctProductsByRecentViews(@Param("currentProductId") Long currentProductId, Pageable pageable);
	public List<ViewedProduct> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
