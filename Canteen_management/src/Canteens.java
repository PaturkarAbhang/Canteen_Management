import javax.swing.JFrame;
import javax.print.event.PrintJobListener;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;
import javax.swing.event.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
// import javax.swing.table.*;
// import javax.swing.table.DefaultTableModel.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


import java.io.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import java.util.jar.JarEntry;

class Canteen extends JFrame implements ActionListener, ItemListener
{
    private JTabbedPane jtp;
    private JPanel panel_Menu, panel_Bill, panel_ViewSell;
    private DefaultTableModel dtm, model;
    private JLabel TotalLbl;
    private JTextField nameField,priceField, name1Field, QtyField, price1Field, StudentField;
    private JComboBox ItemCategory, FilterCat;
    private JTable Items_Details, ItemsTbl, BillTbl, ViewSellTbl;
    private JTableHeader th, th2, th3, th4;
    private Vector<String> column_header, Columns;
    private Vector<Vector> rows;
    private int GrdTot = 0, Key = 0, BNum, PrNum = 0;
    private LocalDateTime now ;
    private static Connection Con;
    private Statement St, St1;
    private ResultSet Rs, Rs1;

    public Canteen()
    {
        super("Canteen Management System");

        //Panel_MenuSection

        panel_Menu = new JPanel();
        panel_Menu.setLayout(null);
        panel_Menu.setBackground(Color.WHITE);

        JButton Logout = new JButton("LOGOUT");
        Logout.setBounds(1400, 10, 100, 30);
        Logout.addActionListener(this);
        panel_Menu.add(Logout);

        JLabel MenuSectionLbl = new JLabel("Menu Management");
        MenuSectionLbl.setFont(new FontUIResource("Gabriola", Font.BOLD, 60));
        MenuSectionLbl.setBounds(600, 10, 600, 100);
        panel_Menu.add(MenuSectionLbl);

        JLabel Item_Name = new JLabel("Name");
        Item_Name.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        Item_Name.setBounds(300, 100, 100, 50);
        panel_Menu.add(Item_Name);

        JLabel Item_Category = new JLabel("Category");
        Item_Category.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        Item_Category.setBounds(675, 100, 100, 50);
        panel_Menu.add(Item_Category);

        JLabel Item_Price = new JLabel("Price");
        Item_Price.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        Item_Price.setBounds(1050, 100, 100, 50);
        panel_Menu.add(Item_Price);

        nameField = new JTextField();
        nameField.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        nameField.setBounds(300, 150, 250, 40);
        panel_Menu.add(nameField);


        String Items[] = {"BreakFast", "Tea", "Coffee", "Cold Drink", "Ice Cream"};
        ItemCategory = new JComboBox<String>(Items);
        ItemCategory.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        ItemCategory.setBounds(675, 150, 250, 40);
        panel_Menu.add(ItemCategory);

        priceField = new JTextField();
        priceField.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        priceField.setBounds(1050, 150, 250, 40);
        panel_Menu.add(priceField);

        String btn[] = {"ADD", "EDIT", "DELETE"};
        int x = 450;
        for(int count = 0; count < btn.length; count++)
        {
            JButton b = new JButton(btn[count]);
            b.setBounds(x, 220, 150, 50);
            b.setFont(new FontUIResource("Arial", Font.BOLD, 20));
            x += 250;
            b.addActionListener(this);
            panel_Menu.add(b);
        }

        JLabel ItemListLbl = new JLabel("-- ITEM LIST --");
        ItemListLbl.setFont(new FontUIResource("Arial Bold", Font.BOLD, 35));
        ItemListLbl.setBounds(670, 290, 300, 50);
        panel_Menu.add(ItemListLbl);

        JLabel FilterLbl = new JLabel("Filter :");
        FilterLbl.setFont(new FontUIResource("Serif", Font.BOLD, 30));
        FilterLbl.setBounds(550, 370, 200, 40);
        panel_Menu.add(FilterLbl);

        String Filter_Items[] = {"BreakFast", "Tea", "Coffee", "Cold Drink", "Ice Cream"};
        FilterCat = new JComboBox<String>(Filter_Items);
        FilterCat.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        FilterCat.setBounds(675, 370, 250, 40);
        FilterCat.addItemListener(this);
        panel_Menu.add(FilterCat);

        JButton Refresh = new JButton("Refresh");
        Refresh.setBounds(950, 370, 100, 40);
        Refresh.addActionListener(this);
        panel_Menu.add(Refresh);

        Columns = new Vector<String>();
        Columns.add("Item ID");
        Columns.add("Item Name");
        Columns.add("Category");
        Columns.add("Price");
        
            
		dtm = new DefaultTableModel(rows, Columns);
		Items_Details = new JTable(dtm);
		JScrollPane scroll = new JScrollPane(Items_Details);
		scroll.setBounds(400, 450, 800, 300);
		panel_Menu.add(scroll);

		// th = Items_Details.getTableHeader();
		// th.setBackground(Color.WHITE);
		// th.setForeground(Color.BLACK);

        Items_Details.setRowHeight(30);
        Items_Details.setShowHorizontalLines(true);
        Items_Details.setShowVerticalLines(true);
        Items_Details.addMouseListener(new MouseAdapter()
        { 
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) 
            {
                DefaultTableModel model = (DefaultTableModel) Items_Details.getModel();
                int MyIndex = Items_Details.getSelectedRow();
                Key = Integer.valueOf(model.getValueAt(MyIndex, 0).toString());
                nameField.setText(model.getValueAt(MyIndex, 1).toString());
                ItemCategory.setSelectedItem(model.getValueAt(MyIndex, 2).toString());
                priceField.setText(model.getValueAt(MyIndex, 3).toString());
            }

        });
        
