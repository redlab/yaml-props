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
package be.redlab.maven.yamlprops.create;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class SimpleYamlWriter implements YamlWriter {
    @Override
    public void write(Map<String, Map<String, String>> properties, Writer out) throws IOException {
        BufferedWriter writer = new BufferedWriter(out);
        for (Map.Entry<String, Map<String, String>> entry : properties.entrySet()) {
            writer.write(entry.getKey());
            writer.write(':');
            writer.newLine();
            for (Map.Entry<String, String> propEntry : entry.getValue().entrySet()) {
                writer.write("  ");
                writer.write(propEntry.getKey());
                writer.write(": ");
                writer.write(escape(propEntry.getValue()));
                writer.newLine();
            }

        }
        writer.flush();
    }

    private String escape(String value) {
        // ScalarEvent e = new ScalarEvent();
        return value;
    }

}
