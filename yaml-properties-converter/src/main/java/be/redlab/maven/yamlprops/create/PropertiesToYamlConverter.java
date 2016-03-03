package be.redlab.maven.yamlprops.create;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesToYamlConverter {

    private YamlWriter yamlWriter;

    public PropertiesToYamlConverter() {
        this(new SimpleYamlWriter());
    }

    public PropertiesToYamlConverter(YamlWriter yamlWriter) {
        this.yamlWriter = yamlWriter;
    }

    public Map<String, Map<String, String>> addProperties(String fileKey, Properties properties, Map<String, Map<String, String>> propertiesMap) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key= String.valueOf(entry.getKey());
            Map<String, String> map = propertiesMap.get(key);
            if (null == map) {
                map = new HashMap<String, String>();
                propertiesMap.put(key, map);
            }
            map.put(fileKey, String.valueOf(entry.getValue()));
        }
        return propertiesMap;
    }

    public Map<String, Map<String, String>> addProperties(String fileKey, Properties properties) {
        return this.addProperties(fileKey, properties, new HashMap<String, Map<String, String>>());
    }

    public void writeYaml(Writer out, Map<String, Map<String, String>> properties) throws IOException {
        this.yamlWriter.write(properties, out);
    }

}