        //Panel_Bil

        panel_Bill = new JPanel();
        panel_Bill.setLayout(null);
        panel_Bill.setBackground(Color.WHITE);

        JLabel BillSectionLbl = new JLabel("Billing Section");
        BillSectionLbl.setFont(new FontUIResource("Gabriola", Font.BOLD, 60));
        BillSectionLbl.setBounds(600, 10, 600, 100);
        panel_Bill.add(BillSectionLbl);

        Logout = new JButton("LOGOUT");
        Logout.setBounds(1400, 10, 100, 30);
        Logout.addActionListener(this);
        panel_Bill.add(Logout);

        JLabel Item_Name1 = new JLabel("Name");
        Item_Name1.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        Item_Name1.setBounds(100, 80, 100, 50);
        panel_Bill.add(Item_Name1);

        JLabel StudentLbl = new JLabel("Student Name");
        StudentLbl.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        StudentLbl.setBounds(400, 80, 150, 50);
        panel_Bill.add(StudentLbl);

        name1Field = new JTextField();
        name1Field.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        name1Field.setEditable(false);
        name1Field.setBounds(100, 130, 250, 40);
        name1Field.setBackground(Color.WHITE);
        panel_Bill.add(name1Field);

        StudentField = new JTextField("Student 1");
        StudentField.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        StudentField.setBounds(400, 130, 250, 40);
        panel_Bill.add(StudentField);

        JLabel Item_Price1 = new JLabel("Price");
        Item_Price1.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        Item_Price1.setBounds(100,180, 100, 50);
        panel_Bill.add(Item_Price1);

        JLabel Item_Quantity = new JLabel("Quatity");
        Item_Quantity.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        Item_Quantity.setBounds(400, 180, 100, 50);
        panel_Bill.add(Item_Quantity);

        price1Field = new JTextField();
        price1Field.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        price1Field.setBounds(100, 230, 250, 40);
        price1Field.setEditable(false);
        price1Field.setBackground(Color.white);
        panel_Bill.add(price1Field);

        QtyField = new JTextField();
        QtyField.setFont(new FontUIResource("Arial",Font.BOLD, 18));
        QtyField.setBounds(400, 230, 250, 40);
        panel_Bill.add(QtyField);

        JButton addToBill = new JButton("ADD TO BILL");
        addToBill.setFont(new FontUIResource("Serif", Font.BOLD, 15));
        addToBill.setBounds(300, 300, 150, 40);
        addToBill.addActionListener(this);
        panel_Bill.add(addToBill);

        Columns = new Vector<String>();
        Columns.add("Item ID");
        Columns.add("Item Name");
        Columns.add("Category");
        Columns.add("Price");

