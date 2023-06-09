package com.cineunq.controllers.dto.response;

import com.cineunq.dominio.Asiento;
import com.cineunq.dominio.Compra;
import com.cineunq.dominio.enums.EstadoAsiento;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class CompraResponse {

    private Compra wrapped;

    public String getSala(){
        return wrapped.getFuncion().getSala().getNombreSala();
    }

    public String getFuncion(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(wrapped.getFuncion().getHoraInicio());
    }

    public String getPelicula(){
        return wrapped.getFuncion().getPeliculaEnFuncion().getNombre();
    }

    public String getCliente(){
        return wrapped.getUsuarioCompra().getCorreo();
    }
    public Long getClienteID(){
        return wrapped.getUsuarioCompra().getId();
    }


    public List<Asiento> getAsientosReservados(){
        return wrapped.getAsientosComprados().stream().filter(asiento -> asiento.getEstado() == EstadoAsiento.RESERVADO).toList();
    }

    public List<Asiento> getAsientosOcupados(){
        return wrapped.getAsientosComprados().stream().filter(asiento -> asiento.getEstado() == EstadoAsiento.OCUPADO).toList();
    }

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate getFechaCompra(){
        return wrapped.getFechaCompra();
    }

    public Long getCompraID(){
        return wrapped.getId();
    }
}
