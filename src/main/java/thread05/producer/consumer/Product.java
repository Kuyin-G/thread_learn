package thread05.producer.consumer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 产品
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private Object value;
}
