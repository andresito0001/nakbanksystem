package interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SidePanel extends JPanel {
    public SidePanel () {
        
        this.setLayout (new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color (41,51,51));
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        String iconTrx = "https://cdn.icon-icons.com/icons2/2601/PNG/512/transaction_icon_155900.png";
        JButton transaccionesLabel = new JButton("Transacciones");
        String iconClients = "https://i.pinimg.com/originals/60/eb/68/60eb68bb3242ef4ac327a3fe28d25719.png";
        JButton clientesLabel = new JButton("Clientes");
        

        try {
            URI uriTrx = new URI(iconTrx);
            URL urlTrx = uriTrx.toURL();

            Image imgTrx = ImageIO.read(urlTrx);
            Image scaleImgTrx = imgTrx.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon iconoTrx = new ImageIcon(scaleImgTrx);

            transaccionesLabel.setIcon(iconoTrx);

            URI uriClientes = new URI(iconClients);
            URL urlClientes = uriClientes.toURL();

            Image img = ImageIO.read(urlClientes);
            Image scalImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon iconClientes = new ImageIcon(scalImage);

            clientesLabel.setIcon(iconClientes);

        } catch (IOException | IllegalArgumentException | java.net.URISyntaxException e) {
            transaccionesLabel.setText("Error");
            e.printStackTrace();
        }

        this.add(transaccionesLabel);
        this.add(clientesLabel);
        transaccionesLabel.addActionListener(e -> JOptionPane.showMessageDialog(null, "Transacciones en construccion"));
        clientesLabel.addActionListener(e -> JOptionPane.showMessageDialog(null, "Transacciones en construccion"));

    }
}
