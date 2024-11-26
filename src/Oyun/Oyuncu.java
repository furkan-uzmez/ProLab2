package Oyun;

import java.util.*;
public class Oyuncu {
    Scanner scanner=new Scanner(System.in);
    Random rand = new Random();
    String oyuncuID,oyuncuAdi;
    private int skor=0,sayi,seviye=0;
    public static boolean son_tur_mu=false;
    private ArrayList<SavasAraclari> DagitimaGirenKartlar = new ArrayList<>();
    private ArrayList<SavasAraclari> Kartlarin = new ArrayList<>();
    private ArrayList<SavasAraclari> SecilenKartlar = new ArrayList<>();
    public  Oyuncu(){
        this.oyuncuAdi="Bilgisayar";
        KartListesi();
    }
    public Oyuncu(String oyuncuAdi,String oyuncuID){
        this.oyuncuAdi=oyuncuAdi;
        this.oyuncuID=oyuncuID;
        KartListesi();
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
    void KartListesi(){
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
    int SkorGoster(){
        return skor;
    }
    public void kartSec(int adim){ // Bilgisayar kartlarindan rastgele 3 tane , oyuncu kartlarından kendi istediği 3 tanesini seçiyor
        SecilenKartlar.clear();
        int kullanilmiskart=0;
        for (SavasAraclari savasAraclari : Kartlarin) {
            if (savasAraclari.kullanimsayisi == 1)
                kullanilmiskart++;
        }
        if(Objects.equals(this.oyuncuAdi, "Bilgisayar")){
            for(int j=0;j<3;){
                sayi = rand.nextInt(Kartlarin.size());
                if(Kartlarin.size()-kullanilmiskart>=3) {
                    if (!SecilenKartlar.contains(Kartlarin.get(sayi)) && Kartlarin.get(sayi).kullanimsayisi==0) {
                        SecilenKartlar.add(Kartlarin.get(sayi));
                        Kartlarin.get(sayi).kullanimsayisi=1;
                        j++;
                    }
                }
                else{
                    if(j<(Kartlarin.size()-kullanilmiskart) && !SecilenKartlar.contains(Kartlarin.get(sayi)) && Kartlarin.get(sayi).kullanimsayisi==0){
                        SecilenKartlar.add(Kartlarin.get(sayi));
                        Kartlarin.get(sayi).kullanimsayisi=1;
                        j++;
                    }
                    else if(j>=(Kartlarin.size()-kullanilmiskart) && !SecilenKartlar.contains(Kartlarin.get(sayi))) {
                        SecilenKartlar.add(Kartlarin.get(sayi));
                        Kartlarin.get(sayi).kullanimsayisi=1;
                        j++;
                    }
                }
            }
        }
        else{
            System.out.println("----" + adim + ".Adim----");
            for(int i=0;i<3;i++){
                boolean kartBulundu;
                do {
                    System.out.print((i + 1) + ".karti seciniz :");
                    String kart = scanner.nextLine();
                    kartBulundu = false;
                    for (int j = 0; j < Kartlarin.size(); j++) {
                        if(Kartlarin.size()-kullanilmiskart>=3) {
                            if (Objects.equals(kart, Kartlarin.get(j).getAltSinif()) && !SecilenKartlar.contains(Kartlarin.get(j)) && Kartlarin.get(j).kullanimsayisi==0) {
                                SecilenKartlar.add(Kartlarin.get(j));
                                Kartlarin.get(j).kullanimsayisi=1;
                                kartBulundu = true;
                                break;
                            }
                        }
                        else{
                            if (i<(Kartlarin.size()-kullanilmiskart) && Objects.equals(kart, Kartlarin.get(j).getAltSinif()) && !SecilenKartlar.contains(Kartlarin.get(j)) && Kartlarin.get(j).kullanimsayisi==0) {
                                SecilenKartlar.add(Kartlarin.get(j));
                                Kartlarin.get(j).kullanimsayisi=1;
                                kartBulundu = true;
                                break;
                            }
                            else if (i>=(Kartlarin.size()-kullanilmiskart) && Objects.equals(kart, Kartlarin.get(j).getAltSinif()) && !SecilenKartlar.contains(Kartlarin.get(j)) ) {
                                SecilenKartlar.add(Kartlarin.get(j));
                                Kartlarin.get(j).kullanimsayisi=1;
                                kartBulundu = true;
                                break;
                            }
                        }
                    }
                    if (!kartBulundu) {
                        System.out.println("Sectiginiz kart listenizde bulunmamaktir");
                    }
                }while(!kartBulundu);
            }
        }
    }
    public void KartKontrol(){
        ArrayList<SavasAraclari> Silinecekler = new ArrayList<>();
        for(SavasAraclari x : Kartlarin){
            if(x.getDayaniklilik()==0)
                Silinecekler.add(x);
        }
        for(SavasAraclari x : Silinecekler)
            Kartlarin.remove(x);
        KartSeviyeKontrol();
        if(Kartlarin.size()==1) {
            KartVer(1);
            son_tur_mu=true;
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
        kartmetni.append("Kartlarin:\n");
        for(SavasAraclari x : Kartlarin){
            kartmetni.append(x.getAltSinif() + " -- " + x.getDayaniklilik() + "-" + x.kullanimsayisi + "\n");
        }
        return kartmetni.toString();
    }
    public String DagitimdakiKartlariYazdir(){
        StringBuilder kartmetni = new StringBuilder();
        for(SavasAraclari x:DagitimaGirenKartlar)
            kartmetni.append("\n" + x.getAltSinif());
        return kartmetni.toString();
    }
}