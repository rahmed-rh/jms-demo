package io.enmasse.example.jms;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.JmsTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args) throws Exception {
        String topic = "mytopic";
        String sub = topic+"::mysub";
        String queue="myqueue-brokered";
        String host = "messaging-d3aa540-amq-online-infra.apps.box.it-speeltuin.nl";
        int port = 443;

        JmsConnectionFactory connectionFactory = new JmsConnectionFactory();
        connectionFactory.setRemoteURI(String.format("amqps://%s:%d?transport.verifyHost=false&transport.trustAll=true", host, port));
        connectionFactory.setUsername("demo-user");
        connectionFactory.setPassword("password");

        JMSProducer producer = new JMSProducer(connectionFactory, getQueue(queue));
        JMSConsumer consumer = new JMSConsumer(connectionFactory, getQueue(queue));

        Executor executor = Executors.newFixedThreadPool(2);
        executor.execute(consumer);
        executor.execute(producer);
    }

    private static Destination getTopic(String address) {
        JmsTopic dest = new JmsTopic(address);
        dest.setAddress(address);
        return dest;
    }

    private static Destination getQueue(String address) {
        JmsQueue dest = new JmsQueue(address);
        dest.setAddress(address);
        return dest;
    }
}
