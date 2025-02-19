package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.user.ViewedProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.example.demo.dto.product.ProductListDto;
import com.example.demo.dto.user.ViewedProductDto;

@Service
public interface ViewedProductService  {

	public List<ProductListDto> getListByUser(String username);

	public ViewedProductDto saveOrUpdate(ViewedProductDto dto);

	public List<ProductListDto> getListMostPopular();

	public List<ProductListDto> getListRecentViewed(Long id);

}
