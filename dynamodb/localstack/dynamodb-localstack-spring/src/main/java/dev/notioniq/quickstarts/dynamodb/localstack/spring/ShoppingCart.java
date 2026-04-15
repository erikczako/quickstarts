package dev.notioniq.quickstarts.dynamodb.localstack.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@Data
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public final class ShoppingCart {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private Long userId;

    private int numberOfItems;

    private BigDecimal totalPrice;

}
