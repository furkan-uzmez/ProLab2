package Oyun;

import java.util.HashMap;
import java.util.Objects;

public abstract class SavasAraclari {
    private int seviyePuani;
    private int dayaniklilik;
    private int vurus;
    private String sinif,altsinif;
    private int havaVurusAvantaj,karaVurusAvantaj,denizVurusAvantaj;
    private HashMap<String,Integer> avantajlar = new HashMap<>();
    protected int kullanimsayisi=0;
    SavasAraclari(int seviyePuani,int dayaniklilik, int vurus,String sinif,String altsinif,int hava_avantaj,int kara_avantaj,int deniz_avantaj) {
        this.seviyePuani = seviyePuani;
        this.dayaniklilik = dayaniklilik;
        this.vurus = vurus;
        this.sinif=sinif;
        this.altsinif = altsinif;
        this.havaVurusAvantaj=hava_avantaj;
        this.karaVurusAvantaj=kara_avantaj;
        this.denizVurusAvantaj=deniz_avantaj;
        avantajlar.put("Hava", getHavaVurusAvantaj());
        avantajlar.put("Kara",getKaraVurusAvantaj());
        avantajlar.put("Deniz",getDenizVurusAvantaj());
    }
    int getSeviye(){
        return seviyePuani;
    }
    public int getDayaniklilik() {
        return dayaniklilik;
    }
    void setDayaniklilik(int dayaniklilik){
        this.dayaniklilik = dayaniklilik;
    }
    int getVurus() {
        return vurus;
    }
    String GetSinif(){ return sinif;}
    public String getAltSinif() {
        return altsinif;
    }
    void setAltsinif(String altsinif){this.altsinif = altsinif;}
    int getKaraVurusAvantaj() {
        return karaVurusAvantaj;
    }
    int getDenizVurusAvantaj() {
        return denizVurusAvantaj;
    }
    int getHavaVurusAvantaj() {
        return havaVurusAvantaj;
    }
    public int SaldiriHesapla(SavasAraclari savunankart){
        int hasar = this.getVurus();
        for (HashMap.Entry<String, Integer> entry : avantajlar.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if(Objects.equals(savunankart.GetSinif(), key))
                hasar += value;
        }
        return hasar;
    }
    public void DurumGuncelle(int hasar) {
        if (getDayaniklilik() >= hasar) {
            setDayaniklilik(getDayaniklilik()-hasar);
        } else {
            setDayaniklilik(0);
        }
    }
    void KartPuaniGoster() {
        System.out.println("Seviye puani: " + seviyePuani);
    }
}