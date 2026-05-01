package com.joanlica.gustos_page_backend.like.service.implementation;

import com.joanlica.gustos_page_backend.core.exception.LikeNotFoundException;
import com.joanlica.gustos_page_backend.core.exception.UserNotOwnerException;
import com.joanlica.gustos_page_backend.like.dto.LikeCreateDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeEditDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeListResponseDTO;
import com.joanlica.gustos_page_backend.like.model.Like;
import com.joanlica.gustos_page_backend.like.repository.LikeRepository;
import com.joanlica.gustos_page_backend.like.service.LikeService;
import com.joanlica.gustos_page_backend.user.mapper.UsuarioMapper;
import com.joanlica.gustos_page_backend.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public LikeListResponseDTO getLikesByUsername(String username){
        var user = userService.buscarUsuarioPorUsername(username);

        List<LikeDTO> likeDTOList = user.getLikesList()
                .stream()
                .map(this::mapToDTO)
                .toList();

        return new LikeListResponseDTO(UsuarioMapper.toDto(user), likeDTOList);
    }

    @Override
    public LikeDTO addLike(String username, LikeCreateDTO newLike){
        Like likeToSave = new Like();
        updateLikeFields(likeToSave, newLike.name(), newLike.description(), newLike.categories(), newLike.imageUrl());

        likeToSave.setUsuario(userService.buscarUsuarioPorUsername(username));
        return mapToDTO(likeRepository.save(likeToSave));
    }

    @Override
    public LikeDTO editLike(Long likeId, LikeEditDTO newLike, String username){
        Like oldLike = likeRepository.findById(likeId)
                .orElseThrow(() -> new LikeNotFoundException("Like no encontrado"));

        if(!oldLike.getUsuario().getUser().getUsername().equals(username)){
            throw new UserNotOwnerException("No tienes permiso para editar este contenido");
        }

        updateLikeFields(oldLike, newLike.name(), newLike.description(), newLike.categories(), newLike.imageUrl());
        return mapToDTO(likeRepository.save(oldLike));
    }

    @Override
    public void deleteLike(Long likeId, String username) {
        Like oldLike = likeRepository.findById(likeId)
                .orElseThrow(() -> new LikeNotFoundException("Like no encontrado"));

        if(!oldLike.getUsuario().getUser().getUsername().equals(username)){
            throw new UserNotOwnerException("No tienes permiso para eliminar este contenido");
        }

        likeRepository.delete(oldLike);
    }

    // Métodos privados para evitar repetición
    private void updateLikeFields(Like entity, String name, String desc, Set<String> cats, String url) {
        entity.setName(name);
        entity.setJustification(desc);
        entity.setCategorySet(cats);
        entity.setImageUrl((url != null && !url.isBlank()) ? url : null);
    }

    private LikeDTO mapToDTO(Like like) {
        return new LikeDTO(
                like.getId(),
                like.getName(),
                like.getJustification(),
                like.getImageUrl(),
                like.getCategorySet()
        );
    }
}