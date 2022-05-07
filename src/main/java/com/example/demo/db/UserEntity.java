package com.example.demo.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_to_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<PermissionEntity> permissions;

    @ManyToMany
    @JoinTable(
            name = "user_to_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "isbn_id"),
            uniqueConstraints = @UniqueConstraint(name = "UniqueNumberAndAddress", columnNames = { "user_id", "isbn_id" })
    )
    private Set<BookEntity> favourites;

}
