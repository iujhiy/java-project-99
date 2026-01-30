package utils;

import hexlet.code.app.spring.exception.ResourceNotFoundException;

public class ExceptionUtils {
    public static ResourceNotFoundException throwResourceNotFoundException(
            String entityName, long id) {
        throw new ResourceNotFoundException(entityName + " with id '" + id + "' not found");
    }

    public static ResourceNotFoundException throwResourceNotFoundException(
            String entityName, String email) {
        throw new ResourceNotFoundException(entityName + " with email '" + email + "' not found");
    }
}
