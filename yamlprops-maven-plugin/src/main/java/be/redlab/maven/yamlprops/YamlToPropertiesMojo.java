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
package be.redlab.maven.yamlprops;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.codehaus.plexus.util.StringUtils;

import be.redlab.maven.yamlprops.parser.YamlPropertyConverter;
import be.redlab.maven.yamlprops.parser.YamlPropertyConverterImpl;

/**
 * 
 * @author redlab
 *
 */
@Mojo(name = "yamlprops", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YamlToPropertiesMojo extends AbstractMojo {

	/**
	 * The yaml file containing the properties to parse. If the path is relative
	 * (does not start with / or a drive letter like C:), the path is relative
	 * to the directory containing the POM. <br />
	 * defautl: "src/main/resources/settings.yaml"
	 */
	@Parameter(property = "yamlfile", defaultValue = "src/main/resources/settings.yaml")
	protected File yamlfile;
	/**
	 * The target location. If the path is relative (does not start with / or a
	 * drive letter like C:), the path is relative to the directory containing
	 * the POM. <br />
	 * defaults to "src/main/resources/settings/${project.artifactId}/"
	 */
	@Parameter(property = "target", defaultValue = "src/main/resources/settings/${project.artifactId}/")
	protected File target;

	/**
	 * An optional target file. This will generate the yaml propertiesfiles with
	 * the given targetFile name instead of giving name as defined in the yaml
	 * config. The defined names in the yaml config are used as folders.
	 */
	@Parameter(property = "targetFile", defaultValue = "")
	protected String targetFile;
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
	/**
	 * The file extension for generated property files. Defaults to properties
	 */
	@Parameter(property = "extension", defaultValue = "properties")
	private String extension;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Parsing and writing properties of " + yamlfile + " to " + target);
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
		if (null != map) {
			for (Entry<String, Properties> e : map.entrySet()) {
				FileOutputStream stream = null;
				try {
					if (!target.mkdirs()) {
						getLog().debug("Directory " + target + " not created, it probably already exists?.");
					} else {
						getLog().debug("Directory " + target + " created.");
					}
					File file;
					if (StringUtils.isBlank(targetFile)) {
						file = new File(target, e.getKey() + "." + extension);
					} else {
						File targetDir = new File(target, e.getKey());
						if (!targetDir.mkdirs()) {
							getLog().debug("Not creating " + targetDir);
						} else {
							getLog().debug("created " + targetDir);
						}
						file = new File(targetDir, targetFile + "." + extension);
					}
					boolean newFile = file.createNewFile();
					stream = new FileOutputStream(file);
					e.getValue().store(stream, "Written "+ (newFile?"new file":"") +" from yaml on " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
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

}
