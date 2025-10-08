package moves.StatusMoves;

import ru.ifmo.se.pokemon.*;

public final class SlackOff extends StatusMove {
    public SlackOff(){
        super(Type.NORMAL, 0, 0);
    }

    @Override
    public void applySelfEffects(Pokemon p){
        Effect eff = new Effect();
        eff.stat(Stat.HP, (int)p.getHP() + (int)p.getHP()/2);

        p.addEffect(eff);
    }

    @Override
    protected String describe(){
        return "Использует SlackOff";
    }
}
