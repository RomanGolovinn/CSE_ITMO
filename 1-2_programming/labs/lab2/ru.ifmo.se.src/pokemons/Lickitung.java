package pokemons;

import moves.specialmoves.Thanderbolt;
import moves.physicalmoves.Stomp;
import moves.statusmoves.SwordDance;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Lickitung extends Pokemon {
    public Lickitung(String name, int level){
        super(name, level);
        setStats(90, 55, 75, 60, 75, 30);
        setType(Type.NORMAL);
        setMove(new Thanderbolt(), new Stomp(), new SwordDance());
    }
}
