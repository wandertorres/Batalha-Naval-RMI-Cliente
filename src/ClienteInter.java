import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteInter extends Remote {
	public void gerarTabuleiro(int tamanho) throws RemoteException;
	
	public void ataque(int l, int c) throws RemoteException;

	public void atacar() throws RemoteException;

	ClienteInter vencedor() throws RemoteException;

	String getNome() throws RemoteException;
}
