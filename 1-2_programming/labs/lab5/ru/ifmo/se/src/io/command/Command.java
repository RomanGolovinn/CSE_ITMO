package io.command;

public interface Command {
    void execute(String argument);
    String getName();
    String getDescription();
}