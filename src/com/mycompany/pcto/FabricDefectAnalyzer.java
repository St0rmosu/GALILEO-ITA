package com.mycompany.pcto;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.Base64;

import com.fasterxml.jackson.databind.*;

public class FabricDefectAnalyzer {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL = "gemma3:2b";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(30))
            .build();

    private static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final int MAX_IMAGE_DIM = 512;
    private static final int MAX_RESPONSE_TOKENS = 256;

    public static String analyzeImageForDefects(String imagePath, String fabricType) throws Exception {
        long start = System.currentTimeMillis();

        BufferedImage image = loadAndValidateImage(imagePath);
        image = resizeIfNeeded(image);
        String base64Image = imageToBase64(image);
        String prompt = createPrompt(fabricType, base64Image);

        System.out.println("Invio richiesta a " + MODEL + "...");
        String result = sendRequestToModel(prompt);
        result = result.trim();

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Analisi completata in %.1f secondi%n", elapsed / 1000.0);

        return result;
    }

    private static BufferedImage loadAndValidateImage(String path) throws IOException {
        path = cleanPath(path);
        File f = new File(path);
        if (!f.exists()) throw new FileNotFoundException("Immagine non trovata: " + path);
        if (f.length() > MAX_IMAGE_SIZE) throw new IOException("Immagine troppo grande (max 10MB).");

        String ext = getExtension(path).toLowerCase();
        if (!ext.matches("jpg|jpeg|png|bmp|gif"))
            throw new IOException("Formato non supportato: " + ext);

        BufferedImage img = ImageIO.read(f);
        if (img == null) throw new IOException("Impossibile leggere l'immagine.");
        return img;
    }

    private static BufferedImage resizeIfNeeded(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        if (w <= MAX_IMAGE_DIM && h <= MAX_IMAGE_DIM) return img;

        double scale = Math.min((double) MAX_IMAGE_DIM / w, (double) MAX_IMAGE_DIM / h);
        int nw = (int) (w * scale), nh = (int) (h * scale);

        Image tmp = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        BufferedImage out = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();

        System.out.printf("Immagine ridotta: %dx%d -> %dx%d%n", w, h, nw, nh);
        return out;
    }

    private static String imageToBase64(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512 * 1024);
        ImageIO.write(img, "jpg", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static String createPrompt(String fabricType, String base64Image) {
        return String.format(
            "Ispeziona tessuto (%s). Elenca SOLO difetti trovati con gravit\u00e0 e posizione. Massimo 3 righe.\n\n" +
            "data:image/jpeg;base64,%s",
            fabricType, base64Image);
    }

    private static String sendRequestToModel(String prompt) throws Exception {
        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.05);
        options.put("num_predict", MAX_RESPONSE_TOKENS);
        options.put("num_ctx", 2048);
        options.put("top_k", 20);
        options.put("top_p", 0.7);
        options.put("repeat_penalty", 1.1);

        Map<String, Object> body = new HashMap<>();
        body.put("model", MODEL);
        body.put("prompt", prompt);
        body.put("stream", false);
        body.put("keep_alive", "5m");
        body.put("options", options);

        String json = mapper.writeValueAsString(body);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .timeout(java.time.Duration.ofMinutes(3))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (resp.statusCode() != 200)
            throw new RuntimeException("HTTP " + resp.statusCode() + ": " + resp.body());

        JsonNode root = mapper.readTree(resp.body());
        if (root.has("response")) return root.get("response").asText();
        if (root.has("error")) throw new RuntimeException("Ollama: " + root.get("error").asText());
        throw new RuntimeException("Risposta inattesa da Ollama");
    }

    private static String cleanPath(String p) {
        p = p.trim();
        if (p.startsWith("\"") && p.endsWith("\"")) p = p.substring(1, p.length() - 1);
        return p;
    }

    private static String getExtension(String f) {
        int i = f.lastIndexOf('.');
        return i > 0 ? f.substring(i + 1) : "";
    }
}
