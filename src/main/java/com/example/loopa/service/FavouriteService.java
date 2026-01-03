package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.response.ProductViewResponse;
import com.example.loopa.entity.Favourites;
import com.example.loopa.entity.Product;
import com.example.loopa.entity.User;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.FavouritesRepository;
import com.example.loopa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteService {

    private final FavouritesRepository favouritesRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ApiResponse<String> addFavourites(User user, UUID productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Mahsulot topilmadi"));

        if (favouritesRepository.existsByUserAndProduct(user,product)){
            return ApiResponse.fail("Mahsulot allaqachon sevimlilarda bor");
        }

        Favourites favourites = Favourites.builder()
                .user(user)
                .product(product)
                .build();

        favouritesRepository.save(favourites);

        return ApiResponse.success("Mahsulot sevimlilarga qo'shildi");
    }

    public boolean isProductFavourite(User user, Product product) {
        return favouritesRepository.existsByUserAndProduct(user, product);
    }

    public ApiResponse<List<ProductViewResponse>> getFavourites(User user) {
        List<Favourites> favourites = favouritesRepository.findByUser(user);

        List<ProductViewResponse> response = favourites.stream()
                .map(f -> productService.mapToViewResponse(f.getProduct()))
                .toList();

        return ApiResponse.success(null,response);
    }

    public ApiResponse<String> deleteFavourites(UUID id){
        Favourites favourites = favouritesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Sevimli topilmadi"));

        favouritesRepository.delete(favourites);

        return ApiResponse.success("Sevimlilardan o'chirildi");
    }
}
