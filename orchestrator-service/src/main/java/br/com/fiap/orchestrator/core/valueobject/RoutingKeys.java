package br.com.fiap.orchestrator.core.valueobject;

public final class RoutingKeys {
    private RoutingKeys() {}

    public static final String ORQ_CLIENTES = "Orquestrador-Clientes.queue";
    public static final String ORQ_RESTAURANTES = "Orquestrador-Restaurantes.queue";
    public static final String ORQ_PEDIDOS = "Orquestrador-Pedidos.queue";

    public static final String CLIENTES_ORQ = "Clientes-Orquestrador.queue";
    public static final String RESTAURANTES_ORQ = "Restaurantes-Orquestrador.queue";
    public static final String PEDIDOS_ORQ = "Pedidos-Orquestrador.queue";

    public static String errorKey(String baseQueue) {
        return baseQueue + ".error";
    }
}