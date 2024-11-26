package sample;

import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;  // Değiştirildi
import javafx.scene.image.Image;      // Eklendi

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Controller{
    Controller deneme;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;
    @FXML
    private ImageView image5;
    @FXML
    private ImageView image6;

    private List<ImageView> imageViews;

    private List<ImageView> constantimageViews;



    private List<ImageView> getimageViews(){
        return imageViews;
    }

    @FXML
    private void initialize() {
        System.out.println("Initialize metodu çağrıldı");

        // ImageView listesini başlat
        imageViews = new ArrayList<>();

        // Her iki sahne için de ImageView kontrolü
        if (imageView != null) {
            System.out.println("Background ImageView initialized");
        }

        // Oyun sahnesindeki ImageView'ları kontrol et
        checkAndAddImageView(image1, "image1");
        checkAndAddImageView(image2, "image2");
        checkAndAddImageView(image3, "image3");
        checkAndAddImageView(image4, "image4");
        checkAndAddImageView(image5, "image5");
        checkAndAddImageView(image6, "image6");
        System.out.println(imageViews);
        if(imageViews!=null)
            constantimageViews = getimageViews();

    }

    private void checkAndAddImageView(ImageView imageView, String name) {
        if (imageView != null) {
            System.out.println(name + " initialized successfully");
            imageViews.add(imageView);
        } else {
            System.err.println(name + " is NULL!");
        }
    }

    private void loadImages(int secim) {
        try {
            System.out.println(constantimageViews);
            if (getimageViews() == null || getimageViews().isEmpty()) {
                System.err.println("ImageViews listesi boş veya null!");
                return;
            }
            String imagePath;
            if(secim==1)
                imagePath = "resimler/ucak.jpeg";
            else
                imagePath = "resimler/mızrakcılar.png";
            var resourceURL = getClass().getResource(imagePath);

            if (resourceURL == null) {
                System.err.println("Resim dosyası bulunamadı: " + imagePath);
                return;
            }

            Image cardImage = new Image(resourceURL.toExternalForm());

            Platform.runLater(() -> {
                for (ImageView imageView : imageViews) {
                    if (imageView != null) {
                        imageView.setImage(cardImage);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        imageView.setPreserveRatio(true);
                    } else {
                        System.err.println("Null ImageView tespit edildi!");
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Resim yükleme hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private TextField nameField; // FXML'deki TextField ile bağlantı

    @FXML
    private TextField level;

    private Arayuz arayuz;
    public void setMainApp(Arayuz arayuz) {
        this.arayuz = arayuz;
    }

    @FXML
    public void handleNameSubmit() throws IOException {
        String name = nameField.getText(); // Kullanıcının adını al
        String userLevel = level.getText(); // Kullanıcının seviyesini al

        // Girdileri kontrol et ve işlem yap
        if(validateInput()) {
            System.out.println("Ad: " + name + ", Seviye: " + userLevel);
            arayuz.receiveDataFromController(name,userLevel);
            System.out.println(arayuz.GetOyuncu().getOyuncuAdi());
            // Sahne değişimi ve oyun mantığını ayrı bir thread'de çalıştır
            AtomicInteger adim = new AtomicInteger(1); // Thread-safe sayaç

            new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        try {
                            changeScene();
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Sahne değiştirme hatası: " + e.getMessage());
                        }
                    });

                    Thread.sleep(500);

                    boolean devamEt = true;
                    while(devamEt) {
                            final int currentAdim = adim.get();
                            deneme.loadImages(currentAdim);
                            System.out.println("Mevcut Adım: " + currentAdim); // Debug için

                            int yeniAdim = arayuz.Savas(currentAdim, arayuz.GetOyuncu(), arayuz.GetPc());
                            adim.set(yeniAdim);

                            System.out.println("Yeni Adım: " + yeniAdim); // Debug için

                            // Döngü kontrol koşullarını kontrol et
                            boolean kartlarBitti = arayuz.GetOyuncu().Kartlar_bitti_mi() || arayuz.GetPc().Kartlar_bitti_mi();
                            boolean turBitti = yeniAdim == 6 || arayuz.GetOyuncu().son_tur_mu;

                            devamEt = !kartlarBitti && !turBitti;

                            // Her adımda UI'ı güncelle
                            Platform.runLater(() -> {
                                // Burada UI güncellemelerini yapabilirsiniz
                                System.out.println("Tur durumu güncellendi - Adım: " + yeniAdim);
                            });
                            Thread.sleep(1000); // Her adım arasında 1 saniye bekle
                    }

                    Platform.runLater(() -> {
                        try {
                            arayuz.Kazanan_kontrol(arayuz.GetOyuncu(), arayuz.GetPc());
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("Kazanan kontrol hatası: " + e.getMessage());
                        }
                    });

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showError("İşlem hatası: " + e.getMessage()));
                }
            }).start();
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

            // Arayüz referansını aktar
            newController.setMainApp(this.arayuz);

            // Sahneyi oluştur
            Scene newScene = new Scene(root, 600, 400);

            // Stage'i al
            Stage stage = (Stage) nameField.getScene().getWindow();
            if (stage == null) {
                throw new IllegalStateException("Mevcut stage bulunamadı");
            }

            // Yeni sahneyi ayarla
            stage.setScene(newScene);
            stage.show();
            deneme = newController;
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
