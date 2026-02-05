package hexlet.code.app.utils;

import hexlet.code.app.spring.model.Label;
import hexlet.code.app.spring.model.Task;
import hexlet.code.app.spring.model.TaskStatus;
import hexlet.code.app.spring.model.User;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;

public class TestUtils {

    private static final Faker FAKER = new Faker();

    public static Task createTestTask(TaskStatus taskStatus) {
        var task = new Task();
        task.setTitle(FAKER.name().title());
        taskStatus.addTask(task);
        return task;
    }

    public static TaskStatus createTestTaskStatus() {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName("В работе");
        taskStatus.setSlug("in_progress");
        return taskStatus;
    }

    public static TaskStatus createAnotherTaskStatus() {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName("Завершено");
        taskStatus.setSlug("completed");
        return taskStatus;
    }

    public static User createUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .ignore(Select.field(User::getTasks))
                .supply(Select.field(User::getPasswordDigest), () -> FAKER.internet().password())
                .supply(Select.field(User::getFirstName), () -> FAKER.name().firstName())
                .supply(Select.field(User::getLastName), () -> FAKER.name().lastName())
                .supply(Select.field(User::getEmail), () -> FAKER.internet().emailAddress())
                .create();
    }

    public static Label createTestLabel() {
        return Instancio.of(Label.class)
                .ignore(Select.field(Label::getTasks))
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getCreatedAt))
                .supply(Select.field(Label::getName), () -> FAKER.name().title())
                .create();
    }
}
