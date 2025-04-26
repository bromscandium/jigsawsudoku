package com.bromscandium.jigsawsudoku.local;

import com.bromscandium.jigsawsudoku.game.SudokuGame;
import com.bromscandium.jigsawsudoku.interfaces.CommentInterface;
import com.bromscandium.jigsawsudoku.interfaces.RatingInterface;
import com.bromscandium.jigsawsudoku.interfaces.ScoreInterface;

import com.bromscandium.jigsawsudoku.local.rest.CommentServiceRestClient;
import com.bromscandium.jigsawsudoku.local.rest.RatingServiceRestClient;
import com.bromscandium.jigsawsudoku.local.rest.ScoreServiceRestClient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.client.RestTemplate;

@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.bromscandium.jigsawsudoku.server.*"))
@SpringBootApplication
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner runner(SudokuGame game) {
        return args -> {
            System.out.println("Jigsaw Sudoku Game Started!");
            boolean playAgain;
            do {
                playAgain = game.startGame();
            } while (playAgain);
            System.out.println("Thanks for playing!");
        };
    }

    @Bean
    public ScoreInterface scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentInterface commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingInterface ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
