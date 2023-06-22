package com.vinsguru.nats.messaging;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Subscriber {

  public static void main(final String[] args) {
    // connect to nats server
    try (final Connection nats = Nats.connect()) {

      // message dispatcher
      final Dispatcher dispatcher = nats.createDispatcher(msg -> {
      });

      // subscribes to nats.demo.service channel
      dispatcher.subscribe("nats.demo.service", msg -> {
            log.info("Received : " + new String(msg.getData()));
            nats.publish(msg.getReplyTo(), "Hello publisher".getBytes());
          }
      );

      Thread.sleep(5000);
    } catch (final IOException e) {
      log.error("Error: " + e.getMessage());
    } catch (final InterruptedException ie) {
      log.warn("Connection interrupted");
      Thread.currentThread().interrupt();
    }

  }

}
