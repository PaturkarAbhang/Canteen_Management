import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.*;
import javax.swing.plaf.FontUIResource;

import com.itextpdf.text.Font;

class Login extends JFrame implements ActionListener
{
    private JLabel l1, l2, l3, l4, l5, UserLbl, PassLbl;
    private JTextField TxtUser;
    private JPasswordField pass;
    private JButton login;

    public Login()
    {
        super("Canteen Management System");

        JPanel p1 = new JPanel();
        p1.setBackground(Color.WHITE);
        p1.setLayout(null);
        this.add(p1);

        l1 = new JLabel();
        ImageIcon img = new ImageIcon(new ImageIcon("gpalogo.png").getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        l1.setIcon(img);
        l1.setBounds(100, 10,200, 200);
        p1.add(l1);
        
        l2 = new JLabel("Government Polytechnic, Amravati");
        l2.setFont(new FontUIResource("Ubuntu", Font.BOLD, 60));
        l2.setBounds(280, 10, 1000, 120);
        p1.add(l2);

        l3 = new JLabel("An Autonomous Institute of Maharashtra");
        l3.setFont(new FontUIResource("Ubuntu", Font.NORMAL, 20));
        l3.setBounds(580, 120, 800, 50);
        p1.add(l3);

        l4 = new JLabel("College Canteen");
        l4.setFont(new FontUIResource("Ubuntu", Font.BOLD, 40));
        l4.setBounds(600, 180, 400, 50);
        p1.add(l4);

        l5 = new JLabel("LOGIN");
        l5.setFont(new FontUIResource("Ubuntu", Font.NORMAL, 30));
        l5.setForeground(Color.RED);
        l5.setBounds(700, 250, 200, 50);
        p1.add(l5);

        UserLbl = new JLabel("Username");
        UserLbl.setBounds(600, 350, 150, 40);
        UserLbl.setFont(new FontUIResource("Calibri", Font.BOLD,25));
        p1.add(UserLbl);

        TxtUser = new JTextField();
        TxtUser.setBounds(600, 390, 300, 40);
        p1.add(TxtUser);

        PassLbl = new JLabel("Password");
        PassLbl.setBounds(600, 450, 100, 40);
        PassLbl.setFont(new FontUIResource("Calibri", Font.BOLD,25));
        p1.add(PassLbl);

        pass = new JPasswordField();
        pass.setBounds(600, 490, 300, 40);
        pass.setEchoChar('*');
        p1.add(pass);

        login = new JButton("LOG IN");
        login.setFont(new FontUIResource("Calibri", Font.BOLD,35));
        login.setBackground(Color.GREEN);
        login.setBounds(600, 560, 300, 35);
        login.addActionListener(this);
        p1.add(login);

        this.setVisible(true);
        this.setSize(1800, 800);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String User = TxtUser.getText();

        char arr[] = pass.getPassword();
        String Pass = new String(arr);

        if(User.isEmpty() || Pass.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Missing Information !!!");
        }
        else
        {
            if(User.equals("Admin") && Pass.equals("Password"))
            {
                new Canteen();
                this.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Wrong UserName OR Password !!!");
                TxtUser.setText("");
                pass.setText("");
            }
        }
    }
    
}


public class Canteen_Login {
    public static void main(String[] args) {
        Login L = new Login();

    }
}
