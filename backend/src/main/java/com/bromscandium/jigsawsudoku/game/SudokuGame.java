package com.bromscandium.jigsawsudoku.game;

import com.bromscandium.jigsawsudoku.entity.Comment;
import com.bromscandium.jigsawsudoku.entity.Rating;
import com.bromscandium.jigsawsudoku.entity.Score;
import com.bromscandium.jigsawsudoku.enums.Difficulty;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;
import com.bromscandium.jigsawsudoku.ui.ConsoleUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static com.bromscandium.jigsawsudoku.game.SudokuGenerator.isValidSudokuSolution;

@Component
public class SudokuGame {
    private static final String GAME_NAME = "Jigsaw Sudoku";
    private final ScoreInterface scoreInterface;
    private final RatingInterface ratingInterface;
    private final CommentInterface commentService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public SudokuGame(@Qualifier("scoreService") ScoreInterface scoreInterface,
                      @Qualifier("ratingService") RatingInterface ratingInterface,
                      CommentInterface commentService) {
        this.scoreInterface = scoreInterface;
        this.ratingInterface = ratingInterface;
        this.commentService = commentService;
    }

    public boolean startGame() {
        String playerName = askNickname();
        Difficulty difficulty = askDifficulty();
        int size = askBoardSize();

        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        Board board = sudokuGenerator.generateSudoku(size, difficulty);
        ConsoleUI.printBoard(board);
        ConsoleUI.printHelp();

        int points = (size * size) * difficulty.multiplier();

        while (!isValidSudokuSolution(board)) {
            String command = askCommand();
            handleCommand(command, board, size, playerName, points);
            ConsoleUI.printBoard(board);
        }

        System.out.println("You have solved a sudoku. Congratulations!");
        scoreInterface.addScore(new Score(GAME_NAME, playerName, points, new Date()));

        return askPlayAgain();
    }

    private String askNickname() {
        System.out.print("Enter your nickname: ");
        return scanner.nextLine().trim();
    }

    private Difficulty askDifficulty() {
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.print("Choose a difficulty (1 - Easy, 2 - Medium, 3 - Hard): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        return Difficulty.values()[choice - 1];
    }

    private int askBoardSize() {
        int size = 0;
        while (size < 3 || size > 9) {
            System.out.print("Choose the size of the board (3 to 9): ");
            if (scanner.hasNextInt()) {
                size = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        scanner.nextLine();
        return size;
    }

    private String askCommand() {
        System.out.print("> ");
        return scanner.next().trim();
    }

    private boolean askPlayAgain() {
        System.out.print("Do you want to play again? (yes/no): ");
        return scanner.next().equalsIgnoreCase("yes");
    }

    private void handleCommand(String command, Board board, int size, String playerName, int points) {
        switch (command.toLowerCase()) {
            case "exit" -> System.exit(0);
            case "help" -> ConsoleUI.printHelp();
            case "score" -> printScores();
            case "rate" -> rate(playerName);
            case "comment" -> comment(playerName);
            case "a", "d" -> fillCell(command, board, size);
            default -> System.out.println("Unknown command. Type 'help' for the list of commands.");
        }
    }

    private void printScores() {
        List<Score> scores = scoreInterface.getTopScores(GAME_NAME);
        System.out.println("Top scores:");
        scores.forEach(score ->
                System.out.println(score.getPlayer() + " - " + score.getPoints() + " points"));
    }

    private void comment(String playerName) {
        scanner.nextLine();
        System.out.print("Enter your comment: ");
        String commentText = scanner.nextLine();

        if (commentText.isEmpty()) {
            System.out.println("Comment cannot be empty.");
            return;
        }

        try {
            Comment comment = new Comment(GAME_NAME, playerName, commentText, new Date());
            commentService.addComment(comment);
            System.out.println("Comment added!");
        } catch (Exception e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }

    private void rate(String playerName) {
        System.out.print("Enter rating (1-5): ");
        if (scanner.hasNextInt()) {
            int ratingValue = scanner.nextInt();
            if (ratingValue >= 1 && ratingValue <= 5) {
                Rating rating = new Rating(GAME_NAME, playerName, ratingValue, new Date());
                ratingInterface.setRating(rating);
                System.out.println("Rating recorded!");
            } else {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
    }

    private void fillCell(String command, Board board, int size) {
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Expected coordinates.");
            scanner.next();
            return;
        }

        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;

        if (x < 0 || x >= size || y < 0 || y >= size) {
            System.out.println("Invalid cell coordinates.");
            return;
        }

        var cell = board.getCells()[x][y];

        if (command.equalsIgnoreCase("a")) {
            if (!scanner.hasNextInt()) {
                System.out.println("Expected value after coordinates.");
                scanner.next();
                return;
            }

            int value = scanner.nextInt();
            if (value < 1 || value > size) {
                System.out.println("Invalid value for this board size.");
            } else if (cell.isFixed() || cell.getValue() != 0) {
                System.out.println("Cell is fixed or already filled.");
            } else {
                cell.setValue(value);
            }
        } else if (command.equalsIgnoreCase("d")) {
            if (cell.isFixed()) {
                System.out.println("Cell is fixed.");
            } else {
                cell.setValue(0);
            }
        }
    }
}
