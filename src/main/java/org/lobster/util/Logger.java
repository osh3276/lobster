package org.lobster.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom logger for the Lobster Flight Tracker application
 * Logs to both console and file (logs/application.log)
 */
public class Logger {
    
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }
    
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "application.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private static Logger instance;
    private final Path logPath;
    private final boolean consoleOutput;
    
    private Logger() {
        this.consoleOutput = true; // Can be configured
        this.logPath = initializeLogFile();
    }
    
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
    
    private Path initializeLogFile() {
        try {
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            Path logFile = logDir.resolve(LOG_FILE);
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }
            
            return logFile;
        } catch (IOException e) {
            System.err.println("Failed to initialize log file: " + e.getMessage());
            return null;
        }
    }
    
    public void debug(String className, String message) {
        log(Level.DEBUG, className, message, null);
    }
    
    public void info(String className, String message) {
        log(Level.INFO, className, message, null);
    }
    
    public void warn(String className, String message) {
        log(Level.WARN, className, message, null);
    }
    
    public void warn(String className, String message, Throwable throwable) {
        log(Level.WARN, className, message, throwable);
    }
    
    public void error(String className, String message) {
        log(Level.ERROR, className, message, null);
    }
    
    public void error(String className, String message, Throwable throwable) {
        log(Level.ERROR, className, message, throwable);
    }
    
    private void log(Level level, String className, String message, Throwable throwable) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logMessage = String.format("[%s] %-5s %s - %s", 
            timestamp, level.name(), className, message);
        
        // Console output
        if (consoleOutput) {
            if (level == Level.ERROR || level == Level.WARN) {
                System.err.println(logMessage);
            } else {
                System.out.println(logMessage);
            }
            
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }
        
        // File output
        writeToFile(logMessage, throwable);
    }
    
    private void writeToFile(String message, Throwable throwable) {
        if (logPath == null) return;
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(logPath, 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            writer.println(message);
            
            if (throwable != null) {
                throwable.printStackTrace(writer);
            }
            
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}