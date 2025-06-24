package com.example.viagem.controller;
 
import com.example.viagem.model.Travel;
import com.example.viagem.repository.TravelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/api/travels")
@Tag(name = "Viagens", description = "Endpoints para gerenciar viagens do diário")
public class TravelController {
 
   @Autowired
   private TravelRepository travelRepository;
 
   @Operation(summary = "Listar todas as viagens")
   @GetMapping
   public List<Travel> getAllTravels() {
      return travelRepository.findAll();
   }
 
   @Operation(summary = "Buscar uma viagem por ID")
   @GetMapping("/{id}")
   public ResponseEntity<Travel> getTravelById(@PathVariable Long id) {
      Optional<Travel> travel = travelRepository.findById(id);
      return travel.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
   }
 
   @Operation(summary = "Criar uma nova viagem")
   @PostMapping
   public Travel createTravel(@RequestBody Travel travel) {
      return travelRepository.save(travel);
   }
 
   @Operation(summary = "Atualizar uma viagem existente")
   @PutMapping("/{id}")
   public ResponseEntity<Travel> updateTravel(@PathVariable Long id, @RequestBody Travel travelDetails) {
      Optional<Travel> travelOptional = travelRepository.findById(id);
      if (!travelOptional.isPresent()) {
         return ResponseEntity.notFound().build();
      }
      Travel travel = travelOptional.get();
      travel.setTitulo(travelDetails.getTitulo());
      travel.setDescricao(travelDetails.getDescricao());
      travel.setDestino(travelDetails.getDestino());
      travel.setFeedback(travelDetails.getFeedback());
      travel.setDuracao(travelDetails.getDuracao());
      travel.setImagem(travelDetails.getImagem());
      return ResponseEntity.ok(travelRepository.save(travel));
   }
 
   @Operation(summary = "Excluir uma viagem por ID")
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteTravel(@PathVariable Long id) {
      if (!travelRepository.existsById(id)) {
         return ResponseEntity.notFound().build();
      }
      travelRepository.deleteById(id);
      return ResponseEntity.noContent().build();
   }
}