package hexlet.code.spring.component;

import hexlet.code.spring.model.Label;
import hexlet.code.spring.model.TaskStatus;
import hexlet.code.spring.model.User;
import hexlet.code.spring.repository.LabelRepository;
import hexlet.code.spring.repository.TaskStatusRepository;
import hexlet.code.spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import static hexlet.code.spring.util.TaskStatusUtil.toCamelCase;


@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        var user = new User();
        var password = "qwerty";
        var passwordDigest = passwordEncoder.encode(password);
        user.setPasswordDigest(passwordDigest);
        user.setEmail("hexlet@example.com");
        userRepository.save(user);

        var taskStatusesSlugSet = Set.of("draft", "to_review", "to_be_fixed", "to_publish", "published");
        for (var slug: taskStatusesSlugSet) {
            var taskStatus = new TaskStatus();
            var name = toCamelCase(slug);
            taskStatus.setSlug(slug);
            taskStatus.setName(name);
            taskStatusRepository.save(taskStatus);
        }

        var labelsNameSet = Set.of("feature", "bug");
        for (var labelName : labelsNameSet) {
            var label = new Label();
            label.setName(labelName);
            labelRepository.save(label);
        }
    }
}
