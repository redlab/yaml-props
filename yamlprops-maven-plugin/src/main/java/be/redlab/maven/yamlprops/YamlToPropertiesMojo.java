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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

import be.redlab.maven.yamlprops.parser.YamlPropertyConverter;
import be.redlab.maven.yamlprops.parser.YamlPropertyConverterImpl;

/**
 * @author redlab
 */
@Mojo(name = "yamlprops", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YamlToPropertiesMojo extends AbstractMojo {

    /**
     * The yaml file containing the properties to parse. If the path is relative
     * (does not start with / or a drive letter like C:), the path is relative
     * to the directory containing the POM. <br />
     * defaults to: "src/main/resources/settings.yaml"
     */
    @Parameter(property = "yamlfile", defaultValue = "src/main/resources/settings.yaml")
    protected File yamlfile;
    /**
     * The yamlprops configuration file location. If the path is relative (does not start with / or a
     * drive letter like C:), the path is relative to the directory containing
     * the POM. <br />
     * defaults to "src/main/resources/yamlprops.yaml"
     */
    @Parameter(property = "configuration", defaultValue = "src/main/resources/yamlprops.yaml")
    protected File configuration;
    /**
     *  The export dir. Defaults to ${project.build.outputDirectory} and is relative to the project root when given.
     */
    @Parameter(property = "targetDir", defaultValue = "${project.build.outputDirectory}")
    protected File targetDir;
    /**
     * The character encoding scheme to be applied when parsing the yaml file.
     * Defaults to ${project.build.sourceEncoding}.
     */
    @Parameter(property = "readEncoding", defaultValue = "${project.build.sourceEncoding}")
    protected String readEncoding;
    /**
     * The character encoding scheme to be applied when writing files. Defaults
     * to ${project.build.sourceEncoding}.
     */
    @Parameter(property = "writeEncoding", defaultValue = "${project.build.sourceEncoding}")
    protected String writeEncoding;
    /**
     * Ignore yaml file not found. Defaults to false. Set true if you do not
     * want the build to fail when source file is not found.
     */
    @Parameter(property = "ignoreNotFound", defaultValue = "false")
    protected boolean ignoreNotFound;

    public void execute() throws MojoExecutionException, MojoFailureException {
        YamlConfiguration yamlConfiguration = getYamlConfiguration();
        getLog().info("Parsing and writing properties of " + yamlfile + " to ");

        YamlPropertyConverter converter = new YamlPropertyConverterImpl();
        Map<String, Properties> map = null;
        try {
            map = converter.convert(StringUtils.isBlank(readEncoding) ? new InputStreamReader(new FileInputStream(yamlfile)) : new InputStreamReader(new FileInputStream(yamlfile),
                    readEncoding));
        } catch (UnsupportedEncodingException e) {
            throw new MojoExecutionException("Unable to use provided encoding", e);
        } catch (FileNotFoundException e) {
            if (ignoreNotFound) {
                super.getLog().warn("Unable to find provided yaml file at " + yamlfile.getAbsolutePath());
                super.getLog().debug(e);
            } else {
                throw new MojoExecutionException("Unable to find provided yaml file ", e);
            }
        }
        String location = yamlConfiguration.getLocation();
        File baseDirectoryOfExport;
        if (StringUtils.isNotBlank(location)) {
            baseDirectoryOfExport = FileUtils.resolveFile(targetDir, location);
        } else {
            baseDirectoryOfExport = targetDir;
        }
        if (null != map) {
            if (!baseDirectoryOfExport.mkdirs()) {
                getLog().debug("Directory " + baseDirectoryOfExport + " not created, it probably already exists?.");
            } else {
                getLog().debug("Directory " + baseDirectoryOfExport + " created.");
            }
            for (Entry<String, Properties> e : map.entrySet()) {
                File directoryOfExport = baseDirectoryOfExport;
                FileOutputStream stream = null;
                try {
                    String targetFile;
                    String extension = "properties";
                    boolean isXml = false;
                    if ("xml".equalsIgnoreCase(yamlConfiguration.getType())) {
                        extension = "xml";
                        isXml = true;
                    }
                    if (yamlConfiguration.getFiles().isEmpty() || !yamlConfiguration.getFiles().containsKey(e.getKey())) {
                        getLog().debug("Not found " + e.getKey() + " to file mapping, using key as file name");
                        targetFile = e.getKey() + "." + extension;
                    } else {
                        String configuredFileName = yamlConfiguration.getFiles().get(e.getKey());
                        String dirname = FileUtils.getPath(configuredFileName, yamlConfiguration.getFileSeparator());
                        directoryOfExport = new File(directoryOfExport, dirname);
                        directoryOfExport.mkdirs();
                        targetFile = FileUtils.removePath(configuredFileName, yamlConfiguration.getFileSeparator());
                        getLog().debug("found mapping " + configuredFileName + " for " + e.getKey() + " in " + directoryOfExport + " with file " + targetFile);
                    }
                    // targetFile
                    File propertyFile = new File(directoryOfExport, targetFile);
                    getLog().info("Writing to " + propertyFile);
                    boolean newFile = propertyFile.createNewFile();
                    stream = new FileOutputStream(propertyFile);
                    if (isXml) {
                        e.getValue().storeToXML(stream, "Written " + (newFile ? "new file" : "") + " from yaml on " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()), writeEncoding);
                    } else {
                        e.getValue().store(new OutputStreamWriter(stream, writeEncoding), "Written " + (newFile ? "new file" : "") + " from yaml on " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
                    }

                } catch (IOException e1) {
                    throw new MojoExecutionException("Unable to write file ", e1);
                } finally {
                    if (null != stream) {
                        try {
                            stream.close();
                        } catch (IOException e1) {
                            getLog().debug("Error closing stream", e1);
                        }
                    }
                }

            }

        }
    }

    private YamlConfiguration getYamlConfiguration() throws MojoFailureException {
        YamlConfiguration yamlConfiguration;
        if (null != configuration) {
            try {
                yamlConfiguration = new YamlConfigurationReaderWriter().getFromFile(configuration);
            } catch (FileNotFoundException e) {
                if (ignoreNotFound) {
                    getLog().info("Ignoring not found yaml configuration at " + configuration.getAbsolutePath() + " using default");
                    yamlConfiguration = new YamlConfiguration();
                } else {
                    throw new MojoFailureException("Unable to read configuration for yaml to properties.", e);
                }
            }
        } else {
            yamlConfiguration = new YamlConfiguration();
        }
        return yamlConfiguration;
    }

}
