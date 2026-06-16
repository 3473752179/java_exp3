package addressbook.validation;

import addressbook.model.Person;

public class EmailValidator implements PersonValidator {
    @Override
    public void validate(Person person) {
        String email = person.getEmail();
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空。");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("邮箱格式不正确。");
        }
    }
}
