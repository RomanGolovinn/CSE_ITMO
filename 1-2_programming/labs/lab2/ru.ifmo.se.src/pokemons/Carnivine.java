package pokemons;

import moves.specialmoves.MagicalLeaf;
import moves.statusmoves.DoubleTeam;
import moves.statusmoves.SleepPowder;
import moves.statusmoves.SweetScent;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Carnivine extends Pokemon {
    public Carnivine(String name, int level){
        super(name, level);
        setStats(74, 100, 72, 90, 72, 46);
        setType(Type.GRASS);
        setMove(new MagicalLeaf(), new SweetScent(), new SleepPowder(), new DoubleTeam());
    }

}
