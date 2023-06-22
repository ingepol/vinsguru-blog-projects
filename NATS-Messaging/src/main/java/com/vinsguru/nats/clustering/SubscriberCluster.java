package com.vinsguru.nats.clustering;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubscriberCluster {

  public static void main(final String[] args) {
    final Options build = new Options.Builder()
        .connectionListener(new NatsConnectionListener())
        .build();
    try (final Connection nats = Nats.connect(build)) {
      // message dispatcher
      final Dispatcher dispatcher = nats.createDispatcher(msg -> {
      });

      // subscribers with queue group
      dispatcher.subscribe("vinsguru", msg -> {
        log.info("Received 1 : " + new String(msg.getData(), StandardCharsets.UTF_8));
        nats.publish(msg.getReplyTo(), "Hello from subscriber 1 of grp1".getBytes());
      });
      dispatcher.subscribe("vinsguru", msg -> {
        log.info("Received 2 : " + new String(msg.getData(), StandardCharsets.UTF_8));
        nats.publish(msg.getReplyTo(), "Hello from subscriber 2 of grp1".getBytes());
      });

    } catch (final IOException e) {
      log.error("Error: " + e.getMessage());
    } catch (final InterruptedException ie) {
      log.warn("Connection interrupted");
      Thread.currentThread().interrupt();
    }
  }
}
