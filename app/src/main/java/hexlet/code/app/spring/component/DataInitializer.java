package hexlet.code.app.spring.component;

import hexlet.code.app.spring.model.User;
import hexlet.code.app.spring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

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
    }
}
