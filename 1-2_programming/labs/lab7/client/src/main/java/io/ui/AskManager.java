package io.ui;

import models.Coordinates;
import models.Flat;
import models.House;
import models.enums.Furnish;
import models.enums.Transport;
import models.enums.View;

import java.util.Scanner;

/**
 * Класс, отвечающий за интерактивное чтение данных из консоли или скрипта.
 * Содержит методы для поэтапного заполнения полей объектов {@link Flat}, {@link House} и {@link Coordinates}
 * с обязательной проверкой ограничений предметной области.
 *
 * @author Roman Golovin
 */
public class AskManager {
    private Scanner scanner;
    private boolean isInteractive;

    /**
     * Конструктор менеджера ввода.
     *
     * @param scanner объект {@link Scanner}, через который осуществляется чтение данных
     */
    public AskManager(Scanner scanner) {
        this.isInteractive = true;
        this.scanner = scanner;
    }

    public void setInteractive(boolean interactive) {
        this.isInteractive = interactive;
    }

    /**
     * Вспомогательный метод для чтения и обрезки лишних пробелов в строке.
     *
     * @return считанная строка с обрезанными пробелами
     */
    private String readNext() {
        return scanner.nextLine().trim();
    }

    /**
     * Запрашивает название квартиры. Поле не может быть пустым.
     *
     * @return название квартиры
     */
    public String askName(){
        while (true){
            if(isInteractive) {
                System.out.println("Введите название квартиры: ");
            }
            String name = scanner.nextLine().trim();
            if (name.isEmpty()){
                if(isInteractive) {
                    System.out.println("Ошибка: имя не может быть пустым");
                }
                continue;
            }
            return name;
        }
    }

