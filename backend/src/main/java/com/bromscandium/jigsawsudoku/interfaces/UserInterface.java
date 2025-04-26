package com.bromscandium.jigsawsudoku.interfaces;

import com.bromscandium.jigsawsudoku.entity.User;

import java.util.Optional;

public interface UserInterface {
    Optional<User> login(String login, String password);

    Optional<User> register(String nickname, String login, String password);

    boolean exists(String login);

    boolean isValidToken(String token);

    Optional<User> findByLogin(String login);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByLoginOrNickname(String login, String nickname);

    Optional<User> findBySessionToken(String token);

    void save(User user);

    void updateNickname(String oldNickname, String newNickname);

    boolean existsByLogin(String login);

    boolean existsByNickname(String nickname);
}
