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

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;

public class YamlConvertCli {

    public static void main(String ... args) throws IOException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        options.addOption(Option.builder("s").argName("source").desc("the source folder or file to convert").longOpt("source").hasArg(true).build());
        options.addOption(Option.builder("t").argName("target").desc("the target file to store in").longOpt("target").hasArg(true).build());
        options.addOption(Option.builder("h").desc("print help").build());

        try {
            CommandLine cmd = parser.parse( options, args);
            if (cmd.hasOption('h')) {
                formatter.printHelp( "converter", options );
            }
            File source = new File(cmd.getOptionValue("s", System.getProperty("user.dir")));
            String name = source.getName();
            if (source.isDirectory()) {
                PropertiesToYamlConverter yamlConverter = new PropertiesToYamlConverter();
                String [] ext = {"properties"};
                Iterator<File> fileIterator = FileUtils.iterateFiles(source, ext, true);
                while (fileIterator.hasNext()) {
                    File next = fileIterator.next();
                    System.out.println(next);
                    String s = StringUtils.removeStart(next.getParentFile().getPath(), source.getPath());
                    System.out.println(s);
                    String f =  StringUtils.split(s, IOUtils.DIR_SEPARATOR)[0];
                    System.out.println("key = "+f);
                    Properties p = new Properties();
                    try {
                        p.load(new FileReader(next));
                        yamlConverter.addProperties(f, p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileWriter fileWriter = new FileWriter(new File(source.getParentFile(), StringUtils.substringBeforeLast(name, ".") + ".yaml"));
                yamlConverter.writeYaml(fileWriter);
                fileWriter.close();
            } else {
                Properties p = new Properties();
                p.load(new FileReader(source));
                FileWriter fileWriter = new FileWriter(new File(source.getParentFile(), StringUtils.substringBeforeLast(name, ".") + ".yaml"));
                new PropertiesToYamlConverter().addProperties(name, p).writeYaml(fileWriter);
                fileWriter.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp( "converter", options );
        }
    }

}
