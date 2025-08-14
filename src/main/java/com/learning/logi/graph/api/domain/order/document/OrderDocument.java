package com.learning.logi.graph.api.domain.order.document;

import com.learning.logi.graph.api.domain.order.dto.OrderUpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "orders")
public class OrderDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Long)
    private Long deliveryManId;

    @Field(type = FieldType.Date)
    private Instant createdOn;

    public static OrderDocument from(final OrderUpdateEvent event) {
        return new OrderDocument(
                event.id(),
                event.description(),
                event.status().name(),
                event.deliveryManId(),
                event.createdAt()
        );
    }
}