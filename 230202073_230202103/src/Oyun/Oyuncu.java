package Oyun;

import java.util.*;
public class Oyuncu {
    Random rand = new Random();
    String oyuncuID,oyuncuAdi;
    private int skor=0,sayi,seviye=0;
    private static int son_tur=0;
    private ArrayList<SavasAraclari> DagitimaGirenKartlar = new ArrayList<>();
    private ArrayList<SavasAraclari> Kartlarin = new ArrayList<>();
    private ArrayList<SavasAraclari> SecilenKartlar = new ArrayList<>();
    public  Oyuncu(){
        this.oyuncuAdi="Bilgisayar";
    }
    public Oyuncu(String oyuncuAdi,String oyuncuID){
        this.oyuncuAdi=oyuncuAdi;
        this.oyuncuID=oyuncuID;
    }
    public void setOyuncuAdi(String ad){
        this.oyuncuAdi = ad;
    }
    public String getOyuncuAdi(){
        return this.oyuncuAdi;
    }
    public int GetSeviye(){
        return this.seviye;
    }
    public static int getSon_tur(){return son_tur;}
    public void SetSeviye(int seviye){
        this.seviye += seviye;
        this.skor += seviye;
    }
    public void SeviyeAyarlama(SavasAraclari kart){
        if(kart.getDayaniklilik()==0){ //Kartin dayanikliligini kontrol ediyor
            if(kart.getSeviye()<=10) // Elenen kartin seviyesi 10 dan azsa 10 seviye puani eklenir
                this.SetSeviye(10);
            else // 10 dan fazlaysa seviyesi neyse o kadar seviye puani eklenir
                this.SetSeviye(kart.getSeviye());
        }
    }
    public void KartListesi(){
        KartSeviyeKontrol(); // Dagitma giren kartlara seviye yetiyorsa ekliyor
        KartVer(6); // Başlangıctaki iki tarafa da rastgele 6 kart verir
    }
    void KartSeviyeKontrol(){ //Oyuncunun seviyesiyle kartlarin seviyesini karsilastirip ona gore dagitima yeni kart ekliyor
        Ucak ucak = new Ucak();
        if (ucak.getSeviye() <= this.seviye && !kartListesindeVar(ucak))
            DagitimaGirenKartlar.add(ucak);

        Obus obus = new Obus();
        if (obus.getSeviye() <= this.seviye && !kartListesindeVar(obus))
            DagitimaGirenKartlar.add(obus);

        Firkateyn firkateyn = new Firkateyn();
        if (firkateyn.getSeviye() <= this.seviye && !kartListesindeVar(firkateyn))
            DagitimaGirenKartlar.add(firkateyn);

        Siha siha = new Siha();
        if (siha.getSeviye() <= this.seviye && !kartListesindeVar(siha))
            DagitimaGirenKartlar.add(siha);

        Sida sida = new Sida();
        if (sida.getSeviye() <= this.seviye && !kartListesindeVar(sida))
            DagitimaGirenKartlar.add(sida);

        KFS kfs = new KFS();
        if (kfs.getSeviye() <= this.seviye && !kartListesindeVar(kfs))
            DagitimaGirenKartlar.add(kfs);
    }
    void KartVer(int sonindex){ //istenen kadar rastgele kart verir
        int i=0;
        do {
            sayi = rand.nextInt(0,DagitimaGirenKartlar.size());
            SavasAraclari selectedCard = DagitimaGirenKartlar.get(sayi);
            // Creating a new instance based on the selected type
            if (selectedCard instanceof Ucak) {
                Kartlarin.add(new Ucak());
                Ucak.sayi++;
            } else if (selectedCard instanceof Obus) {
                Kartlarin.add(new Obus());
                Obus.sayi++;
            } else if (selectedCard instanceof Firkateyn) {
                Kartlarin.add(new Firkateyn());
                Firkateyn.sayi++;
            } else if (selectedCard instanceof Siha) {
                Kartlarin.add(new Siha());
                Siha.sayi++;
            } else if (selectedCard instanceof Sida) {
                Kartlarin.add(new Sida());
                Sida.sayi++;
            } else if (selectedCard instanceof KFS) {
                Kartlarin.add(new KFS());
                KFS.sayi++;
            }
            i++;
        }while(i<sonindex);
    }
    public int SkorGoster(){
        return skor;
    }
    public void kartSec(int adim){ // Bilgisayar kartlarindan rastgele 3 tane , oyuncu kartlarından kendi istediği 3 tanesini seçiyor
        SecilenKartlar.clear();
        int kullanilmiskart=0;
        for (SavasAraclari savasAraclari : Kartlarin) {
            if (savasAraclari.getKullanimsayisi() == 1)
                kullanilmiskart++;
        }
        if(Objects.equals(this.oyuncuAdi, "Bilgisayar")){
            for(int j=0;j<3;){
                sayi = rand.nextInt(Kartlarin.size());
                if(Kartlarin.size()-kullanilmiskart>=3) {
                    if (!SecilenKartlar.contains(Kartlarin.get(sayi)) && Kartlarin.get(sayi).getKullanimsayisi()==0) {
                        SecilenKartlar.add(Kartlarin.get(sayi));
                        Kartlarin.get(sayi).setKullanimsayisi(1);
                        j++;
                    }
                }
                else{
                    if(j<(Kartlarin.size()-kullanilmiskart) && !SecilenKartlar.contains(Kartlarin.get(sayi)) && Kartlarin.get(sayi).getKullanimsayisi()==0){
                        SecilenKartlar.add(Kartlarin.get(sayi));
                        Kartlarin.get(sayi).setKullanimsayisi(1);
                        j++;
                    }
                    else if(j>=(Kartlarin.size()-kullanilmiskart) && !SecilenKartlar.contains(Kartlarin.get(sayi))) {
                        SecilenKartlar.add(Kartlarin.get(sayi));
                        Kartlarin.get(sayi).setKullanimsayisi(1);
                        j++;
                    }
                }
            }
        }
    }
    public void KartKontrol(int adim){
        ArrayList<SavasAraclari> Silinecekler = new ArrayList<>();
        for(SavasAraclari x : Kartlarin){
            if(x.getDayaniklilik()==0)
                Silinecekler.add(x);
        }
        for(SavasAraclari x : Silinecekler)
            Kartlarin.remove(x);
        if(adim==son_tur)
            return;
        KartSeviyeKontrol();
        if(Kartlarin.size()==1) {
            KartVer(1);
            son_tur=adim+1;
        }
        KartVer(1);
    }
    private boolean kartListesindeVar(SavasAraclari kart) {
        for(SavasAraclari mevcutKart : DagitimaGirenKartlar) {
            if(mevcutKart.getClass().equals(kart.getClass()))
                return true;  // Aynı alt sınıfa sahip bir kart zaten var
        }
        return false;
    }
    public ArrayList<SavasAraclari> GetSecilenKartlar(){
        return SecilenKartlar;
    }
    public void SetSecilenKartlar(ArrayList<SavasAraclari> secilenkartlar){
        SecilenKartlar=secilenkartlar;
    }
    public ArrayList<SavasAraclari> GetKartlarin(){
        return Kartlarin;
    }
    public boolean Kartlar_bitti_mi(){
        if(Kartlarin.isEmpty()) {
            System.out.println(this.oyuncuAdi + " kartları bitti.Karsi taraf kazandi.");
            return true;
        }
        else
            return false;
    }
    public String KartlariniYazdir(){
        StringBuilder kartmetni = new StringBuilder();
        kartmetni.append(getOyuncuAdi() + "'ın Kartları(Dayanıklılık -- Kullanım):\n");
        for(SavasAraclari x : Kartlarin){
            kartmetni.append(x.getAltSinif() + " -- " + x.getDayaniklilik() + "-" + x.getKullanimsayisi() + "\n");
        }
        kartmetni.append("\n");
        return kartmetni.toString();
    }
    public String DagitimdakiKartlariYazdir(){
        StringBuilder kartmetni = new StringBuilder();
        for(SavasAraclari x:DagitimaGirenKartlar)
            kartmetni.append("\n" + x.getAltSinif());
        return kartmetni.toString();
    }
}