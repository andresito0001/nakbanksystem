package interfaces;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;

import javax.swing.*;
import main.java.dao.BanksDAO;

public class Menu extends JFrame {

    public Menu (Connection conn) {

        TopPanel topPanel = new TopPanel(conn);

        this.setTitle("NakBank System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200,800);
        this.setLayout(new BorderLayout());

        SidePanel sidePanel = new SidePanel();

        this.add(sidePanel,BorderLayout.WEST);

        this.add(topPanel, BorderLayout.NORTH);



        /*
         * codigo viejo
         *         JFrame frame = new JFrame("Prueba");
        JPanel panelSide = new JPanel();
        int h = frame.getHeight();
        panelSide.setBounds(0, 0, 200, h);
        panelSide.setLayout(new BoxLayout(panelSide, BoxLayout.Y_AXIS));
        panelSide.setBackground(new Color(51,51,51));

        JPanel panelTop = new JPanel();
        panelTop.setBounds(200,0,1000,100);
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.X_AXIS));
        panelTop.setBackground(new Color (51,51,51));

        JLabel disponibleLabel = new JLabel("   Disponible: ");
        JLabel cantidadLabel = new JLabel(respuesta);
        disponibleLabel.setForeground(Color.WHITE);
        disponibleLabel.setFont(new Font("Century Gothic", 0, 20));
        panelTop.add(disponibleLabel);
        panelTop.add(cantidadLabel);
        
        frame.add(panelSide);
        frame.add(panelTop);

        frame.setVisible(true);

         */
    }



   // public void setDisponibleLabel (Connection conn) {
   //     BanksCompany saldo_actual = new BanksCompany();
     //   String codigo = topPanel.codigoActual;
       // topPanel.disponibleJLabel.setText("Disponible en Efectivo: " + saldo_actual.getTotalBalanceOf(conn, codigo));
        //topPanel.eurosLabel.setText("Disponible en Binance: " + saldo_actual.getTotalBalanceOf(conn, "BE-WN-0006").toString());
    //}
}
