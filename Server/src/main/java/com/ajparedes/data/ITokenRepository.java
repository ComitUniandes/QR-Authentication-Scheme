package com.ajparedes.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ajparedes.model.Token;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * ITokenRepository:
 * Interfaz que representa la conexión con la tabla 'tokens' de la base de datos.
 */
@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {


}
