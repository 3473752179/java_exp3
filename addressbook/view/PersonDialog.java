package addressbook.view;

import addressbook.model.Person;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PersonDialog extends JDialog {
    private final JTextField nameField = new JTextField(18);
    private final JTextField phoneField = new JTextField(18);
    private final JTextField emailField = new JTextField(18);
    private boolean confirmed;

    public PersonDialog(Frame owner) {
        super(owner, "添加人员", true);
        initView();
    }

    private void initView() {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        formPanel.add(new JLabel("姓名:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("电话:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("邮箱:"));
        formPanel.add(emailField);

        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        okButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });
        cancelButton.addActionListener(e -> setVisible(false));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
    }

    public Person showDialog() {
        confirmed = false;
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        setVisible(true);
        if (!confirmed) {
            return null;
        }
        return new Person(
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim()
        );
    }
}
