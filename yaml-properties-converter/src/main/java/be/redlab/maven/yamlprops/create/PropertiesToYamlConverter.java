/*
 *  Copyright 2024 Balder Van Camp

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
package be.redlab.maven.yamlprops.create;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesToYamlConverter {

    private final YamlWriter yamlWriter;
    private final Map<String, Map<String, String>> propertiesMap;

    public PropertiesToYamlConverter() {
        this(new SimpleYamlWriter());
    }

    public PropertiesToYamlConverter(YamlWriter yamlWriter) {
        this.yamlWriter = yamlWriter;
        propertiesMap = new HashMap<>();
    }

    public PropertiesToYamlConverter addProperties(String fileKey, Properties properties) {
        properties.forEach((k, v) -> {
            String key = String.valueOf(k);
            Map<String, String> map = propertiesMap.computeIfAbsent(key, newKey -> new HashMap<>());
            map.put(fileKey, String.valueOf(v));
        });
        return this;
    }


    public void writeYaml(Writer out) throws IOException {
        this.yamlWriter.write(propertiesMap, out);
    }

    public Map<String,Map<String,String>> getMap() {
        return this.propertiesMap;
    }
}
