package moves.statusmoves;

import ru.ifmo.se.pokemon.*;

public final class SleepPowder extends StatusMove {
    public SleepPowder(){
        super(Type.GRASS, 0, 75);
    }

    //Эта аттака только у растений
    //Растения сильнее нормальных
    //Нормальные иногда боятся растений
    //Растения быстрее нормальных и лучше уклоняются
    private boolean oppTypeIsNormal;

    @Override
    public void applyOppEffects(Pokemon p){
        this.oppTypeIsNormal = checkType(p.getTypes(), Type.NORMAL);
        if (oppTypeIsNormal){
            System.out.println("Растения vs Нормальные\n");
            var eff = new Effect().chance(0.15).turns(0).condition(Status.POISON);
            p.addEffect(eff);
            if (eff.success()){
                System.out.println("Покемон отравлен");
            }
        }

        Effect.sleep(p);
    }

    public void applySelfEffects(Pokemon p){
        if (oppTypeIsNormal){
            var speed = new Effect().turns(0).stat(Stat.SPEED, (int)p.getStat(Stat.SPEED));
            var ev = new Effect().turns(0).stat(Stat.EVASION, (int)p.getStat(Stat.EVASION));
            p.addEffect(speed);
            p.addEffect(ev);
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
        return "Использует SleepPowder";
    }
}
