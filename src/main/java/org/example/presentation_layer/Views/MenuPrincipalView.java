package org.example.presentation_layer.Views;

import jdk.jfr.ContentType;
import org.example.presentation_layer.Components.CustomButton;
import org.example.presentation_layer.Controllers.LoginController;
import org.example.presentation_layer.Models.UserType;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class MenuPrincipalView extends JFrame {
    private JPanel mainPanel;
    private JPanel MainPanel;
    private JPanel EncogiblePanel;
    private JPanel OpcionesPanel;
    private JPanel ContentPanel;
    private JButton salirButton;
    private JButton medicosButton;
    private JButton farmaceutasButton;
    private JButton pacientesButton;
    private JButton medicamentosButton;
    private JButton dashboardButton;
    private JButton acercadeButton;
    private JButton prescribirButton;
    private JLabel ToggleButton;
    private JLabel MenuLable;

    private boolean menuVisible = false;
    private final int MENU_WIDTH = 150;
    private final int ACTUAL_WIDTH = MENU_WIDTH;

    private final LoginController controller;
    private final UsuarioService usuarioService;

    public MenuPrincipalView(UserType userType, LoginController controller, UsuarioService usuarioService)  {
        this.controller = controller;
        this.usuarioService = usuarioService;
        setContentPane(MainPanel);
        setTitle("Login");
        setSize(680, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setLocationRelativeTo(null);

        ButtonEnable(userType,true);
        ContentPanel.setLayout(new BorderLayout());


        EncogiblePanel.setPreferredSize(new Dimension(ACTUAL_WIDTH,EncogiblePanel.getHeight()));
        EncogiblePanel.setMinimumSize(new Dimension(35,0));
        EncogiblePanel.setMaximumSize(new Dimension(MENU_WIDTH,Integer.MAX_VALUE));
        EncogiblePanel.revalidate();
        EncogiblePanel.repaint();



        ToggleButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                toggleMenu();
            }
        });

        // Ensure everything is validated and visible before showing the frame
        MainPanel.revalidate();
        MainPanel.repaint();
        setVisible(true);


        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close MenuPrincipalView
                LoginView loginView = new LoginView(controller); // Show LoginView again
            }
        });

        medicosButton.addActionListener(e -> showMedicosView() );
        farmaceutasButton.addActionListener(e -> showFarmaceutasView() );
        pacientesButton.addActionListener(e -> showPacientesView() );
        medicamentosButton.addActionListener(e -> showMedicamentosView() );
        dashboardButton.addActionListener(e -> showDashboardView() );
        acercadeButton.addActionListener(e -> showAcercaDeView() );
        prescribirButton.addActionListener(e -> showPrescribirView() );

        init(userType);



    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        //icons
        Image DoctorIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Doctor.png"))).getImage();
        Image FarmaceuticoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Farmaceuta.png"))).getImage();
        Image PacienteIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Paciente.png"))).getImage();
        Image MedicamentoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Medicamento.png"))).getImage();
        Image DashboardIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/DashBoard.png"))).getImage();
        Image LogoutIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Logout.png"))).getImage();
        Image AdminIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Admin.png"))).getImage();
        Image PrescribirIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Prescripcion.png"))).getImage();
        Image InfoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Info.png"))).getImage();


        salirButton = new CustomButton("Salir", new Color(244, 243, 248), Color.BLACK, LogoutIcon);
        medicosButton = new CustomButton("Medicos", new Color(244, 243, 248), Color.BLACK, DoctorIcon);
        farmaceutasButton = new CustomButton("Farmaceutas", new Color(244, 243, 248), Color.BLACK, FarmaceuticoIcon);
        pacientesButton = new CustomButton("Pacientes", new Color(244, 243, 248), Color.BLACK, PacienteIcon);
        medicamentosButton = new CustomButton("Medicamentos", new Color(244, 243, 248), Color.BLACK, MedicamentoIcon);
        dashboardButton = new CustomButton("Dashboard", new Color(244, 243, 248), Color.BLACK, DashboardIcon);
        acercadeButton = new CustomButton("Acerca de", new Color(244, 243, 248), Color.BLACK,InfoIcon);
        prescribirButton = new CustomButton("Prescribir", new Color(244, 243, 248), Color.BLACK, PrescribirIcon);

    }

    public void ButtonEnable(UserType userType, boolean enable) {
        JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton, medicamentosButton, dashboardButton, acercadeButton, prescribirButton, salirButton};
        switch (userType) {
            case ADMINISTRADOR:
                medicosButton.setEnabled(enable);
                farmaceutasButton.setEnabled(enable);
                pacientesButton.setEnabled(enable);
                medicamentosButton.setEnabled(false);
                dashboardButton.setEnabled(enable);
                acercadeButton.setEnabled(enable);
                prescribirButton.setEnabled(enable);
                break;
            case FARMACEUTA:
                medicosButton.setEnabled(false);
                farmaceutasButton.setEnabled(false);
                pacientesButton.setEnabled(false);
                medicamentosButton.setEnabled(enable);
                dashboardButton.setEnabled(enable);
                acercadeButton.setEnabled(enable);
                prescribirButton.setEnabled(false);
                break;
            case MEDICO:
                medicosButton.setEnabled(false);
                farmaceutasButton.setEnabled(false);
                pacientesButton.setEnabled(enable);
                medicamentosButton.setEnabled(enable);
                dashboardButton.setEnabled(enable);
                acercadeButton.setEnabled(enable);
                prescribirButton.setEnabled(enable);
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no soportado: " + userType);
        }
        for (JButton btn : buttons) {
            btn.setVisible(btn.isEnabled());
        }
    }

    void toggleMenu() {
        menuVisible = !menuVisible;
        int targetWidth = menuVisible ? MENU_WIDTH : 25;
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            int currentWidth = EncogiblePanel.getWidth();
            if (currentWidth < targetWidth) { // Expand
                MenuLable.setVisible(true);
                currentWidth += 10;
                if (currentWidth > targetWidth) currentWidth = targetWidth;
            } else if (currentWidth > targetWidth) {// Collapse
                MenuLable.setVisible(false);
                currentWidth -= 10;
                if (currentWidth < targetWidth) currentWidth = targetWidth;
            }
            EncogiblePanel.setPreferredSize(new Dimension(currentWidth, EncogiblePanel.getHeight()));
            EncogiblePanel.revalidate();
            EncogiblePanel.repaint();


            JButton[] buttons = {medicosButton, farmaceutasButton, pacientesButton, medicamentosButton, dashboardButton, acercadeButton, prescribirButton, salirButton};
            for (JButton btn : buttons) {
                if (btn.isEnabled()) {
                    btn.setVisible(menuVisible || currentWidth > 25);
                }
            }

            if (currentWidth == targetWidth) {
                timer.stop();
            }
        });
        timer.start();
    }

    public void showMedicosView() {
        MedicoForm medicoForm = new MedicoForm(usuarioService);
        ContentPanel.removeAll();
        ContentPanel.add(medicoForm.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void showFarmaceutasView() {
        FarmaceutaForm farmaceutaForm = new FarmaceutaForm(usuarioService);
        ContentPanel.removeAll();
        ContentPanel.add(farmaceutaForm.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void showPacientesView() {
        PacienteForm pacienteForm = new PacienteForm();
        ContentPanel.removeAll();
        ContentPanel.add(pacienteForm.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void showMedicamentosView() {
        MedicamentoForm medicamentoForm = new MedicamentoForm();
        ContentPanel.removeAll();
        ContentPanel.add(medicamentoForm.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void showDashboardView() {
        DashboardView dashboardView = new DashboardView();
        ContentPanel.removeAll();
        ContentPanel.add(dashboardView.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void showAcercaDeView() {
        //AcercaDeView acercaDeView = new AcercaDeView();
        ContentPanel.removeAll();
        //ContentPanel.add(acercaDeView.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void showPrescribirView() {
        DespachoForm despachoForm = new DespachoForm();
        ContentPanel.removeAll();
        ContentPanel.add(despachoForm.getMainPanel());
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void init ( UserType userType) {
        toggleMenu();
        toggleMenu();
        switch (userType){
            case ADMINISTRADOR -> showMedicosView();
            case FARMACEUTA -> showMedicamentosView();
            case MEDICO -> showPacientesView();
            default -> throw new IllegalArgumentException("Tipo de usuario no soportado: " + userType);
        }
    }





}
