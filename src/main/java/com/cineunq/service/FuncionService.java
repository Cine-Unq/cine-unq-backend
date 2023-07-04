package com.cineunq.service;

import com.cineunq.controllers.dto.request.PostFuncionRequest;
import com.cineunq.dao.FuncionRepository;
import com.cineunq.dominio.Funcion;
import com.cineunq.dominio.Pelicula;
import com.cineunq.dominio.Sala;
import com.cineunq.exceptions.MovieUnqLogicException;
import com.cineunq.exceptions.NotFoundException;
import com.cineunq.service.interfaces.IFuncionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class FuncionService implements IFuncionService {

    private FuncionRepository funcionRepository;

    private SalaService salaService;

    private PeliculaService peliculaService;

    @Autowired
    public FuncionService(FuncionRepository funcionRepository, SalaService salaService, PeliculaService peliculaService) {
        this.funcionRepository = funcionRepository;
        this.salaService = salaService;
        this.peliculaService = peliculaService;
    }

    @Override
    public Funcion saveFuncion(Funcion f) {
        return funcionRepository.save(f);
    }

    public boolean estaSalaOcupada(Long idSala, LocalDateTime fechaInicioFuncion){
        return funcionRepository.estaSalaOcupada(idSala,fechaInicioFuncion) > 0;
    }

    @Transactional(rollbackOn = Exception.class)
    public Funcion saveFuncion(PostFuncionRequest funcion) {
        try {
            if(funcion.getHoraInicio().isBefore(LocalDateTime.now())){
                throw new MovieUnqLogicException("No se puede crear una Funcion que sea inmediata o pasada");
            }
            Sala s1 = salaService.findById(Long.valueOf(funcion.getSala()));
            Pelicula p1 = peliculaService.findByID(Long.valueOf(funcion.getPelicula()));
            boolean estaOcupada = estaSalaOcupada(Long.valueOf(funcion.getSala()),funcion.getHoraInicio());
            if(!estaOcupada) {
                return funcionRepository.save(Funcion.builder().peliculaEnFuncion(p1).sala(s1).horaInicio(funcion.getHoraInicio()).asientos(new ArrayList<>()).build());
            }else {
                throw new MovieUnqLogicException("No se puede crear una Funcion ahora mismo debido a que ya existe una en curso en la sala");
            }
        } catch (NotFoundException e) {
            throw new MovieUnqLogicException("Funcion : No se a podido guardar la funcion");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public Funcion saveFuncion(Funcion f,Long idSala) {
        try {
            Sala s1 = salaService.findById(idSala);
            Pelicula p1 = peliculaService.findByID(f.getPeliculaEnFuncion().getId());
            boolean estaOcupada = estaSalaOcupada(idSala,f.getHoraInicio());
            if(!estaOcupada) {
                return funcionRepository.save(f);
            }else {
                throw new MovieUnqLogicException("No se puede crear una Funcion ahora mismo debido a que ya existe una en curso en la sala");
            }
        } catch (NotFoundException e) {
            throw new MovieUnqLogicException("Funcion : No se a podido guardar la funcion");
        }
    }

    public Map<String,List<Funcion>> funcionesPorPelicula(Long idPelicula){
        List<Funcion> funcionesPelicula = funcionRepository.funcionesPorPelicula(idPelicula);
        Map<String,List<Funcion>> funcionesPorSala = new HashMap<>();
        for (Funcion f : funcionesPelicula) {
            String tipoSala = f.getSala().getTipoSala().getTipoSala().nombre();
            if(funcionesPorSala.get(tipoSala) != null){
                funcionesPorSala.get(tipoSala).add(f);
            }else{
                List<Funcion> element = new ArrayList<>();
                element.add(f);
                funcionesPorSala.put(tipoSala,element);
            }
        }
        return funcionesPorSala;
    }

    public Funcion findById(Long id){
        Optional<Funcion> funcion = funcionRepository.findById(id);
        if(funcion.isPresent()){
            return funcion.get();
        }
        throw new NotFoundException("Funcion : No se a encontrado la Funcion solicitada");
    }
}
