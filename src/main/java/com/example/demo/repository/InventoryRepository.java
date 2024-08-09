package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.inventory.TotalInventoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.inventory.Inventory;
import com.example.demo.entity.product.Color;
import com.example.demo.entity.product.Product;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import javax.persistence.LockModeType;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	public Boolean existsByProductAndColor(Product p, Color c);
	
	public List<Inventory> getAllByProductId(Long productId);
	
	public Inventory getOneByProductAndColor(Product p, Color c);

	@Query("SELECT new com.example.demo.dto.inventory.TotalInventoryDto(i.product.id,CAST(SUM(i.quantity_item) AS integer)) " +
			"FROM Inventory i " +
			"WHERE i.product.id = :id "+
			"GROUP BY i.product.id")
	TotalInventoryDto findTotalInventoryByProduct(@Param("id") Long id);
	//SELECT count(*) from tbl_inventory i where i.total_item = 0
	@Query("SELECT count(*) from Inventory i where i.quantity_item = 0")
	public Integer getTotalItemOutOfStock();	// lấy số lượng sản phẩm


	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.color.id = :colorId")
	Optional<Inventory> findByProductIdAndColorIdForUpdate(@Param("productId") Long productId, @Param("colorId") Long colorId);
	
}
