package com.truvo.common.yamlprops.parser;

import java.io.Reader;
import java.util.Map;
import java.util.Properties;

public interface YamlPropertyConverter {

	Map<String, Properties> convert(Reader input);

}
