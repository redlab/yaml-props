package be.redlab.maven.yamlprops.create;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class SimpleYamlWriter implements YamlWriter {
    @Override
    public void write(Map<String, Map<String, String>> properties, Writer out) throws IOException {
        BufferedWriter writer = new BufferedWriter(out);
        for (Map.Entry<String, Map<String, String>> entry : properties.entrySet()) {
            writer.write(entry.getKey());
            writer.write(':');
            writer.newLine();
            for (Map.Entry<String, String> propEntry : entry.getValue().entrySet()) {
                writer.write("  ");
                writer.write(propEntry.getKey());
                writer.write(": ");
                writer.write(escape(propEntry.getValue()));
                writer.newLine();
            }

        }
        writer.flush();
    }

    private String escape(String value) {
        // ScalarEvent e = new ScalarEvent();
        return value;
    }

}
