package com.microproject.user.service.serviceimpl;

import com.microproject.user.service.entities.Hotel;
import com.microproject.user.service.entities.Rating;
import com.microproject.user.service.entities.User;
import com.microproject.user.service.exception.ResourceNotFoundException;
import com.microproject.user.service.external.HotelService;
import com.microproject.user.service.repositories.UserRepository;
import com.microproject.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    //to make communication between two project creating resttemplate object for making bean we in config file
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService; //feign client ke hotel service  ka object inject kr rahe hai
    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //it will generate unique id
       String randomUserId= UUID.randomUUID().toString();
       user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        //using rest template
//        //ager id mila to database me se dega aur ager nhi mila to we need to handle this globally
//        User user= userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with given id "+userId));
//       //fetch rating of the above user from ratingservice
//        //http://localhost:8082/api/v1/ratings/users/5191cd2d-2c65-4c46-ae9c-d604a064c0b9
//
//        Rating[] ratingsOfUsers=restTemplate.getForObject("http://RATING-SERVICE/api/v1/ratings/users/"+user.getUserId(), Rating[].class);
//        logger.info("{}",ratingsOfUsers);
//        List<Rating> arrayratings =Arrays.stream(ratingsOfUsers).toList();
//
//        List<Rating> ratingList=arrayratings.stream().map(rating -> {
//            //api call to hotel service to get the hotel
//            //http://localhost:8081/api/v1/hotels/bf59b46e-1131-4f50-a2eb-88a7454a876e
//            ResponseEntity<Hotel> forEntity=restTemplate.getForEntity("http://HOTEL-SERVICE/api/v1/hotels/"+rating.getHotelId(), Hotel.class);
//            Hotel hotel=forEntity.getBody();
//            logger.info("response status code"+forEntity.getStatusCode());
//
//            //set the hotel to rating
//            //return the rating
//            rating.setHotel(hotel);
//            return rating;
//
//         }).collect(Collectors.toList());
//        user.setRatings(ratingList);
//
//        return user; //get user from database with the help of user repo

        // rest template end

        //------Feign client start----------//
        //ager id mila to database me se dega aur ager nhi mila to we need to handle this globally
        User user= userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with given id "+userId));
        //fetch rating of the above user from ratingservice
        //http://localhost:8082/api/v1/ratings/users/5191cd2d-2c65-4c46-ae9c-d604a064c0b9

        Rating[] ratingsOfUsers=restTemplate.getForObject("http://RATING-SERVICE/api/v1/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{}",ratingsOfUsers);
        List<Rating> arrayratings =Arrays.stream(ratingsOfUsers).toList();

        List<Rating> ratingList=arrayratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            //http://localhost:8081/api/v1/hotels/bf59b46e-1131-4f50-a2eb-88a7454a876e
           // ResponseEntity<Hotel> forEntity=restTemplate.getForEntity("http://HOTEL-SERVICE/api/v1/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel=hotelService.getHotel(rating.getHotelId());
            System.out.println("hotel "+hotel);
           // logger.info("response status code"+forEntity.getStatusCode());

            //set the hotel to rating
            //return the rating
            rating.setHotel(hotel);
            return rating;

        }).collect(Collectors.toList());
        user.setRatings(ratingList);

        return user; //get user from database with the help of user repo

    }
}
