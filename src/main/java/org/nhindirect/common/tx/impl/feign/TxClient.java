package org.nhindirect.common.tx.impl.feign;

import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.common.rest.feign.DefaultFeignClientConfiguration;
import org.nhindirect.common.tx.model.Tx;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="direct-msg-monitor", url = "${direct.msgmonitor.service.url}", configuration=DefaultFeignClientConfiguration.class)
public interface TxClient
{
    @PostMapping(value= "/txs", consumes = MediaType.APPLICATION_JSON_VALUE)         
    public void addTx(@RequestBody Tx tx) throws ServiceException;
    
    @PostMapping(value = "/txs/suppressNotification", consumes = MediaType.APPLICATION_JSON_VALUE)        
    public Boolean supressNotification(@RequestBody Tx notificationMessage) throws ServiceException;
}
