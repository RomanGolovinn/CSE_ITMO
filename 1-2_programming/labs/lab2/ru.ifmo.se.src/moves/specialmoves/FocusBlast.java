package moves.specialmoves;

import ru.ifmo.se.pokemon.*;

public final class FocusBlast extends SpecialMove {
    public FocusBlast(){
        super(Type.FIGHTING, 120, 70);
    }

    @Override
    public void applyOppEffects(Pokemon p){
//        if (Math.random() <= 0.1){
//            Effect eff = new Effect();
//            eff.stat(Stat.SPECIAL_DEFENSE, (int)p.getStat(Stat.SPECIAL_DEFENSE)-2);
//        }

        var eff = new Effect().chance(0.3).turns(0).stat(Stat.SPECIAL_DEFENSE, (int)p.getStat(Stat.SPECIAL_DEFENSE)-2);
        p.addEffect(eff);

        if (eff.success()) {
            System.out.println("Специальная защита снижена");
        }
    }

    @Override
    protected String describe(){
        return "Использует FocusBlast";
    }
}
