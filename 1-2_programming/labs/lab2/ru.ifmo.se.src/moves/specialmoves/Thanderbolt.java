package moves.specialmoves;

import ru.ifmo.se.pokemon.*;

public final class Thanderbolt extends SpecialMove {
    public Thanderbolt(){
        super(Type.ELECTRIC, 90, 100);
    }

    //Эта аттака только у нормальный
    //Нормальные сильнее огненых
    //У огненых ниже защита
    //У нормальных выше меткость
    private boolean oppTypeIsFire;

    @Override
    public void applyOppEffects(Pokemon p){
        this.oppTypeIsFire = checkType(p.getTypes(), Type.FIRE);
        if (oppTypeIsFire){
            System.out.println("Нормальный vs Огненый\n");
            var eff = new Effect().turns(0).stat(Stat.DEFENSE, (int)p.getStat(Stat.DEFENSE)-2);
            p.addEffect(eff);
        }

        var eff = new Effect().chance(0.3).turns(0).condition(Status.PARALYZE);
        p.addEffect(eff);

        if (eff.success()) {
            System.out.println("Покемон был парализован");
        }
    }

    @Override
    public void applySelfEffects(Pokemon p){
        if (oppTypeIsFire){
            var eff = new Effect().turns(0).stat(Stat.ACCURACY, (int)p.getStat(Stat.ACCURACY)+2);
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
        return "Использует Thanderbolt";
    }
}
