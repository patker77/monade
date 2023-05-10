package com.josephnzi.monade.repository;

import com.josephnzi.monade.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
}
