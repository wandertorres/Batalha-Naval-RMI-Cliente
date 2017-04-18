import java.io.IOException;
import java.rmi.NotBoundException;

public class Cliente {
	public static void main(String[] args) throws IOException, NotBoundException {
		ClienteInter jogador = (ClienteInter) new ClienteImpl();
		ClienteImpl.entrar(jogador);
		//ClienteImpl.sair(jogador);
	}

}
