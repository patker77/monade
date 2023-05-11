package com.josephnzi.monade.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Campground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long campgroundId;
    @NotBlank(message = "Campground name must not be empty!")
    private String campsName;
    @Nullable
    @Lob
    private String description;
    @NotBlank(message = "The price must be given!")
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",referencedColumnName = "userId")
    private User user;
    private Instant createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId",referencedColumnName = "reviewId")
    private Review review;
}
