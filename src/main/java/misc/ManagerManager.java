package misc;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManagerManager {

    private static final String BASE_DIRECTORY = "guilds/managers/";
    private static final String[] HEADERS = {"RoleID", "Manager1ID", "RoleManagerID"};

    public static void ensureCsvExists(String guildId) throws IOException {
        String path = getCsvPath(guildId);
        File file = new File(path);

        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Create directories if they don't exist
            try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                writer.writeNext(HEADERS);
            }
        }
    }

    private static String getCsvPath(String guildId) {
        return BASE_DIRECTORY + guildId + ".csv";
    }

    private static List<String[]> readCsv(String path) throws IOException, CsvException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("CSV file not found at: " + path);
        }

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            List<String[]> rows = reader.readAll();
            if (rows.isEmpty() || !rows.get(0)[0].equals(HEADERS[0])) {
                throw new IOException("Invalid or corrupt CSV format in file: " + path);
            }
            return rows;
        }
    }

    private static void writeCsv(String path, List<String[]> rows) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeAll(rows);
        }
    }

    public static void addOrUpdateRole(String guildId, String roleId, String managerId, int managerIndex) throws IOException, CsvException {
        ensureCsvExists(guildId);
        String path = getCsvPath(guildId);
        List<String[]> rows = readCsv(path);

        if (managerIndex < 1 || managerIndex >= HEADERS.length) {
            throw new IllegalArgumentException("Invalid manager index. Must be between 1 and " + (HEADERS.length - 1));
        }

        boolean updated = false;

        for (int i = 1; i < rows.size(); i++) { // Skip header row
            String[] row = rows.get(i);
            if (row[0].equals(roleId)) {
                row[managerIndex] = managerId;
                updated = true;
                break;
            }
        }

        if (!updated) {
            String[] newRow = new String[HEADERS.length];
            newRow[0] = roleId;
            newRow[managerIndex] = managerId;
            rows.add(newRow);
        }

        writeCsv(path, rows);
    }

    public static void removeRole(String guildId, String roleId) throws IOException, CsvException {
        ensureCsvExists(guildId);
        String path = getCsvPath(guildId);
        List<String[]> rows = readCsv(path);

        rows.removeIf(row -> row[0].equals(roleId));
        writeCsv(path, rows);
    }

    public static String getManagerForRole(String guildId, String roleId, int managerIndex) throws IOException, CsvException {
        ensureCsvExists(guildId);
        String path = getCsvPath(guildId);
        List<String[]> rows = readCsv(path);

        if (managerIndex < 1 || managerIndex >= HEADERS.length) {
            throw new IllegalArgumentException("Invalid manager index. Must be between 1 and " + (HEADERS.length - 1));
        }

        for (String[] row : rows) {
            if (row[0].equals(roleId)) {
                return row[managerIndex];
            }
        }
        return null;
    }

    public static String getFormattedManagerIds(String guildId, String roleId) throws IOException, CsvException {
        ensureCsvExists(guildId);
        StringBuilder formattedIds = new StringBuilder();

        for (int i = 1; i < HEADERS.length; i++) {
            String id = getManagerForRole(guildId, roleId, i);
            if (id != null && !id.isEmpty()) {
                if (i == HEADERS.length - 1) {
                    formattedIds.append("<@&").append(id).append(">");
                } else {
                    formattedIds.append("<@").append(id).append(">");
                }
            }
        }

        return formattedIds.toString();
    }

    public static boolean hasAtLeastOneManager(String guildId, String roleId) throws IOException, CsvException {
        ensureCsvExists(guildId);

        for (int i = 1; i < HEADERS.length; i++) {
            String managerId = getManagerForRole(guildId, roleId, i);
            if (managerId != null && !managerId.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static int getFirstEmptyManagerIndex(String guildId, String roleId) throws IOException, CsvException {
        ensureCsvExists(guildId);
        String path = getCsvPath(guildId);
        List<String[]> rows = readCsv(path);

        for (String[] row : rows) {
            if (row[0].equals(roleId)) {
                // Iterate through manager columns to find the first empty one
                for (int i = 1; i < HEADERS.length; i++) {
                    if (row[i] == null || row[i].isEmpty()) {
                        return i; // Return the index of the first empty manager column
                    }
                }
                break; // Exit after processing the target row
            }
        }

        return -1; // Return -1 if no empty index is found
    }
}
