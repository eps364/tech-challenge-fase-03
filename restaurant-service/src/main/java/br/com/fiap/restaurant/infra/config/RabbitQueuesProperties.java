package br.com.fiap.restaurant.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.rabbit")
public class RabbitQueuesProperties {

    private Queues queues = new Queues();

    public Queues queues() {
        return queues;
    }

    public void setQueues(Queues queues) {
        this.queues = queues;
    }

    public static class Queues {
        private In in = new In();
        private Out out = new Out();

        public In in() {
            return in;
        }

        public void setIn(In in) {
            this.in = in;
        }

        public Out out() {
            return out;
        }

        public void setOut(Out out) {
            this.out = out;
        }
    }

    public static class In {
        private String orchestratorRestaurants;

        public String orchestratorRestaurants() {
            return orchestratorRestaurants;
        }

        public void setOrchestratorRestaurants(String orchestratorRestaurants) {
            this.orchestratorRestaurants = orchestratorRestaurants;
        }
    }

    public static class Out {
        private String restaurantsOrchestrator;

        public String restaurantsOrchestrator() {
            return restaurantsOrchestrator;
        }

        public void setRestaurantsOrchestrator(String restaurantsOrchestrator) {
            this.restaurantsOrchestrator = restaurantsOrchestrator;
        }
    }
}