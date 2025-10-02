package moves.StatusMoves;

import ru.ifmo.se.pokemon.*;

public final class DoubleTeam extends StatusMove {
    public DoubleTeam(){
        super(Type.NORMAL, 0, 0);
    }

    @Override
    public void applySelfEffects(Pokemon p){
        Effect eff = new Effect();
        eff.stat(Stat.EVASION, (int)p.getStat(Stat.EVASION)+1);

        p.addEffect(eff);
    }

    @Override
    protected String describe(){
        return "Использует DoubleTeam";
    }
}
