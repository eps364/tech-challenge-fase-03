package br.com.fiap.order.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.rabbit")
public class RabbitQueuesProperties {

    private String exchange;
    private Queues queues = new Queues();

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public Queues getQueues() { return queues; }
    public void setQueues(Queues queues) { this.queues = queues; }

    public static class Queues {
        private Out out = new Out();

        public Out getOut() { return out; }
        public void setOut(Out out) { this.out = out; }

        public static class Out {
            private String pedidosOrquestrador;

            public String getPedidosOrquestrador() { return pedidosOrquestrador; }
            public void setPedidosOrquestrador(String pedidosOrquestrador) {
                this.pedidosOrquestrador = pedidosOrquestrador;
            }
        }
    }
}
