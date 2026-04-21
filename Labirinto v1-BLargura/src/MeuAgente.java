import java.awt.Color;
import java.awt.Graphics2D;


public class MeuAgente extends Agente {
	
	Color color;
	double vel = 40;
	double  ang  = 0;
	
	int estado = 0;
	
	double oldx = 0;
	double oldy = 0;
	
	int timeria = 0;
	
	boolean colidiu = false;
	
	int caminho[] = null;
	int idexcaminho = 0;
	
	public MeuAgente(int x,int y, Color color) {
		// TODO Auto-generated constructor stub
		X = x;
		Y = y;
		
		this.color = color;
	}
	
	@Override
	public void SimulaSe(int DiffTime) {
		// TODO Auto-generated method stub
		timeria+=DiffTime;
		
		oldx = X;
		oldy = Y;
		
		if(timeria>10){
			calculaIA(DiffTime);
			timeria = 0;
		}
		
		X+=Math.cos(ang)*vel*DiffTime/1000.0;
		Y+=Math.sin(ang)*vel*DiffTime/1000.0;
		
		for(int i = 0; i < GamePanel.listadeagentes.size();i++){
		    Agente agente = GamePanel.listadeagentes.get(i);
		    
		    if(agente!=this){
			    
			    double dax = agente.X - X;
			    double day = agente.Y - Y;
			    
			    double dista = dax*dax + day*day;
			    
			    if(dista<400){
			    	X = oldx;
			    	Y = oldy;
			    	
			    	colidiu = true;
			    	
			    	break;
			    }
		    }
		}
	}

	@Override
	public void DesenhaSe(Graphics2D dbg, int XMundo, int YMundo) {
		// TODO Auto-generated method stub
		dbg.setColor(color);
		
		dbg.drawOval((int)(X-10)-XMundo, (int)(Y-10)-YMundo, 20, 20);
		
		double linefx = X + 10*Math.cos(ang);
		double linefy = Y + 10*Math.sin(ang);dbg.drawLine((int)X-XMundo,(int)Y-YMundo, (int)linefx-XMundo, (int)linefy-YMundo);
	
	}

	public void calculaIA(int DiffTime){
		
		if(caminho!=null) {
			int nx = caminho[idexcaminho]*16+8;
			int ny = caminho[idexcaminho+1]*16+8;
			
			double dx = nx-X;
			double dy = ny-Y;
			
			//System.out.println("nx "+nx+"ny "+ny);
			
			double ang2 = Math.atan2(dy, dx);
			
			ang = ang2;
			double d = dx*dx+dy*dy;
			//System.out.println("Rodaia "+ang2+" "+d);
			vel = 100;
			
			
			if(d<8) {
				idexcaminho+=2;
				//System.out.println("idexcaminho "+idexcaminho);
				if(idexcaminho>=caminho.length) {
					idexcaminho = 0;
					vel = 0;
					caminho = null;
				}
			}
		}else {
			vel = 0;
		}
		
	}
	
}
