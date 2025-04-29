package edu.tcu.cs.frogcrew.system.exception;

public class ObjectAlreadyExistsException extends RuntimeException {

  public ObjectAlreadyExistsException(String objectName, String parameterName, String argument) {
        super("A " + objectName + " already exist for " + parameterName + " " + argument);
    }
}
