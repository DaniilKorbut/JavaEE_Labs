package com.example.demo.db;

import com.example.demo.config.MyPasswordEncoder;
import com.example.demo.repository.UserRepository;
import com.example.demo.type.UserAlreadyExistAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity registerUser(UserEntity userEntity) {
        if (userRepository.existsByLogin(userEntity.getLogin())) {
            throw new UserAlreadyExistAuthenticationException("Username already exists");
        }
        userEntity.setPermissions(new ArrayList<>());
        userEntity.setFavourites(new HashSet<>());
        return userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    public Optional<UserEntity> getUserByUsername(String login) {
        return userRepository.findFavouritesByLogin(login);
    }

    @Transactional
    public UserEntity addToFavourites(UserEntity userEntity, BookEntity bookEntity) {
        Set<BookEntity> books = userEntity.getFavourites();
        books.add(bookEntity);
        userEntity.setFavourites(books);
        return userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    public UserEntity removeFromFavourites(UserEntity userEntity, BookEntity bookEntity) {
        Set<BookEntity> books = userEntity.getFavourites();
        books.remove(bookEntity);
        userEntity.setFavourites(books);
        return userRepository.saveAndFlush(userEntity);
    }


    public Set<BookEntity> getFavourites(String username) {
        Optional<UserEntity> oUser = userRepository.findFavouritesByLogin(username);
        if (oUser.isPresent()) {
            UserEntity user = oUser.get();
            return user.getFavourites();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional
    public boolean isUserFavourite(UserEntity user, BookEntity book) {
        Set<BookEntity> books = user.getFavourites();
        return books.contains(book);
    }
}
