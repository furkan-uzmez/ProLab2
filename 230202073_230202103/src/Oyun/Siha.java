package Oyun;

public class Siha extends Hava {
    static int sayi=1;
    Siha() {
        super(20, 15, 10, "Siha",0,10,10);  // Pass "Siha" to set altsinif properly
        setAltsinif(getAltSinif()+sayi);
    }

    @Override
    void KartPuaniGoster() {
        super.KartPuaniGoster();
        System.out.println("Altsinif: " + getAltSinif() + "\nDayaniklilik: " + getDayaniklilik()+ "\nVurus: " + getVurus() + "\nKara Vurus Avantaj: " + getKaraVurusAvantaj() + "\nDeniz Vurus Avantaj :" + getDenizVurusAvantaj());
    }
}