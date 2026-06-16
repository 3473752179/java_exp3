package addressbook;

import addressbook.controller.AddressBookController;
import addressbook.export.CsvExporter;
import addressbook.export.Exporter;
import addressbook.export.JsonExporter;
import addressbook.model.AddressBookModel;
import addressbook.validation.EmailValidator;
import addressbook.validation.NameValidator;
import addressbook.validation.PersonValidator;
import addressbook.validation.PhoneValidator;
import addressbook.view.AddressBookView;
import addressbook.view.PersonDialog;
import addressbook.view.PersonTableModel;
import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;

public class AddressBookApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddressBookModel model = new AddressBookModel();
            PersonTableModel tableModel = new PersonTableModel(model);

            List<Exporter> exporters = Arrays.<Exporter>asList(new CsvExporter(), new JsonExporter());
            List<PersonValidator> validators = Arrays.<PersonValidator>asList(
                    new NameValidator(),
                    new PhoneValidator(),
                    new EmailValidator()
            );

            AddressBookView view = new AddressBookView(tableModel, exporters);
            PersonDialog dialog = new PersonDialog(view);

            new AddressBookController(model, tableModel, view, dialog, validators);
            view.setVisible(true);
        });
    }
}