		rows = new Vector <Vector>();		

		dtm = new DefaultTableModel(rows, column_header);
		ItemsTbl = new JTable(dtm);
        // ItemsTbl.setEnabled(false);
		JScrollPane scroll2 = new JScrollPane(ItemsTbl);
		scroll2.setBounds(100, 350, 550, 400);
		panel_Bill.add(scroll2);
        ItemsTbl.setRowHeight(30);
        ItemsTbl.setShowHorizontalLines(true);
        ItemsTbl.setShowVerticalLines(true);
        ItemsTbl.addMouseListener(new MouseAdapter()
        { 
           @Override
            public void mouseClicked(java.awt.event.MouseEvent e) 
            {
                
                model = (DefaultTableModel) ItemsTbl.getModel();
                int MyIndex = ItemsTbl.getSelectedRow();
                Key = Integer.parseInt(model.getValueAt(MyIndex, 0).toString());
                name1Field.setText(model.getValueAt(MyIndex, 1).toString());
                price1Field.setText(model.getValueAt(MyIndex, 3).toString());
            }

        }); 
        

		th2 = ItemsTbl.getTableHeader();
		th2.setBackground(Color.WHITE);
		th2.setForeground(Color.BLACK);

        JLabel yourBill = new JLabel("Your Bill");
        yourBill.setFont(new FontUIResource("Serif", Font.BOLD, 30));
        yourBill.setBounds(1050, 80, 150, 50);
        panel_Bill.add(yourBill);

        Columns = new Vector<String>();
        Columns.add("SR.NO.");
        Columns.add("NAME");
        Columns.add("PRICE");
        Columns.add("QUANTITY");
        Columns.add("AMOUNT");

		rows = new Vector <Vector>();

		dtm = new DefaultTableModel(rows, Columns);
		BillTbl = new JTable(dtm);
        BillTbl.setEnabled(false);
		JScrollPane scroll3 = new JScrollPane(BillTbl);
		scroll3.setBounds(800, 130, 650, 550);
		panel_Bill.add(scroll3);
        BillTbl.setRowHeight(30);
        BillTbl.setShowHorizontalLines(true);
        BillTbl.setShowVerticalLines(true);

        JButton printBtn = new JButton("PRINT");
        printBtn.setFont(new FontUIResource("Arial", Font.BOLD, 20));
        printBtn.setBounds(800, 690, 150, 40);
        printBtn.addActionListener(this);
        panel_Bill.add(printBtn);

        TotalLbl = new JLabel("Rs.0.00");
        TotalLbl.setFont(new FontUIResource("Serif", Font.BOLD, 20));
        TotalLbl.setBounds(1350, 690, 100, 50);
        panel_Bill.add(TotalLbl);

        //Panel_View Sell

        panel_ViewSell = new JPanel();
        panel_ViewSell.setLayout(null);
        panel_ViewSell.setBackground(Color.WHITE);

        JLabel ViewSellLbl = new JLabel("View Sell");
        ViewSellLbl.setFont(new FontUIResource("Gabriola", Font.BOLD, 60));
        ViewSellLbl.setBounds(680, 10, 600, 100);
        panel_ViewSell.add(ViewSellLbl);

        Logout = new JButton("LOGOUT");
        Logout.setBounds(1400, 10, 100, 30);
        Logout.addActionListener(this);
        panel_ViewSell.add(Logout);

        Columns = new Vector<String>();
        Columns.add("Bill ID");
        Columns.add("Student Name");
        Columns.add("Date");
        Columns.add("Amount");

		rows = new Vector <Vector>();		

		dtm = new DefaultTableModel(rows, Columns);
		ViewSellTbl = new JTable(dtm);
        ViewSellTbl.setEnabled(false);
		JScrollPane scroll4 = new JScrollPane(ViewSellTbl);
		scroll4.setBounds(250, 100, 1000, 650);
		panel_ViewSell.add(scroll4);
        ViewSellTbl.setRowHeight(30);
        ViewSellTbl.setShowHorizontalLines(true);
        ViewSellTbl.setShowVerticalLines(true);
        

