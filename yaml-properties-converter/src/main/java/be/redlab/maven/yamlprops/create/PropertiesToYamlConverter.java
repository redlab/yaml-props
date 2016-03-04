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
    private Map<String, Map<String, String>> propertiesMap;

    public PropertiesToYamlConverter() {
        this(new SimpleYamlWriter());
    }

    public PropertiesToYamlConverter(YamlWriter yamlWriter) {
        this.yamlWriter = yamlWriter;
        propertiesMap = new HashMap<String, Map<String, String>>();
    }

    public PropertiesToYamlConverter addProperties(String fileKey, Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key= String.valueOf(entry.getKey());
            Map<String, String> map = propertiesMap.get(key);
            if (null == map) {
                map = new HashMap<String, String>();
                propertiesMap.put(key, map);
            }
            map.put(fileKey, String.valueOf(entry.getValue()));
        }
        return this;
    }


    public void writeYaml(Writer out) throws IOException {
        this.yamlWriter.write(propertiesMap, out);
    }

    public Map<String,Map<String,String>> getMap() {
        return this.propertiesMap;
    }
}
