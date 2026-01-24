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
package be.redlab.maven.yamlprops.parser;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
/**
 *
 */
public class YamlConfigToPropertiesTest {
	
	private final YamlPropertyConverter yamlPropertyConverter = new YamlPropertyConverterImpl();
	private Map<String, Properties> propmap;

	@BeforeEach
	public void setup() {
		InputStream resourceAsStream = YamlConfigToPropertiesTest.class.getResourceAsStream("/configuration.yaml");
		Truth.assertWithMessage("Test requires a configuration.yaml on classpath ").that(resourceAsStream).isNotNull();
		propmap = yamlPropertyConverter.convert(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
	}
	
	@org.junit.jupiter.api.Test
	public void mapContainsAllKeys() {
		Truth.assertThat(propmap.keySet()).containsExactly("dev", "test", "stage", "prod", "default");
	}
	
	@Test
	public void propertyDevContainAllKeys() {
		Truth.assertThat(propmap.get("dev").keySet()).containsExactly("application.name", "application.version");
	}
	
	@org.junit.jupiter.api.Test
	public void propertyDevKeyHasValue() {
		Truth.assertThat(propmap.get("dev").get("application.name")).isEqualTo("Test[Dev]");
	}
	
	@org.junit.jupiter.api.Test
	public void propertyTestKeyHasValue() {
		Truth.assertThat(propmap.get("test").get("application.name")).isEqualTo("Test[Test]");
	}
	@org.junit.jupiter.api.Test
	public void propertyKeyNotAvailableKeyIsAbsent() {
		Assertions.assertFalse(propmap.get("stage").containsKey("application.version"), "Key was found while not expected");
	}
	
	@org.junit.jupiter.api.Test
	public void propertyKeyAvailableButValueEmptyIsEmptyString() {
		Assertions.assertTrue(propmap.get("test").containsKey("application.version"), "Key was found while not expected");
		Assertions.assertEquals("", propmap.get("test").get("application.version"), "value was not empty");
	}
}