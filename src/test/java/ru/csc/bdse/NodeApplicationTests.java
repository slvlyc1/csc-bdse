package ru.csc.bdse;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.shaded.com.google.common.util.concurrent.Futures;


import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class NodeApplicationTests {

/*
	@ClassRule
	public static GenericContainer node0 =
			new GenericContainer(Futures.immediateFuture("(csc/bdse"))
					.withExposedPorts(6379);
*/

	@ClassRule
	public static GenericContainer node0 =
			new GenericContainer(CompletableFuture.completedFuture("csc/bdse:latest"))
					.withExposedPorts(6379);


/*
	@Rule
	public GenericContainer dslContainer = new GenericContainer(
			new ImageFromDockerfile()
					.withFileFromString("Dockerfile", "hello")).;
*/


	@Test
	public void contextLoads() {
	}

}
