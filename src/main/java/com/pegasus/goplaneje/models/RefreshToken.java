package com.pegasus.goplaneje.models;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;
import com.pegasus.goplaneje.models.User;


@Entity
@Table(name = "RefreshToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(generator = 'UUID')
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;
}
