package commands;

import main.java.managers.models.Flat;

public class Exit implements Command{
    public void execute(String argument, Flat flat){
        System.out.println("Завершение программы");
        System.exit(0);
    }

    public String getName(){
        return "exit";
    }

    public String getDescription(){
        return "завершить программу (без сохранения в файл)";
    }
}


