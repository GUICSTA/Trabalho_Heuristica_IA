import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.LinkedList;

// Classe auxiliar para o A* no Roadmap
class AStarNode implements Comparable<AStarNode> {
    GNodo nodo;
    AStarNode pai;
    float g; // Custo do início até aqui
    float h; // Heurística até o final
    float f; // f = g + h

    public AStarNode(GNodo nodo, AStarNode pai, float g, float h) {
        this.nodo = nodo;
        this.pai = pai;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    // Ordena pegando o menor F primeiro
    @Override
    public int compareTo(AStarNode outro) {
        return Float.compare(this.f, outro.f);
    }
}