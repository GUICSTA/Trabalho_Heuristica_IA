import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.ImageIO;


public class GamePanel extends Canvas implements Runnable
{
private static final int PWIDTH = 960;
private static final int PHEIGHT = 800;
private Thread animator;
private boolean running = false;
private boolean gameOver = false; 


int FPS,SFPS;
int fpscount;

public static Random rnd = new Random();

//BufferedImage imagemcharsets;

boolean LEFT, RIGHT,UP,DOWN;

public static int mousex,mousey; 

public static ArrayList<Agente> listadeagentes = new ArrayList<Agente>();

Mapa_Grid mapa;

double posx,posy;

MeuAgente meuHeroi = null;

//TODO ESSE È O RESULTADO
int caminho[] = null;

float zoom = 1;

int ntileW = 60;
int ntileH = 50;

public GamePanel()
{

	setBackground(Color.white);
	setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

	// create game components
	setFocusable(true);

	requestFocus(); // JPanel now receives key events	
	
	
	// Adiciona um Key Listner
	addKeyListener( new KeyAdapter() {
		public void keyPressed(KeyEvent e)
			{ 
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT){
					LEFT = true;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					RIGHT = true;
				}
				if(keyCode == KeyEvent.VK_UP){
					UP = true;
				}
				if(keyCode == KeyEvent.VK_DOWN){
					DOWN = true;
				}	
			}
		@Override
			public void keyReleased(KeyEvent e ) {
				int keyCode = e.getKeyCode();
				
				if(keyCode == KeyEvent.VK_LEFT){
					LEFT = false;
				}
				if(keyCode == KeyEvent.VK_RIGHT){
					RIGHT = false;
				}
				if(keyCode == KeyEvent.VK_UP){
					UP = false;
				}
				if(keyCode == KeyEvent.VK_DOWN){
					DOWN = false;
				}
			}
	});
	
	addMouseMotionListener(new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			mousex = e.getX(); 
			mousey = e.getY();
			

		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if(e.getButton()==3){
				int mousex = (int)((e.getX()+mapa.MapX)/zoom);
				int mousey = (int)((e.getY()+mapa.MapY)/zoom);
				
				int mx = mousex/16;
				int my = mousey/16;
				
				if(mx>mapa.Altura) {
					return;
				}
				if(my>mapa.Largura) {
					return;
				}
				
				mapa.mapa[my][mx] = 1;
			}
		}
	});
	
	addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println(" "+arg0.getButton());
			int mousex = (int)((arg0.getX()+mapa.MapX)/zoom);
			int mousey = (int)((arg0.getY()+mapa.MapY)/zoom);
			
			System.out.println("L "+mapa.Largura+" A "+mapa.Altura+" ");
			System.out.println(""+arg0.getX()+" "+mapa.MapX+" "+zoom);
			System.out.println(""+mousex+" "+mousey);
			
			int mx = mousex/16;
			int my = mousey/16;
			
			if(mx>mapa.Altura) {
				return;
			}
			if(my>mapa.Largura) {
				return;
			}
			
			if(arg0.getButton()==3){

				
				if(mapa.mapa[my][mx]==0){
					mapa.mapa[my][mx] = 1;
				}else{
					mapa.mapa[my][mx] = 0;
				}
			}
			if(arg0.getButton()==1){
				if(mapa.mapa[my][mx]==0) {
					caminho = null;
					long timeini = System.nanoTime();

					// TODO Executa Algoritmo
					System.out.println(""+my+" "+mx);
					
					int hx = (int)(meuHeroi.X/16);
					int hy = (int)(meuHeroi.Y/16);
					
					listadenodos.clear();
					listadenodosvisitados.clear();
					
					caminho = buscaEmLargura(hx,hy,mx,my);
					
					meuHeroi.caminho = caminho;
					meuHeroi.idexcaminho = 0;
					//mapa.mapa[my][mx]

					long timefin = System.nanoTime() - timeini;
					System.out.println("Tempo Final: "+timefin/1000000.0f);
				}else {
					System.out.println("Caminho Final Bloqueado");
				}
			}
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	
	addMouseWheelListener(new MouseWheelListener() {
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			//System.out.println("w "+e.getWheelRotation());
			if(e.getWheelRotation()>0) {
				zoom= zoom*1.1f;
			}else if(e.getWheelRotation()<0) {
				zoom= zoom*0.90f;
			}
			
			ntileW = (int)((960/zoom)/16)+1;
			ntileH = (int)((800/zoom)/16)+1;
			
			if(ntileW>=1000) {
				ntileW = 1000;
			}
			if(ntileH>=1000) {
				ntileH = 1000;
			}
			mapa.NumeroTilesX = ntileW;
			mapa.NumeroTilesY = ntileH;
		}
	});
	
	
