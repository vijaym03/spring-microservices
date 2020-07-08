package com.example.moviecatalogservice.resource;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

   /* @Autowired
    private WebClient.Builder webClientBuilder;*/

    @RequestMapping ("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        //get List of Movie id for the user
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/"+userId, UserRating.class);
        //get the list of movies for each id
        return ratings.getRatings().stream().map(rating ->{
          Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
        /* Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/"+rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/

          return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }).collect(Collectors.toList());
       // return Collections.singletonList(new CatalogItem("Transformers", "Test", 4));
    }
}
