package org.fzb.demo.inventory.controller;

import org.fzb.demo.inventory.entity.Inventory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InventoryController
 *
 * @author fengzhenbing
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {


    @GetMapping("/get")
    public Inventory getInventory(){
        Inventory inventory  = new Inventory();
        inventory.setId(1);
        inventory.setInventoryAmount((long) (10000*Math.random()));
        inventory.setProductName("testInventory");
        return inventory;
    }

}
