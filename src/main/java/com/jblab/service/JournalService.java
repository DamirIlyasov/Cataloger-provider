package com.jblab.service;

import com.jblab.model.Journal;

public interface JournalService {
    void save(Journal journal);

    Journal find(String uid);
}
