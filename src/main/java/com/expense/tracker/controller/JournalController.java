package com.expense.tracker.controller;

import com.expense.tracker.entity.JournalEntity;
import com.expense.tracker.entity.User;
import com.expense.tracker.service.JournalEntryService;
import com.expense.tracker.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/journal")
public class JournalController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntity>> getAllUserJournalEntries() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserId(username);

        if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(user.getJournalEntities(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntity> getJournalEntry(@PathVariable ObjectId id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserId(username);

        List<JournalEntity> journalEntities = user.getJournalEntities().stream().filter(x -> x.getId().equals(id)).toList();
        if (!journalEntities.isEmpty()) {
            JournalEntity journalEntity = journalEntryService.findById(id);

            if (journalEntity == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(journalEntity, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}/user/{username}")
    public boolean deleteJournalEntry(@PathVariable ObjectId id, @PathVariable String username) {

        journalEntryService.deleteById(id, username);
        return true;
    }

    @PostMapping
    public ResponseEntity<JournalEntity> createJournalEntry(@RequestBody JournalEntity journalEntity) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntity.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntity, username);
            return new ResponseEntity<>(journalEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/user/{username}")
    public ResponseEntity<JournalEntity> updateJournalEntry(@PathVariable ObjectId id, @PathVariable String username, @RequestBody JournalEntity journalEntity) {
        JournalEntity oldJournalEntity = journalEntryService.findById(id);

        if (oldJournalEntity != null) {
            oldJournalEntity.setTitle(journalEntity.getTitle() != null && !journalEntity.getTitle().equals("") ? journalEntity.getTitle() : oldJournalEntity.getTitle());
            oldJournalEntity.setContent(journalEntity.getContent() != null && !journalEntity.getContent().equals("") ? journalEntity.getContent() : oldJournalEntity.getContent());
            journalEntryService.saveEntry(oldJournalEntity);
            return new ResponseEntity<>(oldJournalEntity, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
