package character;

import entity.children.Car;
import exeptions.LostException;
import interfaces.Mobile;
import place.Place;
import place.children.River;
import place.children.Town;

abstract public class Character implements Mobile {
    public String name;

    public Character(){}

    public Character(String name){
        this.name = name;
    }

    public abstract void walk(Place p) throws LostException;

    public void whatchOnCar(Car c){
        if (c.isGoOut()){
            System.out.println(name + " смотрит вслед за машиной");
        }
    }

    public void beHappy(){
        System.out.println(name + " рад(а) вернуться в город");
    }

    void comeToPlace(Place p){
        System.out.println(name+ " пришёл(ла) в " + p.getName());
    }

    public void moveTo(Place out){
        System.out.println(name + " ушёл(ла) из " + out.getName());
    }

    public void moveTo(Place out, Place in){
        System.out.println(name + " ушёл(ла) из " + out.getName() + " в " + in.getName());
    }

    public String getName(){
        return name;
    }

    protected void checkLostException(Place p) throws LostException {
        if (p instanceof River){
            var river = (River) p;
            for (var plant : river.getPlants()){
                if(plant.getSize() > 5){
                    String message = this.name + " заблудился(лась) в зарослях " + plant.getName();
                    throw new LostException(message);
                }
            }
        }
    }

    protected void walkOnStreets(Place p){
        if (p instanceof Town){
            var town = (Town) p;
            for (var street : town.getStreets()){
                System.out.println(this.name + " прогуливается по улице " + street.name());
            }
        }
    }
}
