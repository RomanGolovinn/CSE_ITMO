package api;

import models.Flat;

import java.io.Serializable;
import java.util.Collection;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private Collection<Flat> collection;

    public Response(boolean success, String message, Collection<Flat> collection) {
        this.success = success;
        this.message = message;
        this.collection = collection;
    }

    public Response(boolean success, String message) {
        this(success, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Collection<Flat> getCollection() { return collection; }
    public void setCollection(Collection<Flat> collection) {
        this.collection = collection;
    }
}
