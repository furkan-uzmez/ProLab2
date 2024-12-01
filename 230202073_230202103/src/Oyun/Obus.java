package Oyun;

public class Obus extends Kara {
    static int sayi = 1;

    Obus() {
        super(0, 20, 10, "Obus", 0, 0, 5);  // Pass "Obus// " to set altsinif properly
        setAltsinif(getAltSinif() + sayi);
    }

    @Override
    void KartPuaniGoster() {
        super.KartPuaniGoster();
        System.out.println("Altsinif: " + getAltSinif() + "\nDayaniklilik: " + getDayaniklilik() + "\nVurus: " + getVurus() + "\nDeniz Vurus Avantaj: " + getDenizVurusAvantaj());
    }
}