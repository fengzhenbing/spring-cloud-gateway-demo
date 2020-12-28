package org.fzb.demo.order.controller;

import org.fzb.demo.order.entity.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController
 *
 * @author fengzhenbing
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @GetMapping("/get")
    public Order getOrder(){
        Order order  = new Order();
        order.setId(1);
        order.setOrderAmount((long) (10000*Math.random()));
        order.setProductName("testOrder");
        return order;
    }

}
