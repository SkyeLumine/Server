package lightleaf.netcom.common;

import entities.Constants;

import java.io.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newOutputStream;

public class FileIO {
    public static List<String> readCSV(final Path path, final int size) throws IOException {
        try (BufferedReader reader = newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<String> result = new ArrayList<>(size);

            // Skip header line
            reader.readLine();

            for (;;) {
                String line = reader.readLine();
                if (line == null)
                    break;
                result.add(line);
            }
            return result;
        }
    }

    public static Map<String, String> mapCSV(final Path path, final int size) throws IOException {
        try (BufferedReader reader = newBufferedReader(path, StandardCharsets.UTF_8)) {
            Map<String, String> map = new HashMap<>(size);

            // Skip header line
            reader.readLine();

            for (;;) {
                final String line = reader.readLine();
                if (line == null || !line.contains(Constants.CSV_DATA_DELIMITER)) {
                    break;
                }

                final String[] tokens = line.split(Constants.CSV_DATA_DELIMITER);
                map.put(tokens[0].trim(), tokens[1].trim());
            }
            return map;
        }
    }

    public static void writeCSV(final Path path, final String header, final List<String> lines) throws IOException {
        Objects.requireNonNull(lines);
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        OutputStream out = newOutputStream(path);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
            writer.append(header);
            writer.newLine();
            for(int i = 0; i < lines.size(); ++i){
                final CharSequence line = lines.get(i);
                writer.append(line);
                if((i + 1) != lines.size()){
                    writer.newLine();
                }
            }
        }
    }
}
