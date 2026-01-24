package be.redlab.maven.yamlprops;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

import static org.yaml.snakeyaml.DumperOptions.ScalarStyle.PLAIN;

public class YamlConfigurationTest {

    @Test
    public void configurationFileRead() {
        YamlConfiguration c = new YamlConfiguration();
        c.setLocation("location");
        c.setType("properties");
        Map<String, String> map = new HashMap<>();
        map.put("dev", "dev.properties");
        c.setFiles(map);
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(PLAIN);
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(tag -> tag.getClassName().equals(YamlConfiguration.class.getName()));
        Yaml y = new Yaml(loaderOptions, dumperOptions);
        System.out.println( y.dump(c));
        YamlConfiguration load = y.loadAs(y.dump(c), YamlConfiguration.class);
        Assertions.assertEquals("location", load.getLocation());
        Assertions.assertEquals("properties", load.getType());
        Assertions.assertEquals("dev.properties", c.getFiles().get("dev"));
    }
}
