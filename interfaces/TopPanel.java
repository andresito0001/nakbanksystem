package interfaces;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TopPanel extends JPanel {
    JLabel disponibleJLabel = new JLabel();
    JLabel eurosLabel = new JLabel();
    JPanel container = new JPanel();
    int n = 0;
    String codigoActual = "BM-WN-0005";
    JButton nexButton = new JButton(" > ");
    JComboBox listaBox = new JComboBox<String>();

    public TopPanel (Connection conn) {    
        
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.setBackground(new Color (41,51,51));
    this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    container.setBackground(Color.DARK_GRAY);
    container.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    container.add(disponibleJLabel);
    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

    disponibleJLabel.setText("Billetera");
    nexButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    /*nexButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            n+=1;
            if (n > 10)
                n = 1;
            switch (n) {
            case 1:
                codigoActual = "BM-WN-0005";
                break;
            case 2:
                codigoActual = "BU-PP-0002";
                break;
            case 3:
                codigoActual = "BV-WN-0001";
                break;
            case 4: 
                codigoActual = "BA-WN-0002";
                break;
            case 5:
                codigoActual = "BU-WN-0002";
                break;
            case 6:
                codigoActual = "ZE-MZ-0001";
                break;
            case 7:
                codigoActual = "BP-WN-0003";
                break;
            case 8:
                codigoActual = "BE-WN-0006";
                break;
            case 9:
                codigoActual = "CH-NN-XXXX";
                break;
            case 10:
                codigoActual = "BC-WN-0004";
                break;
            default:
                break;
        }
            
        BanksDAO saldo_actual = new BanksDAO(conn);
        try {
            disponibleJLabel.setText(saldo_actual.getInfoOf(conn, "nombre_banco", "codigo", codigoActual).toString() + " - [" + codigoActual + "] " + saldo_actual.getTotalBalanceOf(conn, codigoActual) + " " + saldo_actual.getInfoOf(conn, "moneda", "codigo", codigoActual).toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        }
        
    });*/
    
    String query = "select * from bancos";
        
        try {
            PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                System.out.println(rs.getString("codigo").toString());
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("[ERROR]: " + e.getSQLState() + e.getErrorCode());
        }
        
    
    container.add(nexButton);
    container.add(listaBox);

    this.add(container);
    //this.add(disponibleJLabel);
    //this.add(eurosLabel)
    }

    public void cambiarBilletera(String codigo) {
        
    }
}
