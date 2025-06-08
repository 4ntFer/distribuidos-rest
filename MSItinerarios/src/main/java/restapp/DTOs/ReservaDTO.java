package restapp.DTOs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ReservaDTO implements Serializable {
    public String data_de_embarque;
    public int numero_de_passageiros;
    public int numero_de_cabines;
    public int itinerario;

    public ReservaDTO(String data_de_embarque, int numero_de_passageiros, int numero_de_cabines, int itinerario) {
        this.data_de_embarque = data_de_embarque;
        this.numero_de_passageiros = numero_de_passageiros;
        this.numero_de_cabines = numero_de_cabines;
        this.itinerario = itinerario;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        out.writeObject(this);
        return bos.toByteArray();
    }
}
