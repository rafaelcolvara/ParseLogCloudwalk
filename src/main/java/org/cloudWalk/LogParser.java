package org.cloudWalk;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern playerPattern = Pattern.compile("n\\\\([^\\\\]+)\\\\");
    private static final Pattern killPattern = Pattern.compile("Kill: (\\d+) (\\d+) (\\d+): (.*) killed (.*) by MOD_(.*)");

    public static void parseLogFile(String resourcePath) throws IOException {
        InputStream inputStream = LogParser.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Game currentGame = null;
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.contains("InitGame:")) {
                if (currentGame != null) {
                    currentGame.printSummary();
                }
                currentGame = Game.getInstance();
            } else if (line.contains("ClientUserinfoChanged:")) {
                Matcher matcher = playerPattern.matcher(line);
                if (matcher.find()) {
                    currentGame.addPlayer(matcher.group(1));
                }
            } else if (line.contains("Kill:")) {
                Matcher matcher = killPattern.matcher(line);
                if (matcher.find()) {
                    String killer = matcher.group(4);
                    String victim = matcher.group(5);
                    String method = "MOD_" + matcher.group(6);
                    currentGame.addKill(killer, victim, method);
                }
            }
        }

        if (currentGame != null) {
            currentGame.printSummary();  // Print the last game and restart score
        }

        reader.close();
    }

    public static void main(String[] args) {
        try {
            LogParser.parseLogFile("qgames.log");
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
    }
}
