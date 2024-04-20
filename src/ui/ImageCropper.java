package ui;

import io.ResizedQRCodeGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageCropper extends JFrame implements MouseListener {

    private JLabel imageToCrop;
    private Container container;
    private int clickCounter = 0;
    private int[] coordinates = new int[4];
    private String imagePath = "";
    private boolean autoDeleteOldQRCode = false;

    public ImageCropper(String imagePath, boolean autoDeleteOldQRCode) {
        setTitle("Image Cropper");

        this.container = getContentPane();
        container.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon(imagePath);
        this.imagePath = imagePath;

        this.autoDeleteOldQRCode = autoDeleteOldQRCode;

        imageToCrop = new JLabel();
        imageToCrop.setIcon(imageIcon);
        imageToCrop.setText("");

        JPanel jPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 1);
        jPanel.setLayout(gridLayout);
        jPanel.add(imageToCrop);
        imageToCrop.addMouseListener(this);

        JScrollPane jScrollPane = new JScrollPane(jPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        container.add(jScrollPane);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (clickCounter == 0) {
            coordinates[0] = e.getX();
            coordinates[1] = e.getY();
            JOptionPane.showMessageDialog(this, "First click detected! Now click the bottom right of the QR Code!");
            clickCounter++;
        }
        else {
            JOptionPane.showMessageDialog(this, "Second click detected!");
            coordinates[2] = e.getX();
            coordinates[3] = e.getY();
            if (isValidCoordinates()) {

                BufferedImage qrCodeImage;
                try {
                    qrCodeImage = ImageIO.read(new File(imagePath));
                }
                catch(Exception ex)
                {
                    return;
                }

                saveCroppedResizedQRCode(qrCodeImage);
                setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid Crop! Please try again!");
                coordinates = new int[4];
                clickCounter = 0;
            }
        }
    }

    private void saveCroppedResizedQRCode(BufferedImage qrCodeImage) {
        int croppedImageWidth = coordinates[2] - coordinates[0];
        int croppedImageHeight = coordinates[3] - coordinates[1];

        BufferedImage copyOfQRCodeImage = copyCroppedImage(qrCodeImage, croppedImageWidth, croppedImageHeight);

        ResizedQRCodeGenerator resizedQRCodeGenerator = new ResizedQRCodeGenerator();
        BufferedImage resizedQRCode = resizedQRCodeGenerator.resizeQRCode(copyOfQRCodeImage);

        if (resizedQRCode != null) {
            imagePath = imagePath.substring(0, imagePath.lastIndexOf("."));
            File imageFile = new File(imagePath + "ResizedQRCode.png");
            try
            {
                ImageIO.write(resizedQRCode, "png", imageFile); //this is where the image is saved to
                if (autoDeleteOldQRCode) {
                    File oldQRCodeImageFile = new File(imagePath);
                    oldQRCodeImageFile.delete();
                }
                JOptionPane.showMessageDialog(this, "Your QR Code has been successfully resized");
            }
            catch(Exception ex)
            {
                return;
            }
        }
    }

    private BufferedImage copyCroppedImage(BufferedImage qrCodeImage, int croppedImageWidth, int croppedImageHeight) {
        //copy cropped image, so the original isn't modified
        qrCodeImage = qrCodeImage.getSubimage(coordinates[0], coordinates[1], croppedImageWidth, croppedImageHeight);
        BufferedImage copyOfImage = new BufferedImage(qrCodeImage.getWidth(), qrCodeImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(qrCodeImage, 0, 0, null);
        return copyOfImage;
    }

    private boolean isValidCoordinates() {
        //valid coordinates if the second click's position is farther right and down than the first click
        return coordinates[2] > coordinates[0] && coordinates[3] > coordinates[1];
    }

    //ignore these methods
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
