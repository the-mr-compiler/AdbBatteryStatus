package com.msoft;

enum BatteryStatus {
    Unknown,
    Charging,
    Discharging,
    NotCharging,
    Full
}

public class Battery {
    public int level, scale;
    public BatteryStatus status;

    public Battery(String batteryStatus) {
        var a = batteryStatus.split("\n");
        for (var s : a) {
            if (s.contains("level:")) {
                level = Integer.parseInt(s.split(":")[1].trim());
            }
            if (s.contains("scale:")) {
                scale = Integer.parseInt(s.split(":")[1].trim());
            }
            if (s.contains("status:")) {
                switch (s.split(":")[1].trim()) {
                    case "2":
                        status = BatteryStatus.Charging;
                        break;
                    case "3":
                        status = BatteryStatus.Discharging;
                        break;
                    case "4":
                        status = BatteryStatus.NotCharging;
                        break;
                    case "5":
                        status = BatteryStatus.Full;
                        break;
                    default:
                        status = BatteryStatus.Unknown;
                        break;
                }
            }
        }
    }

}