package io.command;

import models.Flat;

public interface Command {
    void execute(String argument, Flat flatArgument);
    String getName();
    String getDescription();
}