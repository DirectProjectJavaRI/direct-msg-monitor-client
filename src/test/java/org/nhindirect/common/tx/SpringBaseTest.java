package org.nhindirect.common.tx;

import org.junit.jupiter.api.extension.ExtendWith;
import org.nhindirect.common.tx.mock.MockTxsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:bootstrap.properties")
public abstract class SpringBaseTest
{
	@Autowired
	protected TxService client;
	
	@Autowired
	protected MockTxsResource resource;
}
