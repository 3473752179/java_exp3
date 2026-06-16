package addressbook.view;

import addressbook.export.Exporter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class AddressBookView extends JFrame {
    private final JTable personTable;
    private final JButton addButton = new JButton("添加人员");
    private final JButton deleteButton = new JButton("删除选中");
    private final JButton exportButton = new JButton("导出文件");
    private final JComboBox<Exporter> exportComboBox = new JComboBox<>();

    public AddressBookView(PersonTableModel tableModel, List<Exporter> exporters) {
        super("地址簿");
        this.personTable = new JTable(tableModel);
        initView(exporters);
    }

    private void initView(List<Exporter> exporters) {
        setLayout(new BorderLayout(10, 10));

        personTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(personTable), BorderLayout.CENTER);

        for (Exporter exporter : exporters) {
            exportComboBox.addItem(exporter);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(exportButton);
        bottomPanel.add(new javax.swing.JLabel("格式:"));
        bottomPanel.add(exportComboBox);

        add(bottomPanel, BorderLayout.SOUTH);

        setSize(700, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addAddListener(java.awt.event.ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addDeleteListener(java.awt.event.ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addExportListener(java.awt.event.ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public int getSelectedRow() {
        return personTable.getSelectedRow();
    }

    public Exporter getSelectedExporter() {
        return (Exporter) exportComboBox.getSelectedItem();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.ERROR_MESSAGE);
    }
}
