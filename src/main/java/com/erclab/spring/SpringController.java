package com.erclab.spring;

import com.erclab.bean.RestaurantBean;
import com.erclab.mongodb.MongoDbClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class SpringController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/restaurants")
    @ResponseBody
    public List<RestaurantBean> getRestaurants() {
        return new MongoDbClient().getAllRestaurants();
    }

    @GetMapping("/restaurant")
    @ResponseBody
    public RestaurantBean getRestaurantByID(@RequestParam(value = "id") String id) {
        return new MongoDbClient().getRestaurantByObjectId(id);
    }

    @GetMapping("/rest_by_menu")
    @ResponseBody
    public List<RestaurantBean> getRestaurantByMenu(@RequestParam(value = "menu") String menu) {
        return new MongoDbClient().getRestaurantByMenu(menu);
    }

    @GetMapping("/rest_by_address")
    @ResponseBody
    public List<RestaurantBean> getRestaurantByAddress(@RequestParam(value = "address") String address) {
        return new MongoDbClient().getRestaurantByAddress(address);
    }

    @GetMapping("/rest_menu_address")
    @ResponseBody
    public List<RestaurantBean> getRestaurantMenuAddress(@RequestParam(value = "menu") String menu,
                                                         @RequestParam(value = "address") String address) {
        return new MongoDbClient().getRestaurantByMenuAndAddress(menu, address);
    }

    @GetMapping("/rest_query")
    @ResponseBody
    public List<RestaurantBean> getRestaurantQuery(@RequestParam(value = "query") String query) {
        return new MongoDbClient().getRestaurantByTextSearch(query);
    }
}
