package ui;

import io.ResizedQRCodeGenerator;
import validation.ExtensionValidator;

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

    //where to get images from (if the option is selected)
    private String imageFolderPath = "";
    private ArrayList<File> imageFilesInFolder;
    private JButton generateQRCode, cropAndGenerateQRCode;

    private JLabel autoDeleteOldQRCodes;
    private JCheckBox autoDeleteOldQRCodesSetting;

    GridBagConstraints gridBagConstraints = null;

    public IslandScanQRCodeResizerUI()
    {
        setTitle("Island Scan QR Code Resizer");

        generateQRCode = new JButton("Select Image of QR Code and Resize");
        generateQRCode.addActionListener(this);

        cropAndGenerateQRCode = new JButton("Crop Image of QR Code and Resize");
        cropAndGenerateQRCode.addActionListener(this);

        autoDeleteOldQRCodes = new JLabel("Auto Delete Old QR Codes");

        autoDeleteOldQRCodesSetting = new JCheckBox();

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(generateQRCode, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        add(cropAndGenerateQRCode, gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        add(autoDeleteOldQRCodes, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=1;
        add(autoDeleteOldQRCodesSetting, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == generateQRCode) {

            boolean isResizingFolder = false;
            int resizeFolderOfQRCodes = JOptionPane.showConfirmDialog(this, "Would you select a folder of QR Codes to resize? Pressing No will simply allow you to choose a single QR Code.");
            if (resizeFolderOfQRCodes == JOptionPane.YES_OPTION){
                isResizingFolder = true;
            }

            JFileChooser fileChooser = new JFileChooser();

            if (isResizingFolder) {
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    imageFolderPath = fileChooser.getSelectedFile().getAbsolutePath();
                    imageFilesInFolder = getImagesInFolder(imageFolderPath);
                } else {
                    return;
                }
            }
            else {
                FileFilter imageFileFilter = new FileNameExtensionFilter("Image File","jpg", "jpeg", "png", "JPG", "JPEG", "PNG");
                fileChooser.setFileFilter(imageFileFilter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                } else {
                    return;
                }
            }

            ResizedQRCodeGenerator resizedQRCodeGenerator = new ResizedQRCodeGenerator();

            if (!doesImageFileFolderNotExistOrIsEmpty()) {
                for (int i=0; i<imageFilesInFolder.size(); i++) {
                    String imageFilePath = imageFilesInFolder.get(i).getAbsolutePath();
                    BufferedImage resizedQRCode = resizedQRCodeGenerator.resizeQRCode(imageFilePath);
                    saveImage(resizedQRCode, imageFilePath);
                    if (autoDeleteOldQRCodesSetting.isSelected()) {
                        File imageFile = new File(imageFilePath);
                        imageFile.delete();
                    }
                }
            }
            else {
                BufferedImage resizedQRCode = resizedQRCodeGenerator.resizeQRCode(imagePath);
                saveImage(resizedQRCode, imagePath);
                if (autoDeleteOldQRCodesSetting.isSelected()) {
                    File imageFile = new File(imagePath);
                    imageFile.delete();
                }
            }

            if (doesImageFileFolderNotExistOrIsEmpty()) {
                JOptionPane.showMessageDialog(this, "Your QR Code has been successfully resized");
            }
            else {
                JOptionPane.showMessageDialog(this, "Your QR Codes have been successfully resized");
                imageFilesInFolder = null;  //reset arraylist
            }
        }

        if (e.getSource() == cropAndGenerateQRCode) {

            JFileChooser fileChooser = new JFileChooser();
            FileFilter imageFileFilter = new FileNameExtensionFilter("Image File","jpg", "jpeg", "png", "JPG", "JPEG", "PNG");
            fileChooser.setFileFilter(imageFileFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "Please click the top left of your QR Code.");

                imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                ImageCropper imageCropper = new ImageCropper(imagePath, autoDeleteOldQRCodesSetting.isSelected());
                imageCropper.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                imageCropper.pack();
                imageCropper.setVisible(true);
            } else {
                return;
            }
        }
    }

    private boolean doesImageFileFolderNotExistOrIsEmpty() {
        return imageFilesInFolder == null || imageFilesInFolder.isEmpty();
    }

    private void saveImage(BufferedImage resizedQRCode, String filePath) {
        if (resizedQRCode != null) {
            filePath = filePath.substring(0, filePath.lastIndexOf("."));
            File imageFile = new File(filePath + "ResizedQRCode.png");
            try
            {
                ImageIO.write(resizedQRCode, "png", imageFile); //this is where the image is saved to
            }
            catch(Exception ex)
            {
                return;
            }
        }
    }

    private ArrayList<File> getImagesInFolder(String imageFolderPath) {

        File[] imageFolderFileList = new File(imageFolderPath).listFiles();
        ExtensionValidator extensionValidator = new ExtensionValidator();

        ArrayList<File> imageFileList = new ArrayList<>();

        //Grab all image files and check subfolders if the file is a directory
        for (File file: imageFolderFileList) {
            String fileName = file.getName();
            if (extensionValidator.isExtensionValid(fileName) && !file.isDirectory()) {
                imageFileList.add(file);
            }
            else if (file.isDirectory()) {
                imageFileList.addAll(getImagesInFolder(file.getAbsolutePath()));
            }
        }

        return imageFileList;
    }
}