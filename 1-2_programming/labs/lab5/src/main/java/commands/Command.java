package commands;

import models.Flat;

public interface Command {
    void execute(String argument, Flat flatArgument);
    String getName();
    String getDescription();
}