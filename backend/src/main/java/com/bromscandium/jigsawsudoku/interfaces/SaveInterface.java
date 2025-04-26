package com.bromscandium.jigsawsudoku.interfaces;

import com.bromscandium.jigsawsudoku.entity.Save;

public interface SaveInterface {
    void saveGame(String nickname, String boardJson, int attempts, long timeSeconds, String difficulty, int size);

    Save loadGame(String nickname);

    void deleteGame(String nickname);
}
