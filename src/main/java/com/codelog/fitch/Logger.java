package com.codelog.fitch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Logger {

    private List<String> messageList;

    public Logger() {

        messageList = new ArrayList<>();

    }

    public void log(Object sender, LogSeverity severity, String message) {

        String senderName = sender.getClass().getSimpleName();

        String time = getTimeString(LocalTime.now());
        String line = String.format("[%s] %s: %s", senderName, time, message);

        if (severity == LogSeverity.ERROR) {
            System.err.println(line);
        } else {
            System.out.println(line);
        }

        messageList.add(line);

    }

    public void log(Object sender, Exception e) {

        StringBuilder builder = new StringBuilder();
        builder.append(e.getMessage());
        builder.append('\n');

        // Since this loop has to finish quickly,
        // use of a StringBuilder is required
        for (StackTraceElement m : e.getStackTrace()) {
            builder.append("\tat: ");
            builder.append(m.toString());
            builder.append('\n');
        }

        log(sender, LogSeverity.ERROR, builder.toString());

    }

    public void write() {

        try {
            int index = 0;
            var tempFile = new File(String.format("logs/log_%d.log", index));
            while (tempFile.exists()) {
                index++;
                tempFile = new File(String.format("logs/log_%d.log", index));
            }

            var writer = new FileWriter(tempFile);
            for (String msg : messageList) {
                writer.write(msg + "\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println(String.format("Couldn't write log! (%s)", e.getMessage()));
        }

    }

    private static String getTimeString(LocalTime time) {
        return String.format("%s:%s:%s", time.getHour(), time.getMinute(), time.getSecond());
    }

}
