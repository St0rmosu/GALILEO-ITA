package com.mycompany.pcto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
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

    private static final Color BG_0 = new Color(18, 18, 18);
    private static final Color BG_1 = new Color(24, 24, 24);
    private static final Color BG_2 = new Color(30, 30, 30);
    private static final Color BG_3 = new Color(38, 38, 38);
    private static final Color SURFACE = new Color(45, 45, 45);
    private static final Color SURFACE_HOVER = new Color(55, 55, 55);
    private static final Color ACCENT_PRIMARY = new Color(99, 102, 241);
    private static final Color ACCENT_HOVER = new Color(129, 140, 248);
    private static final Color ACCENT_GLOW = new Color(99, 102, 241, 60);
    private static final Color TEXT_WHITE = new Color(240, 240, 245);
    private static final Color TEXT_MUTED = new Color(150, 150, 165);
    private static final Color TEXT_DIM = new Color(100, 100, 115);
    private static final Color BORDER = new Color(50, 50, 55);
    private static final Color SUCCESS = new Color(52, 211, 153);
    private static final Color ERROR = new Color(239, 68, 68);
    private static final Color WARNING = new Color(251, 191, 36);

    private static void applyFlatLaf() {
        try {
            UIManager.put("Panel.background", BG_0);
            UIManager.put("OptionPane.background", BG_2);
            UIManager.put("OptionPane.messageForeground", TEXT_WHITE);
            UIManager.put("Button.background", SURFACE);
            UIManager.put("Button.foreground", TEXT_WHITE);
            UIManager.put("ComboBox.background", BG_2);
            UIManager.put("ComboBox.foreground", TEXT_WHITE);
            UIManager.put("ComboBox.selectionBackground", SURFACE);
            UIManager.put("ComboBox.selectionForeground", TEXT_WHITE);
        } catch (Exception ignored) {}
    }

    public static void run() {
        SwingUtilities.invokeLater(() -> {
            applyFlatLaf();
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG_0);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Interfaccia grafica chiusa. Tornando al menu principale...");
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_0);

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createContent(), BorderLayout.CENTER);
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_1);
        header.setBorder(new EmptyBorder(0, 0, 1, 0));

        JPanel gradientBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(99, 102, 241), getWidth(), 0, new Color(139, 92, 246));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), 4);
                g2.dispose();
            }
        };
        gradientBar.setPreferredSize(new Dimension(0, 4));
        gradientBar.setBackground(BG_1);
        header.add(gradientBar, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_1);
        content.setBorder(new EmptyBorder(20, 30, 18, 30));

        JPanel leftBlock = new JPanel();
        leftBlock.setLayout(new BoxLayout(leftBlock, BoxLayout.Y_AXIS));
        leftBlock.setBackground(BG_1);

        JLabel brand = new JLabel("GALILEO ITALIA");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 11));
        brand.setForeground(ACCENT_PRIMARY);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftBlock.add(brand);

        leftBlock.add(Box.createVerticalStrut(4));

        JLabel title = new JLabel("Analizzatore Difetti Tessuti");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftBlock.add(title);

        leftBlock.add(Box.createVerticalStrut(3));

        JLabel subtitle = new JLabel("Powered by Gemma 3:4b \u00b7 AI Quality Inspection");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftBlock.add(subtitle);

        content.add(leftBlock, BorderLayout.WEST);

        JPanel badge = new JPanel();
        badge.setBackground(new Color(99, 102, 241, 25));
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 102, 241, 50), 1),
            new EmptyBorder(4, 12, 4, 12)
        ));
        badge.setPreferredSize(new Dimension(90, 28));
        badge.setMaximumSize(new Dimension(90, 28));

        JLabel dot = new JLabel("\u25CF");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        dot.setForeground(new Color(52, 211, 153));

        JLabel ver = new JLabel("v2.0");
        ver.setFont(new Font("Segoe UI", Font.BOLD, 11));
        ver.setForeground(TEXT_MUTED);

        badge.add(dot);
        badge.add(ver);
        content.add(badge, BorderLayout.EAST);

        header.add(content, BorderLayout.CENTER);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        header.add(sep, BorderLayout.SOUTH);

        return header;
    }

    private static JPanel createContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG_0);
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

    private static JPanel wrapCard(JPanel inner) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_2);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    private static JPanel createControlCard() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(BG_2);
        root.setPreferredSize(new Dimension(340, 0));
        root.setMaximumSize(new Dimension(340, Integer.MAX_VALUE));

        JLabel sectionLabel = new JLabel("PANNELLO DI CONTROLLO");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        sectionLabel.setForeground(TEXT_DIM);
        sectionLabel.setLetterSpacing(0.02f);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(sectionLabel);

        root.add(Box.createVerticalStrut(18));

        JLabel fabricLabel = new JLabel("Tipo di tessuto");
        fabricLabel.setFont(new Font("Segoe UI", Font.MEDIUM, 12));
        fabricLabel.setForeground(TEXT_WHITE);
        fabricLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(fabricLabel);

        root.add(Box.createVerticalStrut(6));

        fabricTypeCombo = new JComboBox<>(new String[]{
            "Cotone", "Lino", "Seta", "Lana", "Poliestere",
            "Nylon", "Viscosa", "Misto", "Altro"
        });
        styleCombo(fabricTypeCombo);
        fabricTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        fabricTypeCombo.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));
        root.add(fabricTypeCombo);

        root.add(Box.createVerticalStrut(22));

        JLabel imageLabelTitle = new JLabel("Immagine da analizzare");
        imageLabelTitle.setFont(new Font("Segoe UI", Font.MEDIUM, 12));
        imageLabelTitle.setForeground(TEXT_WHITE);
        imageLabelTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(imageLabelTitle);

        root.add(Box.createVerticalStrut(10));

        imageLabel = new JLabel("Nessuna immagine selezionata", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        imageLabel.setForeground(TEXT_DIM);
        imageLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(0, 0, 0, 0)
        ));
        imageLabel.setPreferredSize(new Dimension(300, 200));
        imageLabel.setMaximumSize(new Dimension(Short.MAX_VALUE, 220));
        imageLabel.setBackground(BG_1);
        imageLabel.setOpaque(true);
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(imageLabel);

        root.add(Box.createVerticalStrut(16));

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonRow.setBackground(BG_2);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        JButton selectBtn = createGhostButton("Scegli immagine");
        selectBtn.addActionListener(new SelectImageListener());
        buttonRow.add(selectBtn);

        analyzeButton = createPrimaryButton("Analizza difetti");
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
        progressBar.setBackground(BORDER);
        progressBar.setForeground(ACCENT_PRIMARY);
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(progressBar);

        root.add(Box.createVerticalGlue());

        return root;
    }

    private static JPanel createResultCard() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_2);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_2);

        JLabel sectionLabel = new JLabel("RISULTATI ANALISI");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        sectionLabel.setForeground(TEXT_DIM);
        sectionLabel.setLetterSpacing(0.02f);
        top.add(sectionLabel, BorderLayout.WEST);

        root.add(top, BorderLayout.NORTH);

        root.add(Box.createVerticalStrut(14), BorderLayout.CENTER);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        resultArea.setBackground(BG_1);
        resultArea.setForeground(TEXT_WHITE);
        resultArea.setCaretColor(ACCENT_PRIMARY);
        resultArea.setSelectionColor(ACCENT_GLOW);
        resultArea.setSelectedTextColor(TEXT_WHITE);
        resultArea.setText("Nessuna analisi eseguita. Seleziona un'immagine e avvia l'analisi.");
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        resultArea.setBorder(new EmptyBorder(14, 16, 14, 16));

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(BG_1);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(60, 60, 65);
                this.trackColor = BG_1;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bottomBar.setBackground(BG_2);

        JButton clearBtn = createGhostButton("Pulisci");
        clearBtn.addActionListener(e -> {
            resultArea.setText("");
            statusLabel.setText("Pronto");
            statusLabel.setForeground(TEXT_MUTED);
        });

        JButton saveBtn = createPrimaryButton("Salva report");
        saveBtn.addActionListener(new SaveReportListener());

        bottomBar.add(clearBtn);
        bottomBar.add(saveBtn);

        root.add(scroll, BorderLayout.CENTER);
        root.add(bottomBar, BorderLayout.SOUTH);

        return root;
    }

    private static JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_1);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
            new EmptyBorder(10, 24, 10, 24)
        ));

        statusLabel = new JLabel("\u25CF  Pronto");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_MUTED);
        bar.add(statusLabel, BorderLayout.WEST);

        return bar;
    }

    private static void styleCombo(JComboBox<String> combo) {
        combo.setBackground(BG_2);
        combo.setForeground(TEXT_WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(0, 10, 0, 6)
        ));
        ((JComponent) combo.getRenderer()).setBackground(BG_2);
        ((JComponent) combo.getRenderer()).setForeground(TEXT_WHITE);
    }

    private static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                boolean hover = getModel().isRollover();
                boolean press = getModel().isPressed();
                Color bg = isEnabled() ? (press ? ACCENT_PRIMARY.darker() : hover ? ACCENT_HOVER : ACCENT_PRIMARY) : new Color(60, 60, 65);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 8, 8));
                g2.setColor(TEXT_WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(getText());
                int th = fm.getAscent();
                g2.drawString(getText(), (w - tw) / 2, (h + th) / 2 - 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    private static JButton createGhostButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                boolean hover = getModel().isRollover();
                boolean press = getModel().isPressed();
                Color border = isEnabled() ? (press ? ACCENT_PRIMARY : hover ? ACCENT_HOVER : BORDER) : new Color(45, 45, 50);
                Color bg = isEnabled() ? (press ? new Color(99, 102, 241, 40) : hover ? new Color(99, 102, 241, 18) : BG_2) : BG_2;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 8, 8));
                g2.setColor(border);
                g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 8, 8));
                g2.setColor(isEnabled() ? TEXT_WHITE : TEXT_DIM);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(getText());
                int th = fm.getAscent();
                g2.drawString(getText(), (w - tw) / 2, (h + th) / 2 - 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    private static class SelectImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter(
                "Immagini (*.jpg, *.jpeg, *.png, *.bmp, *.gif)",
                "jpg", "jpeg", "png", "bmp", "gif"));

            int result = chooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                selectedImagePath = f.getAbsolutePath();
                loadImagePreview(f);
                analyzeButton.setEnabled(true);
                statusLabel.setText("\u25CF  Immagine selezionata: " + f.getName());
                statusLabel.setForeground(ACCENT_HOVER);
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
                        statusLabel.setText("\u25CF  Analisi in corso con Gemma 3:4b...");
                        statusLabel.setForeground(WARNING);
                        resultArea.setText("");
                        resultArea.setText("Analisi in corso...\n\nIl modello AI sta esaminando l'immagine.\nPotrebbero essere necessari alcuni secondi.\n");
                    });
                    String fabricType = (String) fabricTypeCombo.getSelectedItem();
                    return FabricDefectAnalyzer.analyzeImageForDefects(selectedImagePath, fabricType);
                }

                @Override
                protected void done() {
                    progressBar.setVisible(false);
                    analyzeButton.setEnabled(true);
                    try {
                        String result = get();
                        resultArea.setText(result);
                        statusLabel.setText("\u25CF  Analisi completata con successo");
                        statusLabel.setForeground(SUCCESS);
                    } catch (Exception ex) {
                        resultArea.setText("Errore durante l'analisi\n\n" + ex.getMessage());
                        statusLabel.setText("\u25CF  Errore durante l'analisi");
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
            if (resultArea.getText().trim().isEmpty() || resultArea.getText().startsWith("Nessuna analisi")) {
                JOptionPane.showMessageDialog(frame, "Nessun report da salvare.",
                    "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("report_analisi_tessuto.txt"));
            int result = chooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Files.write(
                        chooser.getSelectedFile().toPath(),
                        resultArea.getText().getBytes()
                    );
                    JOptionPane.showMessageDialog(frame, "Report salvato con successo!",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Errore: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
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
