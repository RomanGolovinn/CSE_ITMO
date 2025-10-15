package moves.specialmoves;

import ru.ifmo.se.pokemon.*;

public final class Thanderbolt extends SpecialMove {
    public Thanderbolt(){
        super(Type.ELECTRIC, 90, 100);
    }

    @Override
    public void applyOppEffects(Pokemon p){
//        if (Math.random() <= 0.1){
//            Effect.paralyze(p);
//        }

        var eff = new Effect().chance(0.3).turns(0).condition(Status.PARALYZE);
        p.addEffect(eff);

        if (eff.success()) {
            System.out.println("Покемон был парализован");
        }
    }

    @Override
    protected String describe(){
        return "Использует Thanderbolt";
    }
}
