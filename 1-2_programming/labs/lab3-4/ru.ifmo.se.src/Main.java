import character.children.Knopochka;
import character.children.Neznayka;
import character.clother.Color;
import character.clother.Gloves;
import entity.children.Car;
import entity.children.Plant;
import exeptions.LostException;
import place.children.River;
import place.Street;
import place.children.Town;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Gloves g = new Gloves(Color.blue);
        Neznayka neznayka = new Neznayka("Незнайка", g);
        Knopochka knopochka = new Knopochka("Кнопочка");
        var street = new Street("Улица", 2);
        var town = new Town("Цветочный город", new ArrayList<Street>(Arrays.asList(street)));
        var car = new Car( "Машина", town);
        var river = new River("Огуречная река", new ArrayList<Plant>());
        Plant cucamber = new Plant("Заросли огурцов", river);
        river.addPlat(cucamber);

        try{
            neznayka.walk(town);
            knopochka.walk(town);
        }catch (LostException e){
            System.err.println(e.getMessage());
        }
        cucamber.grow();
        neznayka.closeCar(car);
        neznayka.whatchOnCar(car);
        knopochka.whatchOnCar(car);
        cucamber.grow();
        neznayka.beHappy();
        knopochka.beHappy();
        neznayka.moveTo(town, river);
        knopochka.moveTo(town, river);
        try{
            neznayka.walk(river);
            knopochka.walk(river);
        }catch (LostException e){
            System.err.println(e.getMessage());
        }
    }
}