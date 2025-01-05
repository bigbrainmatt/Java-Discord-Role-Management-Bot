package misc;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.util.*;

public class PermissionManager {
    private static final String[] PERMISSIONS = {
            "kick_permissions", "ban_permissions", "unban_permissions",
            "mute_permissions", "mass_tempRole_permissions", "embed_creator",
            "manage_linked_role", "manage_role_permissions", "create_role",
            "giveaway_access", "guild_admin"
    };

    public static boolean deleteCsv(String guildId) {
        String path = getCsvPath(guildId);
        File file = new File(path);

        if (file.exists()) {
            return file.delete();
        } else {
            System.out.println("File does not exist: " + path);
            return false;
        }
    }

    private static String getCsvPath(String guildId) {
        return "guilds/" + guildId + ".csv";
    }

    private static List<String[]> readCsv(String path) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            return reader.readAll();
        }
    }

    private static void writeCsv(String path, List<String[]> rows) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeAll(rows);
        }
    }

    public static void ensureCsvExists(String guildId) throws IOException {
        String path = getCsvPath(guildId);
        File file = new File(path);
        if (!file.exists()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
                String[] header = new String[PERMISSIONS.length + 2];
                header[0] = "ID";
                header[1] = "Type";
                System.arraycopy(PERMISSIONS, 0, header, 2, PERMISSIONS.length);
                writer.writeNext(header);
            }
        }
    }

    private static int getPermissionIndex(List<String> headers, String permission) {
        return headers.indexOf(permission);
    }

    public static Map<String, String> getPermissions(String guildId, String id) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));
        for (String[] row : rows) {
            if (row[0].equals(id)) {
                Map<String, String> permissionsMap = new HashMap<>();
                for (int i = 2; i < row.length; i++) {
                    permissionsMap.put(rows.get(0)[i], row[i]);
                }
                return permissionsMap;
            }
        }
        return null;
    }

    public static boolean doesGuildCsvExist(String guildId) {
        return new File(getCsvPath(guildId)).exists();
    }

    public static void addPermission(String guildId, String id, String permission, int value) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));
        List<String> headers = new ArrayList<>(Arrays.asList(rows.get(0)));

        if (!headers.contains(permission)) {
            headers.add(permission);
        }

        boolean updated = false;
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(id)) {
                if (headers.indexOf(permission) >= row.length) {
                    row = Arrays.copyOf(row, headers.size());
                    Arrays.fill(row, row.length, headers.size(), "0");
                }
                row[getPermissionIndex(headers, permission)] = String.valueOf(value);
                rows.set(i, row);
                updated = true;
                break;
            }
        }

        if (!updated) {
            String[] newRow = new String[headers.size()];
            Arrays.fill(newRow, "0");  // Fill all columns with "0"
            newRow[0] = id;  // Set the ID in the first column
            newRow[getPermissionIndex(headers, permission)] = String.valueOf(value);
            rows.add(newRow);
        }

        rows.set(0, headers.toArray(new String[0]));
        writeCsv(getCsvPath(guildId), rows);
    }

    public static void removePermission(String guildId, String id, String permission) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));
        List<String> headers = new ArrayList<>(Arrays.asList(rows.get(0)));

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(id)) {
                int permIndex = getPermissionIndex(headers, permission);
                if (permIndex != -1) {
                    row[permIndex] = "0";
                    rows.set(i, row);
                    break;
                }
            }
        }

        writeCsv(getCsvPath(guildId), rows);
    }

    public static boolean hasPermission(String guildId, String id, String permission) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));
        List<String> headers = Arrays.asList(rows.get(0));

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(id)) {
                int permIndex = getPermissionIndex(headers, permission);
                return permIndex != -1 && "1".equals(row[permIndex]);
            }
        }
        return false;
    }

    public static boolean rowExistsById(String guildId, String id) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static void setGuildAdmin(String guildId, String id) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));

        boolean updated = false;
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(id)) {
                Arrays.fill(row, "0");  // Set all values to "0"
                row[0] = id;  // Set the ID in the first column
                Arrays.fill(row, 1, row.length, "1");  // Set all permissions to "1"
                rows.set(i, row);
                updated = true;
                break;
            }
        }

        if (!updated) {
            String[] headers = rows.get(0);
            String[] newRow = new String[headers.length];
            Arrays.fill(newRow, "0");  // Fill all columns with "0"
            newRow[0] = id;  // Set the ID in the first column
            Arrays.fill(newRow, 1, newRow.length, "1");  // Set all permissions to "1"
            rows.add(newRow);
        }

        writeCsv(getCsvPath(guildId), rows);
    }


    public static void removeGuildAdmin(String guildId, String id) throws IOException, CsvException {
        ensureCsvExists(guildId);
        List<String[]> rows = readCsv(getCsvPath(guildId));

        boolean updated = false;
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(id)) {
                Arrays.fill(row, "0");
                row[0] = id;
                rows.set(i, row);
                updated = true;
                break;
            }
        }

        if (!updated) {
            String[] headers = rows.get(0);
            String[] newRow = new String[headers.length];
            Arrays.fill(newRow, "1");
            newRow[0] = id;
            rows.add(newRow);
        }

        writeCsv(getCsvPath(guildId), rows);
    }
}