        jtp = new JTabbedPane(JTabbedPane.TOP);
        jtp.addTab("Menu Section", panel_Menu);
        jtp.addTab("Billing Section", panel_Bill);
        jtp.addTab("View Sell", panel_ViewSell);
        this.add(jtp, BorderLayout.CENTER);
        
        showBill();
        showProducts();

        this.setVisible(true);
        this.setSize(1800, 1000);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand().toUpperCase();

        switch(cmd)
        {
            case "ADD" : 
                if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || ItemCategory.getSelectedIndex() == -1) 
                {
                    JOptionPane.showMessageDialog(this, "Missing Information !!!");
                } 
                else 
                {
                    try 
                    {
                        CountProd();
                        connect();
                        PreparedStatement Pst = Con
                                .prepareStatement("insert into ItemsTbl values (?,?,?,?)");
                        Pst.setInt(1, PrNum);
                        Pst.setString(2, nameField.getText());
                        Pst.setString(3, ItemCategory.getSelectedItem().toString());
                        Pst.setInt(4, Integer.valueOf(priceField.getText()));
                        int row = Pst.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Item Added !!!");

                        showProducts();
                        disconnect();
                    } catch (Exception ex) 
                    {
                        JOptionPane.showMessageDialog(this, ex);
                    }
        
                }
            break;

