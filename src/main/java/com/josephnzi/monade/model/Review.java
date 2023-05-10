package com.josephnzi.monade.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    @NotBlank(message = "description must not be empty!")
    @Lob
    private String description;
    @NotNull
    private Integer rating;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant createdDate;
}
