import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClienteImpl extends UnicastRemoteObject implements ClienteInter {
	private static final long serialVersionUID = 1L;
	private static Scanner teclado = new Scanner(System.in);
	private static ServidorInter servidor;
	private static String nome;
	private String[][] tabuleiro;
	private String[][] pAvioes 		= new String[1][5];
	private String[][] submarino 	= new String[1][1];
	private String[][] hAvioes 		= new String[2][3];
	private String[][] destroyer 	= new String[1][4];
	private String[][] cruzador 	= new String[1][3];
	
	protected ClienteImpl() throws RemoteException {
		super();
		for(int i=0; i<pAvioes[0].length; i++)
			pAvioes[0][i] = ("pAviao");
		submarino[0][0] = ("Submar");
		for(int i=0; i<hAvioes.length; i++)
			for(int j=0; j<hAvioes[0].length; j++)
				if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)) 
					hAvioes[i][j] = ("hAviao");
				else
					hAvioes[i][j] = ("~~~~~~");
		for(int i=0; i<destroyer[0].length; i++)
			destroyer[0][i] = ("Destro");
		for(int i=0; i<cruzador[0].length; i++)
			cruzador[0][i] = ("Cruzad");
	}
	
	public static void entrar(ClienteInter jogador) throws RemoteException, MalformedURLException, NotBoundException {
		servidor = (ServidorInter) Naming.lookup("//localhost/batalha");
		System.out.print("Nome: ");
		ClienteImpl.setNome(teclado.nextLine());
		if(servidor.getConexoes()==0) {
			servidor.conectar(jogador);
			System.out.print("Tamanho do Tabuleiro: ");
			servidor.setTamanho(teclado.nextInt());
			System.out.println("Aguardando jogador...");
		}else if(servidor.getConexoes()==1)
			servidor.conectar(jogador);
		else
			System.out.println("Jogo cheio!");
	}
	
	@Override
	public void gerarTabuleiro(int tamanho) throws RemoteException {
		tabuleiro = new String[tamanho][tamanho];
		for(int i=0; i<tamanho; i++)
			for(int j=0; j<tamanho; j++)
				tabuleiro[i][j] = ("~~~~~~");
		System.out.println("Jogo iniciado!");
		posicionar(pAvioes, 2, 2);
		posicionar(submarino, 3, 3);
		posicionar(hAvioes, 5, 5);
		posicionar(destroyer, 7, 7);
		posicionar(cruzador, 7, 2);
		mostrarTabuleiro();
	}
	
	public String posicionar(String[][] tipo, int lI, int cI) { //tipo de embarcação, linha inicial e coluna inicial para inserção
		int tipol = tipo.length; //pega nº de linhas da embarcação
		int tipoc = tipo[0].length; //pega nº de colunas da embarcação
		int tamT = tabuleiro.length; //pega tamanho do tabuleiro
		if(((lI-1)+tipol < tamT || (lI-1)+tipol == tamT) && ((cI-1)+tipoc < tamT || (cI-1)+tipoc == tamT)) //teste para não exceder o tamanho do tabuleiro
			for(int l=lI-1, i=0; i<tipol; l++, i++)
				for(int c=cI-1, j=0; j<tipoc; c++, j++)
					if(!tabuleiro[l][c].equals("~~~~~~"))
						return "Posição já ocupada!";
					else
						for(l=lI-1, i=0; i<tipol; l++, i++)
							for(c=cI-1, j=0; j<tipoc; c++, j++)
								tabuleiro[l][c] = tipo[i][j];
		else
			return "Posição inválida!";
		return "Embarcação inserida!";
	}
	
	public void mostrarTabuleiro() throws RemoteException {
		for(int i=0; i<tabuleiro.length; i++){
			System.out.println("");
			for(int j=0; j<tabuleiro.length; j++)
				System.out.print(tabuleiro[i][j] +" ");
		}
	}
	
	@Override
	public void atacar() throws RemoteException {
		System.out.print("\nAtacar\nLinha: ");
		int l = teclado.nextInt();
		System.out.print("Coluna: ");
		int c = teclado.nextInt();
		servidor.atacar(ClienteImpl.this, l, c);
	}
	
	@Override
	public void ataque(int l, int c) throws RemoteException {
		if(!tabuleiro[l][c].contains("~~~~~~")) {
			String embarcacao = tabuleiro[l][c];
			tabuleiro[l][c] = "~~~~~~";
			System.out.println(embarcacao+ " atingido!");
			
			
			//for(int i=0; i<tabuleiro.length; i++)
			//	for(int j=0; j<tabuleiro.length; j++)
			//		if(!tabuleiro[i][j].contains("~~~~~~"))
			//			return "Atingiu a embarcação " +embarcacao;
			//System.out.println("\n\nVocê perdeu!\nFim de Jogo!");
			//return "Atingiu a embarcação " +embarcacao+ "\n\nVocê venceu!\n" +servidor.fimDeJogo(ClienteImpl.this);
		}
		//return "Atingiu a água";
	}
	
	@Override
	public ClienteInter vencedor() throws RemoteException {
		for(int i=0; i<tabuleiro.length; i++)
			for(int j=0; j<tabuleiro.length; j++)
				if(!tabuleiro[i][j].contains("~~~~~~"))
					return null;
		return ClienteImpl.this;
	}

	@Override
	public String getNome() throws RemoteException {
		return nome;
	}

	public static void setNome(String nome) {
		ClienteImpl.nome = nome;
	}
}
