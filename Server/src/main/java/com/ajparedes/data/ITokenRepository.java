package com.ajparedes.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajparedes.model.Token;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {


}
