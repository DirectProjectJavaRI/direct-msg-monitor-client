package org.nhindirect.common.tx.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;



import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.nhindirect.common.tx.impl.TxParseUtils;
import org.nhindirect.common.tx.model.Tx;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("txs")
public class MockTxsResource
{
	protected Collection<Tx> txs = new ArrayList<Tx>();
	protected static final CacheControl noCache;
	
	static
	{
		noCache = CacheControl.noCache();
	}

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)         
    public ResponseEntity<Void> addTx(@RequestBody Tx tx)
    {
    	
    	txs.add(tx);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).cacheControl(noCache).build();
    }

    @PostMapping(value = "suppressNotification", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)        
    public ResponseEntity<Boolean> supressNotification(@RequestBody Tx notificationMessage) 
    {
    	Boolean retEntity = Boolean.TRUE;
    	
    	final String originalMessageId = TxParseUtils.getOriginalMessageId(notificationMessage, new DefaultTxDetailParser());
    	
    	if (originalMessageId.isEmpty() || originalMessageId.equals("NotNotification"))
    	{
    		retEntity = Boolean.FALSE;
    	}
    	
    	return ResponseEntity.status(HttpStatus.OK).cacheControl(noCache).body(retEntity);
    }
    
    public void clearTxState()
    {
    	txs.clear();
    }

    public Collection<Tx> getTxs()
    {
    	return Collections.unmodifiableCollection(txs);
    }
}

