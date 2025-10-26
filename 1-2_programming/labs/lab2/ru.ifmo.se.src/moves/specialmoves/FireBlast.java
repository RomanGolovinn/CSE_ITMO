package moves.specialmoves;

import ru.ifmo.se.pokemon.*;

public final class FireBlast extends SpecialMove {
    public FireBlast(){
        super(Type.FIRE, 110, 85);
    }

    //Эта атака только у огненых
    //Щгненые сильнее растений
    //Растения могут загорется
    //У огненых выше защита
    private boolean oppTypeIsGrass;

    @Override
    public void applyOppEffects(Pokemon p){
        oppTypeIsGrass = checkType(p.getTypes(), Type.GRASS);
        if (oppTypeIsGrass){
            System.out.println("Огненый vs Растений");
        }
        //Не надо дополнительно прописовать горение для растений
        var eff = new Effect().chance(0.3).turns(0).condition(Status.BURN);
        p.addEffect(eff);

        if (eff.success()) {
            System.out.println("Покемон был подожжён");
        }
    }

    public void applySelfEffects(Pokemon p){
        if (oppTypeIsGrass){
            var eff = new Effect().turns(1).stat(Stat.DEFENSE, (int)p.getStat(Stat.DEFENSE)+2);
            p.addEffect(eff);
        }
    }

    private boolean checkType(Type[] arr, Type type){
        for (Type t : arr){
            if (t == type) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String describe(){
        return "Использует FireBlast";
    }
}
