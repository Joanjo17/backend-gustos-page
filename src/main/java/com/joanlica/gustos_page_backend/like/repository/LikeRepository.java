package com.joanlica.gustos_page_backend.like.repository;

import com.joanlica.gustos_page_backend.like.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

}