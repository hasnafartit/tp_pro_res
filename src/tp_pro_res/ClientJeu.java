package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientJeu extends Application{

	PrintWriter pw;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Client jeu");
		BorderPane borderpane=new BorderPane();
		
		Label labelHost= new Label("Host :");
		TextField textFieldHost=new TextField("localhost");
		Label labelPort= new Label("Port :");
		TextField textFieldPort=new TextField("1234");
		Button buttonJouer=new Button("jouer");
		
		HBox hbox=new HBox(); 
		hbox.setSpacing(12); 
		hbox.setPadding(new Insets(12)); 
		hbox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
		hbox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,buttonJouer);
		borderpane.setTop(hbox); 
		
		VBox vbox=new VBox();
		vbox.setSpacing(15); 
		vbox.setPadding(new Insets(15)); 
		
		ObservableList<String> listModel=FXCollections.observableArrayList(); 

		ListView<String> listView=new ListView<String>(listModel);
		borderpane.setCenter(listView);
		vbox.getChildren().add(listView);
		borderpane.setCenter(vbox);
		
		Label labelMessage = new Label("Devinez le nombre secret:");
		TextField textFieldMessage = new TextField(); textFieldMessage.setPrefSize(200, 30);
		Button buttonEnvoyer = new Button("Envoyer");
		HBox hbox2=new HBox();
		hbox2.setSpacing(15); 
		hbox2.setPadding(new Insets(15));
		hbox2.getChildren().addAll(labelMessage,textFieldMessage,buttonEnvoyer); 
		borderpane.setBottom(hbox2); 

		
		Scene scene = new Scene(borderpane,600,500);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonJouer.setOnAction((evt)->{
			String host=textFieldHost.getText();
			int port = Integer.parseInt(textFieldPort.getText());
			try {
				Socket socket=new Socket(host,port);
				InputStream inputStream=socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(isr);
				pw=new PrintWriter(socket.getOutputStream(),true);
				
				new Thread(()->{
					 
					while(true) {
							try {
								String reponse=bufferedReader.readLine();
								Platform.runLater(()->{
									listModel.add(reponse);
								});
							
							} catch (IOException e) {
									e.printStackTrace();
								}
					}
					
				}).start();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			});
		
		buttonEnvoyer.setOnAction((evt)->{
			String message=textFieldMessage.getText();
			pw.println(message);
	});

}}
