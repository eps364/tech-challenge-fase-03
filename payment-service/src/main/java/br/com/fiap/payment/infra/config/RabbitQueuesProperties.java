package br.com.fiap.payment.infra.config;

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
        private In in = new In();
        private Out out = new Out();

        public In getIn() { return in; }
        public void setIn(In in) { this.in = in; }

        public Out getOut() { return out; }
        public void setOut(Out out) { this.out = out; }

        public static class In {
            private String orchestratorPayments;
            private String orchestratorPaymentsWorker;

            public String getOrchestratorPayments() { return orchestratorPayments; }
            public void setOrchestratorPayments(String orchestratorPayments) {
                this.orchestratorPayments = orchestratorPayments;
            }

            public String getOrchestratorPaymentsWorker() { return orchestratorPaymentsWorker; }
            public void setOrchestratorPaymentsWorker(String orchestratorPaymentsWorker) {
                this.orchestratorPaymentsWorker = orchestratorPaymentsWorker;
            }
        }

        public static class Out {
            private String paymentsOrchestrator;

            public String getPaymentsOrchestrator() { return paymentsOrchestrator; }
            public void setPaymentsOrchestrator(String paymentsOrchestrator) {
                this.paymentsOrchestrator = paymentsOrchestrator;
            }
        }
    }
}
