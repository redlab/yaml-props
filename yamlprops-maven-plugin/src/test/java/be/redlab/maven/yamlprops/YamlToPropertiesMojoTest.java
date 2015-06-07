package be.redlab.maven.yamlprops;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class YamlToPropertiesMojoTest extends AbstractMojoTestCase {

	protected void setUp() throws Exception {
		// required
		super.setUp();
	}

	/** {@inheritDoc} */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testSomething() throws Exception {

		File pom = super.getTestFile("./src/test/resources/project-to-test-pom.xml");
		assertNotNull(pom);
		assertTrue(pom.exists());
		super.newMojoExecution("yamlparse");
//		YamlToPropertiesMojo myMojo = (YamlToPropertiesMojo)  
//		assertNotNull(myMojo);
//		myMojo.execute();
	}
}