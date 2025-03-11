package com.expense.tracker.service;

import com.expense.tracker.entity.JournalEntity;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    UserService userService;

    public void saveEntry(JournalEntity journalEntity, String username) {
        User user = userService.findByUserId(username);
        JournalEntity journal = journalEntryRepository.save(journalEntity);
        user.getJournalEntities().add(journal);
        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntity journalEntity) {
        JournalEntity journal = journalEntryRepository.save(journalEntity);
    }

    public List<JournalEntity> findAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntity findById(ObjectId id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    public void deleteById(ObjectId id, String username) {
        User user = userService.findByUserId(username);
        user.getJournalEntities().removeIf(journal -> journal.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }
}
