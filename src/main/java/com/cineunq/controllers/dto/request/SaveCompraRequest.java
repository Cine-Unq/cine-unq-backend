package com.cineunq.controllers.dto.request;

import com.cineunq.dominio.Asiento;
import com.cineunq.dominio.Cliente;
import com.cineunq.dominio.Pelicula;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SaveCompraRequest {
    private Long idCliente;
    private List<Long> idsAsientosComprados;
    private Long idPelicula;
}
