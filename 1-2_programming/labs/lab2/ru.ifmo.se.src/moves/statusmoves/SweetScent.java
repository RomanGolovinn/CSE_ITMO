package moves.StatusMoves;

import ru.ifmo.se.pokemon.*;

public final class SweetScent extends StatusMove{
    public SweetScent(){
        super(Type.NORMAL, 0, 0);
    }

    @Override
    public void applyOppEffects(Pokemon p){
        Effect eff = new Effect();
        eff.stat(Stat.EVASION, (int)p.getStat(Stat.EVASION)-1);

        p.addEffect(eff);
    }

    @Override
    protected String describe(){
        return "Использует SweetScent";
    }
}
