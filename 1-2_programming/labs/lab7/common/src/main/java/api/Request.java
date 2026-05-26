package api;

import java.io.Serializable;
import models.Flat;

public class Request implements Serializable {
    private final String commandName;
    private final String argument;
    private final Flat flatArgument;
    private final String username;
    private final String password;

    public Request(String commandName, String argument, Flat flatArgument, String username, String password) {
        this.commandName = commandName;
        this.argument = argument;
        this.flatArgument = flatArgument;
        this.username = username;
        this.password = password;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgument() {
        return argument;
    }

    public Flat getFlatArgument() {
        return flatArgument;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}