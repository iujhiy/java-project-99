package hexlet.code.app.utils;

import hexlet.code.app.spring.model.User;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;

public class UserUtilsTest {

    public User createUser() {
        var faker = new Faker();
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .ignore(Select.field(User::getTasks))
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
    }
}
