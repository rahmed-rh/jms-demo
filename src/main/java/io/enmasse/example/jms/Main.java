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
        String sub = "mytopic::mysub";
        String host = "messaging-993fc28-enmasse-infra.apps.ulilleen.rhmw-integrations.net";
        int port = 443;

        JmsConnectionFactory connectionFactory = new JmsConnectionFactory();
        connectionFactory.setRemoteURI(String.format("amqps://%s:%d?transport.verifyHost=false&transport.trustAll=true", host, port));
        connectionFactory.setUsername("test");
        connectionFactory.setPassword("test");

        JMSProducer producer = new JMSProducer(connectionFactory, getTopic(topic));
        JMSConsumer consumer = new JMSConsumer(connectionFactory, getQueue(sub));

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
