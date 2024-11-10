public class Sida extends Deniz{
    static int sayi=1;
    Sida() {
        super( 20,15, 10, "Sida",10,10,0);  // Pass "Sida" to set altsinif properly
        setAltsinif(getAltSinif()+sayi);
    }

    @Override
    void KartPuaniGoster() {
        super.KartPuaniGoster();
        System.out.println("Altsinif: " + getAltSinif() + "\nDayaniklilik: " + getDayaniklilik()+ "\nVurus: " + getVurus() + "\nKara Vurus Avantaj: " + getHavaVurusAvantaj() + "\nKara Vurus Avantaj :" + getKaraVurusAvantaj());
    }
}