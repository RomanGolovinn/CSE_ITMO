package moves.PhisicalMoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Stomp extends PhysicalMove {
    public Stomp(){
        super(Type.NORMAL, 65, 100);
    }

    @Override
    public void applyOppEffects(Pokemon p){
        if (Math.random() <= 0.3){
            Effect.flinch(p);
        }
    }

    @Override
    protected String describe(){
        return "Использует Stomp";
    }
}
