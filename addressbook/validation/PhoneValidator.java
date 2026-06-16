package addressbook.validation;

import addressbook.model.Person;

public class PhoneValidator implements PersonValidator {
    @Override
    public void validate(Person person) {
        String phone = person.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("电话不能为空。");
        }
        if (!phone.matches("\\d{7,15}")) {
            throw new IllegalArgumentException("电话必须是 7 到 15 位数字。");
        }
    }
}
