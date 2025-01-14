package sample;

import Oyun.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;  // Değiştirildi
import javafx.scene.image.Image;      // Eklendi

import java.util.*;

import javafx.util.Duration;

import java.io.IOException;

public class Controller{
    Controller deneme;
    Stage tempstage;
    Scene tempscene;
    Parent temproot;
    TextField tempnamefield;
    private int selectedCardCount = 0; // Seçilen kart sayısını takip eder
    private Map<ImageView, ImageView> selectedToOriginalMap = new HashMap<>(); // Seçilen kart ile orijinal kart eşleşmesi

    @FXML
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;

    @FXML
    private ImageView tersKart1, tersKart2, tersKart3, tersKart4, tersKart5, tersKart6, tersKart7, tersKart8, tersKart9;

    @FXML
    private ImageView secilen1,secilen2,secilen3,secilenters1,secilenters2,secilenters3;

    @FXML
    private Button exitButton;

    @FXML
    private Button battleButton; // Add this field

    @FXML
    private ImageView pcskor;  // PC's score board
    @FXML
    private ImageView oyuncuskor;  // Player's score board

    private List<ImageView> imageViews;

    private ArrayList<SavasAraclari> selectedSavasAraclari = new ArrayList<>();
    private Map<ImageView,SavasAraclari> selectedSavasAraclariMap = new HashMap<>();
    // FXML ile injected olan "WARRIOR CARDS" metni
    @FXML
    private Text warriorText;

    // Çıkış butonuna tıklama işlemi
    @FXML
    private void handleExit() {
        Stage stage = (Stage) deneme.exitButton.getScene().getWindow();
        if (stage != null) {
            stage.close(); // Penceriyi kapat
            Platform.exit();
        }
    }

    private void updateScores(int adim) {
        if(adim>1){
            pcskor = deneme.pcskor;
            oyuncuskor = deneme.oyuncuskor;
        }
        Platform.runLater(() -> {
            // PC skoru için
            Text pcScoreText = new Text(String.valueOf(arayuz.GetPc().SkorGoster()));
            pcScoreText.setFont(Font.font("Arial Black", 20));
            pcScoreText.setFill(Color.WHITE);

            // ImageView boyutlarını al
            double imageWidth = pcskor.getFitWidth();
            double imageHeight = pcskor.getFitHeight();

            // Metni ortalama
            pcScoreText.setX(pcskor.getLayoutX() + (imageWidth / 2));
            pcScoreText.setY(pcskor.getLayoutY() + (imageHeight / 2));

            // Oyuncu skoru için benzer şekilde
            Text oyuncuScoreText = new Text(String.valueOf(arayuz.GetOyuncu().SkorGoster()));
            oyuncuScoreText.setFont(Font.font("Arial Black", 20));
            oyuncuScoreText.setFill(Color.WHITE);

            // Text boyutunu hesapla
            double oyuncuTextWidth = oyuncuScoreText.getBoundsInLocal().getWidth();
            double oyuncuTextHeight = oyuncuScoreText.getBoundsInLocal().getHeight();

            oyuncuScoreText.setX(oyuncuskor.getLayoutX() + (imageWidth - oyuncuTextWidth) / 2);
            oyuncuScoreText.setY(oyuncuskor.getLayoutY() + (imageHeight + oyuncuTextHeight) / 2);

            // Parent Pane'e ekleme
            Pane parent = (Pane) pcskor.getParent();
            // Önceki Text node'ları temizle
            parent.getChildren().removeIf(node -> node instanceof Text);

            // Yeni Text node'ları ekle
            parent.getChildren().addAll(pcScoreText, oyuncuScoreText);
        });
    }

