package cl.aguzman.proyectofinal.models;

public class MedicalHistory {
    String namePet, photoPet, descriptionMedical, key, keyMedical;
    int day, month, year;

    public String getKeyMedical() {
        return keyMedical;
    }

    public void setKeyMedical(String keyMedical) {
        this.keyMedical = keyMedical;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

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

}
