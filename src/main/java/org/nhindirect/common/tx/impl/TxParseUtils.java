package org.nhindirect.common.tx.impl;

import java.util.Enumeration;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

import org.nhindirect.common.tx.TxDetailParser;
import org.nhindirect.common.tx.TxUtil;
import org.nhindirect.common.tx.model.Tx;
import org.nhindirect.common.tx.model.TxDetail;
import org.nhindirect.common.tx.model.TxDetailType;
import org.nhindirect.common.tx.model.TxMessageType;

public class TxParseUtils
{
	public static Tx convertMimeMessageToTx(MimeMessage msg, TxDetailParser parser)
	{
        if (msg == null) 
        {
            throw new IllegalArgumentException("Invalid parameter received. Message cannot be null.");
        }
		
        final Map<String, TxDetail> details = parser.getMessageDetails(msg);
        return new Tx(TxUtil.getMessageType(msg), details);
	}
	
	public static String getOriginalMessageId(Tx tx, TxDetailParser parser)
	{
		///CLOVER:OFF
        if (tx == null) 
        {
            throw new IllegalArgumentException("Invalid parameter received. Tx cannot be null.");
        }
        ///CLOVER:ON
        
        final TxMessageType type = tx.getMsgType();
        if (type != TxMessageType.DSN && type != TxMessageType.MDN)
        	return "";
        
        final TxDetail detail = tx.getDetail(TxDetailType.PARENT_MSG_ID);
        return (detail != null && !detail.getDetailValue().isEmpty()) ? detail.getDetailValue() : "";
        
	}
	
	public static  MimeMessage convertHeadersToMessage(InternetHeaders headers, TxDetailParser parser)
	{
        if (headers == null) 
        {
            throw new IllegalArgumentException("Invalid parameter received. Headers cannot be null.");
        }
        
		// convert into a MimeMessage with only the headers
		final MimeMessage msg = new MimeMessage((Session)null);
		
		try
		{
			final Enumeration<String> henum = headers.getAllHeaderLines();
			while (henum.hasMoreElements())
				msg.addHeaderLine(henum.nextElement());
			
			return msg;
		}
		catch (MessagingException e)
		{
			return null;
		}
	}	
}
