package hexlet.code.app.spring.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TaskStatusUtil {

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return Arrays.stream(input.split("_"))
                .map(word -> {
                    if (word.isEmpty()) {
                        return "";
                    }
                    // Первая буква в верхний регистр, остальные как есть
                    return Character.toUpperCase(word.charAt(0))
                            + word.substring(1).toLowerCase();
                })
                .collect(Collectors.joining());
    }
}
