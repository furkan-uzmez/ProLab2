public class Firkateyn extends Deniz{
    static int sayi=1;
    Firkateyn() {
        super( 0,25, 10, "Firkateyn",5,0,0);  // Pass "Firkateyn" to set altsinif properly
        setAltsinif(getAltSinif()+sayi);
    }

    @Override
    void KartPuaniGoster() {
        super.KartPuaniGoster();
        System.out.println("Altsinif: " + getAltSinif() + "\nDayaniklilik: " + getDayaniklilik()+ "\nVurus: " + getVurus() + "\nHava Vurus Avantaj: " + getHavaVurusAvantaj());
    }
}