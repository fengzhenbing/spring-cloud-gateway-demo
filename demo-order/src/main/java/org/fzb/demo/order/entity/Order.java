package org.fzb.demo.order.entity;

import lombok.Data;

/**
 * Order
 *
 * @author fengzhenbing
 */
@Data
public class Order {
    private Integer id;
    private String productName;
    private Long orderAmount;
}
