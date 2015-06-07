/*
 *  Copyright 2015 Balder Van Camp

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
package com.truvo.common.yamlprops.parser;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.truth.Truth;
/**
 *
 */
public class YamlConfigToPropertiesTest {
	
	private YamlPropertyConverter yamlPropertyConverter = new YamlPropertyConverterImpl();
	private Map<String, Properties> propmap;

	@Before
	public void setup() {
		propmap = yamlPropertyConverter.convert(new InputStreamReader(YamlConfigToPropertiesTest.class.getResourceAsStream("/configuration.yaml"), Charset.forName("UTF-8")));
	}
	
	@Test
	public void mapContainsAllKeys() {
		Truth.assertThat((Iterable<String>) propmap.keySet()).containsAllOf("dev", "test", "stage", "prod", "default");
	}
	
	@Test
	public void propertyDevContainAllKeys() {
		Truth.assertThat(propmap.get("dev").keySet()).containsExactly("application.name", "application.version");
	}
	
	@Test
	public void propertyDevKeyHasValue() {
		Truth.assertThat(propmap.get("dev").get("application.name")).isEqualTo("Test[Dev]");
	}
	
	@Test
	public void propertyTestKeyHasValue() {
		Truth.assertThat(propmap.get("test").get("application.name")).isEqualTo("Test[Test]");
	}
	@Test
	public void propertyKeyNotAvailableKeyIsAbsent() {
		Assert.assertFalse("Key was found while not expected", propmap.get("stage").containsKey("application.version"));
	}
	
	@Test
	public void propertyKeyAvailableButValueEmptyIsEmptyString() {
		Assert.assertTrue("Key was found while not expected", propmap.get("test").containsKey("application.version"));
		Assert.assertEquals("value was not empty", "", propmap.get("test").get("application.version"));
	}
}