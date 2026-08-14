package org.nhindirect.common.tx.autoconfig;

import org.nhindirect.common.rest.exchange.DirectWebClientBuilderConfig;
import org.nhindirect.common.tx.TxDetailParser;
import org.nhindirect.common.tx.TxService;
import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.nhindirect.common.tx.impl.RESTTxServiceClient;
import org.nhindirect.common.tx.impl.exchange.TxClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@AutoConfiguration
@ConditionalOnClass(WebClient.class )
@ConditionalOnProperty(name="direct.msgmonitor.service.url")
@Import(DirectWebClientBuilderConfig.class)
public class TxClientAutoConfiguration {

	@Value("${direct.msgmonitor.service.url}")
	protected String serviceIdentifier;
	
	protected HttpServiceProxyFactory txServiceProxyFactory(WebClient.Builder webClientBuilder) {
		
		final var client = webClientBuilder.baseUrl(serviceIdentifier).build();
		return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
		
	}
	
	@ConditionalOnMissingBean
	@Bean
	TxClient directTxClient(WebClient.Builder webClientBuilder) {
		
		return txServiceProxyFactory(webClientBuilder).createClient(TxClient.class);
	}
	
	@ConditionalOnMissingBean
	@Bean
	TxService directTxService(TxClient txClient, TxDetailParser parser) {
		
		return new RESTTxServiceClient(txClient, parser);
	}
	
	@ConditionalOnMissingBean(TxDetailParser.class)
	@Bean
	TxDetailParser txDetailParser() {
		return new DefaultTxDetailParser();
	}
	
}
