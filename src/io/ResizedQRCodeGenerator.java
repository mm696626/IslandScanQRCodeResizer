package io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static constants.QRCodeConstants.*;

public class ResizedQRCodeGenerator {

    public BufferedImage resizeQRCode(String imagePath) {

        BufferedImage qrCodeImage = null; //QR code image
        BufferedImage resizedQRCode = new BufferedImage(RESIZED_QR_CODE_WIDTH, RESIZED_QR_CODE_HEIGHT, BufferedImage.TYPE_INT_ARGB); //new image

        try {
            qrCodeImage = ImageIO.read(new File(imagePath));
        }
        catch(Exception e)
        {
            //already handled it below
        }

        if (qrCodeImage == null) {
            //this is if the image couldn't be read
            return null;
        }

        return createResizedQRCode(resizedQRCode, qrCodeImage);
    }

    public BufferedImage resizeQRCode(BufferedImage qrCodeImage) {
        BufferedImage resizedQRCode = new BufferedImage(RESIZED_QR_CODE_WIDTH, RESIZED_QR_CODE_HEIGHT, BufferedImage.TYPE_INT_ARGB); //new image

        if (qrCodeImage == null) {
            //this is if the image couldn't be read
            return null;
        }

        return createResizedQRCode(resizedQRCode, qrCodeImage);
    }

    private BufferedImage createResizedQRCode(BufferedImage resizedQRCode, BufferedImage qrCodeImage) {
        Graphics2D g2d = resizedQRCode.createGraphics();
        g2d.setPaint(Color.white);
        g2d.fillRect(0, 0, RESIZED_QR_CODE_WIDTH, RESIZED_QR_CODE_HEIGHT);
        qrCodeImage = resize(qrCodeImage, QR_CODE_X_POSITION_END - QR_CODE_X_POSITION, QR_CODE_Y_POSITION_END - QR_CODE_Y_POSITION);
        g2d.drawImage(qrCodeImage, QR_CODE_X_POSITION, QR_CODE_Y_POSITION, null); //draw section of image

        return resizedQRCode;
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }
}
