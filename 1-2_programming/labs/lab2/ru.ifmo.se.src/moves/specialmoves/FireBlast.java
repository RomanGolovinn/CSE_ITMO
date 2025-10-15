package moves.specialmoves;

import ru.ifmo.se.pokemon.*;

public final class FireBlast extends SpecialMove {
    public FireBlast(){
        super(Type.FIRE, 110, 85);
    }

    @Override
    public void applyOppEffects(Pokemon p){
//        if (Math.random() <= 0.1){
//            Effect.burn(p);
//        }

        var eff = new Effect().chance(0.3).turns(0).condition(Status.BURN);
        p.addEffect(eff);

        if (eff.success()) {
            System.out.println("Покемон был подожжён");
        }
    }

    @Override
    protected String describe(){
        return "Использует FireBlast";
    }
}
