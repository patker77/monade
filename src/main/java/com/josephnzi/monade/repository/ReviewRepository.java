package com.josephnzi.monade.repository;

import com.josephnzi.monade.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
}
