package com.psvm.server.view;

import com.psvm.server.controllers.DBWrapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


class OptionPanelDSLienLacNguoiDung extends JPanel{
    DSLienLacNguoiDungTable table;
    OptionPanelDSLienLacNguoiDung(DSLienLacNguoiDungTable table){
        this.table = table;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        //this.setBorder(new EmptyBorder(10,10,10,10));

        //Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.setOpaque(false);
        //filterPanel.setBackground(Color.RED);
        filterPanel.setBorder(new EmptyBorder(0,0,0,100));
        //filterPanel.setSize(990,180);
        //Filter field
        //name field
        JTextField nameField = new JTextField();
        nameField.setColumns(20);


        //number of friend field
        String[] items = {"Bằng", "Lớn hơn", "Nhỏ hơn"};
        JComboBox<String> dropdown = new JComboBox<>(items);
        JTextField numberOfFriendsField = new JTextField();
        numberOfFriendsField.setColumns(20);

        //Filter button
        JButton filterButton = new JButton("       Lọc       ");
        filterButton.setFocusPainted(false);
        //Filter button logic
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameFilter = nameField.getText();
                String numberOfFriendFilter = numberOfFriendsField.getText();
                String choice = (String) dropdown.getSelectedItem();
                System.out.println(nameFilter);
                table.filterTable(nameFilter,numberOfFriendFilter, choice);
            }
        });
        //Add to filter Panel
        filterPanel.add(new JLabel("Tên: "));
        filterPanel.add(nameField);
        filterPanel.add(dropdown);
        filterPanel.add(numberOfFriendsField);
        filterPanel.add(filterButton);

        //Add to Option Panel
        this.add(filterPanel,BorderLayout.WEST);
    }
}
class DSLienLacNguoiDungTable extends JTable{
    //private MySQLData mySQLData; //Biến để lưu database, coi cách gọi database của tao trong Example/Table
    // Cần dòng này để gọi dữ liệu từ database
    // Khi trả dữ về thì trả với dạng List<Object[]> (ArrayList)
    // Coi trong folder EXAMPLE, MySQLData
    //ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
    private final DefaultTableModel model;
    private final int columnCount;

   // private HashMap<String, Object> userList = new HashMap<>();
    int index = 1;
    DefaultTableModel getTableModel(){
        return model;
    }
    DSLienLacNguoiDungTable(String[] columnNames){
        super(new DefaultTableModel(columnNames,0));
        //Formating the table
        this.model = (DefaultTableModel) this.getModel();
        this.setDefaultEditor(Object.class,null);
        int iconHeight = new ImageIcon("server/src/main/resources/icon/more_vert.png").getIconHeight();
        this.setRowHeight(iconHeight);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.setDefaultRenderer(Object.class, centerRenderer);
        // Enable sorting
        this.setAutoCreateRowSorter(true);
        //Example data
        // CHỖ NÀY ĐỌC KĨ VÀO CHO TAO
        // TẤT cả ngày ở mySQL phải là SQL date, khi gọi hàm ở database, nhớ chuyển nó về LocalDate để add vào data
        //startNextWorker(); // riel data
        model.addRow(new Object[]{0,"hải",2,5});
        model.addRow(new Object[]{1,"khoa",3,4});
        model.addRow(new Object[]{2,"bảo1",4,6});
        model.addRow(new Object[]{3,"bảo2",2,3});
        //formatting table
        setColumnWidthToFitContent();
        //add columnCount for later use
        this.columnCount = this.getColumnCount();

    }

//    protected void startNextWorker() {
//        UserListThread userWorker = new UserListThread(new UserListThread.Observer() {
//            @Override
//            public void workerDidUpdate(HashMap<String, Object> userInfo) {
//                if (!userList.equals(userInfo)) {
//                    List<Object[]> userAccountData = new ArrayList<>();
//                    userInfo.forEach((userId, detail) -> {
//                        Object[] acc = new Object[8];
//                        acc[0] = userId; // Assign row
//                        HashMap<String, Object> castedDetail = (HashMap<String, Object>) detail;
//                        // Loop Through to get value
//                        castedDetail.forEach((field, value) -> {
//                            Object obj = value;
//                            if (obj instanceof LocalDate) {
//                                LocalDate dateValue = (LocalDate) obj;
//                                acc[getIndex(field)] = dateValue;
//                            }
//                            else if (obj instanceof String) {
//                                String stringValue = (String) obj;
//                                acc[getIndex(field)] = stringValue;
//                            } else {
//                                String gender = (Boolean) obj ? "Female" : "Male";
//                                acc[getIndex(field)] = gender;
//                            }
//                        });
//                        userAccountData.add(acc);
//                    });
//
//                    resetModelRow(); // Reset new Data
//                    index = 1;
//                    for (Object[] row: userAccountData){
//                        Object[] newRow = new Object[row.length + 1];
//                        newRow[0] = index++;
//                        System.arraycopy(row,0,newRow,1,row.length);
//                        model.addRow(newRow);
//                    }
//                    userList = userInfo;
//                }
//            }
//        });
//        userWorker.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (userWorker.getState() == SwingWorker.StateValue.DONE) {
//                    userWorker.removePropertyChangeListener(this);
//                    startNextWorker();
//                }
//
//            }
//        });
//        service.schedule(userWorker, 1000, TimeUnit.MILLISECONDS);
//    }
void filterTable(String name, String numberOfFriend, String choice) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            if (!name.isEmpty()) {
                filters.add(RowFilter.regexFilter(name, 1));
            }

            if (!numberOfFriend.isEmpty()) {
                try {
                    int friendValue = Integer.parseInt(numberOfFriend);
                    int columnIndexOfFriend = 2;

                    switch (choice) {
                        case "Bằng":
                            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, friendValue, columnIndexOfFriend));
                            break;
                        case "Lớn hơn":
                            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, friendValue, columnIndexOfFriend));
                            break;
                        case "Nhỏ hơn":
                            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, friendValue, columnIndexOfFriend));
                            break;
                        default:
                            break;
                    }
                } catch (NumberFormatException e) {
                  e.printStackTrace();
                }
            }

            if (!filters.isEmpty()) {
                RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
                sorter.setRowFilter(combinedFilter);
            } else {
                sorter.setRowFilter(null);
            }

            setRowSorter(sorter);
        }
    });
}



    void refreshTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                List<Object[]> updatedData = mySQLData.getAllStudent();
//                model.setRowCount(0);
//                for (Object[] row : updatedData) {
//                    model.addRow(row);
//                }
            }
        });
    }
    void resetModelRow() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    private void setColumnWidthToFitContent() {
        for (int column = 0; column < this.getColumnCount(); column++) {
            int maxwidth = 0;
            for (int row = 0; row < this.getRowCount(); row++) {
                TableCellRenderer renderer = this.getCellRenderer(row, column);
                Component comp = prepareRenderer(renderer, row, column);
                maxwidth = Math.max(comp.getPreferredSize().width, maxwidth);
            }
            TableColumn tableColumn = getColumnModel().getColumn(column);
            tableColumn.setPreferredWidth(maxwidth);
        }
    }
    private void updateIndexColumn() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.setValueAt(i + 1, i, 0); // Update the index column
        }
        index = getRowCount() + 1;
    }

//    private int getIndex(String value) {
//        switch (value) {
//            case "fullName":
//                return 1;
//
//            case "addrs":
//                return 2;
//
//            case "dob":
//                return 3;
//
//            case "gender":
//                return 4;
//
//            case "email":
//                return 5;
//
//            case "creationDate":
//                return 6;
//            default:
//                return -1;
//        }
//    }
//


}
