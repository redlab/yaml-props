# yaml-props
Create Java Properties files from a yaml file

## yamlprops:yamlprops

### Available parameters:
output of mvn be.redlab.maven:yamlprops-maven-plugin:help -Ddetail=true 

*    extension (Default: properties)

      The fle extension for generated property files. Defaults to properties
      User property: extension

*    ignoreNotFound (Default: false)

      Ignore yaml file not found. Defaults to false. Set true if you do not want
      the build to fail when source file is not found.
      User property: ignoreNotFound

*    readEncoding (Default: ${project.build.sourceEncoding})

      The character encoding scheme to be applied when parsing the yaml file.
      Defaults to ${project.build.sourceEncoding}.
      User property: readEncoding

*    target (Default: src/main/resources/settings/${project.artifactId}/)

      The target location. If the path is relative (does not start with / or a
      drive letter like C:), the path is relative to the directory containing
      the POM.
      defaults to 'src/main/resources/settings/'
      User property: target

 *   writeEncoding (Default: ${project.build.sourceEncoding})
 
      The character encoding scheme to be applied when writing files. Defaults
      to ${project.build.sourceEncoding}.
      User property: writeEncoding

 *  yamlfile (Default: src/main/resources/settings.yaml)
 
      The yaml file containing the properties to parse. If the path is relative
      (does not start with / or a drive letter like C:), the path is relative to
      the directory containing the POM.
      defautl: 'src/main/resources/settings.yaml'
      User property: yamlfile


POM excerpt

```
<plugin>
	<groupId>be.redlab.maven</groupId>
	<artifactId>yamlprops-maven-plugin</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<configuration>
		<!-- Specify the target folder to parse to -->
		<target>target/generated-sources/config/</target>
		<!-- Specify the source yaml file -->
		<yamlfile>src/main/config/configuration.yaml</yamlfile>
		<!-- Specify the encoding of source yaml file, you can also use writeEncoding to define the encoding to write in, if not given readEncoding is used -->
		<readEncoding>UTF-8</readEncoding>
	</configuration>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>yamlprops</goal>
			</goals>
		</execution>
	</executions>
</plugin>

```
