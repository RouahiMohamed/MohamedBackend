package com.example.AzurePfe.security.services.infracost;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class InfracostService {
    private static final String INFRACOST_CLI = "infracost";

    public Map<String, String> estimateCosts(String terraformCode) throws IOException, InterruptedException {
        // Write the Terraform code to a temporary file
        File tempFile = File.createTempFile("terraform", ".tf");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(terraformCode);
        }

        // Create an Infracost configuration file
        File configFile = File.createTempFile("infracost", ".yml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            writer.write("version: 0.1\nprojects:\n  - path: " + tempFile.getParent() + "\n    terraform_workspace: default\n");
        }

        System.out.println("Temp Terraform File Path: " + tempFile.getAbsolutePath());
        System.out.println("Infracost Config File Path: " + configFile.getAbsolutePath());

        // Build the command for Infracost using the temporary file and configuration file
        ProcessBuilder processBuilder = new ProcessBuilder(
                INFRACOST_CLI, "breakdown",
                "--config-file", configFile.getAbsolutePath()
        );
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to estimate costs with Infracost: " + removeAnsiEscapeCodes(result.toString()));
        }

        // Clean up temporary files
        tempFile.delete();
        configFile.delete();

        return extractResourcesAndTotalCost(removeAnsiEscapeCodes(result.toString()));
    }

    private String removeAnsiEscapeCodes(String input) {
        return input.replaceAll("\\x1B\\[[;\\d]*m", "");
    }

    private Map<String, String> extractResourcesAndTotalCost(String input) {
        StringBuilder resources = new StringBuilder("Detected Resources:\n");
        String totalCost = "Total cost not found";

        String[] lines = input.split("\n");
        boolean resourceSection = false;
        for (String line : lines) {
            if (line.startsWith(" OVERALL TOTAL")) {
                totalCost = "Total cost is " +line.substring(line.lastIndexOf('$')).trim();
            }
            if (resourceSection && line.contains("├──")) {
                resources.append(line).append("\n");
            }
            if (line.contains(" cloud resource was detected")) {
                resourceSection = true;
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("resources", resources.toString());
        result.put("totalCost", totalCost);
        return result;
    }
}
