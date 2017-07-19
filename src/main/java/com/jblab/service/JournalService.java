package com.jblab.service;

import com.jblab.model.Journal;

/**
 * Created by damir on 11.07.17.
 */
public interface JournalService {
    void save(Journal journal);

    Journal find(String uid);
}
