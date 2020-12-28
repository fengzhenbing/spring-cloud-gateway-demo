package org.fzb.demo.inventory.entity;

import lombok.Data;

/**
 * Inventory
 *
 * @author fengzhenbing
 */
@Data
public class Inventory {
    private Integer id;
    private String productName;
    private Long inventoryAmount;
}
