package com.josephnzi.monade.repository;

import com.josephnzi.monade.model.Campground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampgroundRepository extends JpaRepository<Campground,Long> {
}
