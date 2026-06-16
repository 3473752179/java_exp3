package addressbook.controller;

import addressbook.export.Exporter;
import addressbook.model.AddressBookModel;
import addressbook.model.Person;
import addressbook.validation.PersonValidator;
import addressbook.view.AddressBookView;
import addressbook.view.PersonDialog;
import addressbook.view.PersonTableModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;

public class AddressBookController {
    private final AddressBookModel model;
    private final PersonTableModel tableModel;
    private final AddressBookView view;
    private final PersonDialog personDialog;
    private final List<PersonValidator> validators;

    public AddressBookController(
            AddressBookModel model,
            PersonTableModel tableModel,
            AddressBookView view,
            PersonDialog personDialog,
            List<PersonValidator> validators
    ) {
        this.model = model;
        this.tableModel = tableModel;
        this.view = view;
        this.personDialog = personDialog;
        this.validators = validators;
        bindActions();
    }

    private void bindActions() {
        view.addAddListener(e -> addPerson());
        view.addDeleteListener(e -> deletePerson());
        view.addExportListener(e -> exportPersons());
    }

    private void addPerson() {
        Person person = personDialog.showDialog();
        if (person == null) {
            return;
        }

        try {
            validate(person);
            model.addPerson(person);
            tableModel.refresh();
        } catch (IllegalArgumentException ex) {
            view.showError(ex.getMessage());
        }
    }

    private void deletePerson() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow < 0) {
            view.showError("请先选择要删除的人员。");
            return;
        }
        model.removePerson(selectedRow);
        tableModel.refresh();
    }

    private void exportPersons() {
        Exporter exporter = view.getSelectedExporter();
        if (exporter == null) {
            view.showError("请选择导出格式。");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("导出 " + exporter.getFormatName());
        chooser.setSelectedFile(new File("address-book" + exporter.getSuggestedExtension()));

        int result = chooser.showSaveDialog(view);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(exporter.getSuggestedExtension())) {
            file = new File(file.getAbsolutePath() + exporter.getSuggestedExtension());
        }

        try {
            exporter.export(model.getPersons(), file);
            view.showMessage("导出成功: " + file.getAbsolutePath());
        } catch (IOException ex) {
            view.showError("导出失败: " + ex.getMessage());
        }
    }

    private void validate(Person person) {
        for (PersonValidator validator : validators) {
            validator.validate(person);
        }
    }
}
