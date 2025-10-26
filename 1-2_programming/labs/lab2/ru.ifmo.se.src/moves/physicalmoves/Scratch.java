package moves.physicalmoves;

import ru.ifmo.se.pokemon.*;

public final class Scratch extends PhysicalMove {
    public Scratch(){
        super(Type.NORMAL, 40, 100);
    }

    //Эта атака только у огненых
    //Щгненые сильнее растений
    //Растения могут загорется
    //У огненых выше защита
    private boolean oppTypeIsGrass;

    @Override
    public void applyOppEffects(Pokemon p){
        this.oppTypeIsGrass = checkType(p.getTypes(), Type.GRASS);
        if (oppTypeIsGrass){
            System.out.println("Огненый vs Растений\n");
            var eff = new Effect().turns(0).chance(0.15).condition(Status.BURN);
            p.addEffect(eff);
            if(eff.success()){
                System.out.println("Покемон горит");
            }
        }
    }

    @Override
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
        return "Использует Scratch";
    }
}
