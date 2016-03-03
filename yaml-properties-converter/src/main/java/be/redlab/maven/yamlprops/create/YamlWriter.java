package be.redlab.maven.yamlprops.create;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface YamlWriter {
    void write(Map<String, Map<String, String>> properties, Writer out) throws IOException;
}
