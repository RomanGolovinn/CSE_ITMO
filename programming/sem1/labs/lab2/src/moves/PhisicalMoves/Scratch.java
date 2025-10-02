package moves.PhisicalMoves;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public final class Scratch extends PhysicalMove {
    public Scratch(){
        super(Type.NORMAL, 40, 100);
    }

    @Override
    protected String describe(){
        return "Использует Scratch";
    }
}
