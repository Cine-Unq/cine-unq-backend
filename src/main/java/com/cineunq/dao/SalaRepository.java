package com.cineunq.dao;

import com.cineunq.dominio.Asiento;
import com.cineunq.dominio.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala,Long> {
}
