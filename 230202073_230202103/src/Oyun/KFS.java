package Oyun;

public class KFS extends Kara {
    static int sayi=1;
    KFS() {
        super(20,10, 10, "KFS",20,0,10);  // Pass "KFS" to set altsinif properly
        setAltsinif(getAltSinif()+sayi);
    }

    @Override
    void KartPuaniGoster() {
        super.KartPuaniGoster();
        System.out.println("Altsinif: " + getAltSinif() + "\nDayaniklilik: " + getDayaniklilik()+ "\nVurus: " + getVurus() +  "\nDeniz Vurus Avantaj: " + getDenizVurusAvantaj() + "\nHava Vurus Avantaj :" + getHavaVurusAvantaj());
    }
}