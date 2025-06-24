package com.example.viagem.controller;
 
import com.example.viagem.model.User;
import com.example.viagem.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {
 
   @Autowired
   private UserRepository userRepository;
 
   @Operation(summary = "Listar todos os usuários")
   @GetMapping
   public List<User> getAllUsers() {
      return userRepository.findAll();
   }
 
   @Operation(summary = "Buscar um usuário por ID")
   @GetMapping("/{id}")
   public ResponseEntity<User> getUserById(@PathVariable Long id) {
      Optional<User> user = userRepository.findById(id);
      return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }
 
   @Operation(summary = "Criar um novo usuário")
   @PostMapping
   public User createUser(@RequestBody User user) {
      return userRepository.save(user);
   }
 
   @Operation(summary = "Atualizar um usuário existente")
   @PutMapping("/{id}")
   public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
      Optional<User> userOptional = userRepository.findById(id);
      if (!userOptional.isPresent()) {
         return ResponseEntity.notFound().build();
      }
      User user = userOptional.get();
      user.setNome(userDetails.getNome());
      user.setEmail(userDetails.getEmail());
      user.setIdade(userDetails.getIdade());
      user.setSenha(userDetails.getSenha());
      return ResponseEntity.ok(userRepository.save(user));
   }
 
   @Operation(summary = "Excluir um usuário por ID")
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
      if (!userRepository.existsById(id)) {
         return ResponseEntity.notFound().build();
      }
      userRepository.deleteById(id);
      return ResponseEntity.noContent().build();
   }
}