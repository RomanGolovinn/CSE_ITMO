package pokemons;

import moves.statusmoves.SlackOff;
import ru.ifmo.se.pokemon.*;

public class Monferno extends Chimchar{
    public Monferno(String name, int level){
        super(name, level);
        setStats(64, 78, 52, 78, 52, 81);
        addType(Type.FIGHTING);
        addMove(new SlackOff());
    }
}
