package utils;

import hexlet.code.app.spring.exception.ResourceNotFoundException;

public class ExceptionUtils {
    public static ResourceNotFoundException throwResourceNotFoundException(
            String entityName, long id, String methodName) {
        throw new ResourceNotFoundException(entityName + " with id '" + id + "' not found in '" + methodName + " '");
    }

    public static ResourceNotFoundException throwResourceNotFoundException(
            String entityName, String email, String methodName) {
        throw new ResourceNotFoundException(
                entityName + " with email '" + email + "' not found in '" + methodName + " '"
        );
    }
}
