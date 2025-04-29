package edu.tcu.cs.frogcrew.system.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find " + objectName + " with id " + id);
    }

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find " + objectName + " with id " + id);
    }

    public ObjectNotFoundException(String objectName, Integer userId, Integer gameId) {
        super("Could not find " + objectName + " for user with id " + userId + " and game with id " + gameId);
    }

    public ObjectNotFoundException(String object1, String object2, Integer id, String object3, String valueOf3) {
        super("Could not find " + object1 + " for " + object2 + " with id " + id + " and " + object3 + valueOf3);
    }
}
