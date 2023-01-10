package com.microproject.user.service.external;

import com.microproject.user.service.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// feign client me hume alag se ek interface banana padta hai
@FeignClient(name="HOTEL-SERVICE")
public interface HotelService {

    @GetMapping("/api/v1/hotels/{id}")
    Hotel getHotel(@PathVariable("id") String id);
}
