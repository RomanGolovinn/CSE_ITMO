package moves.StatusMoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class SleepPowder extends StatusMove {
    public SleepPowder(){
        super(Type.GRASS, 0, 75);
    }

    @Override
    public void applyOppEffects(Pokemon p){
        Effect.sleep(p);
    }
}
