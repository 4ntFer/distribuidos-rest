package restapp.DTOs;

public class ReservaDTO {
    public String data_de_embarque;
    public String numero_de_passageiros;
    public String numero_de_cabines;
    public int itinerario;

    public ReservaDTO(String data_de_embarque, String numero_de_passageiros, String numero_de_cabines, int itinerario) {
        this.data_de_embarque = data_de_embarque;
        this.numero_de_passageiros = numero_de_passageiros;
        this.numero_de_cabines = numero_de_cabines;
        this.itinerario = itinerario;
    }
}
