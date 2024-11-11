package store.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions(String path) {
        this.promotions = createPromotions(path);
    }

    private List<Promotion> createPromotions(String filePath) {
        List<Promotion> promotions = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            br.readLine();
            createPromotions(promotions, br);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    private void createPromotions(List<Promotion> promotions, BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            List<String> values = Arrays.stream(line.split(",")).toList();
            String name = values.get(0);
            int buy = Integer.parseInt(values.get(1).trim());
            int get = Integer.parseInt(values.get(2).trim());
            String startDate = values.get(3).trim();
            String endDate = values.get(4).trim();
            promotions.add(new Promotion(name, buy, get, startDate, endDate));
        }
    }

    public Promotion findPromotionByName(String promotionName) {
        for (Promotion promotion : promotions) {
            if (promotion.isSameName(promotionName)) {
                return promotion;
            }
        }
        return null;
    }
}
