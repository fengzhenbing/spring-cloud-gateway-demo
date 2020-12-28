package org.fzb.demo.inventory;

import org.fzb.demo.inventory.entity.Inventory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InventoryApplicaiton
 *
 * @author fengzhenbing
 */
@SpringBootApplication
@RestController
public class InventoryApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplicaiton.class,args);
    }

}
