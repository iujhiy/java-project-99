package hexlet.code.app.utils;

import hexlet.code.app.spring.model.TaskStatus;

public class TaskStatusUtilsTest {
    public TaskStatus createTestTaskStatus() {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName("В работе");
        taskStatus.setSlug("in_progress");
        return taskStatus;
    }

    public TaskStatus createAnotherTaskStatus() {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName("Завершено");
        taskStatus.setSlug("completed");
        return taskStatus;
    }
}
