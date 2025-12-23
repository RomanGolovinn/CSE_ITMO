package exeptions;

public class LostException extends Exception{
    public LostException(String message){
        super(message);
    }

    public String getMessage() {
        return "LostException: нельзя гулять в таких зарослях";
    }
}
