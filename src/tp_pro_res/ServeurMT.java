package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class ServeurMT extends Thread {
	private boolean isActive=true;  
    private int nombreClient=0;
	public static void main(String[] args) {
		new ServeurMT().start();
		
	}
    
	@Override
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(1234);
		    System.out.println("demarage du serveur ...");
			while(isActive) {
		    Socket socket=serverSocket.accept();	
			++nombreClient;
		    new Conversation(socket,nombreClient).start();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	class Conversation extends Thread {
		private Socket socketClient;
		private int numero;
		Conversation(Socket s,int numero){
			this.socketClient=s; 
			this.numero=numero;
		}
		
		@Override
		public void run() {
			try {
				InputStream is=socketClient.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream os=socketClient.getOutputStream();
				PrintWriter pw= new PrintWriter(os,true);
				String IP=socketClient.getRemoteSocketAddress().toString();
				pw.println("Bien venue, vous etes le client numero "+numero);
				System.out.println("Connexion du client némuro "+numero+" IP "+IP);
				//.pw.println("Bien venue, vous etes le client numero "+numero);
				while(true){
					pw.println("ecrire quelque chose");
					String req=br.readLine();
					String reponse="length = "+req.length();
					System.out.println("Le client "+IP +"a envoye une requete "+req);
					pw.println(reponse);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}

