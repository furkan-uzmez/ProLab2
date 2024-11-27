package sample;

import Oyun.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;  // Değiştirildi
import javafx.scene.image.Image;      // Eklendi

import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller{
    Controller deneme;
    Stage tempstage;
    Scene tempscene;
    Parent temproot;
    TextField tempnamefield;
    private int selectedCardCount = 0; // Seçilen kart sayısını takip eder
    private Map<ImageView, ImageView> selectedToOriginalMap = new HashMap<>(); // Seçilen kart ile orijinal kart eşleşmesi

    @FXML
    private ImageView image1, image2, image3, image4, image5, image6;

    @FXML
    private ImageView tersKart1, tersKart2, tersKart3, tersKart4, tersKart5, tersKart6;

    @FXML
    private ImageView secilen1,secilen2,secilen3;

    @FXML
    private Button exitButton;

    @FXML
    private Button battleButton; // Add this field

    private List<ImageView> imageViews;

    private ArrayList<SavasAraclari> selectedSavasAraclari = new ArrayList<>();
    // Çıkış butonuna tıklama işlemi
    @FXML
    private void handleExit() {
        Stage stage = (Stage) deneme.exitButton.getScene().getWindow();
        if (stage != null) {
            stage.close(); // Penceriyi kapat
            Platform.exit();
        }
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

        // Ters kartları geri alma işlemleri
        /*deneme.tersKart1.setOnMouseClicked(event -> returnCardToOriginal(deneme.tersKart1, 0));
        deneme.tersKart2.setOnMouseClicked(event -> returnCardToOriginal(deneme.tersKart2, 1));
        deneme.tersKart3.setOnMouseClicked(event -> returnCardToOriginal(deneme.tersKart3, 2));
        deneme.tersKart4.setOnMouseClicked(event -> returnCardToOriginal(deneme.tersKart4, 3));
        deneme.tersKart5.setOnMouseClicked(event -> returnCardToOriginal(deneme.tersKart5, 4));
        deneme.tersKart6.setOnMouseClicked(event -> returnCardToOriginal(deneme.tersKart6, 5));*/

        // Battle butonu için event listener ekleme
        deneme.battleButton.setOnAction(event -> handleBattle());

        // Exit butonuna tıklama işlemi
        deneme.exitButton.setOnAction(event -> handleExit());
    }

    // Kart seçimi işlemi
    private void handleCardSelection(ImageView sourceCard, int cardIndex){
        if (sourceCard.getImage() != null && deneme.selectedCardCount < 3) {
            ImageView targetCard;
            System.out.println("deneme3");

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
                System.out.println("deneme4");
                SavasAraclari selectedSavasAraci = arayuz.GetOyuncu().GetKartlarin().get(cardIndex);
                System.out.println(selectedSavasAraci);
                targetCard.setImage(sourceCard.getImage());
                sourceCard.setImage(null);
                selectedSavasAraclari.add(selectedSavasAraci);
                deneme.selectedToOriginalMap.put(targetCard, sourceCard); // Seçilen kartı orijinal kartla eşleştir.
                deneme.selectedCardCount++;
            }
        }
    }

    // Kartı geri orijinal yerine koyma işlemi
    private void returnCardToOriginal(ImageView selectedCard, int position) {
        if (selectedCard.getImage() != null) {
            ImageView originalCard = selectedToOriginalMap.get(selectedCard);
            if (originalCard != null) {
                originalCard.setImage(selectedCard.getImage());
                selectedCard.setImage(null);
                selectedToOriginalMap.remove(selectedCard);
                selectedCardCount--;
            }
        }
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

        // Oyun sahnesindeki ImageView'ları kontrol et
        checkAndAddImageView(image1, "image1");
        checkAndAddImageView(image2, "image2");
        checkAndAddImageView(image3, "image3");
        checkAndAddImageView(image4, "image4");
        checkAndAddImageView(image5, "image5");
        checkAndAddImageView(image6, "image6");
    }

    private void checkAndAddImageView(ImageView imageView, String name) {
        if (imageView != null) {
            System.out.println(name + " initialized successfully");
            imageViews.add(imageView);
        } else {
            System.err.println(name + " is NULL!");
        }
    }
    private void loadImages(ArrayList<SavasAraclari> kartlar,int adim) {
        try {
            if (kartlar == null || kartlar.isEmpty()) {
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
            }

            Platform.runLater(() -> {
                if(adim>1)
                    imageViews = deneme.imageViews;
                System.out.println("Loading " + kartlar.size() + " cards");
                for (int i = 0; i < kartlar.size() && i < imageViews.size(); i++) {
                    SavasAraclari kart = kartlar.get(i);
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

                        Image cardImage = new Image(resourceURL.toExternalForm());
                        imageView.setImage(cardImage);
                        imageView.setFitWidth(93.6);
                        imageView.setFitHeight(124.8);
                        imageView.setPreserveRatio(true);

                        System.out.println("Successfully loaded image for card " + i);
                    } catch (Exception e) {
                        System.err.println("Error loading image for card " + i + ": " + e.getMessage());
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
    private String getImagePathForCard(SavasAraclari kart) {
        if (kart instanceof Ucak) return "resimler/ucak.png";
        if (kart instanceof Firkateyn) return "resimler/fırkateyn.png";
        if (kart instanceof Obus) return "resimler/obus.png";
        if (kart instanceof Siha) return "resimler/siha.png";
        if (kart instanceof KFS) return "resimler/KFS.png";
        return "";
    }

    // Kartları sıfırlayan yeni bir metod ekleyin
    private void resetSelectedCards() {
        // Seçilen kartların görüntülerini temizle
        deneme.secilen1.setImage(null);
        deneme.secilen2.setImage(null);
        deneme.secilen3.setImage(null);
        deneme.image1.setImage(null);
        deneme.image2.setImage(null);
        deneme.image3.setImage(null);
        deneme.image4.setImage(null);
        deneme.image5.setImage(null);
        deneme.image6.setImage(null);

        // Listeleri ve sayaçları sıfırla
        deneme.selectedToOriginalMap.clear();
        deneme.selectedSavasAraclari.clear();
        deneme.selectedCardCount = 0;
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
                loadImages(arayuz.GetOyuncu().GetKartlarin(),adim);

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

    private Arayuz arayuz;
    public void setMainApp(Arayuz arayuz) {
        this.arayuz = arayuz;
    }


    private int currentStep = 1;
    @FXML
    private void handleBattle() {
        System.out.println(deneme.selectedCardCount);
        if (deneme.selectedCardCount < 3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uyarı");
            alert.setHeaderText(null);
            alert.setContentText("Lütfen savaşmak için 3 kart seçin!");
            alert.showAndWait();
            return;
        }

        try {
            for(SavasAraclari x : selectedSavasAraclari)
                System.out.println(x.getAltSinif());
            arayuz.GetOyuncu().SetSecilenKartlar(selectedSavasAraclari);
            int yeniAdim = arayuz.Savas(currentStep, arayuz.GetOyuncu(), arayuz.GetPc());
            currentStep = yeniAdim;
            System.out.println("adim:"+ yeniAdim);
            boolean kartlarBitti = arayuz.GetOyuncu().Kartlar_bitti_mi() || arayuz.GetPc().Kartlar_bitti_mi();
            boolean turBitti = yeniAdim == 6 || arayuz.GetOyuncu().son_tur_mu;

            if (kartlarBitti || turBitti) {
                Platform.runLater(() -> {
                    try {
                        arayuz.Kazanan_kontrol(arayuz.GetOyuncu(), arayuz.GetPc());
                    } catch (Exception e) {
                        showError("Kazanan kontrol hatası: " + e.getMessage());
                    }
                });
            } else {
                handleTurnChange(yeniAdim);
                selectedSavasAraclari.clear();
            }
        } catch (Exception e) {
            showError("Savaş sırasında bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    public void handleNameSubmit() throws IOException {
        String name = nameField.getText();
        String userLevel = level.getText();

        if(validateInput()) {
            System.out.println("Ad: " + name + ", Seviye: " + userLevel);
            arayuz.receiveDataFromController(name,userLevel);
            System.out.println(arayuz.GetOyuncu().getOyuncuAdi());
            arayuz.GetOyuncu().KartListesi();
            arayuz.GetPc().KartListesi();

            Platform.runLater(() -> {
                try {
                    changeScene();
                    deneme.loadImages(arayuz.GetOyuncu().GetKartlarin(),1);
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
        if(nameField.getText().trim().isEmpty() && level.getText().trim().isEmpty()){
            showError("Kullanıcı adı ve seviye boş olamaz!");
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            showError("Kullanıcı adı boş olamaz!");
            nameField.requestFocus(); // Kullanıcıyı nameField alanına yönlendir
            return false;
        }

        if (level.getText().trim().isEmpty()) {
            showError("Seviye boş olamaz!");
            level.requestFocus(); // Kullanıcıyı level alanına yönlendir
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

            // Yeni controller'ı al
            Controller newController = loader.getController();
            if (newController == null) {
                throw new IllegalStateException("Controller oluşturulamadı");
            }
            deneme = newController;
            // Arayüz referansını aktar
            newController.setMainApp(this.arayuz);

            // Sahneyi oluştur
            Scene newScene = new Scene(root, 1051, 822);

            // Stage'i al
            Stage stage = (Stage) nameField.getScene().getWindow();
            if (stage == null) {
                throw new IllegalStateException("Mevcut stage bulunamadı");
            }
            tempstage = stage;
            tempscene = newScene;
            temproot = root;
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