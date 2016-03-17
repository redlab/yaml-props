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
package be.redlab.maven.yamlprops;

import java.util.HashMap;
import java.util.Map;

public class YamlConfiguration {

    private String location;
    private String type;
    private Map<String, String> files;
    private char fileSeparator = '/';

    public YamlConfiguration() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getFiles() {
        if (null == files) {
            return new HashMap<String, String>();
        }
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public char getFileSeparator() {
        return fileSeparator;
    }

    public void setFileSeparator(char fileSeparator) {
        this.fileSeparator = fileSeparator;
    }
}