    // Kart tıklama olaylarını ayarlama
    private void setupCardClickEvents() {
        // Kart seçme işlemleri
        if(deneme==null)
            System.out.println("deneme null");
        deneme.image1.setOnMouseClicked(event -> handleCardSelection(deneme.image1, 0));
        deneme.image2.setOnMouseClicked(event -> handleCardSelection(deneme.image2, 1));
        deneme.image3.setOnMouseClicked(event -> handleCardSelection(deneme.image3, 2));
        deneme.image4.setOnMouseClicked(event -> handleCardSelection(deneme.image4, 3));
        deneme.image5.setOnMouseClicked(event -> handleCardSelection(deneme.image5, 4));
        deneme.image6.setOnMouseClicked(event -> handleCardSelection(deneme.image6, 5));
        deneme.image7.setOnMouseClicked(event -> handleCardSelection(deneme.image7, 6));
        deneme.image8.setOnMouseClicked(event -> handleCardSelection(deneme.image8, 7));
        deneme.image9.setOnMouseClicked(event -> handleCardSelection(deneme.image9, 8));


        // Corrected card return handlers
        deneme.secilen1.setOnMouseClicked(event -> {
            if (deneme.secilen1.getImage() != null) {
                animateCardReturn(deneme.secilen1);
                returnCardToOriginal(deneme.secilen1);
            }
        });

        deneme.secilen2.setOnMouseClicked(event -> {
            if (deneme.secilen2.getImage() != null) {
                animateCardReturn(deneme.secilen2);
                returnCardToOriginal(deneme.secilen2);
            }
        });

        deneme.secilen3.setOnMouseClicked(event -> {
            if (deneme.secilen3.getImage() != null) {
                animateCardReturn(deneme.secilen3);
                returnCardToOriginal(deneme.secilen3);
            }
        });

        // Battle butonu için event listener ekleme
        deneme.battleButton.setOnAction(event -> handleBattle());

        // Exit butonuna tıklama işlemi
        deneme.exitButton.setOnAction(event -> handleExit());
    }

    // Kart seçimi işlemi
    private void handleCardSelection(ImageView sourceCard, int cardIndex){
        if (sourceCard.getImage() != null && deneme.selectedCardCount < 3) {
            ImageView targetCard;
            // Seçilen kartları yerleştirmek için boş alanı bul
            switch (deneme.selectedCardCount) {
                case 0:
                    targetCard = deneme.secilen1;
                    break;
                case 1:
                    targetCard = deneme.secilen2;
                    break;
                case 2:
                    targetCard = deneme.secilen3;
                    break;
                default:
                    return; // Üçten fazla kart seçilemez
            }

            // Hedef kart boşsa, orijinal kartı seçili kart olarak yerleştir
            if (targetCard != null && targetCard.getImage() == null) {
                SavasAraclari selectedSavasAraci = arayuz.GetOyuncu().GetKartlarin().get(cardIndex);
                System.out.println(selectedSavasAraci + " -- " + selectedSavasAraci.getKullanimsayisi());
                targetCard.setImage(sourceCard.getImage());
                sourceCard.setImage(null);
                selectedSavasAraclari.add(selectedSavasAraci);
                selectedSavasAraclariMap.put(targetCard,selectedSavasAraci);
                deneme.selectedToOriginalMap.put(targetCard, sourceCard); // Seçilen kartı orijinal kartla eşleştir.
                deneme.selectedCardCount++;
                // Kart seçildiğinde animasyon ekleme
                animateCardSelection(targetCard);
            }
        }
    }

    // Kartı geri orijinal yerine koyma işlemi
    private void returnCardToOriginal(ImageView selectedCard) {
        if (selectedCard.getImage() != null) {
            ImageView originalCard = deneme.selectedToOriginalMap.get(selectedCard);
            if (originalCard != null) {
                selectedSavasAraclari.remove(selectedSavasAraclariMap.get(selectedCard));
                originalCard.setImage(selectedCard.getImage());
                selectedCard.setImage(null);
                selectedToOriginalMap.remove(selectedCard);
                deneme.selectedCardCount--;
            }
        }
    }

