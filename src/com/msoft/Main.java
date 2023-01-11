package com.msoft;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String data = null;
        try {
            data = ProcessExecutor.executeCommand("adb", "devices");

            // convert raw adb output to devices list
            var devicesList = Arrays.stream(data.split("\n"))
                                    .skip(1)
                                    .map(s -> s.split("\t")[0].trim())
                                    .filter(v -> v.length() > 0)
                                    .toArray(String[]::new);
            if (devicesList.length == 0) {
                JOptionPane.showMessageDialog(null, "No connected devices found", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            String deviceIp = devicesList[0];
            if (devicesList.length > 1) {
                deviceIp = showDialog("Select device", devicesList);
                if (deviceIp == null)
                    System.exit(0);
            }
            var batteryStateHandler = new BatteryStateHandler(deviceIp);
            batteryStateHandler.listenBatteryUpdates();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "ADB executable Not Found. Please set path variable for adb.",
                    "Adb Battery State",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String showDialog(String title, String[] options) {
        JList<String> list = new JList<String>(options);
        JScrollPane scrollPane = new JScrollPane(list);

        int result = JOptionPane.showOptionDialog(null,
                scrollPane,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"OK", "Cancel"},
                "OK");

        if (result == JOptionPane.OK_OPTION) {
            return list.getSelectedValue();
        }
        else {
            return null;
        }
    }
}
