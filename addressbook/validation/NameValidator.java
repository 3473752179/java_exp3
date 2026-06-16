package addressbook.validation;

import addressbook.model.Person;

public class NameValidator implements PersonValidator {
    @Override
    public void validate(Person person) {
        String name = person.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空。");
        }
        if (name.trim().length() > 20) {
            throw new IllegalArgumentException("姓名长度不能超过 20 个字符。");
        }
    }
}
