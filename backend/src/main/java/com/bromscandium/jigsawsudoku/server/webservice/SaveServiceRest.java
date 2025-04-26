package com.bromscandium.jigsawsudoku.server.webservice;

import com.bromscandium.jigsawsudoku.server.dto.SaveDTO;
import com.bromscandium.jigsawsudoku.server.requests.SaveRequest;
import com.bromscandium.jigsawsudoku.server.dto.UserDTO;
import com.bromscandium.jigsawsudoku.entity.Save;
import com.bromscandium.jigsawsudoku.interfaces.SaveInterface;
import com.bromscandium.jigsawsudoku.exception.SaveException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/save")
public class SaveServiceRest {
    @Autowired
    private SaveInterface saveInterface;

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody SaveRequest request) {
        try {
            saveInterface.saveGame(
                    request.getLogin(),
                    request.getBoardJson(),
                    request.getAttempts(),
                    request.getTimeSeconds(),
                    request.getDifficulty(),
                    request.getSize()
            );
            return ResponseEntity.ok(Map.of("message", "Game saved successfully!"));
        } catch (SaveException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/load/{nickname}")
    public ResponseEntity<?> load(@PathVariable String nickname) {
        try {
            Save save = saveInterface.loadGame(nickname);
            SaveDTO saveDTO = new SaveDTO(
                    save.getId(),
                    new UserDTO(save.getUser().getId(), save.getUser().getLogin()),
                    save.getBoardJson(),
                    save.getAttempts(),
                    save.getTimeSeconds(),
                    save.getDifficulty(),
                    save.getSize()
            );
            return ResponseEntity.ok(saveDTO);
        } catch (SaveException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{nickname}")
    public ResponseEntity<?> delete(@PathVariable String nickname) {
        try {
            saveInterface.deleteGame(nickname);
            return ResponseEntity.ok(Map.of("message", "Save was deleted successfully!"));
        } catch (SaveException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
