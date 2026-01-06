package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.BannerDTO;
import com.example.loopa.entity.Banner;
import com.example.loopa.entity.User;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public ApiResponse<String> addBanner(User user, BannerDTO request){

        if (bannerRepository.existsBySeller(user)){
            return ApiResponse.fail("Siz banner joylagansiz");
        }

        Banner banner = Banner.builder()
                .seller(user)
                .coverImage(request.getCoverImage())
                .build();

        bannerRepository.save(banner);

        return ApiResponse.success("Banner yaratildi");
    }

    public ApiResponse<List<BannerDTO>> getBanners(){
        List<BannerDTO> banners = bannerRepository.findAll().stream().map(this::mapToDto).toList();
        return ApiResponse.success(null,banners);
    }

    public ApiResponse<BannerDTO> getOwnBanner(User user){

        Banner banner = bannerRepository.findBySeller(user)
                .orElseThrow(() -> new DataNotFoundException("Banner topilmadi"));

        return ApiResponse.success(null,mapToDto(banner));
    }

    public ApiResponse<String> deleteBanner(User seller){
        Banner banner = bannerRepository.findBySeller(seller)
                .orElseThrow(() -> new DataNotFoundException("Banner topilmadi"));

        bannerRepository.delete(banner);
        return ApiResponse.success("Banner o'chirildi");
    }

    private BannerDTO mapToDto(Banner banner){
        return BannerDTO.builder()
                .id(banner.getId())
                .coverImage(banner.getCoverImage())
                .build();
    }
}
