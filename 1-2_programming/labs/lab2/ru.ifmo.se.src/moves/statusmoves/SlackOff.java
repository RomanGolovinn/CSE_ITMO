package moves.statusmoves;

import ru.ifmo.se.pokemon.*;

public final class SlackOff extends StatusMove {
    public SlackOff(){
        super(Type.NORMAL, 0, 0);
    }

    //Эта атака только у огненых
    //Щгненые сильнее растений
    //Растения могут загорется
    //У огненых выше защита
    private boolean oppTypeIsGrass;

    public void applyOppEffects(Pokemon p){
        //Т к это лечение, а не атака растение не может гореть
        this.oppTypeIsGrass = checkType(p.getTypes(), Type.GRASS);
        if (oppTypeIsGrass) {
            System.out.println("Огненый vs Растений\n");
        }
    }

    @Override
    public void applySelfEffects(Pokemon p){
        if (oppTypeIsGrass){
            var eff = new Effect().turns(1).stat(Stat.DEFENSE, (int)p.getStat(Stat.DEFENSE)+2);
            p.addEffect(eff);
        }

        Effect eff = new Effect();
        eff.stat(Stat.HP, (int)p.getHP() + (int)p.getHP()/2);

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
        return "Использует SlackOff";
    }
}
