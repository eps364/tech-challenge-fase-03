package br.com.fiap.order.core.usecase;
import java.util.List;

public class OrderValidationException extends RuntimeException {

    private final List<FieldError> fields;

    public OrderValidationException(List<FieldError> fields) {
        super("Order validation failed");
        this.fields = List.copyOf(fields);
    }

    public List<FieldError> getFields() {
        return fields;
    }
}
