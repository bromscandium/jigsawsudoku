package com.bromscandium.jigsawsudoku.server.dto;

public class UserDTO {
    private int id;
    private String login;

    public UserDTO(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
}
