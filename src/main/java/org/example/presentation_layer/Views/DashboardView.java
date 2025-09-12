package org.example.presentation_layer.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


// Librerías para los gráficos
import java.awt.*;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import org.example.domain_layer.Medicamento;
import org.example.domain_layer.Receta;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.RecetaService;
import org.example.utilities.EstadoReceta;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


public class DashboardView extends JPanel/* implements IServiceObserver<Object>*/ {
    private JPanel MainPanel;
    private JPanel DatosPanel;
    private JPanel MedicamentosGrapPanel;
    private JComboBox SelecMedicametocomboBox;
    private JButton agregarButton;
    private JTable DashBoardTable;
    private JPanel DesdeYearPicker;
    private JPanel HastaYearPicker;
    private JPanel DesdeMesPicker;
    private JPanel HastaMesPicker;
    private JComboBox SelecTipoGraficaCombox;
    private JButton eliminarSelecionadoButton;

    private DefaultTableModel medicamentosTableModel;

    private JMonthChooser desdeMesChooser;
    private JYearChooser  desdeYearChooser;
    private JMonthChooser hastaMesChooser;
    private JYearChooser  hastaYearChooser;

    private static final String COL_ID = "ID";
    private static final String COL_NOMBRE = "Medicamento";

    private final RecetaService recetaService;
    private final MedicamentoService medicamentoService;

    public DashboardView(RecetaService recetaService, MedicamentoService medicamentoService) {
        this.recetaService = recetaService;
        this.medicamentoService = medicamentoService;
        setComboxs();
        initDatePickers();
        initTableModel();
        initListeners();
        initChartsContainer();
        refreshCharts(); // primera carga
    }

