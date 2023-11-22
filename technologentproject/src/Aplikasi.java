package src;

import java.awt.Insets;
import javax.imageio.ImageIO;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Aplikasi extends JFrame {

    static String id_login = "";

    public Aplikasi() {
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(loginorsignup());
        setVisible(true);
    }

    public JPanel loginorsignup() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        loginPanel.add(new JLabel("Username: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        JTextField usernameField = new JTextField(20);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        loginPanel.add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        JPasswordField passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    if (username.equals("ryoaditarta") && password.equals("123")) {
                        setContentPane(adminmenu());
                        setVisible(true);
                        return;
                    }

                    String query = "SELECT * FROM pelanggan WHERE nama_pelanggan = ? AND password = ?";
                    PreparedStatement preparedStatement = sqlQuery(query);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        query = "SELECT id_pelanggan FROM pelanggan WHERE nama_pelanggan = ? AND password = ?";
                        preparedStatement = sqlQuery(query);
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);
                        ResultSet resultSet2 = preparedStatement.executeQuery();
                        id_login = (resultSet2.next()) ? resultSet2.getString("id_pelanggan") : "";
                        setContentPane(usermenu());
                        setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Username atau password salah!");
                    }

                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error: " + e1);
                }
            }
        });
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        JButton signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    String query = "INSERT INTO pelanggan (id_pelanggan, nama_pelanggan, password) VALUES (?, ?, ?)";
                    PreparedStatement preparedStatement = sqlQuery(query);
                    String id = UUID.randomUUID().toString() + " - " + username.toUpperCase();
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, username);
                    preparedStatement.setString(3, password);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Signup berhasil!");
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error: " + e1);
                }
            }
        });
        loginPanel.add(signupButton, gbc);

        return loginPanel;
    }

    JPanel adminmenu() {
        JPanel menuAdmin = new JPanel();
        menuAdmin.setLayout(new BoxLayout(menuAdmin, BoxLayout.PAGE_AXIS)); // BoxLayout to arrange components
                                                                            // vertically
        menuAdmin.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Add padding

        // Adding the image
        ImageIcon originalIcon = new ImageIcon("./technologentlogo.png");

        // Get the original image
        Image originalImage = originalIcon.getImage();

        // Define the new size (width and height)
        int newWidth = 250; // Set your desired width
        int newHeight = 250; // Set your desired height

        // Create a scaled version of the image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the logo
        menuAdmin.add(logoLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button panel

        // Adding buttons
        JButton lihatBarang = new JButton("Tambah Barang");
        JButton hapusBarang = new JButton("Hapus Barang");
        JButton pemesanan = new JButton("Lihat Nota");
        JButton lihat_pemesanan = new JButton("Daftar Pelanggan");

        // Adding action listeners to the buttons (you can customize these)
        lihatBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(lihatBarangAdminPanel());
                setVisible(true);

            }
        });

        hapusBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setContentPane(hapusBarangAdminPanel());
                setVisible(true);

            }
        });

        pemesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(lihatNotaPanel());
                setVisible(true);
            }
        });

        lihat_pemesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(lihatPenggunaPanel());
                setVisible(true);
            }
        });

        buttonPanel.add(lihatBarang);
        buttonPanel.add(hapusBarang);
        buttonPanel.add(pemesanan);
        buttonPanel.add(lihat_pemesanan);

        // Adding components to the menuAdmin panel
        menuAdmin.add(Box.createVerticalStrut(20)); // Add some vertical space between text and buttons
        menuAdmin.add(buttonPanel);

        return menuAdmin;
    }

    public JPanel usermenu() {
        JPanel menuUser = new JPanel();
        menuUser.setLayout(new BoxLayout(menuUser, BoxLayout.PAGE_AXIS)); // BoxLayout to arrange components vertically
        menuUser.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Add padding

        // Adding the image
        ImageIcon originalIcon = new ImageIcon("./technologentlogo.png");

        // Get the original image
        Image originalImage = originalIcon.getImage();

        // Define the new size (width and height)
        int newWidth = 250; // Set your desired width
        int newHeight = 250; // Set your desired height

        // Create a scaled version of the image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the logo
        menuUser.add(logoLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button panel

        // Adding buttons
        JButton lihatBarang = new JButton("Lihat Barang");
        JButton pemesanan = new JButton("Memesan Barang");
        JButton lihat_pemesanan = new JButton("Lihat Pemesanan");
        JButton lihatDataDiri = new JButton("Data Diri");

        // Adding action listeners to the buttons (you can customize these)
        lihatBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(lihatBarangPanel());
                setVisible(true);
            }
        });

        pemesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(pesanBarang());
                setVisible(true);
            }
        });

        lihat_pemesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Lihat Pemesanan button clicked!");
            }
        });

        lihatDataDiri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(lihatDataDiriPanel());
                setVisible(true);
            }
        });

        buttonPanel.add(lihatBarang);
        buttonPanel.add(pemesanan);
        buttonPanel.add(lihat_pemesanan);
        buttonPanel.add(lihatDataDiri);

        // Adding components to the menuUser panel
        menuUser.add(Box.createVerticalStrut(20)); // Add some vertical space between text and buttons
        menuUser.add(buttonPanel);

        return menuUser;
    }

    public JPanel lihatBarangPanel() {
        JPanel panelLihatBarang = new JPanel(new BorderLayout());
        panelLihatBarang.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panelLihatBarang.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query and populate the table
        try {
            // Print the SQL query for debugging
            // System.out.println("SQL Query: SELECT kd_barang, gudang, nama_barang,
            // harga_satuan FROM barang");

            // Execute the query
            PreparedStatement p = sqlQuery("SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("kd_barang"),
                        resultSet.getString("gudang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("harga_satuan")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(usermenu());
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);

        panelLihatBarang.add(buttonPanel, BorderLayout.SOUTH);

        return panelLihatBarang;
    }

    public JPanel pesanBarang() {
        ArrayList<String> barang = new ArrayList<>();
        JPanel panelPesanBarang = new JPanel(new BorderLayout());
        panelPesanBarang.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panelPesanBarang.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query and populate the table
        try {
            // Print the SQL query for debugging
            System.out.println("SQL Query: SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");

            // Execute the query
            PreparedStatement p = sqlQuery("SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("kd_barang"),
                        resultSet.getString("gudang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("harga_satuan")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Add a ListSelectionListener to the table's selection model
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // To avoid multiple events
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedKodeBarang = (String) table.getValueAt(selectedRow, 0);
                    System.out.println("Selected Kode Barang: " + selectedKodeBarang);
                    barang.add(selectedKodeBarang);
                }
            }
        });

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(usermenu());
            setVisible(true);
        });

        JButton pesan = new JButton("Pesan");
        pesan.addActionListener(e -> {
            if (barang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select products before confirming the order.",
                        "Empty Order", JOptionPane.WARNING_MESSAGE);
                return;
            }
            setContentPane(konfirmasiPesanan(barang));
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);
        buttonPanel.add(pesan);

        panelPesanBarang.add(buttonPanel, BorderLayout.SOUTH);

        return panelPesanBarang;
    }

    public JPanel pesanBarang(ArrayList<String> br) {
        ArrayList<String> barang = br;
        JPanel panelPesanBarang = new JPanel(new BorderLayout());
        panelPesanBarang.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panelPesanBarang.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query and populate the table
        try {
            // Print the SQL query for debugging
            System.out.println("SQL Query: SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");

            // Execute the query
            PreparedStatement p = sqlQuery("SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("kd_barang"),
                        resultSet.getString("gudang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("harga_satuan")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Add a ListSelectionListener to the table's selection model
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // To avoid multiple events
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedKodeBarang = (String) table.getValueAt(selectedRow, 0);
                    System.out.println("Selected Kode Barang: " + selectedKodeBarang);
                    barang.add(selectedKodeBarang);
                }
            }
        });

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(usermenu());
            setVisible(true);
        });

        JButton pesan = new JButton("Pesan");
        pesan.addActionListener(e -> {
            if (barang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select products before confirming the order.",
                        "Empty Order", JOptionPane.WARNING_MESSAGE);
                return; // Return null or handle accordingly based on your requirement
            }
            setContentPane(konfirmasiPesanan(barang));
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);
        buttonPanel.add(pesan);

        panelPesanBarang.add(buttonPanel, BorderLayout.SOUTH);

        return panelPesanBarang;
    }

    public JPanel konfirmasiPesanan(ArrayList<String> barang) {
        JPanel konfirmasiPanel = new JPanel(new BorderLayout());
        konfirmasiPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the order details
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        konfirmasiPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query for each selected kd_barang and populate the table
        int count = 0;
        for (String kd_barang : barang) {
            try {
                // Print the SQL query for debugging
                System.out.println(
                        "SQL Query: SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang WHERE kd_barang = "
                                + kd_barang);

                // Execute the query
                PreparedStatement p = sqlQuery(
                        "SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang WHERE kd_barang = ?");
                p.setString(1, kd_barang);
                ResultSet resultSet = p.executeQuery();

                // Iterate through the result set and add data to the table model
                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getString("kd_barang"),
                            resultSet.getString("gudang"),
                            resultSet.getString("nama_barang"),
                            resultSet.getString("harga_satuan")
                    };
                    count += Integer.parseInt(resultSet.getString("harga_satuan"));
                    model.addRow(rowData);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Object[] total = { "", "", "Total", count };

        model.addRow(total);

        table.setModel(model);

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(pesanBarang(barang));
                setVisible(true);
            }
        });

        // OK button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Order created, waiting for confirmation", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                String no_nota = UUID.randomUUID().toString();
                String met_pembayaran = "PRED - Cash";
                String tgl_pembelian = "01/04/2023";
                int garansi_bulan = 6;
                int garansi_hari = 15;
                String nama_pegawai = "Ryo";

                String query = "INSERT INTO nota (no_nota, met_pembayaran, tgl_pembelian, garansi_bulan, garansi_hari, nama_pegawai) VALUES(?,?,?,?,?,?)";
                PreparedStatement p = sqlQuery(query);
                try {
                    p.setString(1, no_nota);
                    p.setString(2, met_pembayaran);
                    p.setString(3, tgl_pembelian);
                    p.setInt(4, garansi_bulan);
                    p.setInt(5, garansi_hari);
                    p.setString(6, nama_pegawai);
                    p.executeUpdate();

                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                for (String x : barang) {

                    query = "INSERT INTO rincian_transaksi (no_nota, kd_barang, jml_barang, harga) VALUES(?,?,?,?)";
                    p = sqlQuery(query);

                    try {
                        p.setString(1, no_nota);
                        p.setString(2, x);
                        int count = 0;
                        for (String k : barang)
                            if (k.equals(x))
                                count++;
                        p.setInt(3, count);

                        String query2 = "SELECT harga_satuan FROM barang WHERE kd_barang = ?";
                        PreparedStatement p2 = sqlQuery(query2);
                        p2.setString(1, x);

                        try (ResultSet rs = p2.executeQuery()) {
                            if (rs.next()) {
                                int harga = rs.getInt("harga_satuan") * count;
                                p.setInt(4, harga);
                            } else {
                                System.out.println("no data");
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    } catch (SQLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }

                setContentPane(usermenu());
                setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);
        buttonPanel.add(okButton);

        konfirmasiPanel.add(buttonPanel, BorderLayout.SOUTH);

        return konfirmasiPanel;
    }

    JPanel lihatDataDiriPanel() {
        JPanel datadiriPanel = new JPanel(new GridBagLayout());
        datadiriPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        String query = "SELECT * FROM pelanggan WHERE id_pelanggan = ?";
        PreparedStatement p = sqlQuery(query);

        try {
            p.setString(1, id_login);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id_pelanggan");
                String nama = rs.getString("nama_pelanggan");
                String no_telp = rs.getString("no_telp");

                // Create labels to display the data
                JLabel idLabel = new JLabel("ID Pelanggan: " + id);
                JLabel namaLabel = new JLabel("Nama Pelanggan: " + nama);
                JLabel noTelpLabel = new JLabel("Nomor Telepon: " + no_telp);

                constraints.gridx = 0;
                constraints.gridy = 0;
                datadiriPanel.add(idLabel, constraints);

                constraints.gridx = 0;
                constraints.gridy = 1;
                datadiriPanel.add(namaLabel, constraints);

                constraints.gridx = 0;
                constraints.gridy = 2;
                datadiriPanel.add(noTelpLabel, constraints);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add "Back" button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(usermenu());
                setVisible(true);
            }
        });

        JTextField[] telp = { new JTextField(15) };

        // Add "Update" button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = telp[0].getText();

                String query = "UPDATE pelanggan SET no_telp = ? WHERE id_pelanggan = ?";
                PreparedStatement p = sqlQuery(query);
                try {
                    p.setString(1, phone);
                    p.setString(2, id_login);
                    p.executeUpdate();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                setContentPane(usermenu());
                setVisible(true);

            }
        });

        // Add text to inform the user
        JLabel textLabel = new JLabel("Ini data diri anda!");

        // Add "Back" button
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;
        datadiriPanel.add(backButton, constraints);

        // Add "Update" button
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.EAST;
        datadiriPanel.add(updateButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.EAST;
        datadiriPanel.add(telp[0], constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        datadiriPanel.add(textLabel, constraints);

        return datadiriPanel;
    }

    JPanel lihatBarangAdminPanel() {
        JPanel panelLihatBarang = new JPanel(new BorderLayout());
        panelLihatBarang.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panelLihatBarang.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query and populate the table
        try {
            // Print the SQL query for debugging
            // System.out.println("SQL Query: SELECT kd_barang, gudang, nama_barang,
            // harga_satuan FROM barang");

            // Execute the query
            PreparedStatement p = sqlQuery("SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("kd_barang"),
                        resultSet.getString("gudang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("harga_satuan")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(adminmenu());
            setVisible(true);
        });

        JButton tambah = new JButton("Tambah");
        tambah.addActionListener(e -> {
            setContentPane(tambahmenu());
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);
        buttonPanel.add(tambah);

        panelLihatBarang.add(buttonPanel, BorderLayout.SOUTH);

        return panelLihatBarang;
    }

    JPanel hapusBarangAdminPanel() {
        JPanel panelLihatBarang = new JPanel(new BorderLayout());
        panelLihatBarang.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panelLihatBarang.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query and populate the table
        try {

            PreparedStatement p = sqlQuery("SELECT kd_barang, gudang, nama_barang, harga_satuan FROM barang");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("kd_barang"),
                        resultSet.getString("gudang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getString("harga_satuan")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(adminmenu());
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);
        JButton hps = new JButton("Hapus");
        buttonPanel.add(hps);

        hps.addActionListener(e -> {
            setContentPane(hapusmenu());
            setVisible(true);
        });

        panelLihatBarang.add(buttonPanel, BorderLayout.SOUTH);

        return panelLihatBarang;
    }

    JPanel lihatNotaPanel() {
        JPanel notaPanel = new JPanel();

        notaPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        notaPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("kode_barang");
        model.addColumn("gudang");
        model.addColumn("nama_barang");
        model.addColumn("harga_satuan");

        // Execute the SQL query and populate the table
        try {
            // Print the SQL query for debugging
            // System.out.println("SQL Query: SELECT kd_barang, gudang, nama_barang,
            // harga_satuan FROM barang");

            // Execute the query
            PreparedStatement p = sqlQuery("SELECT * FROM nota");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("no_nota"),
                        resultSet.getString("met_pembayaran"),
                        resultSet.getString("tgl_pembelian"),
                        resultSet.getString("garansi_bulan"),
                        resultSet.getString("garansi_hari"),
                        resultSet.getString("nama_pegawai")
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(adminmenu());
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);

        notaPanel.add(buttonPanel, BorderLayout.SOUTH);

        return notaPanel;
    }

    JPanel lihatPenggunaPanel() {
        JPanel notaPanel = new JPanel();

        notaPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create a table to display the results
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        notaPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the model for the table
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id_pelanggan");
        model.addColumn("nama_pelanggan");
        model.addColumn("no_telp");

        // Execute the SQL query and populate the table
        try {
            // Print the SQL query for debugging
            // System.out.println("SQL Query: SELECT kd_barang, gudang, nama_barang,
            // harga_satuan FROM barang");

            // Execute the query
            PreparedStatement p = sqlQuery("SELECT * FROM pelanggan");
            ResultSet resultSet = p.executeQuery();

            // Iterate through the result set and add data to the table model
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("id_pelanggan"),
                        resultSet.getString("nama_pelanggan"),
                        resultSet.getString("no_telp"),
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();
        }

        table.setModel(model);

        // Back button
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            setContentPane(adminmenu());
            setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(back);

        notaPanel.add(buttonPanel, BorderLayout.SOUTH);

        return notaPanel;
    }

    JPanel hapusmenu() {
        JPanel hapus = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        hapus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Membuat JTextField
        JTextField textField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0); // Untuk memberikan jarak antara JTextField dan tombol Hapus
        hapus.add(textField, gbc);

        // Membuat JButton "Hapus"
        JButton hapusButton = new JButton("Hapus");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 10); // Untuk memberikan jarak antara tombol Hapus dan tombol Kembali
        hapus.add(hapusButton, gbc);

        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kdBarangToDelete = textField.getText();

                // Delete related rows in rincian_transaksi table
                String deleteRincianQuery = "DELETE FROM rincian_transaksi WHERE kd_barang = ?";
                try (PreparedStatement deleteRincianStatement = sqlQuery(deleteRincianQuery)) {
                    deleteRincianStatement.setString(1, kdBarangToDelete);
                    deleteRincianStatement.executeUpdate();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(hapus, "Gagal menghapus data rincian_transaksi");
                    return; // Exit the method if deleting from rincian_transaksi fails
                }

                // Now, you can safely delete the row in the barang table
                String deleteBarangQuery = "DELETE FROM barang WHERE kd_barang = ?";
                try (PreparedStatement deleteBarangStatement = sqlQuery(deleteBarangQuery)) {
                    deleteBarangStatement.setString(1, kdBarangToDelete);
                    deleteBarangStatement.executeUpdate();
                    JOptionPane.showMessageDialog(hapus, "Data berhasil dihapus");
                } catch (SQLException e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(hapus, "Gagal menghapus data barang");
                }

                setContentPane(adminmenu());
                setVisible(true);
            }
        });

        // Membuat JButton "Kembali"
        JButton backButton = new JButton("Back");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets
        hapus.add(backButton, gbc);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(adminmenu());
                setVisible(true);
            }
        });

        return hapus;
    }

    JPanel tambahmenu() {
        JPanel tambah = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Membuat JLabel dan JTextField untuk kd_barang
        JLabel kdBarangLabel = new JLabel("Kode Barang:");
        JTextField kdBarangField = new JTextField(20);
        kdBarangField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk gudang
        JLabel gudangLabel = new JLabel("Gudang:");
        JTextField gudangField = new JTextField(20);
        gudangField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk teredia_buku
        JLabel terediaBukuLabel = new JLabel("Stok Tersedia (Buku):");
        JTextField terediaBukuField = new JTextField(20);
        terediaBukuField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk tersedia_fisik
        JLabel tersediaFisikLabel = new JLabel("Stok Tersedia (Fisik):");
        JTextField tersediaFisikField = new JTextField(20);
        tersediaFisikField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk nama_barang
        JLabel namaBarangLabel = new JLabel("Nama Barang:");
        JTextField namaBarangField = new JTextField(20);
        namaBarangField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk harga_satuan
        JLabel hargaSatuanLabel = new JLabel("Harga Satuan:");
        JTextField hargaSatuanField = new JTextField(20);
        hargaSatuanField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk harga_pokok
        JLabel hargaPokokLabel = new JLabel("Harga Pokok:");
        JTextField hargaPokokField = new JTextField(20);
        hargaPokokField.setMaximumSize(new Dimension(200, 30));

        // Membuat JLabel dan JTextField untuk disc
        JLabel discLabel = new JLabel("Diskon:");
        JTextField discField = new JTextField(20);
        discField.setMaximumSize(new Dimension(200, 30));

        // Membuat JButton "Tambah"
        JButton tambahButton = new JButton("Tambah");
        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mendapatkan data dari JTextField
                String kdBarang = kdBarangField.getText();
                String gudang = gudangField.getText();
                int terediaBuku = Integer.parseInt(terediaBukuField.getText());
                int tersediaFisik = Integer.parseInt(tersediaFisikField.getText());
                String namaBarang = namaBarangField.getText();
                int hargaSatuan = Integer.parseInt(hargaSatuanField.getText());
                int hargaPokok = Integer.parseInt(hargaPokokField.getText());
                int disc = Integer.parseInt(discField.getText());

                // Query untuk menambah data ke dalam tabel barang
                String insertQuery = "INSERT INTO barang (kd_barang, gudang, tersedia_buku, tersedia_fisik, nama_barang, harga_satuan, harga_pokok, disc) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStatement = sqlQuery(insertQuery)) {
                    insertStatement.setString(1, kdBarang);
                    insertStatement.setString(2, gudang);
                    insertStatement.setInt(3, terediaBuku);
                    insertStatement.setInt(4, tersediaFisik);
                    insertStatement.setString(5, namaBarang);
                    insertStatement.setInt(6, hargaSatuan);
                    insertStatement.setInt(7, hargaPokok);
                    insertStatement.setInt(8, disc);

                    insertStatement.executeUpdate();
                    JOptionPane.showMessageDialog(tambah, "Data berhasil ditambahkan");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(tambah, "Gagal menambahkan data");
                }
            }
        });

        // Membuat JButton "Kembali"
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContentPane(adminmenu());
                setVisible(true);
            }
        });

        // Menetapkan GridBagConstraints untuk komponen-komponen
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        tambah.add(kdBarangLabel, gbc);

        gbc.gridx = 1;
        tambah.add(kdBarangField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(gudangLabel, gbc);

        gbc.gridx = 1;
        tambah.add(gudangField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(terediaBukuLabel, gbc);

        gbc.gridx = 1;
        tambah.add(terediaBukuField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(tersediaFisikLabel, gbc);

        gbc.gridx = 1;
        tambah.add(tersediaFisikField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(namaBarangLabel, gbc);

        gbc.gridx = 1;
        tambah.add(namaBarangField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(hargaSatuanLabel, gbc);

        gbc.gridx = 1;
        tambah.add(hargaSatuanField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(hargaPokokLabel, gbc);

        gbc.gridx = 1;
        tambah.add(hargaPokokField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        tambah.add(discLabel, gbc);

        gbc.gridx = 1;
        tambah.add(discField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        tambah.add(tambahButton, gbc);

        gbc.gridy++;
        tambah.add(backButton, gbc);

        return tambah;
    }

    private PreparedStatement sqlQuery(String query) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection url = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost\\16.0.1000:1433;databaseName=technologent;encrypt=true;trustServerCertificate=true",
                    "sa", "1122334455");
            return url.prepareStatement(query);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e);
            return null;
        }
    }
}
