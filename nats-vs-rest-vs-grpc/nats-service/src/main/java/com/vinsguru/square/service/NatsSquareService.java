package com.vinsguru.square.service;

import java.io.IOException;
import java.util.Objects;

import com.google.protobuf.InvalidProtocolBufferException;
import com.vinsguru.model.Input;
import com.vinsguru.model.Output;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;

public class NatsSquareService {

  public static void main(final String[] args) throws IOException, InterruptedException {
    final String natsServer = Objects.toString(System.getenv("NATS_SERVER"), "nats://localhost:4222");
    System.out.println(natsServer);
    try (final Connection nats = Nats.connect(natsServer)) {
      System.out.println("connected");
      final Dispatcher dispatcher = nats.createDispatcher(msg -> {
      });
      dispatcher.subscribe("nats.square.service", msg -> {
        try {
          final Input input = Input.parseFrom(msg.getData());
          final Output output = Output.newBuilder().setNumber(input.getNumber()).setResult(input.getNumber() * input.getNumber()).build();
          nats.publish(msg.getReplyTo(), output.toByteArray());
        } catch (final InvalidProtocolBufferException e) {
          e.printStackTrace();
        }
      });
      Thread.sleep(1000000);
    }

  }

}
