package com.example.loopa.repository;

import com.example.loopa.entity.Favourites;
import com.example.loopa.entity.Product;
import com.example.loopa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavouritesRepository extends JpaRepository<Favourites, UUID> {

    boolean existsByUserAndProduct(User user, Product product);
    List<Favourites> findByUser(User user);
}
