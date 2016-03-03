package be.redlab.maven.yamlprops.create;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.yaml.snakeyaml.DumperOptions.ScalarStyle.LITERAL;
import static org.yaml.snakeyaml.DumperOptions.ScalarStyle.PLAIN;

/**
 * Created by redlab on 03.03.16.
 */
public class SnakeYamlWriter implements YamlWriter {
    @Override
    public void write(Map<String, Map<String, String>> properties, Writer out) throws IOException {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(PLAIN);
        Yaml y = new Yaml(dumperOptions);
        y.dump(properties, out);
        for (Map.Entry<String, Map<String, String>> entry : properties.entrySet()) {

            for (Map.Entry<String, String> stringEntry : entry.getValue().entrySet()) {

            }


        }


    }
}
