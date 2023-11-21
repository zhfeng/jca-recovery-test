package org.acme;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.Message;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionManager;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import io.quarkus.narayana.jta.QuarkusTransaction;

@Path("/jca")
@ApplicationScoped
public class JcaResource {

    @Inject
    ConnectionFactory factory;

    @Inject
    TransactionManager transactionManager;

    @POST
    @Transactional
    public void send(@QueryParam("name") @DefaultValue("JCA") String name) throws Exception {
        DummyXAResource xaResource = new DummyXAResource("crash".equals(name));
        transactionManager.getTransaction().enlistResource(xaResource);

        try (JMSContext context = factory.createContext()) {
            Queue myQueue = context.createQueue("MyQueue");
            JMSProducer producer = context.createProducer();
            producer.send(myQueue, "Hello " + name);
            if (name.equals("rollback"))
                QuarkusTransaction.setRollbackOnly();
        }
    }

    @GET
    public String get() throws Exception {
        try (JMSContext context = factory.createContext(JMSContext.AUTO_ACKNOWLEDGE);
                JMSConsumer consumer = context.createConsumer(context.createQueue("MyQueue"))) {
            Optional<Message> maybeMessage = Optional.ofNullable(consumer.receive(1000L));
            if (maybeMessage.isPresent()) {
                return maybeMessage.get().getBody(String.class);
            } else {
                return "no message";
            }
        } catch (JMSException e) {
            throw new RuntimeException("Could not receive message", e);
        }
    }
}
