package moves.statusmoves;

import ru.ifmo.se.pokemon.*;

public final class SwordDance extends StatusMove {
    public SwordDance(){
        super(Type.NORMAL, 0, 0);
    }

    //Эта аттака только у нормальный
    //Нормальные сильнее огненых
    //У огненых ниэе защита
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
    }

    @Override
    public void applySelfEffects(Pokemon p){
        if (oppTypeIsFire){
            var eff = new Effect().turns(0).stat(Stat.ACCURACY, (int)p.getStat(Stat.ACCURACY)+2);
            p.addEffect(eff);
        }

        Effect eff = new Effect();
        eff.stat(Stat.ATTACK, (int)p.getStat(Stat.ATTACK)+2);
        p.addEffect(eff);
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
        return "Использует SwordDance";
    }
}
