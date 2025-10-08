package pokemons;

import moves.SpecialMoves.FocusBlast;

public final class Lickilicky extends Lickitung {
    public Lickilicky(String name, int level){
        super(name, level);
        setStats(110, 85, 95, 80, 95, 50);
        addMove(new FocusBlast());
    }
}
