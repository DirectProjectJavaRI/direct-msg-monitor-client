package org.nhindirect.common.tx;

import org.junit.runner.RunWith;
import org.nhindirect.common.tx.mock.MockTxsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext
@TestPropertySource("classpath:bootstrap.properties")
public abstract class SpringBaseTest
{
	@Autowired
	protected TxService client;
	
	@Autowired
	protected MockTxsResource resource;
}
