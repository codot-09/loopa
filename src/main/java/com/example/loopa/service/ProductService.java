package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.request.ProductCreateRequest;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.entity.Location;
import com.example.loopa.entity.Product;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Category;
import com.example.loopa.entity.enums.DeliveryType;
import com.example.loopa.entity.enums.PriceType;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.LocationRepository;
import com.example.loopa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    public ApiResponse<String> createProduct(User seller,ProductCreateRequest request){

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new DataNotFoundException("Joylashuv topilmadi"));

        Product newProduct = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .priceType(request.getPriceType())
                .deliveryType(request.getDeliveryType())
                .medias(request.getMedias())
                .seller(seller)
                .location(location)
                .build();

        productRepository.save(newProduct);

        return ApiResponse.success("Mahsulot chop etildi");
    }

    public ApiResponse<String> deleteProduct(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Mahsulot topilmadi"));

        product.setDeleted(true);
        productRepository.save(product);

        return ApiResponse.success("Mahsulot o'chirildi");
    }

    public ApiResponse<PageableRes<ProductViewResponse>> searchProducts(
            Category category,
            double minPrice,
            double maxPrice,
            Pageable pageable
    ){
        Page<ProductViewResponse> products = productRepository.search(category,minPrice,maxPrice,pageable)
                .map(this::mapToViewResponse);

        return ApiResponse.success(null, PageableRes.fromPage(products));
    }

    private ProductViewResponse mapToViewResponse(Product product){
        return ProductViewResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .recommendedCount(product.getRecommendedCount())
                .cover(product.getMedias().get(0))
                .build();
    }
}
