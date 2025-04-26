package com.bromscandium.jigsawsudoku.server.webservice;

import com.bromscandium.jigsawsudoku.server.dto.BoardDTO;
import com.bromscandium.jigsawsudoku.server.dto.CellDTO;
import com.bromscandium.jigsawsudoku.enums.Difficulty;
import com.bromscandium.jigsawsudoku.game.Board;
import com.bromscandium.jigsawsudoku.game.SudokuGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GenerateServiceRest {

    @Autowired
    private SudokuGenerator generateService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateSudoku(@RequestBody BoardDTO dto) {
        Board board = new Board(dto.getSize());
        board.setRegionMap(dto.getRegionMap());

        for (int i = 0; i < dto.getSize(); i++) {
            for (int j = 0; j < dto.getSize(); j++) {
                CellDTO cellDto = dto.getCells()[i][j];
                board.setCellValue(i, j, cellDto.getValue(), cellDto.isFixed());
            }
        }

        boolean isValid = SudokuGenerator.isValidSudokuSolution(board);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/generate")
    public ResponseEntity<Board> generateSudoku(@RequestParam int size, @RequestParam Difficulty difficulty) {
        if (size < 3 || size > 9) {
            return ResponseEntity.badRequest().body(null);
        }
        Board board = generateService.generateSudoku(size, difficulty);
        return ResponseEntity.ok(board);
    }
}
