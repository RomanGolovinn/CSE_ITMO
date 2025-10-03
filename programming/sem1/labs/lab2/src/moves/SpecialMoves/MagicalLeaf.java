package moves.SpecialMoves;

import ru.ifmo.se.pokemon.*;

public final class MagicalLeaf extends SpecialMove {
    public MagicalLeaf(){
        super(Type.GRASS, 60, Double.POSITIVE_INFINITY);
    }

    @Override
    public void applyOppEffects(Pokemon p){
        Effect eff = new Effect();
        eff.stat(Stat.EVASION, -6);

        p.addEffect(eff);
    }

    @Override
    public void applySelfEffects(Pokemon p){
        Effect eff = new Effect();
        eff.stat(Stat.ACCURACY, 6);

        p.addEffect(eff);
    }

    @Override
    protected String describe(){
        return "Использует MagicalLeaf";
    }

}
