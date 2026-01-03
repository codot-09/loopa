package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.request.LocationRequest;
import com.example.loopa.dto.response.LocationResponse;
import com.example.loopa.entity.Location;
import com.example.loopa.entity.User;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public ApiResponse<String> addLocation(User seller, LocationRequest request){

        Location location = Location.builder()
                .latitude(request.getLat())
                .longitude(request.getLng())
                .region(request.getRegion())
                .seller(seller)
                .build();

        locationRepository.save(location);

        return ApiResponse.success("Joylashuv qo'shildi");
    }

    public ApiResponse<List<LocationResponse>> getOwnLocations(User user){
        List<Location> locations = locationRepository.findBySeller(user);

        return ApiResponse.success(null,locations.stream().map(this::mapToResponse).toList());
    }

    public ApiResponse<String> deleteLocation(UUID id){
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Joylashuv topilmadi"));

        locationRepository.delete(location);

        return ApiResponse.success("Joylashuv malumotlari o'chirildi");
    }

    public LocationResponse mapToResponse(Location location){
        return LocationResponse.builder()
                .id(location.getId())
                .lat(location.getLatitude())
                .lng(location.getLongitude())
                .region(location.getRegion())
                .build();
    }
}
