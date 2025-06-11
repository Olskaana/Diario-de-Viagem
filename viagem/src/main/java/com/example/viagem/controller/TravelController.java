package com.example.viagem.controller;

import com.example.viagem.model.Travel;
import com.example.viagem.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;

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

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createTravel(
        @RequestParam("titulo") String titulo,
        @RequestParam("descricao") String descricao,
        @RequestParam("destino") String destino,
        @RequestParam("feedback") String feedback,
        @RequestParam("duracao") Integer duracao,
        @RequestParam("imagem") MultipartFile imagemFile) {

    try {
        String uploadDir = System.getProperty("user.dir") + "/photos/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(imagemFile.getOriginalFilename());
        File dest = new File(uploadDir + fileName);
        imagemFile.transferTo(dest);

        Travel travel = new Travel();
        travel.setTitulo(titulo);
        travel.setDescricao(descricao);
        travel.setDestino(destino);
        travel.setFeedback(feedback);
        travel.setDuracao(duracao);
        travel.setImagem("/photos/" + fileName);

        return ResponseEntity.ok(travelRepository.save(travel));

    } catch (IOException e) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erro ao salvar imagem: " + e.getMessage());
}
}

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateTravel(
    @PathVariable("id") Long id,
    @RequestParam("titulo") String titulo,
    @RequestParam("descricao") String descricao,
    @RequestParam("destino") String destino,
    @RequestParam("feedback") String feedback,
    @RequestParam("duracao") Integer duracao,
    @RequestParam(value = "imagem", required = false) MultipartFile imagemFile) {

    Optional<Travel> travelOptional = travelRepository.findById(id);
    if (!travelOptional.isPresent()) {
        return ResponseEntity.notFound().build();
    }

    Travel travel = travelOptional.get();
    travel.setTitulo(titulo);
    travel.setDescricao(descricao);
    travel.setDestino(destino);
    travel.setFeedback(feedback);
    travel.setDuracao(duracao);

    try {
        if (imagemFile != null && !imagemFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/photos/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(imagemFile.getOriginalFilename());
            File dest = new File(uploadDir + fileName);
            imagemFile.transferTo(dest);

            travel.setImagem("/photos/" + fileName);
        }
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erro ao salvar imagem: " + e.getMessage());
    }

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
