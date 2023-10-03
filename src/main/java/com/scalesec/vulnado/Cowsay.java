package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Cowsay {
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());

  // Private constructor to hide the implicit public one.
  private Cowsay() {
    throw new IllegalStateException("Utility class");
  }

  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    // Make sure the "PATH" used to find this command includes only what you intend.
    String cmd = "/usr/games/cowsay '" + sanitizeInput(input) + "'";
    // Replace this use of System.out or System.err by a logger.
    LOGGER.info(cmd);
    processBuilder.command("bash", "-c", cmd);

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
    } catch (Exception e) {
      LOGGER.severe(e.getMessage());
    }
    return output.toString();
  }

  // Make sure that this user-controlled command argument doesn't lead to unwanted behavior.
  private static String sanitizeInput(String input) {
    return input.replaceAll("[^a-zA-Z0-9 ]", "");
  }
}