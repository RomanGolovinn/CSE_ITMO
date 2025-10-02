package moves.StatusMoves;

import ru.ifmo.se.pokemon.*;

public final class SwordDance extends StatusMove {
    public SwordDance(){
        super(Type.NORMAL, 0, 0);
    }

    @Override
    public void applySelfEffects(Pokemon p){
        Effect eff = new Effect();
        eff.stat(Stat.ATTACK, (int)p.getStat(Stat.ATTACK)+2);
    }

    @Override
    protected String describe(){
        return "Использует SwordDance";
    }
}
