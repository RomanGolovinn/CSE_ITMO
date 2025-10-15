package moves.physicalmoves;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Scratch extends PhysicalMove {
    public Scratch(){
        super(Type.NORMAL, 40, 100);
    }

    @Override
    public void attack(Pokemon att, Pokemon def){
        Type[] types = def.getTypes();
        boolean isFire = false;
        for(Type t: types) {
            if(t==Type.FIRE){
                isFire = true;
                break;
            }
        }
        if (isFire){

        }
    }

    @Override
    protected String describe(){
        return "Использует Scratch";
    }
}
