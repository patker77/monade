package com.josephnzi.monade.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    @NotEmpty
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",referencedColumnName = "userId")
    private User user;
    private Instant createdDate;
}
