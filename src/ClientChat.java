/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author user
 */
public class ClientChat extends JFrame implements Runnable {
    private JTextField jtf = new JTextField();
    private JTextField jtf1 = new JTextField("Type your name here !");
    private JTextArea jtarea = new JTextArea();
    private Socket socket;
    private DataOutputStream dataout;
    private DataInputStream datain;
    
    public static void main(String[] args) {
       
        new ClientChat();
    }
    public ClientChat() {
        
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(new JLabel("Enter the Text: "), BorderLayout.WEST);
        p1.add(jtf,BorderLayout.CENTER);
        
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(new JLabel("Name: "), BorderLayout.WEST);
        p2.add(jtf1,BorderLayout.CENTER);
        
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(p1,BorderLayout.SOUTH);
        p.add(p2,BorderLayout.NORTH);
        
        setLayout(new BorderLayout());
        add(p,BorderLayout.NORTH);
        add(new JScrollPane(jtarea), BorderLayout.CENTER);
        
        jtf.addActionListener(new ButtonListener());
        jtarea.setEditable(false);
        
        setTitle("Multi Chat Client");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        try {
            socket = new Socket("localhost",8000);
            datain = new DataInputStream(socket.getInputStream());
            dataout = new DataOutputStream(socket.getOutputStream());
            new Thread(this).start();
        }
        catch(IOException ex) {
            jtarea.append(ex.toString()+'\n');
        }
    }
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //Get the text from the text field
                String string = jtf1.getText().trim()+":"+jtf.getText().trim();
                
                //Send the text to the server
                dataout.writeUTF(string);
                
                //Clear jtf
                jtf.setText("");
            }
            catch(IOException ex) {
                System.out.println(ex);
            }
        }
    }
    public void run() {
        try {
            while(true) {
                String text = datain.readUTF();
                jtarea.append(text+'\n');
            }
        }
        catch(IOException ex) {
            System.out.println(ex);
        }
    }
}