//	try {
//		imagemcharsets = ImageIO.read( getClass().getResource("Chara1.png") );
//	}
//	catch(IOException e) {
//		System.out.println("Load Image error:");
//	}
	
	
//	for(int i = 0; i < 20; i++){
//		Color cor = Color.black;
//		
//		switch (rnd.nextInt(4)) {
//		case 0:
//			cor = Color.red;
//			break;
//		case 1:
//			cor = Color.BLUE;
//			break;
//		case 2:
//			cor = Color.green;
//			break;
//
//			
//		default:
//			break;
//		}
//		
//		
//		listadeagentes.add(new MeuAgente(10+rnd.nextInt(780), 10+rnd.nextInt(480), cor));		
//	}

	meuHeroi = new MeuAgente(10, 10, Color.red);
	
	listadeagentes.add(meuHeroi);
	
	mousex = mousey = 0;
	
	mapa = new Mapa_Grid(100,100,ntileW, ntileH);
	mapa.loadmapfromimage("/imagemlabirinto1000.png");
	
} // end of GamePanel()

LinkedList<Nodo> listadenodos = new LinkedList<Nodo>();
HashMap<Integer,Nodo> listadenodosvisitados = new HashMap<Integer,Nodo>();
public int[] buscaEmLargura(int ix,int iy,int ox,int oy) {

	listadenodos.add(new Nodo(ix, iy,null));
	
	boolean achouObjetivo = false;
	int nodosabertos = 0;
	do {
		Nodo n = listadenodos.remove();
		synchronized(listadenodosvisitados) {
			listadenodosvisitados.put(n.X+(n.Y*mapa.Largura),n);
		}
		if(addnodovizinhos(n,n.X+1,n.Y,ox,oy)){
			achouObjetivo = true;
			continue;
		}
		if(addnodovizinhos(n,n.X,n.Y+1,ox,oy)){
			achouObjetivo = true;
			continue;
		}
		if(addnodovizinhos(n,n.X-1,n.Y,ox,oy)){
			achouObjetivo = true;
			continue;
		}
		if(addnodovizinhos(n,n.X,n.Y-1,ox,oy)){
			achouObjetivo = true;
			continue;
		}
		nodosabertos++;
		if(nodosabertos%10000==0) {
			System.out.println("nodosabertos "+nodosabertos+" Lista de Nodos abertos "+listadenodos.size()+" "+listadenodosvisitados.size());
		}
	}while(achouObjetivo==false);
	
	Nodo f = listadenodos.getLast();
	
	LinkedList<Nodo> caminho = new LinkedList<Nodo>();
	caminho.add(f);
	while(f.pai!=null) {
		f = f.pai;
		caminho.add(f);
	}
	
	int path[] = new int[caminho.size()*2];
	int index = caminho.size()*2;
	
	for (Iterator iterator = caminho.iterator(); iterator.hasNext();) {
		Nodo nodo = (Nodo) iterator.next();
		path[index-1] = nodo.Y;
		path[index-2] = nodo.X;
		index-=2;
	}
	
	
	System.out.println("Nodos Abertos: "+nodosabertos+" Nodos na Lista: "+listadenodos.size());
	
	return path;
}

public boolean addnodovizinhos(Nodo n,int x,int y,int ox,int oy){
	if(x>=mapa.Altura) {
		return false;
	}
	if(y>=mapa.Largura) {
		return false;
	}
	if(x<0) {
		return false;
	}
	if(y<0) {
		return false;
	}
	if(mapa.mapa[y][x]==1) {

		return false;
	}
//	for (Iterator iterator = listadenodosvisitados.iterator(); iterator.hasNext();) {
//		Nodo nv = (Nodo) iterator.next();
//		if(x==nv.X && y==nv.Y) {
//			return false;
//		}
//	}
	
	if(listadenodosvisitados.containsKey(x+(y*mapa.Largura))) {
		return false;
	}
	
	listadenodos.add(new Nodo(x,y,n));
	synchronized(listadenodosvisitados) {
		listadenodosvisitados.put(x+(y*mapa.Largura),n);
	}
	if(x==ox && y==oy) {
		return true;
	}
	return false;
}

