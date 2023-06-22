package com.vinsguru.nats.messaging;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Publisher {

  public static void main(final String[] args) {
    // connect to nats server
    try (final Connection nats = Nats.connect()) {
      // publish a message to the channel
      nats.publish("nats.demo.service", "Hello NATS without receiver".getBytes());
      nats.request("nats.demo.service", "Hello NATS with receiver".getBytes())
          .thenApply(Message::getData)  // gets executed when we get response from receiver
          .thenApply(String::new)
          .thenAccept(s -> log.info("Response from Receiver: " + s));
      Thread.sleep(5000);
    } catch (final IOException e) {
      log.error("Error: " + e.getMessage());
    } catch (final InterruptedException ie) {
      log.warn("Connection interrupted");
      Thread.currentThread().interrupt();
    }

  }

}
