package common;

import models.Flat;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final String argument;
    private final Flat flatArgument;

    public Request(String commandName, String argument, Flat flatArgument) {
        this.commandName = commandName;
        this.argument = argument;
        this.flatArgument = flatArgument;
    }

    public String getCommandName() { return commandName; }
    public String getArgument() { return argument; }
    public Flat getFlatArgument() { return flatArgument; }
}
