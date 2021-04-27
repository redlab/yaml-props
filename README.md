# yaml-props

A Maven plugin to parse a yaml file with a certain structure to property files.

## A bit of background
This project was developed to replace the current way of generating property and settings files from Excel files at Truvo&reg;.
 After migrating from svn to git it became a real horror to maintain settings and messages (e.g. translations) in Excel files. Certainly when changing the Excel in different branches. I probably don't need to draw a picture for you to get the point. (For what it's worth I found that it was a horror in svn to)

## The yaml file(s)

The plugin requires 2 files per execution. One with the configuration and one with the content. In the future the configuration file might be enclosed as a first document in the settings file.

The configuration one contains information for yamlprops-maven-plugin on how to export the content. It has a specific structure since it's loaded in the `YamlConfiguration.class` object.
Example:
```yaml
location: 'configfiles/'
type: 'properties'
files:
  default: settings-default.properties
  dev:  'dev/settings.properties'
  key: 'file.properties'
  test: 'test/settings.properties'
  prod: 'production/settings.properties'
```

Note: all elements are optional
* The *location* field defines the directory relative to modules project.build.outputDirectory.
* The *type* field can have any value it will always be a property file. Except for value xml then the property file is exported as xml.
* the *files* is a map that optionally defines in which files certain properties end up. Properties with that have a key that conforms to a key in this map will end up in a file that is defined by the value in this map. When a key is not in this map, the key itself will be used as property file name.

Example properties yaml file.

```yaml
some.db.url:
  default: localhost:5432
  dev: db-dev.example.org:5432
  test: db-test.example.org:5432
  prod: db-prod.example.org:5432
some.db.user:
  default: dbuser
some.db.pass:
  default: aaa
  dev: flzue554rfjfrujeuj
  test: dliergigoijiz45kgkf
  prod: yojdEGGsk49kE33

```

Given the above configuration and the maven configuration
```xml
<plugin>
    <groupId>be.redlab.maven</groupId>
    <artifactId>yamlprops-maven-plugin</artifactId>
    <version>1.1</version>
    <configuration>
        <yamlfile>src/main/resources/configuration.yaml</yamlfile>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>yamlprops</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
the property files would be as follows: 4 property files exported to target/classes/configfiles/
* settings-default.properties containing some.db.url, some.db.user, some.db.pass
* dev/settings.properties containing some.db.url, some.db.pass
* test/settings.properties containing some.db.url, some.db.pass
* prod/settings.properties containing some.db.url, some.db.pass


### Other Available parameters for the maven plugin:
output of mvn be.redlab.maven:yamlprops-maven-plugin:help -Ddetail=true 

*    ignoreNotFound (Default: false)

      Ignore yaml file not found. Defaults to false. Set true if you do not want
      the build to fail when source file is not found.
      User property: ignoreNotFound

*    readEncoding (Default: ${project.build.sourceEncoding})

      The character encoding scheme to be applied when parsing the yaml file.
      Defaults to ${project.build.sourceEncoding}.
      User property: readEncoding

 *   writeEncoding (Default: ${project.build.sourceEncoding})
 
      The character encoding scheme to be applied when writing files. Defaults
      to ${project.build.sourceEncoding}.
      User property: writeEncoding

 *  yamlfile (Default: src/main/resources/settings.yaml)
 
      The yaml file containing the properties to parse. If the path is relative
      (does not start with / or a drive letter like C: ), the path is relative to
      the directory containing the POM.
      defautl: 'src/main/resources/settings.yaml'
      User property: yamlfile
*    targetDir (Default: /)

      The target location. If the path is relative (does not start with / or a
      drive letter like C:), the path is relative to the directory containing
      the POM.
      defaults to '${}/'
      User property: targetDir
*   configuration

    the location f the configuration file, defaults to rc/main/resources/yamlprops.yaml
