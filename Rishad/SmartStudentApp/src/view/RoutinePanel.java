package view;

import model.Routine;
import model.Student;
import service.RoutineService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;

public class RoutinePanel extends JPanel {

    private Student student;
    private RoutineService routineService;
    private JTable routineTable;
    private DefaultTableModel tableModel;

    public RoutinePanel(Student student) {
        this.student = student;
        routineService = new RoutineService();
        buildUI();
        loadData();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("My Class Routine");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        add(title, BorderLayout.NORTH);

        // define table columns
        String[] columns = {"ID", "Code", "Course Name", "Day", "Time", "Room"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;  // don't allow direct editing in table
            }
        };

        routineTable = new JTable(tableModel);
        routineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        routineTable.setRowHeight(22);
        routineTable.getColumnModel().getColumn(0).setMaxWidth(40);

        add(new JScrollPane(routineTable), BorderLayout.CENTER);

        // action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton addBtn     = new JButton("Add Class");
        JButton editBtn    = new JButton("Edit");
        JButton deleteBtn  = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> showEditDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Routine> routines = routineService.getRoutines(student.getUserID());
        for (Routine r : routines) {
            tableModel.addRow(new Object[]{
                r.getRoutineID(),
                r.getCourseCode(),
                r.getCourseName(),
                r.getDayOfWeek(),
                r.getStartTime(),
                r.getLocation()
            });
        }
    }

    private void showAddDialog() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Add New Class", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(340, 290);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 7));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField codeField     = new JTextField();
        JTextField nameField     = new JTextField();
        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        JComboBox<String> dayBox = new JComboBox<>(days);
        JTextField timeField     = new JTextField("09:00");
        JTextField roomField     = new JTextField();

        panel.add(new JLabel("Course Code:"));   panel.add(codeField);
        panel.add(new JLabel("Course Name *:")); panel.add(nameField);
        panel.add(new JLabel("Day *:"));         panel.add(dayBox);
        panel.add(new JLabel("Time (HH:MM) *:")); panel.add(timeField);
        panel.add(new JLabel("Room / Location:")); panel.add(roomField);

        JButton saveBtn   = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        panel.add(saveBtn);
        panel.add(cancelBtn);

        dialog.add(panel);

        saveBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String day  = (String) dayBox.getSelectedItem();
            String time = timeField.getText().trim();
            String room = roomField.getText().trim();

            if (name.isEmpty() || time.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Course Name and Time are required.");
                return;
            }

            boolean ok = routineService.addRoutine(code, name, day, time, room, student.getUserID());
            if (ok) {
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Could not save. Check the time format (HH:MM).");
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int row = routineTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class first.");
            return;
        }

        int    id   = (int)    tableModel.getValueAt(row, 0);
        String code = (String) tableModel.getValueAt(row, 1);
        String name = (String) tableModel.getValueAt(row, 2);
        String day  = (String) tableModel.getValueAt(row, 3);
        String time = (String) tableModel.getValueAt(row, 4);
        String room = (String) tableModel.getValueAt(row, 5);

        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Edit Class", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(340, 290);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 7));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField codeField     = new JTextField(code);
        JTextField nameField     = new JTextField(name);
        String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        JComboBox<String> dayBox = new JComboBox<>(days);
        dayBox.setSelectedItem(day);
        JTextField timeField     = new JTextField(time);
        JTextField roomField     = new JTextField(room);

        panel.add(new JLabel("Course Code:"));    panel.add(codeField);
        panel.add(new JLabel("Course Name *:"));  panel.add(nameField);
        panel.add(new JLabel("Day *:"));          panel.add(dayBox);
        panel.add(new JLabel("Time (HH:MM) *:")); panel.add(timeField);
        panel.add(new JLabel("Room / Location:")); panel.add(roomField);

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");
        panel.add(updateBtn);
        panel.add(cancelBtn);

        dialog.add(panel);

        updateBtn.addActionListener(e -> {
            Routine r = new Routine();
            r.setRoutineID(id);
            r.setCourseCode(codeField.getText().trim());
            r.setCourseName(nameField.getText().trim());
            r.setDayOfWeek((String) dayBox.getSelectedItem());
            r.setStartTime(timeField.getText().trim());
            r.setLocation(roomField.getText().trim());

            boolean ok = routineService.updateRoutine(r);
            if (ok) {
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Update failed.");
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = routineTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this class?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            routineService.deleteRoutine(id);
            loadData();
        }
    }
}
