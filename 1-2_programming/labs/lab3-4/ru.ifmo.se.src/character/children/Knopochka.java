package character.children;

import character.Character;
import exeptions.LostException;
import interfaces.Mobile;
import place.Place;

final public class Knopochka extends Character {
    public Knopochka (String name){
        super(name);
    }

    @Override
    public void walk(Place p) throws LostException {
        checkLostException(p);
        System.out.println(this.name + " гуляет по " + p.getName());
        walkOnStreets(p);
    }
}
