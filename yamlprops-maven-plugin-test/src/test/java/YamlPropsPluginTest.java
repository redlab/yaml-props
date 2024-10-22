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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

public class YamlPropsPluginTest {

    @Test
    public void validateFileWithoutKeyNameMappingCreated() {
        Properties p = new Properties();
        try {
            p.load(YamlPropsPluginTest.class.getResourceAsStream("/test-yamlprops/default.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Failed with " + e.getMessage());
        }
    }

    @Test
    public void validateFileWithKeyNameMappingCreated() {
        Properties p = new Properties();
        try {
            p.load(YamlPropsPluginTest.class.getResourceAsStream("/test-yamlprops/production/settings.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Failed with " + e.getMessage());
        }
    }
}
