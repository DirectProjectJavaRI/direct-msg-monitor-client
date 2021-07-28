package org.nhindirect.common.tx.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;

import org.nhindirect.common.tx.BaseTestPlan;
import org.nhindirect.common.tx.SpringBaseTest;
import org.nhindirect.common.tx.TestUtils;
import org.nhindirect.common.tx.model.Tx;
import org.nhindirect.common.tx.model.TxDetailType;
import org.nhindirect.common.tx.model.TxMessageType;

public class RESTTxServiceClient_addTxTest extends SpringBaseTest
{
	abstract class TestPlan<T> extends BaseTestPlan 
	{

		
		@Override
		protected void tearDownMocks()
		{
			if (resource != null)
				resource.clearTxState();
		}

		
		protected Collection<T> getTxsToSubmit()
		{
			return Collections.emptyList();
		}
									
		
		@Override
		protected void performInner() throws Exception
		{
			Collection<T> txs = getTxsToSubmit();
			if (txs != null)
				for (T tx : txs)
					trackMessage(tx);
				
			doAssertions(resource.getTxs());
		}
		
		
		protected void doAssertions(Collection<Tx> txs) throws Exception
		{
			
		}
		
		protected abstract void trackMessage(T tx) throws Exception;
	}	
	
	@Test
	public void testSingle_assertTxReceived() throws Exception
	{
		new TestPlan<Tx>()
		{
			
			@Override
			protected void trackMessage(Tx tx) throws Exception
			{
				client.trackMessage(tx);
			}
			
			@Override
			protected Collection<Tx> getTxsToSubmit()
			{
				Collection<Tx> txs = new ArrayList<Tx>();
				
				// send original message
				final String originalMessageId = UUID.randomUUID().toString();	
				
				Tx originalMessage = TestUtils.makeMessage(TxMessageType.IMF, originalMessageId, "", "gm2552@cerner.com", "gm2552@direct.securehealthemail.com", "");
				txs.add(originalMessage);
				
				return txs;

			}
			
			
			protected void doAssertions(Collection<Tx> txs) throws Exception
			{				
				assertEquals(1, txs.size());
				assertEquals(TxMessageType.IMF, txs.iterator().next().getMsgType());
			}
		}.perform();		
	}
	
	@Test
	public void testSendMimeMessage_assertTxsReceived() throws Exception
	{
		new TestPlan<MimeMessage>()
		{
			
			@Override
			protected Collection<MimeMessage> getTxsToSubmit()
			{					
				Collection<MimeMessage> txs = new ArrayList<MimeMessage>();
				try
				{
					MimeMessage msg = TestUtils.readMimeMessageFromFile("MDNMessage.txt");			

					txs.add(msg);
				}
				catch (Exception e){}
				
				return txs;

			}
			
			@Override
			protected void trackMessage(MimeMessage tx) throws Exception
			{
				client.trackMessage(tx);
			}
			
			protected void doAssertions(Collection<Tx> txs) throws Exception
			{				
				assertEquals(1, txs.size());
			}
		}.perform();		
	}
	
	@Test
	public void testSendMimeMessage_friendlyFinalRecipName_assertTxsReceived() throws Exception
	{
		new TestPlan<MimeMessage>()
		{
			
			@Override
			protected Collection<MimeMessage> getTxsToSubmit()
			{					
				Collection<MimeMessage> txs = new ArrayList<MimeMessage>();
				try
				{
					MimeMessage msg = TestUtils.readMimeMessageFromFile("MDNMessageFriendlyNameFinalRecip.txt");			

					txs.add(msg);
				}
				catch (Exception e){}
				
				return txs;

			}
			
			@Override
			protected void trackMessage(MimeMessage tx) throws Exception
			{
				client.trackMessage(tx);
			}
			
			protected void doAssertions(Collection<Tx> txs) throws Exception
			{				
				assertEquals(1, txs.size());
				final Tx tx = txs.iterator().next();
				assertEquals("externaluser <externuser1@starugh-stateline.com>",  tx.getDetail(TxDetailType.FINAL_RECIPIENTS).getDetailValue());
			}
		}.perform();		
	}
	
	@Test
	public void testSendInputStream_assertTxsReceived() throws Exception
	{
		new TestPlan<InternetHeaders>()
		{
			
			@SuppressWarnings("deprecation")
			@Override
			protected Collection<InternetHeaders> getTxsToSubmit()
			{					
				Collection<InternetHeaders> txs = new ArrayList<InternetHeaders>();
				try
				{
					
					InputStream inStr = IOUtils.toInputStream(TestUtils.readMessageFromFile("MessageWithAttachment.txt"));			
					InternetHeaders headers = new InternetHeaders(inStr);
					txs.add(headers);
					IOUtils.closeQuietly(inStr);
				}
				catch (Exception e){}
				
				return txs;

			}
			
			@Override
			protected void trackMessage(InternetHeaders tx) throws Exception
			{
				client.trackMessage(tx);
			}
			
			protected void doAssertions(Collection<Tx> txs) throws Exception
			{				
				assertEquals(1, txs.size());
			}
		}.perform();		
	}

}
