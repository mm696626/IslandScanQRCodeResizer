package ui;

import io.ResizedQRCodeGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class IslandScanQRCodeResizerUI extends JFrame implements ActionListener {


    //where to get the image from
    private String imagePath = "";
    private JButton generateQRCode;

    GridBagConstraints gridBagConstraints = null;

    public IslandScanQRCodeResizerUI()
    {
        setTitle("Island Scan QR Code Resizer");

        generateQRCode = new JButton("Select Image of QR Code and Resize");
        generateQRCode.addActionListener(this);

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(generateQRCode, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == generateQRCode) {

            JFileChooser fileChooser = new JFileChooser();
            FileFilter imageFileFilter = new FileNameExtensionFilter("Image File","jpg", "jpeg", "png", "JPG", "JPEG", "PNG");
            fileChooser.setFileFilter(imageFileFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                return;
            }

            ResizedQRCodeGenerator resizedQRCodeGenerator = new ResizedQRCodeGenerator();
            BufferedImage resizedQRCode = resizedQRCodeGenerator.resizeQRCode(imagePath);

            if (resizedQRCode != null) {
                File imageFile = new File("resizedQRCode.png");
                try
                {
                    ImageIO.write(resizedQRCode, "png", imageFile); //this is where the image is saved to
                }
                catch(Exception ex)
                {
                    return;
                }

                JOptionPane.showMessageDialog(this, "Your QR Code has been successfully resized and saved to: " + imageFile.getAbsolutePath());
            }
        }
    }
}