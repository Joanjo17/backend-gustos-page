package com.joanlica.gustos_page_backend.like.model;

import com.joanlica.gustos_page_backend.user.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "likes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Like {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String justification;

    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "like_categories",
            joinColumns = @JoinColumn(name = "like_id")
    )
    @Column(name = "category")
    private Set<String> categorySet;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}