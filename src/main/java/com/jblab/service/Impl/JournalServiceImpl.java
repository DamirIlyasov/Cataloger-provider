package com.jblab.service.Impl;

import com.jblab.model.Journal;
import com.jblab.repository.JournalRepository;
import com.jblab.service.JournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by damir on 11.07.17.
 */
@Service
public class JournalServiceImpl implements JournalService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JournalRepository journalRepository;

    @Autowired
    public JournalServiceImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void save(Journal journal) {
        logger.info("-------------------------------------");
        logger.info("Journal : " + "saving started...");
        journalRepository.save(journal);
        logger.info("Journal : " + "saved successfully!");
    }

    @Override
    public Journal find(String uid) {
        return journalRepository.findOneByUid(uid);
    }
}
