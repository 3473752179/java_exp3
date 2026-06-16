package addressbook.view;

import addressbook.model.AddressBookModel;
import addressbook.model.Person;
import javax.swing.table.AbstractTableModel;

public class PersonTableModel extends AbstractTableModel {
    private final String[] columns = {"姓名", "电话", "邮箱"};
    private final AddressBookModel model;

    public PersonTableModel(AddressBookModel model) {
        this.model = model;
    }

    @Override
    public int getRowCount() {
        return model.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person person = model.getPerson(rowIndex);
        switch (columnIndex) {
            case 0:
                return person.getName();
            case 1:
                return person.getPhone();
            case 2:
                return person.getEmail();
            default:
                return "";
        }
    }

    public void refresh() {
        fireTableDataChanged();
    }
}
