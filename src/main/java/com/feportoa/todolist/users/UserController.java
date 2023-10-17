package com.feportoa.todolist.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController // Cria um controller Rest (em alguns casos da pra usar só @Controller)
@RequestMapping("/user") // Cria a Rota do "/user"-> http://localhost:8080/user
public class UserController {

    @Autowired // Gerencia os valores do userRepository
    private IUserRepository userRepository;

    @PostMapping("") // Mapping para fazer post. Pode ser "" ou "/", mas o "/" nao funciona por algum motivo
    public ResponseEntity create(@RequestBody /* Nao entendi o RequestBody */ UserModel user) {
        var userEmail = this.userRepository.findByEmail(user.getEmail());

        if(userEmail != null) {
            // Mensagem de erro
            // Status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"" + user.getUsername() + "\"" + " já existe. \n UUID: " + user.getId()); // Não sei por que retorna null
        }

        String passHash = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()); 
        // hashToString recebe array de char, por isso o toCharArray()

        user.setPassword(passHash);

        var userCreated = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

}
