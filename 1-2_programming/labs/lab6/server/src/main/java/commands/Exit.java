package commands;

import models.Flat;

/**
 * Команда для немедленного завершения работы программы.
 * Выполнение этой команды завершает процесс без автоматического сохранения текущей коллекции в файл.
 *
 * @author Roman Golovin
 */
public class Exit implements Command{

    /**
     * Выполняет команду завершения программы.
     * Выводит сообщение в консоль и останавливает виртуальную машину Java с кодом 0 (успешное завершение).
     *
     * @param argument строковый аргумент (не используется)
     * @param flat     объект квартиры (не используется)
     */
    public void execute(String argument, Flat flat){
        System.out.println("Завершение программы");
        System.exit(0);
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова ("exit")
     */
    public String getName(){
        return "exit";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "завершить программу (без сохранения в файл)";
    }
}