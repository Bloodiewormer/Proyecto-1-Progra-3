package org.example.domain_layer;

import jakarta.xml.bind.annotation.*;
import org.example.utilities.EstadoReceta;

import java.util.*;

@XmlRootElement(name = "receta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    @XmlElement private int id;
    @XmlElement private int idPaciente;
    @XmlElement private int idMedico;
    @XmlElement private Date fechaConfeccion;
    @XmlElement private Date fechaRetiro;
    @XmlElement private String estado;

    @XmlElementWrapper(name = "detalles")
    @XmlElement(name = "detalle")
    private List<DetalleReceta> detalles = new ArrayList<>();

    public Receta() {}

    public Receta(int id, int idPaciente, int idMedico, Date fechaConfeccion, Date fechaRetiro, String estado) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
    }
    public int getId() { return id; }
    public int getIdPaciente() { return idPaciente; }
    public int getIdMedico() { return idMedico; }
    public Date getFechaConfeccion() { return fechaConfeccion; }
    public Date getFechaRetiro() { return fechaRetiro; }
    public String getEstado() { return estado; }
    public EstadoReceta getEstadoEnum() {return EstadoReceta.valueOf(estado);}
    public void setId(int id) { this.id = id; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }
    public void setFechaConfeccion(Date fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }
    public void setFechaRetiro(Date fechaRetiro) { this.fechaRetiro = fechaRetiro; }
    public void setEstado(EstadoReceta estado) {this.estado = estado.name();}
    public List<DetalleReceta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleReceta> detalles) { this.detalles = detalles; }

    public List<Medicamento> getMedicamentos() {
        List<Medicamento> medicamentos = new ArrayList<>();
        for (DetalleReceta detalle : detalles) {
            medicamentos.add(detalle.getMedicamento());
        }
        return medicamentos;
    }

    public Calendar getFecha() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaConfeccion);
        return cal;
    }
}
