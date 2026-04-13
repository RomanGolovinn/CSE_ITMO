package commands;

import managers.collection.CollectionManager;
import models.Flat;

import java.util.Comparator;
import java.util.Objects;

/**
 * Команда для вывода значений поля numberOfRooms всех элементов коллекции в порядке убывания.
 * Извлекает количество комнат из каждой квартиры, фильтрует пустые значения (null),
 * сортирует их по убыванию и выводит результат в консоль.
 *
 * @author Roman Golovin
 */
public class PrintFieldDescendingNumberOfRooms implements Command{
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, из которого будут извлекаться данные о квартирах
     */
    public PrintFieldDescendingNumberOfRooms(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду извлечения, сортировки и вывода количества комнат.
     * Использует Stream API для обработки данных и безопасной фильтрации null-значений.
     *
     * @param argument     строковый аргумент (для данной команды не используется)
     * @param flatArgument объект квартиры (для данной команды не используется)
     */
    public void execute(String argument, Flat flatArgument){
        Long[] descNumberOfRooms = collection.getCollection().stream()
                .map(Flat::getNumberOfRooms)
                .filter(Objects::nonNull).sorted(Comparator.reverseOrder())
                .toArray(Long[]::new);

        if (descNumberOfRooms.length == 0){
            System.out.println("В коллекции нет квартир с комнатами");
            return;
        }

        System.out.println("Количество комнат в порядке убывания");
        for (long n : descNumberOfRooms){
            System.out.println(n);
        }
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("print_field_descending_number_of_rooms")
     */
    public String getName(){
        return "print_field_descending_number_of_rooms";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "вывести значения поля numberOfRooms всех элементов в порядке убывания";
    }
}