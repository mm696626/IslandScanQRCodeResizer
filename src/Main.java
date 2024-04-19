// Island Scan QR Code Resizer by Matt McCullough
// This is to resize QR codes for Island Scan (primarily for Citra use)

import ui.IslandScanQRCodeResizerUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        IslandScanQRCodeResizerUI islandScanQRCodeResizerUI = new IslandScanQRCodeResizerUI();
        islandScanQRCodeResizerUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        islandScanQRCodeResizerUI.pack();
        islandScanQRCodeResizerUI.setVisible(true);
    }
}