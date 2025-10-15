package moves.physicalmoves;

import ru.ifmo.se.pokemon.*;

public final class Stomp extends PhysicalMove {
    public Stomp(){
        super(Type.NORMAL, 65, 100);
    }

    @Override
    public void applyOppEffects(Pokemon p){
//        if (Math.random() <= 0.3){
//            Effect.flinch(p);
//        }
        var eff = new Effect().chance(0.3).turns(0).condition(Status.SLEEP);
        p.addEffect(eff);

        if (eff.success()) {
            System.out.println("Покемон был испуган");
        }
    }

    @Override
    protected String describe(){
        return "Использует Stomp";
    }
}
