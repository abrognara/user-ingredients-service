package com.brognara.useringredientsservice.resource;

import com.brognara.useringredientsservice.model.PutIngredientRequest;
import com.brognara.useringredientsservice.model.UserIngredient;
import com.brognara.useringredientsservice.repository.UserIngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserIngredientsResource {

    public final UserIngredientsRepository userIngredientsRepository;

    @Autowired
    public UserIngredientsResource(UserIngredientsRepository userIngredientsRepository) {
        this.userIngredientsRepository = userIngredientsRepository;
    }

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello world");
    }

    // Resource archetype: STORE
    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<UserIngredient>> getIngredients(@PathVariable final String id) {
        List<UserIngredient> userIngredients = userIngredientsRepository.getUserIngredients(id);
        if (userIngredients == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok(userIngredients);
    }

    // Resource archetype: STORE
    @PutMapping("/{id}/ingredients")
    public ResponseEntity<String> putIngredients
            (@PathVariable final String id, @RequestBody final PutIngredientRequest putIngredientRequest) {
        final boolean success = userIngredientsRepository.putItem(id, putIngredientRequest);
        if (success)
            return ResponseEntity.internalServerError().body("Request failed.");

        return ResponseEntity.ok("Success");
    }

}
