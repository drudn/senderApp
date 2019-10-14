package com.drudn;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.concurrent.TimeoutException;

public class Sender {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        File file = new File("ShoppingCart.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare("qA", false, false, false, null);
            channel.queueDeclare("qB", false, false, false, null);
            String ln;
            String message;

            while ((ln = br.readLine()) != null) {//reads line by line from text file

                switch (ln.charAt(0)) {//evaluates first character in line and following case statements publish to queues
                    case 'A'://publishes line to queue A
                        channel.basicPublish("", "qA", false, null, ln.getBytes());
                        break;
                    case 'B'://publishes line to queue B
                        channel.basicPublish("", "qB", false, null, ln.getBytes());
                        break;
                    case 'C'://publishes line to queues A and B
                        channel.basicPublish("", "qA", false, null, ln.getBytes());
                        channel.basicPublish("", "qB", false, null, ln.getBytes());
                        break;
                }
            }
        }
    }
}