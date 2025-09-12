package org.example.service_layer;

import org.example.data_access_layer.IFileStore;
import org.example.domain_layer.Paciente;
import org.example.utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class  PacienteService implements IService<Paciente> {

    private final IFileStore<Paciente> fileStore;
    private final List<IServiceObserver<Paciente>> observers = new ArrayList<>();

    public PacienteService(IFileStore<Paciente> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(Paciente entity) {
        List<Paciente> pacientes = fileStore.readAll();
        // Verificar que el ID no se repita
        for (Paciente p : pacientes) {
            if (p.getId() == entity.getId()) {
                throw new IllegalArgumentException("Ya existe un paciente con el ID " + entity.getId());
            }
        }
        pacientes.add(entity);
        fileStore.writeAll(pacientes);
        notifyObservers(ChangeType.CREATED, entity);
    }

    @Override
    public void borrar(int id) {
        List<Paciente> pacientes = fileStore.readAll();
        Paciente eliminado = null;
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == id) {
                eliminado = pacientes.remove(i);
                break;
            }
        }
        fileStore.writeAll(pacientes);
        if (eliminado != null) notifyObservers(ChangeType.DELETED, eliminado);
    }

    @Override
    public void actualizar(Paciente entity) {
        List<Paciente> pacientes = fileStore.readAll();
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == entity.getId()) {
                pacientes.set(i, entity);
                break;
            }
        }
        fileStore.writeAll(pacientes);
        notifyObservers(ChangeType.UPDATED, entity);
    }

    @Override
    public List<Paciente> leerTodos() {
        return fileStore.readAll();
    }

    @Override
    public Paciente leerPorId(int id) {
        return fileStore.readAll()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addObserver(IServiceObserver<Paciente> listener) {
        if (listener != null) observers.add(listener);
    }

    private void notifyObservers(ChangeType type, Paciente entity) {
        for (IServiceObserver<Paciente> obs : observers) {
            obs.onDataChanged(type, entity);
        }
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        List<Paciente> result = new ArrayList<>();
        for (Paciente p : leerTodos()) {
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

}
