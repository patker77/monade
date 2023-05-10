package com.josephnzi.monade.repository;

import com.josephnzi.monade.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository  extends JpaRepository<VerificationToken,Long> {
}
