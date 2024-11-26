package Oyun;

public abstract class Kara extends SavasAraclari {
    Kara(int seviyepuani,int dayaniklilik, int vurus, String AltSinif,int hava_avantaj,int kara_avantaj,int deniz_avantaj){
        super(seviyepuani,dayaniklilik,vurus,"Kara", AltSinif,hava_avantaj,kara_avantaj,deniz_avantaj);
    }
    void KartPuaniGoster(){
        super.KartPuaniGoster();
        System.out.println("Sinif: " + GetSinif() + "\nDeniz vurus avantaji: ");
    }
}