package hexlet.code.app.utils;

import hexlet.code.app.spring.model.Task;
import hexlet.code.app.spring.model.TaskStatus;
import net.datafaker.Faker;

public class TaskUtilsTest {

    public Task createTestTask(TaskStatus taskStatus) {
        var faker = new Faker();
        var task = new Task();
        task.setTitle(faker.food().fruit());
        taskStatus.addTask(task);
        return task;
    }
}
