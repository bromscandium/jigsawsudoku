package com.bromscandium.jigsawsudoku.server.jpa;

import com.bromscandium.jigsawsudoku.entity.User;
import com.bromscandium.jigsawsudoku.interfaces.UserInterface;

import jakarta.persistence.EntityManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceJPA implements UserInterface {

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public UserServiceJPA(EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> login(String login, String password) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
            if (user != null && user.getPasswordHash().equals(password)) {
                return Optional.of(user);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> register(String nickname, String login, String password) {
        if (exists(login)) {
            return Optional.empty();
        }
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setLogin(login);
        user.setNickname(nickname);
        user.setPasswordHash(hashedPassword);
        entityManager.persist(user);
        return Optional.of(user);
    }

    @Override
    public boolean exists(String login) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.login = :login", Long.class)
                .setParameter("login", login)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean isValidToken(String token) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.sessionToken = :token", Long.class)
                .setParameter("token", token)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLoginOrNickname(String login, String nickname) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login OR u.nickname = :nickname", User.class)
                    .setParameter("login", login)
                    .setParameter("nickname", nickname)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    @Override
    public void updateNickname(String oldNickname, String newNickname) {
        User user = entityManager.createQuery("SELECT u FROM User u WHERE u.nickname = :nickname", User.class)
                .setParameter("nickname", oldNickname)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (user != null) {
            user.setNickname(newNickname);
            entityManager.merge(user);

            entityManager.createQuery("UPDATE Comment c SET c.player = :newNickname WHERE c.player = :oldNickname")
                    .setParameter("newNickname", newNickname)
                    .setParameter("oldNickname", oldNickname)
                    .executeUpdate();

            entityManager.createQuery("UPDATE Score s SET s.player = :newNickname WHERE s.player = :oldNickname")
                    .setParameter("newNickname", newNickname)
                    .setParameter("oldNickname", oldNickname)
                    .executeUpdate();

            entityManager.createQuery("UPDATE Rating r SET r.player = :newNickname WHERE r.player = :oldNickname")
                    .setParameter("newNickname", newNickname)
                    .setParameter("oldNickname", oldNickname)
                    .executeUpdate();
        }
    }

    @Override
    public Optional<User> findBySessionToken(String token) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.sessionToken = :token", User.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        return findByLogin(login).isPresent();
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return findByNickname(nickname).isPresent();
    }
}
