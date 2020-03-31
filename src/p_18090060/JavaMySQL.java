/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p_18090060;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zdaf
 */
public class JavaMySQL extends javax.swing.JFrame {
    int idBaris=0;
    String role;
    DefaultTableModel model;
    /**
     * Creates new form JavaMySQL
     */
    public JavaMySQL() {
        initComponents();
        
//        KoneksiDB.sambungDB();
        aturModelTabel();
        pangkat();
        jafung();
        showForm(false);
        showData("");
    }
    
    private void aturModelTabel(){
        Object[] kolom = {"No","NIDN","NAMA","JAFUNG","Golongan/Pangkat","No HP"};
        model = new DefaultTableModel(kolom,0){
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };
            
            @Override
            public boolean isCellEditable(int rowindex, int columindex){
                return canEdit [columindex];
            }
        };
        tblData.setModel(model);
        tblData.getRowHeight();
        tblData.getColumnModel().getColumn(0).setMinWidth(0);
        tblData.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    private void showForm(boolean b){
       areaSplit.setDividerLocation(0.3);
       areaSplit.getLeftComponent().setVisible(b);
    }
    private void resetForm(){
        tblData.clearSelection();
        txtNIDN.setText("");
        txtNama.setText("");
        cmbJafung.setSelectedIndex(0);
        cmbPangkat.setSelectedIndex(0);
        txtNoHp.setText("");
        txtNIDN.requestFocus();
    }
    private void pangkat(){
        cmbPangkat.removeAllItems();
        cmbPangkat.addItem("Pilih Golongan/Pangkat");
        cmbPangkat.addItem("Belum ada");
        cmbPangkat.addItem("II/c (Pengatur)");
        cmbPangkat.addItem("II/d (Pengatur Tk. I)");
        cmbPangkat.addItem("III/a (Penata Muda)");
        cmbPangkat.addItem("III/b (Penata Muda Tk. I)");
        cmbPangkat.addItem("III/c (Penata)");
        cmbPangkat.addItem("III/d (Penata Tk. I)");
        cmbPangkat.addItem("IV/a (Pembina)");
        cmbPangkat.addItem("IV/b (Pembina Tk. I)");
        cmbPangkat.addItem("IV/c (Pembina Utama Muda)");
        cmbPangkat.addItem("IV/d (Pembina Utama Madya)");
        cmbPangkat.addItem("IV/e (Pembina Utama)");
    }
    private void jafung(){
        cmbJafung.removeAllItems();
        cmbJafung.addItem("Pilih Golongan/Pangkat");
        cmbJafung.addItem("Belum ada");
        cmbJafung.addItem("Asisten Ahli (100)");
        cmbJafung.addItem("Asisten Ahli (150)");
        cmbJafung.addItem("Lektor (200)");
        cmbJafung.addItem("Lektor (300)");
        cmbJafung.addItem("Lektor Kepala (400)");
        cmbJafung.addItem("Lektor Kepala (550)");
        cmbJafung.addItem("Lektor Kepala (700)");
        cmbJafung.addItem("Profesor (850)");
        cmbJafung.addItem("Profesor (1050)");
    }
    private void showData(String key) {
        model.getDataVector().removeAllElements();
        String where = "";
        if (!key.isEmpty()) {
            where += "WHERE nidn LIKE '%" + key + "%' "
                    + "OR nama LIKE '%" + key + "%' "
                    + "OR jabatan_fungsional LIKE '%" + key + "%' "
                    + "OR pangkat_golongan LIKE '%" + key + "%' "
                    + "OR no_hp LIKE '%" + key + "%'";
        }
        String sql = "SELECT * FROM data_dosen " + where;
        Connection con;
        Statement st;
        ResultSet rs;
        int baris = 0;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object id = rs.getInt(1);
                Object nidn = rs.getString(2);
                Object nama = rs.getString(3);
                Object jafung = rs.getString(4);
                Object pangkat = rs.getString(5);
                Object no_hp = rs.getString(6);
                Object[] data = {id, nidn, nama, jafung, pangkat, no_hp};
                model.insertRow(baris, data);
                baris++;
            }
            st.close();
            con.close();
            tblData.revalidate();
            tblData.repaint();
        } catch (SQLException e) {
            System.err.println("showData(): " + e.getMessage());
        }
    }
    private void resetView(){
        resetForm();
        showForm(false);
        showData("");
        btnHapus.setEnabled(false);
        idBaris = 0;
    }
    private void pilihanData(String n){
        btnHapus.setEnabled(true);
        
        String sql = "SELECT * FROM data_dosen WHERE id='"+n+"'";
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()){
                int id = rs.getInt(1);
                String nidn = rs.getString(2);
                String nama = rs.getString(3);
                Object jafung = rs.getString(4);
                Object pangkat = rs.getString(5);
                String no_hp = rs.getString(6);
                idBaris = id;
                txtNIDN.setText(nidn);
                txtNama.setText(nama);
                cmbJafung.setSelectedItem(jafung);
                cmbPangkat.setSelectedItem(pangkat);
                txtNoHp.setText(no_hp);
            }
            st.close();
            con.close();
            showForm(true);
        } catch (SQLException e) {
            System.out.println("pilihData(): "+e.getMessage());
        }
    }
    private void simpanData() {
        String nidn = txtNIDN.getText();
        String nama = txtNama.getText();
        int jafung = cmbJafung.getSelectedIndex();
        int pangkat = cmbPangkat.getSelectedIndex();
        String nohp = txtNoHp.getText();
        if (nidn.isEmpty() || nama.isEmpty() || jafung == 0 || pangkat == 0
                || nohp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        } else {
            String jafung_isi = cmbJafung.getSelectedItem().toString();
            String pangkat_isi = cmbPangkat.getSelectedItem().toString();
            String sql
                    = "INSERT INTO data_dosen (nidn,nama,jabatan_fungsional,"
                    + "pangkat_golongan,no_hp) "
                    + "VALUES (\"" + nidn + "\",\"" + nama + "\","
                    + "\"" + jafung_isi + "\",\"" + pangkat_isi + "\",\"" + nohp
                    + "\")";
            Connection con;
            Statement st;
            try {
                con = KoneksiDB.sambungDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this, "Data telah disimpan!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    private void ubahData() {
        String nidn = txtNIDN.getText();
        String nama = txtNama.getText();
        int jafung = cmbJafung.getSelectedIndex();
        int pangkat = cmbPangkat.getSelectedIndex();
        String nohp = txtNoHp.getText();
        if (nidn.isEmpty() || nama.isEmpty() || jafung == 0 || pangkat == 0
                || nohp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
        } else {
            String jafung_isi = cmbJafung.getSelectedItem().toString();
            String pangkat_isi = cmbPangkat.getSelectedItem().toString();
            String sql = "UPDATE data_dosen "
                    + "SET nidn=\"" + nidn + "\","
                    + "nama=\"" + nama + "\","
                    + "jabatan_fungsional=\"" + jafung_isi + "\","
                    + "pangkat_golongan=\"" + pangkat_isi + "\","
                    + "no_hp=\"" + nohp + "\" WHERE id=\"" + idBaris + "\"";
            Connection con;
            Statement st;
            try {
                con = KoneksiDB.sambungDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this, "Data telah diubah!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    private void hapusData(int baris) {
        Connection con;
        Statement st;
        try {
            con = KoneksiDB.sambungDB();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM data_dosen WHERE id=" + baris);
            st.close();
            con.close();
            resetView();
            JOptionPane.showMessageDialog(this, "Data telah dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        areaSplit = new javax.swing.JSplitPane();
        panelKiri = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNIDN = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtNoHp = new javax.swing.JTextField();
        cmbJafung = new javax.swing.JComboBox<>();
        cmbPangkat = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        btnTutup = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        btnTambah.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnTambah.setText("Tambah Data");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnHapus.setText("Hapus Data");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        btnCari.setText("Cari");

        panelKiri.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setText("NIDN");

        jLabel2.setText("NAMA");

        jLabel3.setText("JABATAN FUNGSIONAL");

        jLabel4.setText("PANGKAT/GOLONGAN");

        jLabel5.setText("NO.HP");

        txtNoHp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoHpActionPerformed(evt);
            }
        });

        cmbJafung.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbPangkat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKiriLayout = new javax.swing.GroupLayout(panelKiri);
        panelKiri.setLayout(panelKiriLayout);
        panelKiriLayout.setHorizontalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKiriLayout.createSequentialGroup()
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(cmbPangkat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNIDN, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(cmbJafung, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(62, 62, 62))))
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTutup)
                .addGap(51, 51, 51)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        panelKiriLayout.setVerticalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNIDN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbJafung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbPangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTutup)
                    .addComponent(btnSimpan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        areaSplit.setLeftComponent(panelKiri);

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblData);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        areaSplit.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addGap(18, 18, 18)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCari))
                    .addComponent(areaSplit))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnHapus)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari))
                .addGap(53, 53, 53)
                .addComponent(areaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(144, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNoHpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoHpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoHpActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        role = "Tambah";
        btnSimpan.setText("SIMPAN");
        idBaris = 0;
        resetForm();
        showForm(true);
        btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (role.equals("TAMBAH")) {
            simpanData();
        }else if(role.equals("Ubah")){
            ubahData();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        resetForm();
        showForm(false);
        btnHapus.setEnabled(false);
        idBaris = 0;
    }//GEN-LAST:event_btnTutupActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (idBaris == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
        }else {
            hapusData(idBaris);
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        String key = txtCari.getText();
        showData(key);
    }//GEN-LAST:event_txtCariKeyReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        areaSplit.setDividerLocation(0.3);
    }//GEN-LAST:event_formComponentResized

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        role = "Ubah";
        int row = tblData.getRowCount();
        if (row > 0) {
            int sel = tblData.getSelectedRow();
            if (sel != -1) {
                pilihanData(tblData.getValueAt(sel, 0).toString());
                btnSimpan.setText("UBAH DATA");
            }
        }
    }//GEN-LAST:event_tblDataMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaMySQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaMySQL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JComboBox<String> cmbJafung;
    private javax.swing.JComboBox<String> cmbPangkat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panelKiri;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNIDN;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoHp;
    // End of variables declaration//GEN-END:variables
}
