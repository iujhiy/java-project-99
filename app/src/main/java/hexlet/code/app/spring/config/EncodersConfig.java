package hexlet.code.app.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncodersConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        int passwordStrength = 11;
        return new BCryptPasswordEncoder(passwordStrength);
    }
}
