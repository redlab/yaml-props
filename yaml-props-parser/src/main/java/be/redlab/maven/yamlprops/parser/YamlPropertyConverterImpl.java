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

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.events.ScalarEvent;

/**
 * 
 *	Parses a yaml formated file to different property files.<br />Format of the yaml properties
 *<pre>
 *a.key.for.property:
 *  file1: fileOnePropertyValue
 *  file2: fileTwoPropertyValue
 *another.key:
 *  file2: fileTwoAnotherPropertyValue
 *more.key:
 *  file3: fileThreeAnotherPropertyValue
 *  file2: fileThreeAnotherPropertyValue
 *  file1: 
 *</pre>
 * and so on. Each of the keys will be put in the different files, for file found that does not exist yet, a new Properties will be created.
 * The result returned by {@link #convert(Reader)} for the above yaml example will be a map with 3 keys, <code>file1</code>, <code>file2</code> and <code>file3</code><p><code>file1</code> will contain properties for 2 keys, 
 * <code>file2</code> properties for 3 keys and <code>file3</code> properties for 1 key.</p>
 *
 */
public class YamlPropertyConverterImpl implements YamlPropertyConverter {

	@Override
	public Map<String, Properties> convert(Reader input) {
		Yaml y = new Yaml();
		Map<String, Properties> propertiesMap = new HashMap<String, Properties>();
		String currentKey = "";
		String currentEnv = "";
		for (Event event : y.parse(input)) {
			// mapping: scalar
			int level = event.getStartMark().getColumn();
			if (event.is(ID.Scalar)) {
				ScalarEvent s = (ScalarEvent) event;
				if (level == 0) {
					currentKey = s.getValue();
				} else if (level == 2) {
					currentEnv = s.getValue();
				} else {
					Properties properties = propertiesMap.get(currentEnv);
					properties = createIfNoProperties(propertiesMap, currentEnv, properties);
					properties.put(currentKey, s.getValue());
				}

			}
		}

		return propertiesMap;
	}

	private Properties createIfNoProperties(Map<String, Properties> propertiesMap, String currentEnv, Properties properties) {
		if (null == properties) {
			properties =  new Properties(); 
			propertiesMap.put(currentEnv,properties);
		}
		return properties;
	}
}
