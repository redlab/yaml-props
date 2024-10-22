package be.redlab.maven.yamlprops.create;

import com.google.common.truth.Truth;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

class PropertiesToYamlConverterTest {

    private Map<String, Map<String, String>> propertiesMap;
    ByteArrayOutputStream out;
        PropertiesToYamlConverter propertiesToYamlConverter;

    @BeforeEach
    void setup() {
        Properties setOne = new Properties();
        Properties setTwo = new Properties();
        setOne.put("key1", "theOne");
        setOne.put("key2", "key2value1");
        setOne.put("key3", "same");
        setTwo.put("key1", "theOther");
        setTwo.put("key3", "same");
        setTwo.put("key4", "@key4v@alue:2");
        propertiesToYamlConverter = new PropertiesToYamlConverter(new SnakeYamlWriter());
        propertiesMap = propertiesToYamlConverter.addProperties("file2", setTwo).addProperties("file1", setOne).getMap();


    }

    @Test
    public void differentValuesForSameKeyFromDifferentFile() {
        Map<String, String> key1 = propertiesMap.get("key1");
        Truth.assertThat(key1).containsEntry("file1", "theOne");
        Truth.assertThat(key1).containsEntry("file2", "theOther");
    }

    @Test
    public void sameValuesForSameKeyFromDifferentFile() {
        Map<String, String> key1 = propertiesMap.get("key3");
        Truth.assertThat(key1).containsEntry("file1", "same");
        Truth.assertThat(key1).containsEntry("file2", "same");
    }

    @Test
    public void keyInOneButNotInOther() {
        Map<String, String> key1 = propertiesMap.get("key2");
        Truth.assertThat(key1).containsEntry("file1", "key2value1");
        Truth.assertThat(key1).doesNotContainKey("file2");
    }

    @Test
    public void keyInOtherButNotInOne() {
        Map<String, String> key1 = propertiesMap.get("key4");
        Truth.assertThat(key1).containsEntry("file2", "@key4v@alue:2");
        Truth.assertThat(key1).doesNotContainKey("file1");
    }

    @Disabled
    @Test
    public void write() throws IOException {
        out = new ByteArrayOutputStream();
        propertiesToYamlConverter.writeYaml(new OutputStreamWriter(out));
        InputStream resourceAsStream = this.getClass().getResourceAsStream("expected.yaml");
        Truth.assertThat(resourceAsStream).isNotNull();
        String expected = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        Truth.assertThat(out.toString()).isEqualTo(expected);
    }
}