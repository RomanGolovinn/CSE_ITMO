package io.auth;

public class UserContext {
    private static final ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

    public static void setId(int id) {
        currentUserId.set(id);
    }

    public static int getId() {
        if (currentUserId.get() == null) {
            return -1;
        }
        return currentUserId.get();
    }

    public static void clear() {
        currentUserId.remove();
    }
}