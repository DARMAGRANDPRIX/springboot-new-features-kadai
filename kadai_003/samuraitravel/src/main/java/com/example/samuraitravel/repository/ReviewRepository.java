package com.example.samuraitravel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	Page<Review> findByHouseId(Integer houseId, Pageable pageable);
	List<Review> findByHouseIdAndUserId(Integer houseId, Integer userId);
    List<Review> findTop6ByHouseIdOrderByCreatedAtDesc(Integer houseId); /*最新の6件を取得*/
    Optional<Review> findById(Integer reviewId);
}
