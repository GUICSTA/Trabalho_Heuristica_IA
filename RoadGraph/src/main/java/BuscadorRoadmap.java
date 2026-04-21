import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.LinkedList;

public class BuscadorRoadmap {

    // A Heurística Euclidiana
    public float calcularHeuristicaEuclidiana(GNodo n1, GNodo n2) {
        double dlat = n1.lat - n2.lat;
        double dlon = n1.lon - n2.lon;
        
        double dsize = Math.sqrt(dlat * dlat + dlon * dlon);
        return (float) dsize * 1000000f; 
    }

    // O Método Principal A*
    public GPath buscaAStarRoadmap(GNodo inicio, GNodo destino) {
        PriorityQueue<AStarNode> listaAberta = new PriorityQueue<>();
        HashMap<Long, Float> melhorG = new HashMap<>();

        float hInicial = calcularHeuristicaEuclidiana(inicio, destino);
        listaAberta.add(new AStarNode(inicio, null, 0f, hInicial));
        melhorG.put(inicio.id, 0f);

        AStarNode nodoFinal = null;

        while (!listaAberta.isEmpty()) {
            AStarNode atual = listaAberta.poll();

            if (atual.nodo.id == destino.id) {
                nodoFinal = atual;
                break;
            }

            if (atual.g > melhorG.getOrDefault(atual.nodo.id, Float.MAX_VALUE)) {
                continue;
            }

            for (Aresta aresta : atual.nodo.listaArestas) {
                GNodo vizinho = (aresta.A.id == atual.nodo.id) ? aresta.B : aresta.A;
                float novoG = atual.g + aresta.size;

                if (novoG < melhorG.getOrDefault(vizinho.id, Float.MAX_VALUE)) {
                    melhorG.put(vizinho.id, novoG); 
                    float h = calcularHeuristicaEuclidiana(vizinho, destino);
                    listaAberta.add(new AStarNode(vizinho, atual, novoG, h));
                }
            }
        }

        if (nodoFinal == null) {
            System.out.println("Caminho não encontrado no Roadmap!");
            return null;
        }

        GPath caminhoFinal = new GPath();
        AStarNode rastreador = nodoFinal;
        
        LinkedList<Long> rotaOrdenada = new LinkedList<>();
        while (rastreador != null) {
            rotaOrdenada.addFirst(rastreador.nodo.id);
            rastreador = rastreador.pai;
        }
        
        caminhoFinal.idnodos.addAll(rotaOrdenada);
        return caminhoFinal;
    }
}