package org.nhindirect.common.tx.impl.exchange;

import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.common.tx.model.Tx;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface TxClient
{
    @PostExchange(value= "/txs", contentType=MediaType.APPLICATION_JSON_VALUE,  accept = MediaType.APPLICATION_JSON_VALUE)         
    public void addTx(@RequestBody Tx tx) throws ServiceException;
    
    @PostExchange(value = "/txs/suppressNotification", contentType=MediaType.APPLICATION_JSON_VALUE,  accept = MediaType.APPLICATION_JSON_VALUE)        
    public Boolean supressNotification(@RequestBody Tx notificationMessage) throws ServiceException;
}
