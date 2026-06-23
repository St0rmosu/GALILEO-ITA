package com.mycompany.pcto;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GuiInterface {

    private static JFrame frame;
    private static JLabel imageLabel;
    private static JTextArea resultArea;
    private static JButton analyzeButton;
    private static JComboBox<String> fabricTypeCombo;
    private static JLabel statusLabel;
    private static String selectedImagePath;
    private static JProgressBar progressBar;

    private static final Color BG = UIManager.getColor("Panel.background");
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color SUCCESS = new Color(52, 211, 153);
    private static final Color ERROR = new Color(239, 68, 68);
    private static final Color WARNING = new Color(251, 191, 36);

    public static void run() {
        setupFlatLaf();
        SwingUtilities.invokeLater(GuiInterface::createAndShowGUI);
    }

    private static void setupFlatLaf() {
        FlatDarkLaf.setup();
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 8);
        UIManager.put("ProgressBar.arc", 4);
        UIManager.put("TextComponent.arc", 6);
        UIManager.put("ScrollBar.thumbArc", 8);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.trackInsets", new Insets(4, 4, 4, 4));
        UIManager.put("Button.focusWidth", 0);
        UIManager.put("Button.innerFocusWidth", 0);
        UIManager.put("Component.focusWidth", 0);
        UIManager.put("Component.innerFocusWidth", 0);
        FlatLaf.updateUI();
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Analizzatore Difetti Tessuti — Galileo Italia");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                System.out.println("Interfaccia grafica chiusa.");
            }
        });

        JPanel root = new JPanel(new BorderLayout());
        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createContent(), BorderLayout.CENTER);
        root.add(createStatusBar(), BorderLayout.SOUTH);

        frame.add(root);
        frame.setVisible(true);
    }

    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.putClientProperty("FlatLaf.style", "background: tint($Panel.background,5%)");

        JPanel bar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0, 0, new Color(99, 102, 241), getWidth(), 0, new Color(139, 92, 246)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(0, 4));
        header.add(bar, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.putClientProperty("FlatLaf.style", "background: tint($Panel.background,5%)");
        body.setBorder(new EmptyBorder(20, 28, 18, 28));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.putClientProperty("FlatLaf.style", "background: tint($Panel.background,5%)");

        JLabel brand = new JLabel("GALILEO ITALIA");
        brand.setFont(new Font("Inter", Font.BOLD, 11));
        brand.setForeground(ACCENT);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(brand);

        left.add(Box.createVerticalStrut(4));

        JLabel title = new JLabel("Analizzatore Difetti Tessuti");
        title.setFont(new Font("Inter", Font.BOLD, 24));
        title.setForeground(UIManager.getColor("Label.foreground"));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(title);

        left.add(Box.createVerticalStrut(3));

        JLabel subtitle = new JLabel("Powered by Gemma 3:4b \u00b7 AI Quality Inspection");
        subtitle.setFont(new Font("Inter", Font.PLAIN, 13));
        subtitle.setForeground(UIManager.getColor("Label.disabledForeground"));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(subtitle);

        body.add(left, BorderLayout.WEST);

        JLabel version = new JLabel("v2.0");
        version.setFont(new Font("Inter", Font.BOLD, 12));
        version.setForeground(UIManager.getColor("Label.disabledForeground"));
        version.putClientProperty("FlatLaf.styleClass", "h3");
        body.add(version, BorderLayout.EAST);

        header.add(body, BorderLayout.CENTER);
        return header;
    }

    private static JPanel createContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 16);
        content.add(createControlCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        content.add(createResultCard(), gbc);

        return content;
    }

    private static JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.putClientProperty("FlatLaf.style", "arc: 12");
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(1, 1, 1, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Inter", Font.BOLD, 10));
        sectionLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        card.add(sectionLabel, BorderLayout.NORTH);

        return card;
    }

    private static JPanel createControlCard() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setPreferredSize(new Dimension(340, 0));
        root.setMaximumSize(new Dimension(340, Integer.MAX_VALUE));

        JLabel sectionLabel = new JLabel("PANNELLO DI CONTROLLO");
        sectionLabel.setFont(new Font("Inter", Font.BOLD, 10));
        sectionLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(sectionLabel);

        root.add(Box.createVerticalStrut(18));

        JLabel fabricLabel = new JLabel("Tipo di tessuto");
        fabricLabel.setFont(new Font("Inter", Font.PLAIN, 13));
        fabricLabel.setForeground(UIManager.getColor("Label.foreground"));
        fabricLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(fabricLabel);

        root.add(Box.createVerticalStrut(6));

        fabricTypeCombo = new JComboBox<>(new String[]{
            "Cotone", "Lino", "Seta", "Lana", "Poliestere",
            "Nylon", "Viscosa", "Misto", "Altro"
        });
        fabricTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        fabricTypeCombo.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));
        root.add(fabricTypeCombo);

        root.add(Box.createVerticalStrut(22));

        JLabel imageLabelTitle = new JLabel("Immagine da analizzare");
        imageLabelTitle.setFont(new Font("Inter", Font.PLAIN, 13));
        imageLabelTitle.setForeground(UIManager.getColor("Label.foreground"));
        imageLabelTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(imageLabelTitle);

        root.add(Box.createVerticalStrut(10));

        imageLabel = new JLabel("Nessuna immagine selezionata", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Inter", Font.ITALIC, 12));
        imageLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        imageLabel.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));
        imageLabel.setPreferredSize(new Dimension(300, 200));
        imageLabel.setMaximumSize(new Dimension(Short.MAX_VALUE, 220));
        imageLabel.setOpaque(true);
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(imageLabel);

        root.add(Box.createVerticalStrut(16));

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        JButton selectBtn = new JButton("Scegli immagine");
        selectBtn.addActionListener(new SelectImageListener());
        buttonRow.add(selectBtn);

        analyzeButton = new JButton("Analizza difetti");
        analyzeButton.setEnabled(false);
        analyzeButton.addActionListener(new AnalyzeListener());
        buttonRow.add(analyzeButton);

        root.add(buttonRow);

        root.add(Box.createVerticalStrut(20));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(0, 4));
        progressBar.setMaximumSize(new Dimension(Short.MAX_VALUE, 4));
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(progressBar);

        root.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.putClientProperty("FlatLaf.style", "arc: 12");
        wrapper.add(root, BorderLayout.CENTER);
        return wrapper;
    }

    private static JPanel createResultCard() {
        JPanel root = new JPanel(new BorderLayout());

        JLabel sectionLabel = new JLabel("RISULTATI ANALISI");
        sectionLabel.setFont(new Font("Inter", Font.BOLD, 10));
        sectionLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        resultArea.setText("Nessuna analisi eseguita. Seleziona un'immagine e avvia l'analisi.");
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setBorder(new EmptyBorder(14, 16, 14, 16));

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")));

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bottomBar.setOpaque(false);

        JButton clearBtn = new JButton("Pulisci");
        clearBtn.addActionListener(e -> {
            resultArea.setText("");
            statusLabel.setText("Pronto");
        });

        JButton historyBtn = new JButton("Cronologia");
        historyBtn.addActionListener(e -> showHistoryDialog());

        JButton saveBtn = new JButton("Salva report");
        saveBtn.addActionListener(new SaveReportListener());

        bottomBar.add(clearBtn);
        bottomBar.add(historyBtn);
        bottomBar.add(saveBtn);

        root.add(sectionLabel, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottomBar, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.putClientProperty("FlatLaf.style", "arc: 12");
        wrapper.add(root, BorderLayout.CENTER);
        return wrapper;
    }

    private static JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.putClientProperty("FlatLaf.style", "background: tint($Panel.background,3%)");
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("Component.borderColor")),
            new EmptyBorder(10, 24, 10, 24)
        ));

        statusLabel = new JLabel("Pronto");
        statusLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        statusLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
        bar.add(statusLabel, BorderLayout.WEST);

        return bar;
    }

    private static void showHistoryDialog() {
        java.util.List<String[]> reports = ReportDatabase.getAll();
        if (reports.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessun report salvato.", "Cronologia", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] cols = {"ID", "Data", "Immagine", "Tessuto"};
        String[][] rows = new String[reports.size()][4];
        for (int i = 0; i < reports.size(); i++) rows[i] = reports.get(i);

        JTable table = new JTable(rows, cols);
        table.setFont(new Font("Inter", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] res = new int[1];
        JButton viewBtn = new JButton("Visualizza");
        viewBtn.addActionListener(ev -> {
            int r = table.getSelectedRow();
            if (r >= 0) res[0] = Integer.parseInt(rows[r][0]);
            Window w = SwingUtilities.getWindowAncestor(table);
            if (w != null) w.dispose();
        });

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnP.add(viewBtn);
        panel.add(btnP, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(frame, "Cronologia Report", true);
        dialog.add(panel);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        if (res[0] > 0) {
            String content = ReportDatabase.getById(res[0]);
            if (content != null) {
                resultArea.setText(content);
                statusLabel.setText("Report #" + res[0] + " caricato dal database");
                statusLabel.setForeground(ACCENT);
            }
        }
    }

    private static class SelectImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter(
                "Immagini (*.jpg, *.jpeg, *.png, *.bmp, *.gif)",
                "jpg", "jpeg", "png", "bmp", "gif"));

            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                selectedImagePath = f.getAbsolutePath();
                loadImagePreview(f);
                analyzeButton.setEnabled(true);
                statusLabel.setText("Immagine selezionata: " + f.getName());
                statusLabel.setForeground(ACCENT);
            }
        }
    }

    private static class AnalyzeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedImagePath == null) return;

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    SwingUtilities.invokeLater(() -> {
                        analyzeButton.setEnabled(false);
                        progressBar.setVisible(true);
                        statusLabel.setText("Analisi in corso con Gemma 3:4b...");
                        statusLabel.setForeground(WARNING);
                        resultArea.setText("Analisi in corso...\n\nIl modello AI sta esaminando l'immagine.\nPotrebbero essere necessari alcuni secondi.\n");
                    });
                    return FabricDefectAnalyzer.analyzeImageForDefects(selectedImagePath, (String) fabricTypeCombo.getSelectedItem());
                }

                @Override
                protected void done() {
                    progressBar.setVisible(false);
                    analyzeButton.setEnabled(true);
                    try {
                        String result = get();
                        resultArea.setText(result);
                        String fname = new File(selectedImagePath).getName();
                        String ftype = (String) fabricTypeCombo.getSelectedItem();
                        ReportDatabase.save(fname, ftype, result);
                        statusLabel.setText("Analisi completata \u2014 report salvato nel database");
                        statusLabel.setForeground(SUCCESS);
                    } catch (Exception ex) {
                        resultArea.setText("Errore durante l'analisi\n\n" + ex.getMessage());
                        statusLabel.setText("Errore durante l'analisi");
                        statusLabel.setForeground(ERROR);
                    }
                }
            };
            worker.execute();
        }
    }

    private static class SaveReportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = resultArea.getText().trim();
            if (text.isEmpty() || text.startsWith("Nessuna analisi")) {
                JOptionPane.showMessageDialog(frame, "Nessun report da salvare.", "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("report_analisi_tessuto.txt"));
            if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Files.write(chooser.getSelectedFile().toPath(), text.getBytes());
                    JOptionPane.showMessageDialog(frame, "Report salvato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static void loadImagePreview(File imageFile) {
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage();
            int mw = 280, mh = 180;
            int w = img.getWidth(null), h = img.getHeight(null);
            if (w > mw || h > mh) {
                double s = Math.min((double) mw / w, (double) mh / h);
                w = (int) (w * s);
                h = (int) (h * s);
                img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            }
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } catch (Exception ex) {
            imageLabel.setIcon(null);
            imageLabel.setText("Errore nel caricamento");
        }
    }
}
