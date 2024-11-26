package Oyun;

public abstract class Deniz extends SavasAraclari {
    Deniz(int seviyepuani,int dayaniklilik, int vurus, String AltSinif,int hava_avantaj,int kara_avantaj,int deniz_avantaj){
        super(seviyepuani,dayaniklilik,vurus,"Deniz", AltSinif,hava_avantaj,kara_avantaj,deniz_avantaj);
    }
    void KartPuaniGoster(){
        super.KartPuaniGoster();
        System.out.println("Sinif: " + GetSinif() + "\nHava vurus avantaji: ");
    }
}