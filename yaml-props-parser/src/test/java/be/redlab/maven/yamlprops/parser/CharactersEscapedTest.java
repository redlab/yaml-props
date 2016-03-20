/*
 *  Copyright 2016 Balder Van Camp

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package be.redlab.maven.yamlprops.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

/**
 * Test to see if conversions are done ok. ( made for #2 but forgot that #, !, =, and : are escaped, so not really needed )
 */
public class CharactersEscapedTest {
    @Rule
    public TemporaryFolder folder =new TemporaryFolder();
    private YamlPropertyConverter yamlPropertyConverter = new YamlPropertyConverterImpl();
    private Map<String, Properties> propmap;
    private Properties properties;

    @Before
    public void setup() throws IOException {
        propmap = yamlPropertyConverter.convert(new InputStreamReader(YamlConfigToPropertiesTest.class.getResourceAsStream("/specialchars.yaml"), Charset.forName("UTF-8")));
        Properties key = propmap.get("key");
        File file = folder.newFile("key.properties");
        key.store(new FileOutputStream(file), "test");
        System.out.println(file);
        properties = new Properties();
        properties.load(new FileInputStream(file));
    }

    @Test
    public void anUrl() {
        Assert.assertEquals("http://www.example.org", properties.getProperty("a.url"));
    }
    @Test
    public void aUrlWithPort() {
        Assert.assertEquals("https://www.example.org:443/", properties.getProperty("url.with.port"));
    }
}
