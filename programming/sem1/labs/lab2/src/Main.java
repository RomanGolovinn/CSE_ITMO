

import ru.ifmo.se.pokemon.Battle;
import pokemons.*;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        int level = genLevel();
        Pokemon p1 = createFirePokemon(level);

        level = genLevel();
        Pokemon p2 = createNormalPokemon(level);

        level = genLevel();
        Pokemon p3 = new Carnivine("Carnivine", level);

        level = genLevel();
        Pokemon p4 = createFirePokemon(level);

        level = genLevel();
        Pokemon p5 = createFirePokemon(level);

        level = genLevel();
        Pokemon p6 = createNormalPokemon(level);

        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);

        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);

        b.go();
    }

    static int genLevel(){
        return (int)(Math.random()*100);
    }

    static Pokemon createFirePokemon(int level){
        Pokemon p;
        if (level < 14){
            p = new Chimchar("Chimchar", level);
        }
        else if (level < 36) {
            p = new Monferno("Monferno", level);
        }
        else{
            p = new Infernape("Infernape", level);
        }
        return p;
    }

    static Pokemon createNormalPokemon(int level){
        Pokemon p;
        if (level < 14){
            p = new Lickitung("Lickitung", level);
        }
        else{
            p = new Lickilicky("Lickilicky", level);
        }
        return p;
    }
}