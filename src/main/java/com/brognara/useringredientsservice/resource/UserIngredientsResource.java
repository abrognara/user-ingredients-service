package com.brognara.useringredientsservice.resource;

import com.brognara.useringredientsservice.model.PutIngredientRequest;
import com.brognara.useringredientsservice.model.UserIngredients;
import com.brognara.useringredientsservice.repository.UserIngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserIngredients> getIngredients(@PathVariable final String id) {
        // TODO integrate user_email google sign-in integration
        UserIngredients userIngredients = userIngredientsRepository.getUserIngredients(id);
        if (userIngredients == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok(userIngredients);
    }

    // Resource archetype: STORE
    @PutMapping(value = "/{id}/ingredients", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putIngredients
            (@PathVariable final String id, @RequestBody final PutIngredientRequest putIngredientRequest) {
        System.out.println(putIngredientRequest);
        final boolean success = userIngredientsRepository.putUserIngredients(id, putIngredientRequest);
        if (!success)
            return ResponseEntity.internalServerError().body("Request failed.");

        return ResponseEntity.ok("Success");
    }

}