    private void animateCardSelection(ImageView selectedCard) {
        // Kartın başlangıç boyutu küçük olacak, sonra büyüyecek
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(700), selectedCard);
        scaleTransition.setFromX(0.3); // Başlangıçta çok küçük
        scaleTransition.setFromY(0.3); // Başlangıçta çok küçük
        scaleTransition.setToX(1.0);   // Normal boyut
        scaleTransition.setToY(1.0);   // Normal boyut
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);  // Akıcı büyüme ve küçülme
        scaleTransition.setCycleCount(1);

        // Kartın döndürülmesi (hafifçe dönmesi) - RotateTransition
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(700), selectedCard);
        rotateTransition.setByAngle(360);  // Kartı tam tur döndürme
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH); // Akıcı dönüş
        rotateTransition.setCycleCount(1);

        // Kartın opaklık animasyonu
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), selectedCard);
        fadeTransition.setFromValue(0.0);  // Başlangıçta tamamen saydam
        fadeTransition.setToValue(1.0);    // Sonrasında tamamen opak
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);  // Akıcı opaklık değişimi
        fadeTransition.setCycleCount(1);

        // Animasyonları başlat
        scaleTransition.play();
        rotateTransition.play();
        fadeTransition.play();
    }

    private void animateCardReturn(ImageView selectedCard) {
        // Kartın geri küçülmesi için ScaleTransition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(700), selectedCard);
        scaleTransition.setFromX(1.0);  // Normal boyuttan
        scaleTransition.setFromY(1.0);  // Normal boyuttan
        scaleTransition.setToX(0.3);   // Küçük boyut
        scaleTransition.setToY(0.3);   // Küçük boyut
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);  // Akıcı küçülme
        scaleTransition.setCycleCount(1);

        // Kartın döndürülmesi (geri döndürme) - RotateTransition
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(700), selectedCard);
        rotateTransition.setByAngle(-360);  // Kartı geri döndürme (tersine)
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH); // Akıcı dönüş
        rotateTransition.setCycleCount(1);

        // Kartın opaklık animasyonu (geri giderken az opak olacak)
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), selectedCard);
        fadeTransition.setFromValue(1.0);  // Başlangıçta tamamen opak
        fadeTransition.setToValue(0.0);    // Sonrasında tamamen saydam
        fadeTransition.setInterpolator(Interpolator.EASE_BOTH);  // Akıcı opaklık değişimi
        fadeTransition.setCycleCount(1);

        // Animasyonları başlat
        scaleTransition.play();
        rotateTransition.play();
        fadeTransition.play();
    }

    @FXML
    private void initialize() {
        System.out.println("Initialize metodu çağrıldı");

        // ImageView listesini başlat
        imageViews = new ArrayList<>();

        // Her iki sahne için de ImageView kontrolü
        if (imageViews != null) {
            System.out.println("Background ImageView initialized");
        }

        // ScaleTransition animasyonunu oluşturuyoruz
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), warriorText);

        // Yazıyı biraz büyütüp sonra tekrar küçültmek için animasyon ayarlarını yapıyoruz
        scaleTransition.setFromX(1);  // Başlangıçta normal boyut
        scaleTransition.setToX(1.2); // %120 boyutunda büyüyecek
        scaleTransition.setFromY(1);  // Y-ekseni için aynı şekilde
        scaleTransition.setToY(1.2); // %120 boyutunda büyüyecek

        // Animasyonun sürekli tekrarlanması için döngü sayısını INDEFINITE (sonsuz) yapıyoruz
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

        // Animasyon her döngüde tersine dönecek (yani büyüdükten sonra tekrar küçülecek)
        scaleTransition.setAutoReverse(true);

        // Animasyonu başlatıyoruz
        scaleTransition.play();

        // Oyun sahnesindeki ImageView'ları kontrol et
        checkAndAddImageView(image1, "image1");
        checkAndAddImageView(image2, "image2");
        checkAndAddImageView(image3, "image3");
        checkAndAddImageView(image4, "image4");
        checkAndAddImageView(image5, "image5");
        checkAndAddImageView(image6, "image6");
        checkAndAddImageView(image7, "image7");
        checkAndAddImageView(image8, "image8");
        checkAndAddImageView(image9, "image9");
        checkAndAddImageView(tersKart1,"terskart1");
        checkAndAddImageView(tersKart2,"terskart2");
        checkAndAddImageView(tersKart3,"terskart3");
        checkAndAddImageView(tersKart4,"terskart4");
        checkAndAddImageView(tersKart5,"terskart5");
        checkAndAddImageView(tersKart6,"terskart6");
        checkAndAddImageView(tersKart7,"terskart7");
        checkAndAddImageView(tersKart8,"terskart8");
        checkAndAddImageView(tersKart9,"terskart9");
    }

    private void checkAndAddImageView(ImageView imageView, String name) {
        if (imageView != null) {
            System.out.println(name + " initialized successfully");
            imageViews.add(imageView);
        } else {
            System.err.println(name + " is NULL!");
        }
    }

    private void loadImages(ArrayList<SavasAraclari> oyuncukartlar,ArrayList<SavasAraclari> pckartlar,int adim) {
        try {
            if (oyuncukartlar == null || oyuncukartlar.isEmpty()) {
                System.err.println("Kart listesi boş veya null!");
                return;
            }

            if (imageViews == null || imageViews.isEmpty()) {
                // Initialize imageViews if it's null
                imageViews = new ArrayList<>();
                imageViews.add(image1);
                imageViews.add(image2);
                imageViews.add(image3);
                imageViews.add(image4);
                imageViews.add(image5);
                imageViews.add(image6);
                imageViews.add(image7);
                imageViews.add(image8);
                imageViews.add(image9);
            }
            updateScores(adim);
            Platform.runLater(() -> {
                if(adim>1)
                    imageViews = deneme.imageViews;
                System.out.println("Loading " + oyuncukartlar.size() + " cards");
                System.out.println(imageViews.size());
                for (int i = 0; i < oyuncukartlar.size(); i++) {
                    SavasAraclari kart = oyuncukartlar.get(i);
                    ImageView imageView = imageViews.get(i);
                    if (imageView == null) {
                        System.err.println("ImageView " + i + " is null!");
                        continue;
                    }

                    // Get image path based on card type
                    String imagePath = getImagePathForCard(kart);
                    System.out.println("Loading image: " + imagePath + " for card: " + kart.getAltSinif());

                    try {
                        var resourceURL = getClass().getResource(imagePath);
                        if (resourceURL == null) {
                            System.err.println("Resource not found: " + imagePath);
                            continue;
                        }
                        // Create usage label
                        System.out.println("Kullanım sayisi:" + kart.getKullanimsayisi());
                        Label usageLabel = new Label(String.valueOf(kart.getKullanimsayisi()));
                        usageLabel.setFont(Font.font("Arial Black", 18));
                        usageLabel.setAlignment(Pos.CENTER);
                        usageLabel.setPrefWidth(93.6);
                        usageLabel.setTextFill(Color.WHITE);

                        // Get parent container
                        var parent = imageView.getParent();
                        if (parent != null) {
                            // Position label relative to the card
                            usageLabel.setLayoutX(imageView.getLayoutX());
                            usageLabel.setLayoutY(imageView.getLayoutY() + imageView.getFitHeight() + 5);

                            // Add label to parent
                            if (parent instanceof Pane) {
                                ((Pane) parent).getChildren().add(usageLabel);
                            }
                        }
                        Image cardImage = new Image(resourceURL.toExternalForm());
                        imageView.setImage(cardImage);
                        imageView.setFitWidth(83);
                        imageView.setFitHeight(114);
                        imageView.setPreserveRatio(true);
                        System.out.println("Successfully loaded image for card " + i);
                    } catch (Exception e) {
                        System.err.println("Error loading image for card " + i + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                // Ters kartlar için
                for(int i=9; i< pckartlar.size()+9 && i<imageViews.size() ;i++) {
                    ImageView imageView = imageViews.get(i);
                    String tersKartImagePath = "resimler/terskart.png";
                    try {
                        var tersKartResourceURL = getClass().getResource(tersKartImagePath);
                        if (tersKartResourceURL == null) {
                            System.err.println("Ters kart resmi bulunamadı: " + tersKartImagePath);
                            continue;
                        }
                        Image tersKartImage = new Image(tersKartResourceURL.toExternalForm());
                        imageView.setImage(tersKartImage);
                        imageView.setFitWidth(83);
                        imageView.setFitHeight(114);
                        imageView.setPreserveRatio(true);
                        System.out.println("Successfully loaded ters kart image for " + imageView.getId());
                    } catch (Exception e) {
                        System.err.println("Error loading ters kart image: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error in loadImages: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Add helper method for getting image paths
    private String getImagePathForCard(SavasAraclari kart){
        if (kart instanceof Ucak) return "resimler/ucak.png";
        if (kart instanceof Firkateyn) return "resimler/fırkateyn.png";
        if (kart instanceof Obus) return "resimler/obus.png";
        if (kart instanceof Siha) return "resimler/siha.png";
        if (kart instanceof KFS) return "resimler/KFS.png";
        if (kart instanceof Sida) return "resimler/sida.png";
        return "";
    }

    // Kartları sıfırlayan yeni bir metod ekleyin
    private void resetSelectedCards() {
        // Remove all usage labels
        if (deneme.image1.getParent() instanceof Pane) {
            Pane parent = (Pane) deneme.image1.getParent();
            parent.getChildren().removeIf(node -> node instanceof Label);
        }

        // Seçilen kartların görüntülerini temizle
        deneme.image1.setImage(null);
        deneme.image2.setImage(null);
        deneme.image3.setImage(null);
        deneme.image4.setImage(null);
        deneme.image5.setImage(null);
        deneme.image6.setImage(null);
        deneme.image7.setImage(null);
        deneme.image8.setImage(null);
        deneme.image9.setImage(null);
        deneme.tersKart1.setImage(null);
        deneme.tersKart2.setImage(null);
        deneme.tersKart3.setImage(null);
        deneme.tersKart4.setImage(null);
        deneme.tersKart5.setImage(null);
        deneme.tersKart6.setImage(null);
        deneme.tersKart7.setImage(null);
        deneme.tersKart8.setImage(null);
        deneme.tersKart9.setImage(null);

        // Listeleri ve sayaçları sıfırla
        deneme.selectedToOriginalMap.clear();
        deneme.selectedSavasAraclari.clear();
        deneme.selectedCardCount = 0;
    }
    private void clearSelectedCards() {
        deneme.secilen1.setImage(null);
        deneme.secilen2.setImage(null);
        deneme.secilen3.setImage(null);
        deneme.secilenters1.setImage(null);
        deneme.secilenters2.setImage(null);
        deneme.secilenters3.setImage(null);
    }
    private void revealComputerCards() {
        Platform.runLater(() -> {
            int[] index = new int[3];
            int k=0;
            for(int i=0;i<arayuz.GetPc().GetKartlarin().size();i++){
                SavasAraclari card = arayuz.GetPc().GetKartlarin().get(i);
               for(SavasAraclari x : arayuz.GetPc().GetSecilenKartlar()){
                   if(Objects.equals(card.getAltSinif(), x.getAltSinif()))
                       index[k++]=i;
               }
            }
            for (int i=0;i<3;i++){
            // Sadece bilgisayarın seçtiği kartı bul

            ImageView tersKartView = null;
            ImageView imageView = null;
            ImageView desteImageView = null;
            switch (i){
                case 0:
                    tersKartView = deneme.secilenters1;
                    imageView = deneme.secilenters1;
                    break;
                case 1:
                    tersKartView = deneme.secilenters2;
                    imageView = deneme.secilenters2;
                    break;
                case 2:
                    tersKartView = deneme.secilenters3;
                    imageView = deneme.secilenters3;
                    break;
            }
            // Sadece seçilen kartın indexine göre işlem yap
            switch (index[i]) {
                case 0:
                    desteImageView = deneme.tersKart1;
                    break;
                case 1:
                    desteImageView = deneme.tersKart2;;
                    break;
                case 2:
                    desteImageView = deneme.tersKart3;;
                    break;
                case 3:
                    desteImageView = deneme.tersKart4;
                    break;
                case 4:
                    desteImageView = deneme.tersKart5;;
                    break;
                case 5:
                    desteImageView = deneme.tersKart6;;
                    break;
                case 6:
                    desteImageView = deneme.tersKart7;
                    break;
                case 7:
                    desteImageView = deneme.tersKart8;;
                    break;
                case 8:
                    desteImageView = deneme.tersKart9;;
                    break;
            }

            if (tersKartView != null && imageView != null) {
                // Get image path based on the card type
                String imagePath = getImagePathForCard(arayuz.GetPc().GetSecilenKartlar().get(i));

                try {
                    var resourceURL = getClass().getResource(imagePath);
                    if (resourceURL != null) {
                        Image cardImage = new Image(resourceURL.toExternalForm());
                        imageView.setImage(cardImage);
                        imageView.setFitWidth(129);
                        imageView.setFitHeight(170);
                        imageView.setPreserveRatio(true);

                        // Sadece seçilen kartı desteden gizle
                        if (desteImageView != null) {
                            //desteImageView.setVisible(false);
                            desteImageView.setImage(null);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error revealing computer card: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            }
        });
    }

    // Savaş metodunda veya tur değişiminde bu metodu çağırın
    private void handleTurnChange(int adim){
        Platform.runLater(() -> {
            try {
                resetSelectedCards(); // Reset current selections

                // Debug output
                System.out.println("Starting turn change");
                System.out.println("Player cards count: " + arayuz.GetOyuncu().GetKartlarin().size());

                // Load images for the new turn
                loadImages(arayuz.GetOyuncu().GetKartlarin(),arayuz.GetPc().GetKartlarin(),adim);

                // Reset the scene
                tempstage.setScene(tempscene);
                tempstage.show();

                // Set up click events for the new cards
                setupCardClickEvents();

                System.out.println("Turn change completed");
            } catch (Exception e) {
                System.err.println("Turn change error: " + e.getMessage());
                e.printStackTrace();

                // Show error to user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText(null);
                alert.setContentText("Tur değişimi sırasında bir hata oluştu: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    private TextField nameField; // FXML'deki TextField ile bağlantı

    @FXML
    private TextField level;

    @FXML
    private TextField tur;

    private Arayuz arayuz;
    public void setMainApp(Arayuz arayuz) {
        this.arayuz = arayuz;
    }

    private void startBattleAnimation() {
        // Seçilen kartlar ve ters kartlar için animasyonlar
        List<ImageView> selectedCards = Arrays.asList(deneme.secilen1, deneme.secilen2, deneme.secilen3);
        List<ImageView> selectedTersCards = Arrays.asList(deneme.secilenters1, deneme.secilenters2, deneme.secilenters3);

        // Kartların başlangıç pozisyonları kaydediliyor
        List<Double> initialX = new ArrayList<>();
        List<Double> initialY = new ArrayList<>();
        for (ImageView card : selectedCards) {
            initialX.add(card.getLayoutX());
            initialY.add(card.getLayoutY());
        }
        for (ImageView tersCard : selectedTersCards) {
            initialX.add(tersCard.getLayoutX());
            initialY.add(tersCard.getLayoutY());
        }

        // Kartların hareket etmesi
        for (int i = 0; i < selectedCards.size(); i++) {
            ImageView card = selectedCards.get(i);
            ImageView tersCard = selectedTersCards.get(i);

            // Kartların birbirine yaklaşması (Y ekseninde)
            double targetYCard = card.getLayoutY() - 100;  // Kartın yukarıya doğru hareket etmesi
            double targetYTersCard = tersCard.getLayoutY() + 100;  // Ters kartın aşağıya doğru hareket etmesi

            TranslateTransition translateToTarget = new TranslateTransition(Duration.seconds(1.2), card); // Hız yavaşlatıldı
            translateToTarget.setByY(targetYCard - card.getLayoutY());  // Y ekseninde yukarıya doğru
            translateToTarget.setCycleCount(1);

            TranslateTransition tersTranslateToTarget = new TranslateTransition(Duration.seconds(1.2), tersCard); // Hız yavaşlatıldı
            tersTranslateToTarget.setByY(targetYTersCard - tersCard.getLayoutY());  // Y ekseninde aşağıya doğru
            tersTranslateToTarget.setCycleCount(1);

            // Kartların hafif çalkalanma hareketi (titreşimli hareket)
            TranslateTransition shake = new TranslateTransition(Duration.seconds(0.2), card);  // Hız yavaşlatıldı
            shake.setByX(10);  // Sağ/sol hareket
            shake.setCycleCount(2);
            shake.setAutoReverse(true);

            TranslateTransition tersShake = new TranslateTransition(Duration.seconds(0.2), tersCard);  // Hız yavaşlatıldı
            tersShake.setByX(10);  // Ters kartlar için de titreşimli hareket
            tersShake.setCycleCount(2);
            tersShake.setAutoReverse(true);

            // Kartların kaybolma animasyonu (FadeOut)
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), card);  // Hız yavaşlatıldı
            fadeOut.setFromValue(1.0);  // Başlangıçta görünür
            fadeOut.setToValue(0.0);    // Sonra kaybolacak
            fadeOut.setCycleCount(1);
            fadeOut.setAutoReverse(false);  // Kaybolduktan sonra geri gelmeyecek

            FadeTransition tersFadeOut = new FadeTransition(Duration.seconds(1.5), tersCard);  // Hız yavaşlatıldı
            tersFadeOut.setFromValue(1.0);  // Başlangıçta görünür
            tersFadeOut.setToValue(0.0);    // Sonra kaybolacak
            tersFadeOut.setCycleCount(1);
            tersFadeOut.setAutoReverse(false);

            // Kartların animasyonlarını paralel olarak başlatıyoruz
            ParallelTransition parallelTransition = new ParallelTransition(
                    translateToTarget, shake, fadeOut
            );
            ParallelTransition tersParallelTransition = new ParallelTransition(
                    tersTranslateToTarget, tersShake, tersFadeOut
            );

            // Animasyonlar başlatılıyor
            parallelTransition.play();
            tersParallelTransition.play();

            // Animasyon tamamlandığında kartları başlangıç pozisyonlarına geri getirme
            int finalI = i;
            parallelTransition.setOnFinished(event -> {
                // Kartları kaydedilmiş başlangıç konumlarına geri getir
                card.setLayoutX(initialX.get(finalI));
                card.setLayoutY(initialY.get(finalI));  // Sabit değer yerine kaydedilen başlangıç Y pozisyonu
                card.setTranslateY(0);  // TranslateY değerini sıfırla

                tersCard.setLayoutX(initialX.get(finalI + selectedCards.size()));
                tersCard.setLayoutY(initialY.get(finalI + selectedCards.size()));  // Sabit değer yerine kaydedilen başlangıç Y pozisyonu
                tersCard.setTranslateY(0);  // TranslateY değerini sıfırla
            });
        }
    }

    private int currentStep = 1;
    @FXML
    private void handleBattle() {
        if (deneme.selectedCardCount < 3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText(null);
            alert.setContentText("Lütfen savaşmak için 3 kart seçin!");
            alert.showAndWait();
            return;
        }

        boolean hasValidCardSelection = checkCardSelectionValidity();
        if (!hasValidCardSelection) {
            Platform.runLater(() -> {
                clearSelectedCards();
                handleTurnChange(currentStep);
                selectedSavasAraclari.clear();
                deneme.selectedCardCount = 0;
            });
            return;
        }
        revealComputerCards();
        startBattleAnimation();
        try {
            boolean kartlarBitti = arayuz.GetOyuncu().Kartlar_bitti_mi() || arayuz.GetPc().Kartlar_bitti_mi();
            boolean turBitti;
            if(arayuz.GetTur()==currentStep || Oyuncu.getSon_tur()==currentStep)
                turBitti = true;
            else {
                turBitti = false;
            }
            arayuz.GetOyuncu().SetSecilenKartlar(selectedSavasAraclari);
            int yeniAdim = arayuz.Savas(currentStep, arayuz.GetOyuncu(), arayuz.GetPc());
            currentStep = yeniAdim;

            // Use PauseTransition to add a delay before changing turn or ending the game
            PauseTransition pause = new PauseTransition(Duration.seconds(2)); // 2 seconds delay
            boolean finalKartlarBitti = kartlarBitti;
            pause.setOnFinished(event -> {
                Platform.runLater(() -> {
                    if (finalKartlarBitti || turBitti) {
                        try {
                            String kazanan = arayuz.Kazanan_kontrol(arayuz.GetOyuncu(), arayuz.GetPc());
                            showError(arayuz.GetOyuncu().getOyuncuAdi() + " skor:" + arayuz.GetOyuncu().SkorGoster() + "\nBilgisayar skor:" + arayuz.GetPc().SkorGoster() + "\n" + kazanan);
                        } catch (Exception e) {
                            showError("Kazanan kontrol hatası: " + e.getMessage());
                        }
                    } else {
                        clearSelectedCards();
                        handleTurnChange(currentStep);
                        selectedSavasAraclari.clear();
                    }
                });
            });
            pause.play();

        } catch (Exception e) {
            showError("Savaş sırasında bir hata oluştu: " + e.getMessage());
        }
    }
    private boolean checkCardSelectionValidity() {
        boolean hasUsedCards=false;
        boolean hasUnusedCardsRemaining=false;
        for (SavasAraclari card : selectedSavasAraclari) { // secilen kartlarda hiç kullanılmış kart var mı diye bakıyor
            if(card.getKullanimsayisi()==1) {
                hasUsedCards = true;
                break;
            }
        }

        for (SavasAraclari card : arayuz.GetOyuncu().GetKartlarin()) { // kartlarındaki ve secilen kartlarda olmayan kullanılmamışlara bakıyor
            if( (!selectedSavasAraclari.contains(card)) && card.getKullanimsayisi() == 0 ) {
                hasUnusedCardsRemaining = true;
                break;
            }
        }

        if (hasUsedCards && hasUnusedCardsRemaining) {
            showError("Kullanılmamış kartları seç!");
            return false;
        }
        return true;
    }
    @FXML
    public void handleNameSubmit() throws IOException {
        String name = nameField.getText();
        String userLevel = level.getText();
        String tursayisi = tur.getText();
        if(validateInput()) {
            System.out.println("Ad: " + name + ", Seviye: " + userLevel);
            arayuz.receiveDataFromController(name,userLevel,tursayisi);
            System.out.println(arayuz.GetOyuncu().getOyuncuAdi());
            arayuz.GetOyuncu().KartListesi();
            arayuz.GetPc().KartListesi();

            Platform.runLater(() -> {
                try {
                    changeScene();
                    deneme.loadImages(arayuz.GetOyuncu().GetKartlarin(),arayuz.GetPc().GetKartlarin(), 1);
                    setupCardClickEvents();
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Hata: " + e.getMessage());
                }
            });
        }
    }
    // Giriş doğrulama metodu
    private boolean validateInput() {
        if(nameField.getText().trim().isEmpty() && level.getText().trim().isEmpty() && tur.getText().trim().isEmpty()){
            showError("Kullanıcı adı,seviye ve tur boş olamaz!");
            return false;
        }
        else if(nameField.getText().trim().isEmpty() && level.getText().trim().isEmpty()) {
            showError("Kullanıcı adı ve seviye boş olamaz!");
            return false;
        }

        else if(nameField.getText().trim().isEmpty() && tur.getText().trim().isEmpty()) {
            showError("Kullanıcı adı ve tur boş olamaz!");
            return false;
        }

        else if(level.getText().trim().isEmpty() && tur.getText().trim().isEmpty()) {
            showError("Seviye ve tur boş olamaz!");
            return false;
        }

        else if(nameField.getText().trim().isEmpty()) {
            showError("Kullanıcı adı boş olamaz!");
            nameField.requestFocus(); // Kullanıcıyı nameField alanına yönlendir
            return false;
        }

        else if(level.getText().trim().isEmpty()) {
            showError("Seviye boş olamaz!");
            level.requestFocus(); // Kullanıcıyı level alanına yönlendir
            return false;
        }
        else if(tur.getText().trim().isEmpty()){
            showError("Tur sayisi boş olamaz!");
            tur.requestFocus();
            return false;
        }
        return true; // Giriş geçerli
    }

    // Hata mesajı gösteren metot
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void changeScene() {
        try {
            // FXML yükleyiciyi oluştur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample2.fxml"));

            // Yükleme işlemini kontrol et
            Parent root = loader.load();
            if (root == null) {
                throw new IllegalStateException("FXML dosyası yüklenemedi");
            }
            temproot = root;
            // Yeni controller'ı al
            Controller newController = loader.getController();
            if (newController == null) {
                throw new IllegalStateException("Controller oluşturulamadı");
            }
            deneme = newController;
            // Arayüz referansını aktar
            newController.setMainApp(this.arayuz);

            // Sahneyi oluştur
            //Scene newScene = new Scene(root, 1051, 822);
            Scene newScene = new Scene(root, 1151.44, 950.12);

            // Stage'i al
            Stage stage = (Stage) nameField.getScene().getWindow();
            if (stage == null) {
                throw new IllegalStateException("Mevcut stage bulunamadı");
            }
            tempstage = stage;
            tempscene = newScene;
            tempnamefield = nameField;
            // Yeni sahneyi ayarla
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            System.err.println("FXML yükleme hatası: " + e.getMessage());
            e.printStackTrace();

            // Kullanıcıya hata mesajı göster
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText("Sayfa Yükleme Hatası");
                alert.setContentText("Oyun sayfası yüklenirken bir hata oluştu: " + e.getMessage());
                alert.showAndWait();
            });
        } catch (Exception e) {
            System.err.println("Genel hata: " + e.getMessage());
            e.printStackTrace();

            // Kullanıcıya genel hata mesajı göster
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hata");
                alert.setHeaderText("Bilinmeyen Hata");
                alert.setContentText("Bir hata oluştu: " + e.getMessage());
                alert.showAndWait();
            });
        }
    }
}