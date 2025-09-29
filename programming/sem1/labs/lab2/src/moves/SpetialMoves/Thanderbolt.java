package moves.SpetialMoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class Thanderbolt extends SpecialMove {
    public Thanderbolt(){
        super(Type.ELECTRIC, 90, 100);
    }

    @Override
    public void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.1){
            Effect.paralyze(p);
        }
    }
}
