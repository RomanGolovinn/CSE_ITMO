package io;

import models.Coordinates;
import models.enums.Furnish;
import models.enums.Transport;
import models.enums.View;

import java.util.Scanner;


public class AskManager {
    private Scanner scanner;

    public AskManager(Scanner scanner) {
        this.scanner = scanner;
    }

    private String readNext() {
        return scanner.nextLine().trim();
    }

    public String askName(){
        while (true){
            System.out.println("Введите название квартиры: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()){
                System.out.println("Ошибка: имя не может быть пустым");
                continue;
            }
            return name;
        }
    }

    private Long askX() {
        while (true) {
            System.out.print("Введите X: ");
            String input = readNext();
            try {
                Long x = Long.parseLong(input);
                if (x > 853) throw new IllegalArgumentException("X не может быть больше 853.");
                return x;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private float askY() {
        while (true) {
            System.out.print("Введите Y: ");
            String input = readNext();
            try {
                float y = Float.parseFloat(input);
                if (y <= -226) throw new IllegalArgumentException("Y должен быть больше -226.");
                return y;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    public Coordinates askCoordinates(){
        return new Coordinates(askX(), askY());
    }

    public Long askArea(){
        while (true){
            System.out.println("Введите площадь: ");
            String input = readNext();
            try{
                Long Area = Long.parseLong(input);
                if (Area == null) return Area;
                if (Area < 0) throw new IllegalArgumentException("Area болжно быть больше 0");
                return Area;
            }catch (Exception e){
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    public Long askNumberOfRooms(){
        while (true){
            System.out.println("Введите площадь: ");
            String input = readNext();
            try{
                Long rooms = Long.parseLong(input);
                if (rooms == null) return rooms;
                if (rooms < 0) throw new IllegalArgumentException("Rooms болжно быть больше 0");
                return rooms;
            }catch (Exception e){
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    public Furnish askFurnish() {
        while (true) {
            System.out.println("Доступные варианты отделки:");
            for (Furnish f : Furnish.values()) {
                System.out.println("- " + f.name());
            }

            System.out.print("Введите вариант отделки: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка: поле отделки не может быть пустым.");
                continue;
            }

            try {
                return Furnish.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: такого варианта нет в списке. Попробуйте снова.");
            }
        }
    }

    public View askView(){
        while (true) {
            System.out.println("Доступные варианты вида:");
            for (Furnish f : Furnish.values()) {
                System.out.println("- " + f.name());
            }

            System.out.print("Введите вариант вида: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Ошибка: поле вида не может быть пустым.");
                continue;
            }

            try {
                return View.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: такого варианта нет в списке. Попробуйте снова.");
            }
        }
    }

    public Transport askTransport(){
        while (true) {
            System.out.println("Доступные варианты транспорта:");
            for (Furnish f : Furnish.values()) {
                System.out.println("- " + f.name());
            }

            System.out.print("Введите вариант транспорта: ");
            String input = scanner.nextLine().trim();

            try {
                return Transport.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: такого варианта нет в списке. Попробуйте снова.");
            }
        }
    }

    public String askNameHouse(){
        while (true){
            System.out.println("Введите название дома: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()){
                System.out.println("Ошибка: имя не может быть пустым");
                continue;
            }
            return name;
        }
    }


    private Integer askYear(){
        while (true){
            System.out.println("Введите год постройки дома: ");
            String input = readNext();

            try{
                int year = Integer.parseInt(input);
                if (Integer.valueOf(year) == null) throw new IllegalArgumentException("Год не может быть null");
                if (year  <= 0) throw new IllegalArgumentException("Год должен быть больше 0");
                return year;
            }catch (Exception e){
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }


}