public void startGame()
// initialise and start the thread
{
	if (animator == null || !running) {
		animator = new Thread(this);
		animator.start();
	}
} // end of startGame()

public void stopGame()
// called by the user to stop execution
{ running = false; }


public void run()
/* Repeatedly update, render, sleep */
{
	running = true;
	
	long DifTime,TempoAnterior;
	
	int segundo = 0;
	DifTime = 0;
	TempoAnterior = System.currentTimeMillis();
	
	this.createBufferStrategy(2);
	BufferStrategy strategy = this.getBufferStrategy();
	
	while(running) {
	
		gameUpdate(DifTime); // game state is updated
		Graphics g = strategy.getDrawGraphics();
		gameRender((Graphics2D)g); // render to a buffer
		strategy.show();
	
		try {
			Thread.sleep(0); // sleep a bit
		}	
		catch(InterruptedException ex){}
		
		DifTime = System.currentTimeMillis() - TempoAnterior;
		TempoAnterior = System.currentTimeMillis();
		
		if(segundo!=((int)(TempoAnterior/1000))){
			FPS = SFPS;
			SFPS = 1;
			segundo = ((int)(TempoAnterior/1000));
		}else{
			SFPS++;
		}
	
	}
System.exit(0); // so enclosing JFrame/JApplet exits
} // end of run()

int timerfps = 0;
private void gameUpdate(long DiffTime)
{ 
	
	if(LEFT){
		posx-=1000*DiffTime/1000.0;
	}
	if(RIGHT){
		posx+=1000*DiffTime/1000.0;
	}	
	if(UP){
		posy-=1000*DiffTime/1000.0;
	}
	if(DOWN){
		posy+=1000*DiffTime/1000.0;
	}
	
	if(posx>mapa.Largura*16) {
		posx=mapa.Largura*16;
	}
	if(posy>mapa.Altura*16) {
		posy=mapa.Altura*16;
	}
	if(posx<0) {
		posx=0;
	}
	if(posy<0) {
		posy=0;
	}
	
	mapa.Posiciona((int)posx,(int)posy);
	
	for(int i = 0;i < listadeagentes.size();i++){
		  listadeagentes.get(i).SimulaSe((int)DiffTime);
	}
}

private void gameRender(Graphics2D dbg)
// draw the current frame to an image buffer
{
	// clear the background
	dbg.setColor(Color.white);
	dbg.fillRect (0, 0, PWIDTH, PHEIGHT);

	AffineTransform trans = dbg.getTransform();
	dbg.scale(zoom, zoom);
	
	try {
		mapa.DesenhaSe(dbg);
	}catch (Exception e) {
		System.out.println("Erro ao desenhar mapa");
	}
	

	

	/*
	if(listadenodosvisitados.size()>0) {
		synchronized(listadenodosvisitados) {
			for (Iterator iterator = listadenodosvisitados.keySet().iterator(); iterator.hasNext();) {
				Integer key = (Integer) iterator.next();
				Nodo nv = listadenodosvisitados.get(key);
				dbg.setColor(Color.blue);
				dbg.fillRect(nv.X*16-mapa.MapX, nv.Y*16-mapa.MapY, 16, 16);
			}
		}
	}
	*/
	if(caminho!=null){
		
		try {
			if(caminho!=null){
				for(int i = 0; i < caminho.length/2;i++){
					int nx = caminho[i*2];
					int ny = caminho[i*2+1];
					
					dbg.setColor(Color.green);
					dbg.fillRect(nx*16-mapa.MapX, ny*16-mapa.MapY, 16, 16);
				}
			}
		}catch (Exception e) {
		}
	}
	
	for(int i = 0;i < listadeagentes.size();i++){
	  listadeagentes.get(i).DesenhaSe(dbg, mapa.MapX, mapa.MapY);
	}
	
	dbg.setTransform(trans);
	
	dbg.setColor(Color.red);	
	dbg.drawString("FPS: "+FPS, 10, 10);	
	
	//System.out.println("left "+LEFT);
		
}

}

