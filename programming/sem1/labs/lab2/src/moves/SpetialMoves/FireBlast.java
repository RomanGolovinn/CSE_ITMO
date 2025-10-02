package moves.SpetialMoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public final class FireBlast extends SpecialMove {
    public FireBlast(){
        super(Type.FIRE, 110, 85);
    }

    @Override
    public void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.1){
            Effect.burn(p);
        }
    }

    @Override
    protected String describe(){
        return "Использует FireBlast";
    }
}
