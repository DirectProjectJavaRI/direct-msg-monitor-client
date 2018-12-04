package org.nhindirect.common.tx;

import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients({"org.nhindirect.common.tx.impl.feign"})
@SpringBootApplication
@ComponentScan({"org.nhindirect.common.tx"})
public class TestApplication
{
    public static void main(String[] args) 
    {
        SpringApplication.run(TestApplication.class, args);
    }  
    
    @Bean
    public TxDetailParser txDetailParser()
    {
    	return new DefaultTxDetailParser();
    }
}
