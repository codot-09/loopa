package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.PageableRes;
import com.example.loopa.dto.request.ProductCreateRequest;
import com.example.loopa.dto.response.LocationResponse;
import com.example.loopa.dto.response.ProductResponse;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.entity.Location;
import com.example.loopa.entity.Product;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Category;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.LocationRepository;
import com.example.loopa.repository.ProductRepository;
import com.example.loopa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final LocationService locationService;

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

    @Transactional(readOnly = true)
    public ApiResponse<ProductResponse> getById(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Mahsulot topilmadi"));

        return ApiResponse.success(null,mapToResponse(product));
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<ProductViewResponse>> getSellerProducts(User seller){
        List<Product> products = productRepository.findBySeller(seller);

        return ApiResponse.success(null,products.stream().map(this::mapToViewResponse).toList());
    }

    public ApiResponse<String> deleteProduct(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Mahsulot topilmadi"));

        product.setDeleted(true);
        productRepository.save(product);

        return ApiResponse.success("Mahsulot o'chirildi");
    }

    @Transactional(readOnly = true)
    public ApiResponse<PageableRes<ProductViewResponse>> searchProducts(
            Category category,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    ) {
        double min = minPrice != null ? minPrice : 0.0;
        double max = maxPrice != null ? maxPrice : Double.MAX_VALUE;

        Page<ProductViewResponse> products = productRepository
                .search(category, min, max, pageable)
                .map(this::mapToViewResponse);

        return ApiResponse.success(null, PageableRes.fromPage(products));
    }

    public ApiResponse<PageableRes<ProductViewResponse>> getForUser(User user, Pageable pageable) {

        List<Category> userInterests = userRepository.findFavouriteCategories(user.getChatId());

        Page<ProductViewResponse> productPage;

        if (userInterests.isEmpty()) {
            productPage = productRepository
                    .findAllByDeletedFalse(pageable)
                    .map(this::mapToViewResponse);
        } else {
            productPage = productRepository
                    .findAllByCategoryInAndDeletedFalseOrderByCreatedAtDesc(userInterests, pageable)
                    .map(this::mapToViewResponse);
        }

        return ApiResponse.success(null, PageableRes.fromPage(productPage));
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

    private ProductResponse mapToResponse(Product product){
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .priceType(product.getPriceType())
                .deliveryType(product.getDeliveryType())
                .category(product.getCategory())
                .locationResponse(locationService.mapToResponse(product.getLocation()))
                .sellerContact(product.getSeller().getUsername())
                .medias(product.getMedias())
                .createdAt(product.getCreatedAt())
                .recommendedPrecent(calculateRecommendedPercent(product.getTotalVotes(),product.getRecommendedCount()))
                .build();
    }

    public static double calculateRecommendedPercent(long totalVotes, long recommendedVotes) {
        if (totalVotes <= 0 || recommendedVotes <= 0) {
            return 0.0;
        }
        return Math.round(((double) recommendedVotes / totalVotes) * 10000.0) / 100.0;
    }
}
