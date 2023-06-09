package vinsguru.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import vinsguru.api.service.NatsSquareService;

@RestController
@RequestMapping("nats")
public class NatsServiceController {

    @Autowired
    private NatsSquareService service;

    @GetMapping("/square/unary/{number}")
    public Flux<Object> getResponseUnary(@PathVariable int number) {
        return this.service.getSquareResponseUnary(number);

    }
}