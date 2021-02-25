package programacion.entregaut6.modelo;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import programacion.entregaut6.modelo.CalendarioEventos;
import programacion.entregaut6.io.CalendarioIO;
/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario;

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        if(!calendario.containsKey(nuevo.getMes())){
            ArrayList<Evento> evento = new ArrayList<>();
            evento.add(nuevo);
            calendario.put(nuevo.getMes(), evento);
        }else{
            ArrayList<Evento> eventos = calendario.get(nuevo.getMes());
            for(int i = 0; i < eventos.size(); i++){
                if(nuevo.antesDe(eventos.get(i))){
                    eventos.add(i, nuevo);
                }
            }
            calendario.put(nuevo.getMes(), eventos);
        }
    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Mes, ArrayList<Evento>>> conjunto = calendario.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it = conjunto.iterator();
        while (it.hasNext()) {
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            sb.append(entrada.getKey()).append("\n").append(entrada.getValue().toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        if(calendario.get(mes) == null){
            return 0;
        }else{
            return calendario.get(mes).size();
        }
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        Set<Mes> meses = calendario.keySet();
        TreeSet<Mes> conjunto = new TreeSet<>();
        int mayor = 0;
        for(Mes mes : meses){
            if(calendario.get(mes).size() >= mayor){
                conjunto.add(mes);                
            }
        }
        return conjunto;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        Set<Mes> meses = calendario.keySet();
        String nombreEvento = "";
        int duracion = 0;
        for(Mes mes : meses){
            for(Evento evento : calendario.get(mes)){
                if(evento.getDuracion() > duracion){
                    duracion = evento.getDuracion();
                    nombreEvento = evento.getNombre();
                }
            }
        }
        return nombreEvento;
    }

    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int eliminados = 0;
        for(Mes mes : meses){
            if(calendario.containsKey(mes)){
                ArrayList<Evento> eventosMes = calendario.get(mes);
                for(Evento evento : eventosMes){
                    if(evento.getDia() == dia){
                        eventosMes.remove(evento);
                        eliminados++;
                    }
                }
                if (eventosMes.isEmpty()){
                    calendario.remove(mes);
                }
            }
        }
        return eliminados;
    } 

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.FEBRERO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        mes = Mes.MARZO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
            + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
            + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.FEBRERO, Mes.MARZO, Mes.MAYO, Mes.JUNIO};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
            " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
