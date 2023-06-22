package com.vinsguru.nats.clustering;

import io.nats.client.Connection;
import io.nats.client.ConnectionListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NatsConnectionListener implements ConnectionListener {

  @Override
  public void connectionEvent(final Connection connection, final Events events) {
    log.info(events.toString() + " : " + connection.getServers());
  }
}
