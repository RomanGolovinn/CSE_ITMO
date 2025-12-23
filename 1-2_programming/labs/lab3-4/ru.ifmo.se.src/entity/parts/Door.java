package entity.parts;

public class Door {
    boolean DoorIsOpen;

    public Door(boolean dio){
        this.DoorIsOpen = dio;
    }

    public void changeStatus(){
        if (DoorIsOpen){
            System.out.println("Дверь открылась");
        }else{
            System.out.println("Дверь закрылась");
        }
        DoorIsOpen = !DoorIsOpen;
    }
}
