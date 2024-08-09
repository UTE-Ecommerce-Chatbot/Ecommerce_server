package com.example.demo.rest;

import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.entity.user.ViewedProduct;
import com.example.demo.repository.ViewedProductRepository;
import com.example.demo.service.ViewedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SearchDto;
import com.example.demo.dto.other.RecommendProduct;
import com.example.demo.dto.product.ProductListDto;
import com.example.demo.entity.product.Product;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.impl.ContentBasedService;
import com.example.demo.service.impl.RecommendService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/recommend")
public class RecommendController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepos;

    @Autowired
    private OrderDetailRepository orderDetailRepos;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private ViewedProductService viewedProductService;

    @Autowired
    private ViewedProductRepository viewedProductRepository;

    @GetMapping(value = "/")
    public ResponseEntity<List<Long>> test() {
        List<Long> result = recommendService.getAllProductIdUserLikeAndRating("huonghuongnewton1@gmail.com");
        return new ResponseEntity<List<Long>>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/similar/{category}")
    public ResponseEntity<List<ProductListDto>> getSimilarListProduct(@RequestBody String[] tags,
                                                                      @PathVariable String category) {

        List<List<String>> documents = new ArrayList<List<String>>();
        int page = 1, limit = 100;
        String keyword = "";
        SearchDto dto = new SearchDto(page, limit, keyword, category, null);
        dto.setSortBy("createdDate");
        dto.setSortValue("DESC");
        dto.setBrand("");
        dto.setPrice("");
        Page<ProductListDto> dtos = productService.productList(dto);
        System.out.println("tags :" + tags.length);
        if (tags == null || tags.length == 0 ) {

            List<ProductListDto> defaultProducts = new ArrayList<>(dtos.toList());
            return new ResponseEntity<List<ProductListDto>>(defaultProducts, HttpStatus.OK);
        }

        for (ProductListDto item : dtos.toList()) {
            List<String> listTags = Optional.ofNullable(item.getFeatures())
                    .filter(features -> !features.isEmpty())
                    .map(features -> Arrays.stream(features.split(","))
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            documents.add(listTags);
        }

//		List<List<String>> documents1 = new ArrayList<List<String>>();
//		List<String> doc1 = Arrays.asList("người", "lên", "ngựa", "kẻ", "chia", "bào", "rừng", "phong", "thu", "đã",
//				"nhuốm", "màu", "quan", "san");
//		List<String> doc2 = Arrays.asList("ô", "hay", "buồn", "vương", "cây", "ngô", "đồng", "vàng", "rơi", "vàng",
//				"rơi", "thu", "mênh", "mông");
//		List<String> doc3 = Arrays.asList("một", "chiều", "về", "bên", "bến", "sông", "thu", "nghe", "tin", "em",
//				"cưới", "á", "cái", "đù");
//		List<List<String>> documents1 = Arrays.asList(doc1, doc2, doc3);
//		System.out.println(documents1);
        //		String[] tagSplit = tags.split(",");

        List<String> tagList = new ArrayList<String>(Arrays.asList(tags));
        List<RecommendProduct> list = ContentBasedService.similarByTags(tagList, documents, false);

        List<ProductListDto> result = new ArrayList<ProductListDto>();
        if (list.isEmpty()){
            result = new ArrayList<>(dtos.toList());

        }
        for (RecommendProduct recommendProduct : list) {
            Product p = productRepos.getById(dtos.toList().get(recommendProduct.getIndex()).getId());
            ProductListDto pDto = new ProductListDto(p);
            result.add(pDto);
        }
        Integer seller_count = 0;
        for (ProductListDto item : result) {
            if (orderDetailRepos.countAllByProductId(item.getId()) != null) {
                seller_count += orderDetailRepos.countAllByProductId(item.getId());
            } else {
                seller_count = 0;
            }
            item.setSeller_count(seller_count);
            seller_count = 0;
        }
        return new ResponseEntity<List<ProductListDto>>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/view/{id}")
    public ResponseEntity<?>viewRecent(@PathVariable Long id){
        List<ProductListDto> recentViewedProducts = viewedProductService.getListRecentViewed(id);
        if (recentViewedProducts.isEmpty()) {
            return new ResponseEntity<>("No recent viewed products", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recentViewedProducts, HttpStatus.OK);
        }
    }


    @PostMapping("/similar/recent")
    public ResponseEntity<List<ProductListDto>> getSimilarListToRecentProduct() {
        Pageable pageable = PageRequest.of(0, 1); // Lấy sản phẩm gần đây nhất
        List<ViewedProduct> recentViewedProducts = viewedProductRepository.findAllByOrderByCreatedDateDesc(pageable);
        List<ProductListDto> similarProducts;
        if (recentViewedProducts.isEmpty()) {
            similarProducts =  new ArrayList<>();
        }

        Product recentProduct = recentViewedProducts.get(0).getProduct();
        String[] tags = recentProduct.getFeatures().split(",");

        List<List<String>> documents = new ArrayList<>();
        int page = 1, limit = 100;
        String keyword = "";
        SearchDto dto = new SearchDto(page, limit, keyword, null, null);
        dto.setSortBy("createdDate");
        dto.setSortValue("DESC");
        Page<ProductListDto> dtos = productService.productList(dto);
        if (tags == null || tags.length == 0 ) {

            List<ProductListDto> defaultProducts = new ArrayList<>(dtos.toList());
            return new ResponseEntity<List<ProductListDto>>(defaultProducts, HttpStatus.OK);
        }
        try{      for (ProductListDto item : dtos.toList()) {
            if (item.getFeatures() != null && !item.getFeatures().isEmpty()) {
                String[] t = item.getFeatures().split(",");
                List<String> listTags = new ArrayList<>(Arrays.asList(t));
                documents.add(listTags);
            }
        }}catch(Exception e){System.out.println(e.getMessage());}

        List<String> tagList = new ArrayList<>(Arrays.asList(tags));
        List<RecommendProduct> list = ContentBasedService.similarByTags(tagList, documents, false);

        List<ProductListDto> result = new ArrayList<>();
        if (list.isEmpty()) {
            result = new ArrayList<>(dtos.toList());
        } else {
            for (RecommendProduct recommendProduct : list) {
                Product p = productRepos.getById(dtos.toList().get(recommendProduct.getIndex()).getId());
                ProductListDto pDto = new ProductListDto(p);
                result.add(pDto);
            }
        }

        Integer seller_count = 0;
        for (ProductListDto item : result) {
            if (orderDetailRepos.countAllByProductId(item.getId()) != null) {
                seller_count += orderDetailRepos.countAllByProductId(item.getId());
            } else {
                seller_count = 0;
            }
            item.setSeller_count(seller_count);
            seller_count = 0;
        }
        similarProducts = result.stream().limit(12).collect(Collectors.toList());
        return new ResponseEntity<>(similarProducts, HttpStatus.OK);
    }

    @PostMapping(value = "/list")
    public ResponseEntity<?> recommendList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
		System.out.println(username);
        if (Objects.equals(username, "anonymousUser")) {
            return new ResponseEntity<>("No recent viewed products", HttpStatus.OK);
        } else {
            List<List<String>> documents = new ArrayList<List<String>>();
            int page = 1, limit = 100;
            String keyword = "";
            SearchDto dto = new SearchDto(page, limit, keyword, null, null);
            dto.setSortBy("createdDate");
            dto.setSortValue("DESC");
            dto.setBrand("");
            dto.setPrice("");
            Page<ProductListDto> dtos = productService.productList(dto);

            List<Long> list_id_user_comment_and_like = recommendService.getAllProductIdUserLikeAndRating(username);
            if (!list_id_user_comment_and_like.isEmpty()) {
                List<ProductListDto> list_product_rating_and_like = new ArrayList<ProductListDto>();
                for (Long i : list_id_user_comment_and_like) {
                    Product p = productRepos.getById(i);
                    list_product_rating_and_like.add(new ProductListDto(p));
                }

                List<List<String>> tags = new ArrayList<List<String>>();
                for (ProductListDto item : list_product_rating_and_like) {
                    List<String> listTags = new ArrayList<String>();
//					for (TagDto tag : item.getTags()) {
//						listTags.add(tag.getCode());
//					}
                    System.out.println("Feature:" + item.getFeatures());
                    if (Objects.nonNull(item.getFeatures())) {
                        String t[] = item.getFeatures().split(",");
                        for (int i = 0; i < t.length; i++) {
                            listTags.add(t[i]);
                        }
                        tags.add(listTags);
                    }
                }

                for (ProductListDto item : dtos.toList()) {
                    List<String> listTags = new ArrayList<String>();
//					for (TagDto tag : item.getTags()) {
//						listTags.add(tag.getCode());
//					}
                    if (Objects.nonNull(item.getFeatures())) {
                        String t[] = item.getFeatures().split(",");
                        for (int i = 0; i < t.length; i++) {
                            listTags.add(t[i]);
                        }
                        documents.add(listTags);
                    }
                }

                List<RecommendProduct> list = new ArrayList<>();
                int tagsSize = tags.size();
                for (int i = 0; i < tagsSize; i++) {
                    int similarProductSize = ContentBasedService.similarByTags(tags.get(i), documents, true).size();
                    for (int j = 0; j < similarProductSize; j++) {
                        list.add(ContentBasedService.similarByTags(tags.get(i), documents, true).get(j));
                    }
                }
                list.sort((RecommendProduct o1, RecommendProduct o2) -> o2.getValue() - o1.getValue() > 0 ? 1 : -1);
                List<Integer> ids = new ArrayList<>();
                for (RecommendProduct item : list) {
                    ids.add(item.getIndex());
                }
                ids = ids.stream().distinct().collect(Collectors.toList());
                List<ProductListDto> result = new ArrayList<ProductListDto>();
                for (int i = 0; i < 5; i++) {
                    Product p = productRepos.getById(dtos.toList().get(ids.get(i)).getId());
                    ProductListDto pDto = new ProductListDto(p);
                    result.add(pDto);
                }
                Integer seller_count = 0;
                for (ProductListDto item : result) {
                    if (orderDetailRepos.countAllByProductId(item.getId()) != null) {
                        seller_count += orderDetailRepos.countAllByProductId(item.getId());
                    } else {
                        seller_count = 0;
                    }
                    item.setSeller_count(seller_count);
                    seller_count = 0;
                }
                return new ResponseEntity<List<ProductListDto>>(result, HttpStatus.OK);
            } else {
                return getSimilarListToRecentProduct();
            }
        }
    }

}
