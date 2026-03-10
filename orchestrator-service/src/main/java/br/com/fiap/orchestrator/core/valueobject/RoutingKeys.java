package br.com.fiap.orchestrator.core.valueobject;

public final class RoutingKeys {
    private RoutingKeys() {}

    public static final String ORCHESTRATOR_CLIENTS_QUEUE = "Orchestrator-Clients.queue";
    public static final String ORCHESTRATOR_RESTAURANTS_QUEUE = "Orchestrator-Restaurants.queue";
    public static final String ORCHESTRATOR_ORDERS_QUEUE = "Orchestrator-Orders.queue";
    public static final String ORCHESTRATOR_PAYMENTS_QUEUE = "Orchestrator-Payments.queue";
    public static final String ORCHESTRATOR_PAYMENTS_WORKER_QUEUE = "Orchestrator-Payments-Worker.queue";

    public static final String CLIENTS_ORCHESTRATOR_QUEUE = "Clients-Orchestrator.queue";
    public static final String RESTAURANTS_ORCHESTRATOR_QUEUE = "Restaurants-Orchestrator.queue";
    public static final String ORDERS_ORCHESTRATOR_QUEUE = "Orders-Orchestrator.queue";
    public static final String PAYMENTS_ORCHESTRATOR_QUEUE = "Payments-Orchestrator.queue";

    public static String errorKey(String baseQueue) {
        return baseQueue + ".error";
    }
}