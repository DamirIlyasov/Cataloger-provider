package com.jblab.repository;

import com.jblab.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by damir on 11.07.17.
 */
@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    Journal findOneByUid(String uid);
}