    /**
     * Запрашивает координату X. Максимальное значение — 853.
     *
     * @return координата X
     */
    private Long askX() {
        while (true) {
            if(isInteractive) {
                System.out.print("Введите X: ");
            }
            String input = readNext();
            try {
                Long x = Long.parseLong(input);
                if (x > 853) throw new IllegalArgumentException("X не может быть больше 853.");
                return x;
            } catch (Exception e) {
                if(isInteractive) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Запрашивает координату Y. Минимальное значение — больше -226.
     *
     * @return координата Y
     */
    private float askY() {
        while (true) {
            if(isInteractive) {
                System.out.print("Введите Y: ");
            }
            String input = readNext();
            try {
                float y = Float.parseFloat(input);
                if (y <= -226) throw new IllegalArgumentException("Y должен быть больше -226.");
                return y;
            } catch (Exception e) {
                if(isInteractive) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Создает новый объект координат, запрашивая X и Y.
     *
     * @return заполненный объект {@link Coordinates}
     */
    public Coordinates askCoordinates(){
        return new Coordinates(askX(), askY());
    }

    /**
     * Запрашивает площадь. Значение должно быть больше 0.
     *
     * @return значение площади
     */
    public Long askArea(){
        while (true){
            if(isInteractive) {
                System.out.println("Введите площадь: ");
            }
            String input = readNext();
            try{
                Long Area = Long.parseLong(input);
                if (Area == null) return Area;
                if (Area < 0) throw new IllegalArgumentException("Area болжно быть больше 0");
                return Area;
            }catch (Exception e){
                if(isInteractive) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Запрашивает количество комнат. Значение должно быть больше 0.
     *
     * @return количество комнат
     */
    public Long askNumberOfRooms(){
        while (true){
            if(isInteractive) {
                System.out.println("Введите количество квартир: ");
            }
            String input = readNext();
            try{
                Long rooms = Long.parseLong(input);
                if (rooms == null) return rooms;
                if (rooms < 0) throw new IllegalArgumentException("Rooms болжно быть больше 0");
                return rooms;
            }catch (Exception e){
                if(isInteractive) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Запрашивает тип отделки из списка доступных перечислений.
     *
     * @return элемент перечисления {@link Furnish}
     */
    public Furnish askFurnish() {
        while (true) {
            if(isInteractive) {
                System.out.println("Доступные варианты отделки:");
            }
            if(isInteractive) {
                for (Furnish f : Furnish.values()) {
                    System.out.println("- " + f.name());
                }
                System.out.print("Введите вариант отделки: ");
            }
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if(isInteractive) {
                    System.out.println("Ошибка: поле отделки не может быть пустым.");
                }
                continue;
            }

            try {
                return Furnish.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                if(isInteractive) {
                    System.out.println("Ошибка: такого варианта нет в списке. Попробуйте снова.");
                }
            }
        }
    }

    /**
     * Запрашивает тип вида из окна.
     *
     * @return элемент перечисления {@link View}
     */
    public View askView(){
        while (true) {
            if(isInteractive) {
                System.out.println("Доступные варианты вида:");
                for (View f : View.values()) {
                    System.out.println("- " + f.name());
                }

                System.out.print("Введите вариант вида: ");
            }
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if(isInteractive) {
                    System.out.println("Ошибка: поле вида не может быть пустым.");
                }
                continue;
            }

            try {
                return View.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                if(isInteractive) {
                    System.out.println("Ошибка: такого варианта нет в списке. Попробуйте снова.");
                }
            }
        }
    }

    /**
     * Запрашивает тип транспорта рядом.
     *
     * @return элемент перечисления {@link Transport}
     */
    public Transport askTransport(){
        while (true) {
            if(isInteractive) {
                System.out.println("Доступные варианты транспорта:");
                for (Transport f : Transport.values()) {
                    System.out.println("- " + f.name());
                }

                System.out.print("Введите вариант транспорта: ");
            }
            String input = scanner.nextLine().trim();

            try {
                return Transport.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                if(isInteractive) {
                    System.out.println("Ошибка: такого варианта нет в списке. Попробуйте снова.");
                }
            }
        }
    }

    /**
     * Запрашивает название дома.
     *
     * @return название дома
     */
    public String askHouseName(){
        while (true){
            if(isInteractive) {
                System.out.println("Введите название дома: ");
            }
            String name = scanner.nextLine().trim();
            if (name.isEmpty()){
                if(isInteractive) {
                    System.out.println("Ошибка: имя не может быть пустым");
                }
                continue;
            }
            return name;
        }
    }


    /**
     * Запрашивает год постройки дома. Должен быть больше 0.
     *
     * @return год постройки
     */
    private Integer askYear(){
        while (true){
            if(isInteractive) {
                System.out.println("Введите год постройки дома: ");
            }
            String input = readNext();

            try{
                int year = Integer.parseInt(input);
                if (Integer.valueOf(year) == null) throw new IllegalArgumentException("Год не может быть null");
                if (year  <= 0) throw new IllegalArgumentException("Год должен быть больше 0");
                return year;
            }catch (Exception e){
                if(isInteractive) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Запрашивает количество этажей в доме. Должно быть больше 0.
     *
     * @return количество этажей
     */
    private Long askNumperOfFloors(){
        while (true){
            if(isInteractive) {
                System.out.println("Введите количество этажей постройки дома: ");
            }
            String input = readNext();

            try{
                Long floors = Long.parseLong(input);
                if (floors == null) throw new IllegalArgumentException("Количество этажей не может быть null");
                if (floors <= 0) throw new IllegalArgumentException("Количество этажей должен быть больше 0");
                return floors;
            }catch (Exception e){
                if(isInteractive) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Собирает и возвращает полностью заполненный объект дома.
     *
     * @return объект {@link House}
     */
    public House askHouse(){
        return new House(askHouseName(), askYear(), askNumperOfFloors());
    }

    /**
     * Главный метод для сборки нового объекта квартиры.
     * Поочередно запрашивает все поля у пользователя.
     *
     * @return заполненный объект {@link Flat}
     */
    public Flat askFlat(){
        return new Flat(
                askName(),
                askCoordinates(),
                askArea(),
                askNumberOfRooms(),
                askFurnish(),
                askView(),
                askTransport(),
                askHouse()
        );
    }

    /**
     * Устанавливает новый источник ввода.
     * Используется при переключении между консолью и чтением из скрипта.
     *
     * @param scanner новый экземпляр {@link Scanner}
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Возвращает текущий используемый сканер.
     *
     * @return текущий сканер
     */
    public Scanner getScanner (){
        return this.scanner;
    }
}