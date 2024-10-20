package view;
import controller.NADRADB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class NADRAGUI extends JFrame {
    private ArrayList<NADRADB> nadraInfo;
    private JTable nadraTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public NADRAGUI(ArrayList<NADRADB> nadraInfo) {
        this.nadraInfo = nadraInfo;
        init();
    }

    private void init() {
        setTitle("NADRA Information");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel for the search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setToolTipText("Search by CNIC or Date");
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Set up the table model
        String[] columnNames = {"CNIC", "Issue Date", "Expiry Date", "Update"};
        tableModel = new DefaultTableModel(columnNames, 0);
        nadraTable = new JTable(tableModel);

        // Add custom cell editors and renderers for the buttons
        nadraTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        nadraTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), nadraInfo));

        populateTable();

        // Add TableRowSorter to enable row filtering
        rowSorter = new TableRowSorter<>(tableModel);
        nadraTable.setRowSorter(rowSorter);

        // Add search functionality to the searchField
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String searchText = searchField.getText().trim();
                if (searchText.length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }
        });

        // Add the search panel and the table to the frame
        add(searchPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(nadraTable);
        add(scrollPane, BorderLayout.CENTER);

        // Finalize frame setup
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (NADRADB nadra : nadraInfo) {
            Object[] rowData = {
                nadra.getCNIC(),
                nadra.getIssueDate(),
                nadra.getExpiryDate(),
                "Update" // Placeholder for button text
            };
            tableModel.addRow(rowData);
        }
    }

    // Renderer for button cells
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor for button cells
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;
        private ArrayList<NADRADB> nadraList;

        public ButtonEditor(JCheckBox checkBox, ArrayList<NADRADB> nadraList) {
            super(checkBox);
            this.nadraList = nadraList;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    NADRADB nadra = nadraList.get(row);
                    updateNadra(nadra);
                }
            });
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        private void updateNadra(NADRADB nadra) {
            new UpdateNADRAGUI(nadra,() ->populateTable());
            
        }
    }
}
