package MSItinerarios.api.services;

import MSItinerarios.api.DTOs.ConsultaItinerariosDTO;
import MSItinerarios.api.DTOs.ItinerarioDTO;
import MSItinerarios.api.services.mock.ItinerarioMock;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import restapp.DTOs.ReservaDTO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ItinerariosService {
    private static final List<ItinerarioDTO> itinerarios = ItinerarioMock.objects;

    private static final String RESERVAS_EXCHANGE_NAME = "reservas";

    private static final String RESERVA_CRIADA_RK = "reserva_criada";
    private static final String RESERVA_CANCELADA_RK = "reserva_cancelada";

    private final ConnectionFactory factory;
    private final Connection connection;

    public ItinerariosService() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String reserva_criada_queue_name = channel.queueDeclare().getQueue();
        String reserva_cancelada_queue_name = channel.queueDeclare().getQueue();

        channel.exchangeDeclare(RESERVAS_EXCHANGE_NAME, "direct");

        channel.queueBind(reserva_criada_queue_name,
                RESERVAS_EXCHANGE_NAME,
                RESERVA_CRIADA_RK);

        channel.queueBind(reserva_cancelada_queue_name,
                RESERVAS_EXCHANGE_NAME,
                RESERVA_CANCELADA_RK);

        DeliverCallback reservaCriadaCallback = (consumerTag, delivery) -> {
            try {
                reservaCriadaHandle(delivery.getBody());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };

        DeliverCallback reservaCanceladaCallback = (consumerTag, delivery) -> {
            try {
                reservaCanceladaHandle(delivery.getBody());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };

        channel.basicConsume(reserva_cancelada_queue_name,true, reservaCanceladaCallback, consumerTag -> {});
        channel.basicConsume(reserva_criada_queue_name,true, reservaCriadaCallback, consumerTag -> {});
    }

    public ItinerarioDTO[] consultaItinerarios(ConsultaItinerariosDTO consultaItinerariosDTO){
        List<ItinerarioDTO> result = new ArrayList<>();

        for(int i = 0 ; i < itinerarios.size() ; i++){
            ItinerarioDTO itinerario = itinerarios.get(i);
            boolean validacaoPartida = false;
            boolean validacaoPorto = false;
            boolean validacaoDestino = false;

            if(itinerario.porto_de_desembarque.equals(
                    consultaItinerariosDTO.destino)){
                validacaoDestino = true;
            }

            if(itinerario.porto_de_embarque.equals(
                    consultaItinerariosDTO.porto_de_embarque)){
                validacaoPorto = true;
            }

            for(int j = 0 ; j < itinerario.datas_de_partida_disponiveis.length ; j++){
                String data = itinerario.datas_de_partida_disponiveis[j];

                if(consultaItinerariosDTO.data_de_embarque.equals(data)){
                    validacaoPartida = true;
                }
            }

            if(validacaoDestino && validacaoPartida && validacaoPorto){
                result.add(itinerario);
            }
        }


        return result.toArray(new ItinerarioDTO[0]);
    }

    private void reservaCriadaHandle(byte[] massage) throws IOException, ClassNotFoundException {
        ReservaDTO reservaCriada;
        ItinerarioDTO itinerario;

        ByteArrayInputStream bis = new ByteArrayInputStream(massage);
        ObjectInputStream in = new ObjectInputStream(bis);

        reservaCriada = (ReservaDTO) in.readObject();

        itinerario = itinerarios.get(reservaCriada.itinerario);
        itinerario.cabines_disponiveis -= reservaCriada.numero_de_cabines;
    }

    private void reservaCanceladaHandle(byte[] massage) throws IOException, ClassNotFoundException {
        ReservaDTO reserva;
        ItinerarioDTO itinerario;

        ByteArrayInputStream bis = new ByteArrayInputStream(massage);
        ObjectInputStream in = new ObjectInputStream(bis);

        reserva = (ReservaDTO) in.readObject();

        itinerario = itinerarios.get(reserva.itinerario);
        itinerario.cabines_disponiveis += reserva.numero_de_cabines;
    }

    public ItinerarioDTO[] consultaItinerarios(){
        return itinerarios.toArray(new ItinerarioDTO[0]);
    }
}
