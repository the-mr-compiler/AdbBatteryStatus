# Battery Level Indicator

This program uses the Android Debug Bridge (ADB) to retrieve the battery level of an Android device and display it in
the system tray.

## Prerequisites

- Java 8 or later
- ADB installed and added to the system PATH
- Connected Android device with USB Debugging enabled

## How to use

1. Clone the repository

```shell
git clone https://github.com/the-mr-compiler/battery-level-indicator.git
```

2. Open a terminal in the src folder of cloned repository and run the following command

```shell
javac com.msoft.Main.java
java com.msoft.Main
```

3. The program will now retrieve the battery level of the connected Android device and display it in the system tray.

## Additional information

- You can also import the project into your favourite IDE and build and run the project from there
- The program will continuously check the battery level and update the system tray icon every 3 seconds
- If no device is connected or USB Debugging is not enabled, the program will display error dialog.
- You can configure the time duration in the code for how frequently you want to check the battery level.

## Additional options

- You can add some options to the program that sends you notifications when the battery level is low
- You can also make the program run on system startup.

## Issues

- Please open an issue on the GitHub repository if you run into any problems
- If you don't see the battery level in the system tray then make sure the `adb` is available in the system PATH and the
  device is connected and Debugging is enabled.

## Contributing

If you're interested in contributing to the project, please submit a pull request.

### References

1. [Android Debug Bridge](https://developer.android.com/studio/command-line/adb)
2. [Java Tray Icon](https://docs.oracle.com/javase/tutorial/uiswing/misc/systemtray.html)
3. [Convert text content to Image(StackOverflow)](https://stackoverflow.com/questions/18800717/convert-text-content-to-image)