            case "EDIT" :
            if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || ItemCategory.getSelectedIndex() == -1) 
            {
                JOptionPane.showMessageDialog(this, "Missing Information !!!");
            } 
            else 
            {
                try 
                {
                    CountProd();
                    connect();
                    PreparedStatement Pst = Con
                            .prepareStatement("update ItemsTbl set ItemName = ?, Category = ?, Price = ? where ItemId = ?");
                    Pst.setInt(4, Key);
                    Pst.setString(1, nameField.getText());
                    Pst.setString(2, ItemCategory.getSelectedItem().toString());
                    Pst.setInt(3, Integer.valueOf(priceField.getText()));
                    int row = Pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Item Updated !!!");
                    disconnect();
                    showProducts();
                } 
                catch (Exception ex) 
                {
                    JOptionPane.showMessageDialog(this, ex);
                }
    
            }
            break;

            case "DELETE" :
            if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || ItemCategory.getSelectedIndex() == -1) 
            {
                JOptionPane.showMessageDialog(this, "Missing Information !!!");
            } 
            else 
            {
                try 
                {
                    CountProd();
                    connect();
                    PreparedStatement Pst = Con.prepareStatement("delete from ItemsTbl where ItemId = ?");
                    Pst.setInt(1, Key);
                    int row = Pst.executeUpdate();
                    dtm.removeRow(Items_Details.getSelectedRow());
                    JOptionPane.showMessageDialog(this, "Item Deleted !!!");
    
                    disconnect();
                    showProducts();
                } 
                catch (Exception ex) 
                {
                    JOptionPane.showMessageDialog(this, ex);
                }
    
            }
            break;

            case "REFRESH" :
            showProducts();
            break;

            case "ADD TO BILL" :
            try
            {
                int Total;
                if (name1Field.getText().isEmpty() || QtyField.getText().isEmpty()) 
                {
                    JOptionPane.showMessageDialog(this, "Missing Information");
                } 
                else 
                {
                    Total = Integer.valueOf(price1Field.getText()) * Integer.valueOf(QtyField.getText());
                    GrdTot = GrdTot + Total;
                    TotalLbl.setText("Rs." + GrdTot);
                    DefaultTableModel model = (DefaultTableModel) BillTbl.getModel();
                    String nextRowId = Integer.toString(model.getRowCount());
                    model.addRow(new Object[]{
                                    Integer.valueOf(nextRowId) + 1,
                                    name1Field.getText(),
                                    price1Field.getText(),
                                    QtyField.getText(),
                                    Total
                    });


                    connect();
                    PreparedStatement Pst = Con.prepareStatement("insert into BillDetails values (?,?,?,?,?)");
                    Pst.setInt(1, PrNum);
                    Pst.setString(2, name1Field.getText());
                    Pst.setString(3, price1Field.getText());
                    Pst.setInt(4, Integer.valueOf(QtyField.getText()));
                    Pst.setInt(5, Total);
                    int row = Pst.executeUpdate();
                    disconnect();
                }
            }
            catch(Exception ex)
            {
                    ex.printStackTrace();
            }
        
            break;

            case "PRINT" :
            try 
            {
                InsertBill();
                Document doc = new Document();
            	PdfWriter w = PdfWriter.getInstance(doc, new FileOutputStream("Bills_pdf/"+BNum+".pdf"));						            		
	           	doc.open();

	           	// Title	           			
	           	Font f = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.RED);								
	           	Paragraph p = new Paragraph("*** GOVERNMENT POLYTECHNIC, AMRAVATI ***", f);
	           	p.setAlignment(1);
                doc.add(p);

                doc.add(new Paragraph("\n"));

                Font f1 = new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL, BaseColor.BLACK);								
                Paragraph p1 = new Paragraph("CANTEEN BILL", f1);
                p1.setAlignment(1);
                doc.add(p1);
                doc.add(new Paragraph("\n"));


                doc.add(new Paragraph("Student Name: "+StudentField.getText()));
	           	doc.add(new Paragraph("Bill Number: "+BNum));
	           	doc.add(new Paragraph("Bill Date: "+now));

	           	doc.add(new Paragraph("\n\n"));

	           	doc.add(new Paragraph("Bill Details:"));

	           	PdfPTable bill_table = new PdfPTable(5);
            	bill_table.setWidthPercentage(100);
            	bill_table.setSpacingBefore(11f);
            	bill_table.setSpacingAfter(11f);

            	float col_width[] = {2f, 3f, 2f, 2f, 2f};
            	bill_table.setWidths(col_width);

            	String cols[] = {"Item ID", "Item Name", "Price", "Quantity", "Amount"};

            	for(int i = 0; i < cols.length; i++)
            	{
                	PdfPCell c = new PdfPCell(new Paragraph(cols[i], new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK)));
                	c.setHorizontalAlignment(Element.ALIGN_CENTER);       
                	bill_table.addCell(c);
            	}

                Vector <Vector>rows = new Vector<Vector>();
                
                connect();
                String sql = "select * from BillDetails;";
                PreparedStatement ps = Con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) 
                {
                    String Item_Id = Integer.toString(rs.getInt("ItemId"));
                    String Item_Name = rs.getString("ItemName");
                    String Item_Price = rs.getString("Price");
                    String Quantity = rs.getString("Qty");
                    String Amount = rs.getString("Amount");

                    Vector row = new Vector<String>();

                    row.add(Item_Id);
                    row.add(Item_Name);
                    row.add(Item_Price);
                    row.add(Quantity);
                    row.add(Amount);
                
                    rows.add(row);

                

            	for(Vector <String> single_row : rows)
            	{
            		for(Object item : single_row)
            		{
            			PdfPCell c = new PdfPCell(new Paragraph((String)item));
             			c.setHorizontalAlignment(Element.ALIGN_CENTER);       
                		bill_table.addCell(c);
            		}
        		}
            }

            	doc.add(bill_table);        
                // doc.add(BillTbl);
            	p = new Paragraph("Total Bill Amount: "+GrdTot+" Rs.");
            	p.setAlignment(2);
            	doc.add(p);

            	p = new Paragraph("*** Thank You!!! ***");
            	p.setAlignment(1);
            	doc.add(p);

            	p = new Paragraph("*** Visit Again!!!***");
            	p.setAlignment(1);
            	doc.add(p);

            	doc.close();
           	 	w.close();

				JOptionPane.showMessageDialog(this, "Bill Saved..");
                PreparedStatement Pst = Con.prepareStatement("Truncate Table BillDetails");
                Pst.executeUpdate();
                disconnect();

                DefaultTableModel model = (DefaultTableModel) BillTbl.getModel();
                model.setRowCount(0);
                // for(int count = 0; count < BillTbl.getRowCount(); count++)
                // model.removeRow(count);

            } 
            catch (Exception ex) 
            {

            }
            break;

            case "LOGOUT" :
            new Login();
            this.dispose();
            break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        FilterProducts();
    }
    
    private void connect() 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/MyDatabase", "root", "");
        } catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void disconnect() 
    {
        try 
        {
            if (!Con.isClosed())
                Con.close();
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void CountProd() 
    {
        try 
        {
                connect();
                St1 = Con.createStatement();
                Rs1 = St1.executeQuery("Select Max(ItemId) from ItemsTbl");
                Rs1.next();
                PrNum = Rs1.getInt(1) + 1;
                disconnect();
        } 
        catch (SQLException ex) 
        {
                JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void showProducts() 
    {
        try 
        {
            Vector column = new Vector<String>();

            column.add("Item ID");
            column.add("Item Name");
            column.add("Category");
            column.add("Price");

            Vector rows = new Vector<Vector>();


            connect();
            String sql = "select * from ItemsTbl;";
            PreparedStatement ps = Con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) 
            {
                String Item_Id = Integer.toString(rs.getInt("ItemId"));
                String Item_Name = rs.getString("ItemName");
                String Item_Category = rs.getString("Category");
                String Item_Price = rs.getString("Price");

                Vector row = new Vector<String>();

                row.add(Item_Id);
                row.add(Item_Name);
                row.add(Item_Category);
                row.add(Item_Price);
              
                rows.add(row);

            }

            dtm = new DefaultTableModel(rows, column);
            Items_Details.setModel(dtm);
            ItemsTbl.setModel(dtm);

            ps.close();
            rs.close();
            disconnect();
        } catch (SQLException ex) 
        {

        }
    }

    public void FilterProducts() 
    {
        try 
        {
            Vector column = new Vector<String>();

            column.add("Item ID");
            column.add("Item Name");
            column.add("Category");
            column.add("Price");

            Vector rows = new Vector<Vector>();

            connect();
            String sql = "Select *from ItemsTbl where Category = '" + FilterCat.getSelectedItem().toString() + "'";
            PreparedStatement ps = Con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
            {
                String Item_Id = Integer.toString(rs.getInt("ItemId"));
                String Item_Name = rs.getString("ItemName");
                String Item_Category = rs.getString("Category");
                String Item_Price = rs.getString("Price");

                Vector row = new Vector<String>();

                row.add(Item_Id);
                row.add(Item_Name);
                row.add(Item_Category);
                row.add(Item_Price);
              
                rows.add(row);

            }

            dtm = new DefaultTableModel(rows, column);
            Items_Details.setModel(dtm);

            ps.close();
            rs.close();
            disconnect();
           
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void InsertBill() 
    {
        try 
        {
            CountBill();
            connect();
            PreparedStatement Pst = Con.prepareStatement("insert into Bills values (?,?,?,?)");
            Pst.setInt(1, BNum);
            Pst.setString(2, StudentField.getText());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
            now = LocalDateTime.now();
            Pst.setString(3, now.toString().substring(1, 10));
            Pst.setInt(4, GrdTot);
            int row = Pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bill Added !!!");
            disconnect();
            showProducts();
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void CountBill() 
    {
        try 
        {
            connect();
            St1 = Con.createStatement();
            Rs1 = St1.executeQuery("Select Max(BNum) from Bills");
            Rs1.next();
            BNum = Rs1.getInt(1) + 1;
            disconnect();

        } 
        catch (SQLException ex) 
        {

        }
    }

    private void showBill() 
    {
        
        try 
        {
            Vector column = new Vector<String>();
            column.add("Bill ID");
            column.add("Student Name");
            column.add("Bill Date");
            column.add("Amount");

            Vector rows = new Vector<Vector>();

            connect();
            String sql = "select * from Bills;";
            PreparedStatement ps = Con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                int BillId = rs.getInt("BNum");
                String StudentName = rs.getString("StudnetName");
                String BillDate = rs.getString("BDate");
                String Amount = rs.getString("Amount");

                Vector row = new Vector<String>();

                row.add(BillId);
                row.add(StudentName);
                row.add(BillDate);
                row.add(Amount);

                rows.add(row);
            }

            model = new DefaultTableModel(rows, column);
            ViewSellTbl.setModel(model);

            ps.close();
            Rs.close();
            disconnect();
        } 
        catch (SQLException ex) 
        {

        }
    }
   
}

public class Canteens {
    public static void main(String[] args) {
        Canteen f = new Canteen();
    }
}
