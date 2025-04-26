package com.bromscandium.jigsawsudoku.server.jpa;

import com.bromscandium.jigsawsudoku.entity.Save;
import com.bromscandium.jigsawsudoku.entity.User;
import com.bromscandium.jigsawsudoku.exception.SaveException;
import com.bromscandium.jigsawsudoku.exception.UserException;
import com.bromscandium.jigsawsudoku.interfaces.SaveInterface;
import com.bromscandium.jigsawsudoku.interfaces.UserInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service("saveService")
@Transactional
public class SaveServiceJPA implements SaveInterface {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserInterface userInterface;

    public SaveServiceJPA(EntityManager entityManager, UserInterface userInterface) {
        this.entityManager = entityManager;
        this.userInterface = userInterface;
    }

    @Override
    public void saveGame(String nickname, String boardJson, int attempts, long timeSeconds, String difficulty, int size) {
        User user = userInterface.findByNickname(nickname)
                .orElseThrow(() -> new UserException("User not found: " + nickname));

        Save save = entityManager.createQuery("SELECT s FROM Save s WHERE s.user = :user", Save.class)
                .setParameter("user", user)
                .getResultStream()
                .findFirst()
                .orElse(new Save());

        save.setUser(user);
        save.setBoardJson(boardJson);
        save.setAttempts(attempts);
        save.setTimeSeconds(timeSeconds);
        save.setDifficulty(difficulty);
        save.setSize(size);

        try {
            if (save.getId() == null) {
                entityManager.persist(save);
            } else {
                entityManager.merge(save);
            }
        } catch (Exception e) {
            throw new SaveException("Error of saving game: " + e.getMessage(), e);
        }
    }

    @Override
    public Save loadGame(String nickname) {
        User user = userInterface.findByNickname(nickname)
                .orElseThrow(() -> new UserException("User not found: " + nickname));

        return entityManager.createQuery("SELECT s FROM Save s WHERE s.user = :user", Save.class)
                .setParameter("user", user)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new SaveException("Save not found: " + nickname, "SAVE_NOT_FOUND"));
    }

    @Override
    public void deleteGame(String nickname) {
        User user = userInterface.findByNickname(nickname)
                .orElseThrow(() -> new UserException("User not found: " + nickname));

        Save save = entityManager.createQuery("SELECT s FROM Save s WHERE s.user = :u", Save.class)
                .setParameter("u", user)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new SaveException("Save not found: " + nickname));

        entityManager.remove(save);
    }
}
