package com.joanlica.gustos_page_backend.like.service;

import com.joanlica.gustos_page_backend.like.dto.LikeCreateDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeEditDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeListResponseDTO;

public interface LikeService {

    LikeListResponseDTO getLikesByUsername(String username);

    LikeDTO addLike(String username, LikeCreateDTO newLike);

    LikeDTO editLike(Long idLike, LikeEditDTO newLike, String username);
    void deleteLike(Long likeId, String username);
}