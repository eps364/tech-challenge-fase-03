//package br.com.fiap.order.infra.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//@Component
//@ConfigurationProperties(prefix = "app.rabbit")
//public class RabbitQueuesProperties {
//
//    private String exchange;
//    private Queues queues = new Queues();
//
//    public String getExchange() { return exchange; }
//    public void setExchange(String exchange) { this.exchange = exchange; }
//
//    public Queues getQueues() { return queues; }
//    public void setQueues(Queues queues) { this.queues = queues; }
//
//    public static class Queues {
//        private In in = new In();
//        private Out out = new Out();
//
//        public In getIn() { return in; }
//        public void setIn(In in) { this.in = in; }
//
//        public Out getOut() { return out; }
//        public void setOut(Out out) { this.out = out; }
//
//        public static class In {
//            private String orchestratorOrders;
//
//            public String getOrchestratorOrders() { return orchestratorOrders; }
//            public void setOrchestratorOrders(String orchestratorOrders) {
//                this.orchestratorOrders = orchestratorOrders;
//            }
//        }
//
//        public static class Out {
//            private String ordersOrchestrator;
//
//            public String getOrdersOrchestrator() { return ordersOrchestrator; }
//            public void setOrdersOrchestrator(String ordersOrchestrator) {
//                this.ordersOrchestrator = ordersOrchestrator;
//            }
//        }
//    }
//}
