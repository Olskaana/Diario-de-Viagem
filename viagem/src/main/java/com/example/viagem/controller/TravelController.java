package com.example.viagem.controller;

import com.example.viagem.model.Travel;
import com.example.viagem.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    @Autowired
    private TravelRepository travelRepository;

    @GetMapping
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Travel> getTravelById(@PathVariable("id") Long id) {
        Optional<Travel> travel = travelRepository.findById(id);
        return travel.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Travel createTravel(@RequestBody Travel travel) {
        return travelRepository.save(travel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Travel> updateTravel(@PathVariable("id") Long id, @RequestBody Travel travelDetails) {
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

        Travel updatedTravel = travelRepository.save(travel);
        return ResponseEntity.ok(updatedTravel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravel(@PathVariable("id") Long id) {
        if (!travelRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        travelRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
