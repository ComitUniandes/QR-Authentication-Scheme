package com.ajparedes.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajparedes.model.Token;

public interface ITokenRepository extends JpaRepository<Token, Integer> {

}
