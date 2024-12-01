package Oyun;

public class Ucak extends Hava {
    static int sayi=1;
    Ucak() {
        super(0, 20, 10, "Ucak",0,10,0);// Pass "Ucak" to set altsinif properly
        setAltsinif(getAltSinif()+sayi);
    }

    @Override
    void KartPuaniGoster() {
        super.KartPuaniGoster();
        System.out.println("Altsinif: " + getAltSinif() + "\nDayaniklilik: " + getDayaniklilik()+ "\nVurus: " + getVurus() + "\nKara Vurus Avantaj: " + getKaraVurusAvantaj());
    }
}