package org.example.presentation_layer.Views;

import com.toedter.calendar.JDateChooser;
import org.example.domain_layer.DetalleReceta;
import org.example.domain_layer.Medicamento;
import org.example.domain_layer.Paciente;
import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Components.CustomTextField;
import org.example.presentation_layer.Models.DetalleRecetaTableModel;
import org.example.presentation_layer.Models.MedicamentoTableModel;
import org.example.presentation_layer.Models.PacienteTableModel;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.PacienteService;
import org.example.service_layer.RecetaService;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescribirForm extends JPanel {

    private JPanel mainPanel;
    private JPanel controlPanel;
    private JButton buscarMainMenuPacienteButton;
    private JButton agregarMedicamentoMainMenuButton;
    private JPanel RectaPanel;
    private JTable MedicamentoRecetatable;
    private JButton guardarMainMenuButton;
    private JButton detallesButton;
    private JButton limpiarMainMenuButton;
    private JButton descartarMainMenuButton;

    private JPanel BuscarPacienteLable;
    private JComboBox<String> filtrarPacientecomboBox;
    private JTextField busquedaPacientetextField;
    private JTable pacientesTable;

    private JPanel AgregarMedicamento;
    private JComboBox<String> FiltraMedicamentoComboBox;
    private JTextField BusquedaMedicamentoTextField;
    private JTable MedicamentosTable;

    private JSpinner catidadSpinner;
    private JSpinner DuracionSpinner;
    private JTextField textField1; // Indicaciones
    private JButton guardarDetallesMenuButton;
    private JButton cancerlarButton;

    private JPanel DetallePanel;
    private JButton cancePacienteMenulButton;
    private JButton guardarPacienteMenuButton;
    private JButton cancelMedicamentoMenuButton;
    private JButton guardarMedicamentoMenuButton;

    private JPanel window;
    private JPanel DatePickerPanel;
    private JLabel PacientePromtPlace;
    private JButton buscarPacientMenuButton;
    private JButton buscarMedicamentoMenuButton;

    private JDateChooser DatePicker;

    private final List<DetalleReceta> detalleRecetaList = new ArrayList<>();
    private Paciente selectedPaciente;
    private Medicamento currentMedicamento;

    private JDialog buscarPacienteDialog;
    private JDialog agregarMedicamentoDialog;
    private JDialog detalleDialog;

    public PrescribirForm(UsuarioService usuarioService,
                          PacienteService pacienteService,
                          MedicamentoService medicamentoService,
                          RecetaService recetaService,
                          int idMedico) {
        MedicamentoRecetatable.setModel(new DetalleRecetaTableModel(detalleRecetaList));
        initDatePicker();
        MedicamentosTable.setModel(new MedicamentoTableModel(medicamentoService.leerTodos()));
        pacientesTable.setModel(new PacienteTableModel(pacienteService.leerTodos()));

        catidadSpinner.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        DuracionSpinner.setModel(new SpinnerNumberModel(1, 1, 365, 1));

        filtrarPacientecomboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));
        FiltraMedicamentoComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ID", "Nombre"}));
    }

    // --- Getters for Controller ---
    public JPanel getMainPanel() { return mainPanel; }
    public JButton getBuscarMainMenuPacienteButton() { return buscarMainMenuPacienteButton; }
    public JButton getAgregarMedicamentoMainMenuButton() { return agregarMedicamentoMainMenuButton; }
    public JButton getGuardarMainMenuButton() { return guardarMainMenuButton; }
    public JButton getLimpiarMainMenuButton() { return limpiarMainMenuButton; }
    public JButton getDescartarMainMenuButton() { return descartarMainMenuButton; }
    public JButton getGuardarDetallesMenuButton() { return guardarDetallesMenuButton; }
    public JButton getCancerlarButton() { return cancerlarButton; }
    public JButton getGuardarPacienteMenuButton() { return guardarPacienteMenuButton; }
    public JButton getCancePacienteMenulButton() { return cancePacienteMenulButton; }
    public JButton getGuardarMedicamentoMenuButton() { return guardarMedicamentoMenuButton; }
    public JButton getCancelMedicamentoMenuButton() { return cancelMedicamentoMenuButton; }
    public JButton getBuscarPacientMenuButton() { return buscarPacientMenuButton; }
    public JButton getBuscarMedicamentoMenuButton() { return buscarMedicamentoMenuButton; }
    public JTable getPacientesTable() { return pacientesTable; }
    public JTable getMedicamentosTable() { return MedicamentosTable; }
    public JTable getMedicamentoRecetatable() { return MedicamentoRecetatable; }
    public JTextField getBusquedaPacientetextField() { return busquedaPacientetextField; }
    public JTextField getBusquedaMedicamentoTextField() { return BusquedaMedicamentoTextField; }
    public JComboBox<String> getFiltrarPacientecomboBox() { return filtrarPacientecomboBox; }
    public JComboBox<String> getFiltraMedicamentoComboBox() { return FiltraMedicamentoComboBox; }
    public JSpinner getCatidadSpinner() { return catidadSpinner; }
    public JSpinner getDuracionSpinner() { return DuracionSpinner; }
    public JTextField getIndicacionesField() { return textField1; }
    public JLabel getPacientePromtPlace() { return PacientePromtPlace; }
    public JDateChooser getDatePicker() { return DatePicker; }
    public List<DetalleReceta> getDetalleRecetaList() { return detalleRecetaList; }
    public Medicamento getCurrentMedicamento() { return currentMedicamento; }
    public void setCurrentMedicamento(Medicamento m) { this.currentMedicamento = m; }
    public Paciente getSelectedPaciente() { return selectedPaciente; }
    public void setSelectedPaciente(Paciente paciente) { this.selectedPaciente = paciente; }


    // --- Dialog Builders (for controller to call) ---
    public void showBuscarPacienteDialog() {
        if (buscarPacienteDialog == null) {
            buscarPacienteDialog = buildDialog("Buscar Paciente", BuscarPacienteLable);
        }
        if (!buscarPacienteDialog.isVisible()) buscarPacienteDialog.setVisible(true);
        else buscarPacienteDialog.toFront();
    }

    public void showAgregarMedicamentoDialog() {
        if (agregarMedicamentoDialog == null) {
            agregarMedicamentoDialog = buildDialog("Agregar Medicamento", AgregarMedicamento);
        }
        if (!agregarMedicamentoDialog.isVisible()) agregarMedicamentoDialog.setVisible(true);
        else agregarMedicamentoDialog.toFront();
    }

    public void showDetalleDialog() {
        if (detalleDialog == null) {
            detalleDialog = buildDialog("Detalle Medicamento", DetallePanel);
        }
        if (!detalleDialog.isVisible()) detalleDialog.setVisible(true);
        else detalleDialog.toFront();
    }

    public void closeBuscarPacienteDialog() { if (buscarPacienteDialog != null) buscarPacienteDialog.setVisible(false); }
    public void closeAgregarMedicamentoDialog() { if (agregarMedicamentoDialog != null) agregarMedicamentoDialog.setVisible(false); }
    public void closeDetalleDialog() { if (detalleDialog != null) detalleDialog.setVisible(false); }

    private JDialog buildDialog(String title, JPanel content) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(mainPanel), title, Dialog.ModalityType.APPLICATION_MODAL);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(content, BorderLayout.CENTER);
        d.setContentPane(wrapper);
        d.pack();
        d.setLocationRelativeTo(mainPanel);
        d.setResizable(false);
        return d;
    }

    private void initDatePicker() {
        DatePicker = new JDateChooser();
        DatePicker.setDate(new Date());
        DatePickerPanel.setLayout(new BorderLayout());
        DatePickerPanel.add(DatePicker, BorderLayout.CENTER);
    }

    private void createUIComponents() {
        buscarMainMenuPacienteButton = new BlueRoundedButton("Buscar Paciente");
        agregarMedicamentoMainMenuButton = new BlueRoundedButton("Agregar Medicamento");
        guardarMainMenuButton = new BlueRoundedButton("Guardar");
        detallesButton = new BlueRoundedButton("Detalles");
        limpiarMainMenuButton = new BlueRoundedButton("Limpiar");
        descartarMainMenuButton = new BlueRoundedButton("Descartar");
        guardarDetallesMenuButton = new BlueRoundedButton("Guardar");
        cancerlarButton = new BlueRoundedButton("Cancelar");
        cancePacienteMenulButton = new BlueRoundedButton("Cancelar");
        guardarPacienteMenuButton = new BlueRoundedButton("Guardar");
        cancelMedicamentoMenuButton = new BlueRoundedButton("Cancelar");
        guardarMedicamentoMenuButton = new BlueRoundedButton("Guardar");
        busquedaPacientetextField = new CustomTextField();
        BusquedaMedicamentoTextField = new CustomTextField();
    }
}