package com.msoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BatteryStateHandler {
    static private Battery oldBatteryState;
    private final TrayIcon trayIcon;
    private final String deviceIp;
    private Battery batteryState;
    private ScheduledExecutorService executor;

    public BatteryStateHandler(String deviceIp) {
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (executor != null)
                    stopListeningBatteryUpdates();
                System.exit(0);
            }
        });
        popup.add(exitItem);
        this.trayIcon = new TrayIcon(createImage("0", Color.black), "0%", popup);
        trayIcon.setImageAutoSize(true);
        try {
            SystemTray.getSystemTray()
                      .add(trayIcon);
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(null, "System tray not supported", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        this.deviceIp = deviceIp;
    }

    private void updateTrayIcon() {
        if (oldBatteryState == null || oldBatteryState.status != batteryState.status
                || oldBatteryState.level != batteryState.level) {
            // if battery level less than 40% then show red icon
            if (batteryState.status == BatteryStatus.Discharging && batteryState.level < 40)
                trayIcon.setImage(createImage(String.valueOf(batteryState.level), Color.red));
            else
                trayIcon.setImage(createImage(String.valueOf(batteryState.level), Color.green));
            trayIcon.setToolTip(String.format("%d%% %s", batteryState.level, batteryState.status));
            oldBatteryState = batteryState;
        }
    }


    public void listenBatteryUpdates() {
        Runnable runnable = () -> {
            try {
                String data = ProcessExecutor.executeCommand("adb", "-s", deviceIp, "shell", "dumpsys", "battery");
                if (data.isBlank() || data.contains("not found") || data.contains("no devices")) {
                    JOptionPane.showMessageDialog(null, "Device Disconnected", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                BatteryStateHandler.this.batteryState = new Battery(data);
                updateTrayIcon();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new JFrame(),
                        e.getMessage(),
                        "Adb Battery State Handler",
                        JOptionPane.ERROR_MESSAGE);
            }
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
    }

    public void stopListeningBatteryUpdates() {
        if (executor != null) {
            executor.shutdown();
        }

    }

    // Reference : https://stackoverflow.com/questions/18800717/convert-text-content-to-image
    private Image createImage(String text, Color color) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graph = img.createGraphics();
        Font font = new Font("Arial", Font.BOLD, 50);
        graph.setFont(font);
        FontMetrics fm = graph.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        graph.dispose();
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graph = img.createGraphics();
        graph.setFont(font);
        fm = graph.getFontMetrics();
        graph.setColor(color);
        graph.drawString(text, 0, fm.getAscent());
        graph.dispose();
        return img;
    }
}
