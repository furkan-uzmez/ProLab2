package sample;

import Oyun.Oyuncu;
import Oyun.SavasAraclari;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Arayuz extends Application {
    private String receivedName;
    private String receivedLevel;
    private String receivedTur;
    private Oyuncu oyuncu;
    private Oyuncu bilgisayar;
    Parent temproot;
    public Oyuncu GetOyuncu(){
        return this.oyuncu;
    }
    public Oyuncu GetPc(){
        return this.bilgisayar;
    }
    public int GetTur(){
        return Integer.parseInt(receivedTur);
    }


    public void receiveDataFromController(String name, String level,String tur) throws IOException {
        receivedName = name;
        receivedLevel = level;
        receivedTur = tur;
        this.oyuncu = new Oyuncu(receivedName,"1");
        oyuncu.SetSeviye(Integer.parseInt(receivedLevel));
        this.bilgisayar = new Oyuncu();
        System.out.println("Arayüz'e Gelen Veriler: ");
        System.out.println("Ad: " + name + ", Seviye: " + level + "Tur:" + tur);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // İlk sahne yükleme
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("sample.fxml")));
        Parent root = loader.load();

        temproot = root;
        //Controller referansını alın
        Controller controller = loader.getController();
        controller.setMainApp(this); // Controller'a Main referansını ver

        // İlk sahneyi göster
        primaryStage.setTitle("JavaFX Örneği");
        //primaryStage.setScene(new Scene(root, 1051, 822 ));
        primaryStage.setScene(new Scene(root, 1151.44, 900.12 ));
        primaryStage.show();
    }

    public static int Savas(int adim,Oyuncu oyuncu,Oyuncu bilgisayar) throws IOException {
        WriteFile(adim,"\n----------" + adim + ".Adim----------\n" + oyuncu.KartlariniYazdir() + "\nOyuncu(Vuracağı Hasar) -- Bilgisayar(Vuracağı Hasar)\n");
        bilgisayar.kartSec(0);
        for(int i = 0; i < 3; i++){
            SavasAraclari insankart = oyuncu.GetSecilenKartlar().get(i);
            SavasAraclari pckart = bilgisayar.GetSecilenKartlar().get(i);
            insankart.DurumGuncelle(pckart.SaldiriHesapla(insankart)); // bilgisayar insana vuruyor
            pckart.DurumGuncelle(insankart.SaldiriHesapla(pckart)); // insan bilgisayara vuruyor
            oyuncu.SeviyeAyarlama(pckart); //Bilgisayar kartinin dayanikliligini kontrol eder ona gore insanin seviyesini ayarlar
            bilgisayar.SeviyeAyarlama(insankart); //Insan kartinin dayanikliligini kontrol eder ona gore bilgisayarin seviyesini ayarlar
            WriteFile(0,i+1 + ".Eslesme:    " + oyuncu.GetSecilenKartlar().get(i).getAltSinif() + "(" + insankart.SaldiriHesapla(pckart) + ")" + "  <-->  " +  bilgisayar.GetSecilenKartlar().get(i).getAltSinif() + "(" + pckart.SaldiriHesapla(insankart) + ")" + "\n");
            insankart.setKullanimsayisi(1);
        }
        WriteFile(0,"\nOyuncu seviye: " + oyuncu.GetSeviye() + "\nBilgisayar seviye: " + bilgisayar.GetSeviye() + "\n\nDagitimdaki Kartlarin:" + oyuncu.DagitimdakiKartlariYazdir() + "\n");
        oyuncu.KartKontrol();
        bilgisayar.KartKontrol();
        return adim+1;
    }
    public static String Kazanan_kontrol(Oyuncu oyuncu, Oyuncu bilgisayar) throws IOException {
        String yazilacak="";
        if(oyuncu.GetSeviye()> bilgisayar.GetSeviye())
            yazilacak = "Oyuncu kazandı , bilgisayar kaybetti";
        else if (oyuncu.GetSeviye()== bilgisayar.GetSeviye()) {
            int insandayaniklilik=0,pcdayaniklilik=0;
            for(SavasAraclari x:oyuncu.GetKartlarin())
                insandayaniklilik+=x.getDayaniklilik();
            for (SavasAraclari x:bilgisayar.GetKartlarin())
                pcdayaniklilik+=x.getDayaniklilik();
            if(insandayaniklilik>pcdayaniklilik)
                yazilacak = "Oyuncu kazandı , bilgisayar kaybetti";
            else
                yazilacak = "Bilgisayar kazandı , oyuncu kaybetti";
        } else
            yazilacak = "Bilgisayar kazandı , oyuncu kaybetti";
            WriteFile(0,yazilacak);
            return yazilacak;
    }
    public static void WriteFile(int adim,String metin) throws IOException {
        if(adim==1) {
            BufferedWriter clear = new BufferedWriter(new FileWriter("adimlar.txt"));
            clear.close();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("adimlar.txt",true));
        writer.write(metin);
        writer.close();
    }
}
