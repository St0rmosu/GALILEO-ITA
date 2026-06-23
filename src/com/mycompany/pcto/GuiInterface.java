package com.mycompany.pcto;

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

    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(22, 22, 22);
    private static final Color SURFACE_BG = new Color(40, 40, 40);
    private static final Color ACCENT = new Color(0, 188, 212);
    private static final Color ACCENT_DARK = new Color(0, 150, 170);
    private static final Color TEXT_PRIMARY = new Color(220, 220, 220);
    private static final Color TEXT_SECONDARY = new Color(160, 160, 160);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color SUCCESS = new Color(76, 175, 80);
    private static final Color ERROR = new Color(244, 67, 54);

    public static void run() {
        SwingUtilities.invokeLater(GuiInterface::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("ANALIZZATORE DIFETTI TESSUTI - GALILEO ITALIA");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(DARK_BG);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Interfaccia grafica chiusa. Tornando al menu principale...");
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setBackground(DARK_BG);
        centerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        centerPanel.add(createControlPanel(), BorderLayout.WEST);
        centerPanel.add(createResultPanel(), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARKER_BG);
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("ANALIZZATORE DIFETTI TESSUTI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT);

        JLabel subtitleLabel = new JLabel("GALILEO ITALIA SRL  |  Powered by Gemma3:4b AI");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(DARKER_BG);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        JLabel versionLabel = new JLabel("v2.0");
        versionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        versionLabel.setForeground(TEXT_SECONDARY);
        headerPanel.add(versionLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(SURFACE_BG);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        controlPanel.setPreferredSize(new Dimension(350, 0));

        JLabel sectionTitle = new JLabel("CONTROLLI");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 13));
        sectionTitle.setForeground(ACCENT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(sectionTitle);
        controlPanel.add(Box.createVerticalStrut(15));

        JLabel fabricLabel = new JLabel("Tipo Tessuto");
        fabricLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fabricLabel.setForeground(TEXT_PRIMARY);
        fabricLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(fabricLabel);
        controlPanel.add(Box.createVerticalStrut(5));

        fabricTypeCombo = new JComboBox<>(new String[]{
            "Cotone", "Lino", "Seta", "Lana", "Poliestere",
            "Nylon", "Viscosa", "Misto", "Altro"
        });
        styleCombo(fabricTypeCombo);
        fabricTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(fabricTypeCombo);

        controlPanel.add(Box.createVerticalStrut(20));

        JButton selectImageButton = createStyledButton("SELEZIONA IMMAGINE", ACCENT);
        selectImageButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectImageButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        selectImageButton.addActionListener(new SelectImageListener());
        controlPanel.add(selectImageButton);

        controlPanel.add(Box.createVerticalStrut(15));

        imageLabel = new JLabel("Nessuna immagine selezionata", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        imageLabel.setForeground(TEXT_SECONDARY);
        imageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        imageLabel.setPreferredSize(new Dimension(300, 200));
        imageLabel.setMaximumSize(new Dimension(320, 220));
        imageLabel.setBackground(DARKER_BG);
        imageLabel.setOpaque(true);
        controlPanel.add(imageLabel);

        controlPanel.add(Box.createVerticalStrut(20));

        analyzeButton = createStyledButton("ANALIZZA DIFETTI", ACCENT);
        analyzeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        analyzeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        analyzeButton.setFont(new Font("Arial", Font.BOLD, 15));
        analyzeButton.setEnabled(false);
        analyzeButton.addActionListener(new AnalyzeListener());
        controlPanel.add(analyzeButton);

        controlPanel.add(Box.createVerticalGlue());

        return controlPanel;
    }

    private static JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(SURFACE_BG);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel headerResultPanel = new JPanel(new BorderLayout());
        headerResultPanel.setBackground(SURFACE_BG);

        JLabel resultTitle = new JLabel("RISULTATI ANALISI");
        resultTitle.setFont(new Font("Arial", Font.BOLD, 13));
        resultTitle.setForeground(ACCENT);
        headerResultPanel.add(resultTitle, BorderLayout.WEST);

        resultPanel.add(headerResultPanel, BorderLayout.NORTH);
        resultPanel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setBackground(DARKER_BG);
        resultArea.setForeground(TEXT_PRIMARY);
        resultArea.setCaretColor(ACCENT);
        resultArea.setSelectionColor(new Color(0, 188, 212, 80));
        resultArea.setSelectedTextColor(Color.WHITE);
        resultArea.setText("Seleziona un'immagine e clicca 'Analizza Difetti' per iniziare...");
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(DARKER_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(SURFACE_BG);

        JButton clearButton = createStyledButton("PULISCI", TEXT_SECONDARY);
        clearButton.addActionListener(e -> resultArea.setText(""));

        JButton saveButton = createStyledButton("SALVA REPORT", SUCCESS);
        saveButton.addActionListener(new SaveReportListener());

        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);

        resultPanel.add(scrollPane, BorderLayout.CENTER);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);

        return resultPanel;
    }

    private static JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(DARKER_BG);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(8, 15, 8, 15)
        ));

        statusLabel = new JLabel("Pronto");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusPanel.add(statusLabel, BorderLayout.WEST);

        return statusPanel;
    }

    private static void styleCombo(JComboBox<String> combo) {
        combo.setBackground(DARKER_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        ((JComponent) combo.getRenderer()).setBackground(DARKER_BG);
        ((JComponent) combo.getRenderer()).setForeground(TEXT_PRIMARY);
    }

    private static JButton createStyledButton(String text, Color accentColor) {
        JButton button = new JButton(text);
        button.setBackground(accentColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(180, 38));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(accentColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(accentColor);
            }
        });
        return button;
    }

    private static class SelectImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Immagini (*.jpg, *.jpeg, *.png, *.bmp, *.gif)",
                "jpg", "jpeg", "png", "bmp", "gif"));

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath = selectedFile.getAbsolutePath();
                loadImagePreview(selectedFile);
                analyzeButton.setEnabled(true);
                statusLabel.setText("Immagine selezionata: " + selectedFile.getName());
                statusLabel.setForeground(ACCENT);
            }
        }
    }

    private static class AnalyzeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedImagePath == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona prima un'immagine!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    SwingUtilities.invokeLater(() -> {
                        analyzeButton.setEnabled(false);
                        analyzeButton.setText("ANALISI IN CORSO...");
                        analyzeButton.setBackground(new Color(255, 152, 0));
                        statusLabel.setText("Analisi in corso con Gemma3:4b... Attendere");
                        statusLabel.setForeground(new Color(255, 152, 0));
                        resultArea.setText("Analizzando l'immagine con Gemma3:4b...\nQuesto potrebbe richiedere alcuni minuti...");
                    });

                    String fabricType = (String) fabricTypeCombo.getSelectedItem();
                    return FabricDefectAnalyzer.analyzeImageForDefects(selectedImagePath, fabricType);
                }

                @Override
                protected void done() {
                    try {
                        String result = get();
                        resultArea.setText("ANALISI COMPLETATA CON SUCCESSO\n\n" + result);
                        statusLabel.setText("Analisi completata con successo");
                        statusLabel.setForeground(SUCCESS);
                    } catch (Exception ex) {
                        resultArea.setText("ERRORE DURANTE L'ANALISI\n\nErrore: " + ex.getMessage() + "\n\nSuggerimenti:\n- Verifica che Ollama sia in esecuzione\n- Controlla che il modello Gemma3:4b sia installato\n- Assicurati che l'immagine sia valida");
                        statusLabel.setText("Errore durante l'analisi");
                        statusLabel.setForeground(ERROR);
                    } finally {
                        analyzeButton.setEnabled(true);
                        analyzeButton.setText("ANALIZZA DIFETTI");
                        analyzeButton.setBackground(ACCENT);
                    }
                }
            };
            worker.execute();
        }
    }

    private static class SaveReportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (resultArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nessun report da salvare!",
                    "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("report_analisi_tessuto.txt"));

            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Files.write(
                        fileChooser.getSelectedFile().toPath(),
                        resultArea.getText().getBytes()
                    );
                    JOptionPane.showMessageDialog(frame, "Report salvato con successo!",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Errore nel salvare il file: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static void loadImagePreview(File imageFile) {
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage();

            int maxWidth = 280;
            int maxHeight = 180;
            int width = img.getWidth(null);
            int height = img.getHeight(null);

            if (width > maxWidth || height > maxHeight) {
                double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
                width = (int) (width * scale);
                height = (int) (height * scale);
                img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }

            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } catch (Exception ex) {
            imageLabel.setIcon(null);
            imageLabel.setText("Errore nel caricamento dell'immagine");
        }
    }
}
