package com.vinsguru.nats.clustering;

import java.io.IOException;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublisherCluster {

  public static void main(final String[] args) {
    // connect to nats server
    try (final Connection nats = Nats.connect()) {
      for (int i = 0; i < 60000; i++) {
        nats.request("vinsguru", "Hi".getBytes())
            .thenApply(Message::getData)
            .thenApply(String::new)
            .thenAccept(log::info);
        Thread.sleep(1000);
      }
    } catch (final IOException e) {
      log.error("Error: " + e.getMessage());
    } catch (final InterruptedException ie) {
      log.warn("Connection interrupted");
      Thread.currentThread().interrupt();
    }

  }

}
