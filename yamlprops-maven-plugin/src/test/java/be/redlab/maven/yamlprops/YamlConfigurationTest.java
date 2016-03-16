package be.redlab.maven.yamlprops;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("dev", "dev.properties");
        c.setFiles(map);
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(PLAIN);
        Yaml y = new Yaml(dumperOptions);
        System.out.println( y.dump(c));
        YamlConfiguration load = y.loadAs(y.dump(c), YamlConfiguration.class);
        Assert.assertEquals("location", load.getLocation());
        Assert.assertEquals("properties", load.getType());
        Assert.assertEquals("dev.properties", c.getFiles().get("dev"));
    }
}
