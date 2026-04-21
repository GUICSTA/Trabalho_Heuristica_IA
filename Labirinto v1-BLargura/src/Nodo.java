public class Nodo implements Comparable<Nodo> {
    int X;
    int Y;
    Nodo pai = null;
    
    int g; // Custo do caminho até aqui
    int h; // Custo heurístico estimado até o destino
    int f; // f = g + h
    
    public Nodo(int x, int y, Nodo pai, int g, int h) {
        this.X = x;
        this.Y = y;
        this.pai = pai;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    // Ordena para que a Fila de Prioridade sempre pegue o menor F primeiro
    @Override
    public int compareTo(Nodo outro) {
        return Integer.compare(this.f, outro.f);
    }
}