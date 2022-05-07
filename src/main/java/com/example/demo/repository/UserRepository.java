package com.example.demo.repository;

import com.example.demo.db.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT user FROM UserEntity user "
            + "LEFT JOIN FETCH user.permissions "
            + "WHERE user.login = :login")
    Optional<UserEntity> findByLogin(@Param("login") String login);

    @Query("SELECT user FROM UserEntity user "
            + "LEFT JOIN FETCH user.favourites "
            + "WHERE user.login = :login")
    Optional<UserEntity> findFavouritesByLogin(@Param("login") String login);

    boolean existsByLogin(String login);

}
