import java.util.ArrayList;
import java.util.Scanner;

public class HandleClient{
    public Reserva reserva;
    private String embarque = "";

    public void criaReservaDoCliente(){
        Itinerario[] itinerariosEncontrados = buscaItinerariosParaCliente();
        Itinerario escolhido;

        if(itinerariosEncontrados.length != 0){
            escolhido = clienteEscolheItinerarios(itinerariosEncontrados);
            registraReserva(escolhido, embarque);
        }
        else{
            System.out.println(
                    "\n\n=================\n" +
                            "Nenhum itinerario encontrado" +
                            "\n=================\n"
            );
        }
    }

    private void registraReserva(Itinerario itinerario, String data_de_embarque){
        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "\n\n=================\n" +
                        "Realizando reserva" +
                        "\n=================\n"
        );

        System.out.print("Informe o numero de passageiros: ");
        String passageiros = scanner.nextLine();

        System.out.print("Informe o numero de cabines: ");
        String cabines = scanner.nextLine();

        reserva = new Reserva(data_de_embarque,passageiros,cabines,itinerario);
    }

    private Itinerario clienteEscolheItinerarios(Itinerario[] itinerarios){
        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "\n\n=================\n" +
                "Escolha um itinerario" +
                "\n=================\n"
        );

        int i = 0;
        for(Itinerario it : itinerarios){
            System.out.println("["+ i +"]\t" + it.toString() + "\n");
            i++;
        }

        System.out.print("Informe o itinerario: ");
        int indexEscolhido = scanner.nextInt();

        return itinerarios[indexEscolhido];
    }

    private Itinerario [] buscaItinerariosParaCliente(){
        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "\n\n=================\n" +
                        "Buscando itinerarios" +
                        "\n=================\n"
        );

        System.out.print("Informe o destino: ");
        String destino = scanner.nextLine();

        System.out.print("Informe a data de embarque: ");
        String data_de_embarque = scanner.nextLine();

        this.embarque = data_de_embarque;

        System.out.print("Informe o porto de embarque: ");
        String porto_de_embarque = scanner.nextLine();

        System.out.flush();

        return buscaItinerario(destino, data_de_embarque, porto_de_embarque);
    }

    private Itinerario [] buscaItinerario(String destino, String data_de_embarque, String porto_de_embarque){
        Itinerario [] itinerarios = BuildItinerarios.getItinerarios();
        ArrayList<Itinerario> result = new ArrayList<>();

        for(Itinerario it : itinerarios){
            if(it.matchesWith(destino, data_de_embarque, porto_de_embarque))
                result.add(it);
        }

        return result.toArray(new Itinerario[]{new Itinerario(new String[0], "", "", "", "", "", "")});
    }
}
