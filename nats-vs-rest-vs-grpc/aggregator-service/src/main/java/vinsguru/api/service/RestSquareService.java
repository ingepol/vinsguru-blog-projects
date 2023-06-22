package vinsguru.api.service;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;

@Service
public class RestSquareService {

    @Value("${rest.square.service.endpoint}")
    private String baseUrl;

    private WebClient webClient;

    @PostConstruct
    private void init(){
        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom")
                .maxConnections(1000)
                .pendingAcquireMaxCount(5000)
                .pendingAcquireTimeout(Duration.ofMillis(1000))
                .maxIdleTime(Duration.ofMillis(60000))
                .build();
        HttpClient httpClient = HttpClient.create(connectionProvider).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        
        ReactorClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(httpClient);
        this.webClient = WebClient.builder()
                .clientConnector(clientHttpConnector)
                .baseUrl(baseUrl).build();
    }

    public Flux<Object> getUnaryResponse(int number){
        return Flux.range(1, number)
                .flatMap(i -> this.webClient.get()
                        .uri(String.format("/api/square/unary/%d", i))
                        .retrieve()
                        .bodyToMono(Object.class)
                        .map(o -> (Object) Map.of(i, o)))
                .subscribeOn(Schedulers.boundedElastic());
    }

}