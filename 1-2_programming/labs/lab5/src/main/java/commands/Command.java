package commands;

import main.java.managers.models.Flat;

public interface Command {
    void execute(String argument, Flat flatArgument);
    String getName();
    String getDescription();
}