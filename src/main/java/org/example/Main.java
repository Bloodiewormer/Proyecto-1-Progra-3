package org.example;

import org.example.data_access_layer.UsuarioFileStore;
import org.example.domain_layer.Administrador;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Usuario;
import org.example.presentation_layer.Controllers.LoginController;
import org.example.presentation_layer.Views.LoginView;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.io.File;

//Azul usado para botones #004aad
//Gris claro para fondos #F4F3F8

public class Main {
    public static void main(String[] args) {
        UsuarioFileStore fileStore = new UsuarioFileStore(new File("usuarios.xml"));
        UsuarioService usuarioService = new UsuarioService(fileStore);
        LoginController controller = new LoginController(usuarioService);

        if (usuarioService.leerTodos().isEmpty()) {
            usuarioService.agregar(new Administrador(1, "123", "Admin"));
            usuarioService.agregar(new Medico(2, "123", "Medico1", "Cardiologia"));
            usuarioService.agregar(new Farmaceuta(3, "123", "Farmaceuta1"));
        }

        SwingUtilities.invokeLater(() -> {LoginView loginView = new LoginView(controller);});
    }
}