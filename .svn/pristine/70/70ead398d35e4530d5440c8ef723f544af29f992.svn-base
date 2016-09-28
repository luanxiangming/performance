package net.unit8.jmeter.protocol.socket_io.control.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

import net.unit8.jmeter.protocol.socket_io.control.gui.SocketIOSamplerGui.UserTableModel;
import net.unit8.jmeter.protocol.socket_io.sampler.SocketIOSampler;
import net.unit8.jmeter.protocol.socket_io.util.SocketIOUserInfo;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.GuiUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class SocketIOUserTablePanel extends JPanel implements ActionListener {
    
    private class HeaderRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 240L;

        public HeaderRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null){
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
                setText(value.toString());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                setHorizontalAlignment(SwingConstants.CENTER);
            }
            return this;
        }
    }

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggingManager.getLoggerForClass();

    /** The title label for this component. */
    private JLabel tableLabel;

    /** The table containing the list of users. */
    private transient JTable table;

    /** The model for the users table. */
    private transient UserTableModel tableModel;

    /** A button for adding new users to the table. */
    private JButton add;

    /** A button for removing users from the table. */
    private JButton delete;

    /** Command for adding a row to the table. */
    private static final String ADD = "add"; // $NON-NLS-1$

    /** Command for removing a row from the table. */
    private static final String DELETE = "delete"; // $NON-NLS-1$


    public SocketIOUserTablePanel(UserTableModel tableModel) {
        this("", tableModel);
    }
    
    public SocketIOUserTablePanel(String label, UserTableModel tableModel) {
        tableLabel = new JLabel(label);
        setTableModel(tableModel);
        init();
    }

    public void setTableModel(UserTableModel tableModel) {
        this.tableModel = tableModel;
    }

    /**
     * Save the GUI data in the SocketIOSampler element.
     *
     * @param testElement
     */
    public void modifyTestElement(TestElement testElement) {
        GuiUtils.stopTableEditing(table);
        if (testElement instanceof SocketIOSampler) {
            SocketIOSampler base = (SocketIOSampler) testElement;
            int rows = tableModel.getRowCount();
            @SuppressWarnings("unchecked")
            Iterator<SocketIOUserInfo> modelData = (Iterator<SocketIOUserInfo>) tableModel.iterator();
            SocketIOUserInfo[] users = new SocketIOUserInfo[rows];
            int row=0;
            while (modelData.hasNext()) {
                SocketIOUserInfo user = modelData.next();
                users[row++] = user;
            }
            base.setUserInfos(users);
        }
    }

    /**
     * A newly created component can be initialized with the contents of a
     * SocketIOSampler object by calling this method. The component is responsible for
     * querying the Test Element object for the relevant information to display
     * in its GUI.
     *
     * @param testElement the SocketIOSampler to be used to configure the GUI
     */
    public void configure(TestElement testElement) {
        if (testElement instanceof SocketIOSampler) {
            SocketIOSampler base = (SocketIOSampler) testElement;
            tableModel.clearData();
            for(SocketIOUserInfo user: base.getUserInfos()){
                tableModel.addRow(user);
            }
            checkDeleteStatus();
        }
    }


    /**
     * Enable or disable the delete button depending on whether or not there is
     * a row to be deleted.
     */
    private void checkDeleteStatus() {
        // Disable DELETE and BROWSE buttons if there are no rows in
        // the table to delete.
        if (tableModel.getRowCount() == 0) {
            delete.setEnabled(false);
        } else {
            delete.setEnabled(true);
        }
    }

    /**
     * Clear all rows from the table.
     */
    public void clear() {
        GuiUtils.stopTableEditing(table);
        tableModel.clearData();
    }

    /**
     * Invoked when an action occurs. This implementation supports the add and
     * delete buttons.
     *
     * @param e
     *  the event that has occurred
     */
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals(ADD)) {
            addUser();
        }
        runCommandOnSelectedFile(action);
    }
    
    public int getRowCount() {
        return table.getRowCount();
    }

    /**
     * runs specified command on currently selected file.
     *
     * @param command specifies which process will be done on selected
     * file. it's coming from action command currently catched by
     * action listener.
     *
     * @see runCommandOnRow
     */
    private void runCommandOnSelectedFile(String command) {
        // If a table cell is being edited, we must cancel the editing before
        // deleting the row
        if (table.isEditing()) {
            TableCellEditor cellEditor = table.getCellEditor(table.getEditingRow(), table.getEditingColumn());
            cellEditor.cancelCellEditing();
        }
        int rowSelected = table.getSelectedRow();
        if (rowSelected >= 0) {
            runCommandOnRow(command, rowSelected);
            tableModel.fireTableDataChanged();
            // Disable DELETE and BROWSE if there are no rows in the table to delete.
            checkDeleteStatus();
            // Table still contains one or more rows, so highlight (select)
            // the appropriate one.
            if (tableModel.getRowCount() != 0) {
                int rowToSelect = rowSelected;
                if (rowSelected >= tableModel.getRowCount()) {
                    rowToSelect = rowSelected - 1;
                }
                table.setRowSelectionInterval(rowToSelect, rowToSelect);
            }
        }
    }

    /**
     * runs specified command on currently selected table row.
     *
     * @param command specifies which process will be done on selected
     * file. it's coming from action command currently catched by
     * action listener.
     *
     * @param rowSelected index of selected row.
     */
    private void runCommandOnRow(String command, int rowSelected) {
        if (DELETE.equals(command)) {
            tableModel.removeRow(rowSelected);
        }
    }

    /**
     * Add a new user row to the table.
     */
    private void addUser() {
        // If a table cell is being edited, we should accept the current value
        // and stop the editing before adding a new row.
        GuiUtils.stopTableEditing(table);

        tableModel.addRow(new SocketIOUserInfo());

        // Enable DELETE (which may already be enabled, but it won't hurt)
        delete.setEnabled(true);

        // Highlight (select) the appropriate row.
        int rowToSelect = tableModel.getRowCount() - 1;
        table.setRowSelectionInterval(rowToSelect, rowToSelect);
    }


    /**
     * Stop any editing that is currently being done on the table. This will
     * save any changes that have already been made.
     */
    protected void stopTableEditing() {
        GuiUtils.stopTableEditing(table);
    }
    
    /**
     * Create the main GUI panel which contains the file table.
     *
     * @return the main GUI panel
     */
    private Component makeMainPanel() {
//        initializeTableModel();
        table = new JTable(tableModel);
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return makeScrollPane(table);
    }

    /**
     * Create a panel containing the title label for the table.
     *
     * @return a panel containing the title label
     */
    private Component makeLabelPanel() {
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(tableLabel);
        return labelPanel;
    }

    /**
     * Create a panel containing the add and delete buttons.
     *
     * @return a GUI panel containing the buttons
     */
    private JPanel makeButtonPanel() {
        add = new JButton(JMeterUtils.getResString("add")); // $NON-NLS-1$
        add.setActionCommand(ADD);
        add.setEnabled(true);

        delete = new JButton(JMeterUtils.getResString("delete")); // $NON-NLS-1$
        delete.setActionCommand(DELETE);

        checkDeleteStatus();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add.addActionListener(this);
        delete.addActionListener(this);
        buttonPanel.add(add);
        buttonPanel.add(delete);
        return buttonPanel;
    }

    /**
     * Initialize the components and layout of this component.
     */
    private void init() {
        JPanel p = this;

        p.setLayout(new BorderLayout());

        p.add(makeLabelPanel(), BorderLayout.NORTH);
        p.add(makeMainPanel(), BorderLayout.CENTER);
        // Force a minimum table height of 70 pixels
        p.add(Box.createVerticalStrut(70), BorderLayout.WEST);
        p.add(makeButtonPanel(), BorderLayout.SOUTH);

        table.revalidate();
//        sizeColumns(table);
    }

    private JScrollPane makeScrollPane(Component comp) {
        JScrollPane pane = new JScrollPane(comp);
        Dimension dimension = pane.getMinimumSize();
        dimension.setSize(dimension.width, dimension.height * 8);
        pane.setPreferredSize(dimension);
        return pane;
    }
}
