package cl.aguzman.proyectofinal.models;

import java.util.Date;

public class MedicalHistory {
    String namePet, photoPet, descriptionMedical, key;
    Date date;

    public MedicalHistory() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamePet() {
        return namePet;
    }

    public void setNamePet(String namePet) {
        this.namePet = namePet;
    }

    public String getPhotoPet() {
        return photoPet;
    }

    public void setPhotoPet(String photoPet) {
        this.photoPet = photoPet;
    }

    public String getDescriptionMedical() {
        return descriptionMedical;
    }

    public void setDescriptionMedical(String descriptionMedical) {
        this.descriptionMedical = descriptionMedical;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
