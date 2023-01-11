package com.msoft;

import java.io.IOException;

public class ProcessExecutor {
    public static String executeCommand(String... arr) throws IOException {

        Process p = Runtime.getRuntime().exec(arr);
        var data = new String(p.getInputStream().readAllBytes());
        p.destroy();
        return data;

    }
}