    private void initTableModel() {
        medicamentosTableModel = new DefaultTableModel(new Object[]{COL_ID, COL_NOMBRE}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        DashBoardTable.setModel(medicamentosTableModel);
    }

    private void initListeners() {

        agregarButton.addActionListener(e -> {
            String nombreSel = (String) SelecMedicametocomboBox.getSelectedItem();
            if (nombreSel == null) return;
            Medicamento med = medicamentoService.leerTodos()
                    .stream()
                    .filter(m -> nombreSel.equals(m.getNombre()))
                    .findFirst()
                    .orElse(null);
            if (med == null) return;

            // Evitar duplicados
            if (!containsMedicamento(med.getCodigo())) {
                medicamentosTableModel.addRow(new Object[]{med.getCodigo(), med.getNombre()});
                refreshCharts();
            } else {
                JOptionPane.showMessageDialog(this, "Ya agregado.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        eliminarSelecionadoButton.addActionListener(e -> {
            int row = DashBoardTable.getSelectedRow();
            if (row >= 0) {
                medicamentosTableModel.removeRow(row);
                refreshCharts();
            }
        });
// Podrías agregar listeners de cambio de rango:
        // (Se fuerza refresh cuando cambie año/mes)
        // Para simplicidad se añade un botón externo si quieres luego;
        // aquí forzamos con popup close:
        addMonthYearSync(desdeMesChooser);
        addMonthYearSync(hastaMesChooser);

        SelecTipoGraficaCombox.addActionListener(e -> refreshCharts());

    }

    private void addMonthYearSync(JMonthChooser chooser) {
        if (chooser == null) return;
        chooser.addPropertyChangeListener(evt -> {
            if ("month".equals(evt.getPropertyName())) {
                refreshCharts();
            }
        });
    }

    private boolean containsMedicamento(int id) {
        for (int i = 0; i < medicamentosTableModel.getRowCount(); i++) {
            if ((int) medicamentosTableModel.getValueAt(i, 0) == id) return true;
        }
        return false;
    }

    private void initChartsContainer() {
        MedicamentosGrapPanel.setLayout(new GridLayout(1, 2));
    }

    private String getTipoGraficaSeleccionado() {
        Object v = SelecTipoGraficaCombox.getSelectedItem();
        return v == null ? "" : v.toString();
    }


    private void initDatePickers() {
        DesdeYearPicker.setLayout(new BorderLayout());
        DesdeMesPicker.setLayout(new BorderLayout());
        HastaYearPicker.setLayout(new BorderLayout());
        HastaMesPicker.setLayout(new BorderLayout());

        JMonthChooser desdeMes = new JMonthChooser();
        JYearChooser  desdeYear = new JYearChooser();
        JMonthChooser hastaMes = new JMonthChooser();
        JYearChooser  hastaYear = new JYearChooser();

        // Guardar referencias para uso posterior
        this.desdeMesChooser = desdeMes;
        this.desdeYearChooser = desdeYear;
        this.hastaMesChooser = hastaMes;
        this.hastaYearChooser = hastaYear;

        DesdeMesPicker.add(desdeMes, BorderLayout.CENTER);
        DesdeYearPicker.add(desdeYear, BorderLayout.CENTER);
        HastaMesPicker.add(hastaMes, BorderLayout.CENTER);
        HastaYearPicker.add(hastaYear, BorderLayout.CENTER);

        DesdeMesPicker.revalidate();
        DesdeMesPicker.repaint();
        DesdeYearPicker.revalidate();
        DesdeYearPicker.repaint();
        HastaMesPicker.revalidate();
        HastaMesPicker.repaint();
        HastaYearPicker.revalidate();
        HastaYearPicker.repaint();
    }


    private void refreshCharts() {
        if (desdeYearChooser == null || hastaYearChooser == null) return;

        YearMonth inicio = YearMonth.of(desdeYearChooser.getYear(), desdeMesChooser.getMonth() + 1);
        YearMonth fin = YearMonth.of(hastaYearChooser.getYear(), hastaMesChooser.getMonth() + 1);

        if (fin.isBefore(inicio)) {
            YearMonth tmp = inicio;
            inicio = fin;
            fin = tmp;
        }

        List<Receta> recetas = recetaService.leerTodos();
        List<Receta> recetasEnRango = filtrarRecetasPorRango(recetas, inicio, fin);

        String tipo = getTipoGraficaSeleccionado();

        MedicamentosGrapPanel.removeAll();

        if ("Medicamentos".equalsIgnoreCase(tipo)) {
            DefaultCategoryDataset lineDataset = buildLineDataset(recetasEnRango, inicio, fin);
            JFreeChart lineChart = ChartFactory.createLineChart(
                    "Unidades recetadas por medicamento (" + formatoYM(inicio) + " a " + formatoYM(fin) + ")",
                    "Mes",
                    "Unidades", // ==== CAMBIO (antes: Cantidad)
                    lineDataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );
            MedicamentosGrapPanel.add(new ChartPanel(lineChart), BorderLayout.CENTER);
        } else if ("Recetas".equalsIgnoreCase(tipo)) {
            DefaultPieDataset pieDataset = buildPieDataset(recetasEnRango);
            JFreeChart pieChart = ChartFactory.createPieChart(
                    "Recetas por Estado (" + formatoYM(inicio) + " a " + formatoYM(fin) + ")",
                    pieDataset,
                    true,
                    true,
                    false
            );
            MedicamentosGrapPanel.add(new ChartPanel(pieChart), BorderLayout.CENTER);
        } else {
            JLabel vacio = new JLabel("Seleccione un tipo de gráfica", SwingConstants.CENTER);
            MedicamentosGrapPanel.add(vacio, BorderLayout.CENTER);
        }

        MedicamentosGrapPanel.revalidate();
        MedicamentosGrapPanel.repaint();
    }

    private String formatoYM(YearMonth ym) {
        return String.format("%02d-%d", ym.getMonthValue(), ym.getYear());
    }

    private java.util.List<Receta> filtrarRecetasPorRango(java.util.List<Receta> origen, YearMonth ini, YearMonth fin) {
        return origen.stream()
                .filter(r -> {
                    YearMonth ym = YearMonth.from(r.getFecha().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    return !ym.isBefore(ini) && !ym.isAfter(fin);
                })
                .collect(Collectors.toList());
    }

    private DefaultCategoryDataset buildLineDataset(List<Receta> recetasEnRango, YearMonth inicio, YearMonth fin) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<YearMonth> meses = new ArrayList<>();
        YearMonth cursor = inicio;
        while (!cursor.isAfter(fin)) {
            meses.add(cursor);
            cursor = cursor.plusMonths(1);
        }

        // Medicamentos seleccionados en la tabla
        Map<Integer, String> medsSeleccionados = new LinkedHashMap<>();
        for (int i = 0; i < medicamentosTableModel.getRowCount(); i++) {
            int id = (int) medicamentosTableModel.getValueAt(i, 0);
            String nombre = (String) medicamentosTableModel.getValueAt(i, 1);
            medsSeleccionados.put(id, nombre);
        }
        if (medsSeleccionados.isEmpty()) return dataset;

        // Para cada medicamento sumar unidades recetadas por mes
        for (Map.Entry<Integer, String> entry : medsSeleccionados.entrySet()) {
            int medId = entry.getKey();
            String etiqueta = entry.getValue();
            Map<YearMonth, Integer> cantidades = sumarUnidadesPorMesYMedicamento(recetasEnRango, medId);
            for (YearMonth m : meses) {
                int valor = cantidades.getOrDefault(m, 0);
                dataset.addValue(valor, etiqueta, formatoYM(m));
            }
        }
        return dataset;
    }

    private Map<YearMonth, Integer> sumarUnidadesPorMesYMedicamento(List<Receta> recetas, int medicamentoId) {
        Map<YearMonth, Integer> mapa = new HashMap<>();
        for (Receta r : recetas) {
            if (r == null || r.getDetalles() == null) continue;
            YearMonth ym = YearMonth.from(r.getFecha().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            int acumulado = mapa.getOrDefault(ym, 0);

            // Sumar todas las cantidades de ese medicamento en la receta
            int sumaReceta = r.getDetalles().stream()
                    .filter(d -> d != null
                            && d.getMedicamento() != null
                            && d.getMedicamento().getCodigo() == medicamentoId)
                    .mapToInt(d -> d.getCantidad())
                    .sum();

            if (sumaReceta > 0) {
                mapa.put(ym, acumulado + sumaReceta);
            }
        }
        return mapa;
    }

    private DefaultPieDataset buildPieDataset(List<Receta> recetasEnRango) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Long> porEstado = recetasEnRango.stream()
                .collect(Collectors.groupingBy(Receta::getEstado, Collectors.counting()));

        if (porEstado.isEmpty()) {
            dataset.setValue("Sin datos", 1);
            return dataset;
        }

        porEstado.forEach((estado, cantidad) -> dataset.setValue(
                estado.toString() != null ? estado.toString() : "DESCONOCIDO",
                cantidad
        ));

        return dataset;
    }



    private void setComboxs() {

         SelecMedicametocomboBox.setModel(new DefaultComboBoxModel<>(medicamentoService.leerTodos().stream()
                    .map(m -> m.getNombre())
                    .toArray(String[]::new)));
        SelecTipoGraficaCombox.setModel(new DefaultComboBoxModel<>(new String[]{"Medicamentos", "Recetas"}));
    }


    public JPanel  getMainPanel() {return MainPanel;}




}
