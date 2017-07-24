package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import common.CommandType;
import common.Level;
import model.data.MyTextLevelLoader;
import model.data.Player_solved_level;
import model.sokobanSolver.SokobanSolver;

public class SokobanClientHandler extends ClientHandler {
	
	String action = "";
	
	public SokobanClientHandler(Socket connection_socket) {
		super(connection_socket);
	}

	@Override
	public void handleClient(){
		try{
		
		if(this.client_connection == null){
			disconnectUser();
			return;
		}
			
		BufferedReader in = new BufferedReader(new InputStreamReader(client_connection.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client_connection.getOutputStream()));

		//TODO actual interpreting of commands
		String cmd = in.readLine();
		if(cmd.equals("solve")){
			String lvl = in.readLine();
			lvl = lvl.replace('k', '\n');
			Level l = (new MyTextLevelLoader()).getLevelFromText(lvl);
			String sol = (new SokobanSolver()).solveLevel(l) +"\n";
			System.out.println(sol);
			out.write(sol);
			out.flush();
		}
		else if(cmd.equals("store")){
			//save a user highscore to the db via the web service
			String lvl_name = in.readLine();
			String plyr_name = in.readLine();
			System.out.println(lvl_name+ "," + plyr_name);
			
			int steps = Integer.parseInt(in.readLine());
			int time = Integer.parseInt(in.readLine());
			String solution = in.readLine();
			
			String url = "http://localhost:8080/SokoDBServer/webapi/registerHighscore";
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(url);
			Player_solved_level psl = new Player_solved_level(steps,time,lvl_name,plyr_name,solution);

			////////////////////////////
			System.out.println(psl);
			
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.post(Entity.json(psl));

			if (response.getStatus() == 204) {
				System.out.println("highscore added successfully");
			} else {
				System.out.println(response.getHeaderString("errorResponse"));
			}
		}
		else if(cmd.equals("get_scores")){
			//send the user the highscores table entries
			
			String url = "http://localhost:8080/SokoDBServer/webapi/";
			String order = in.readLine();
			LinkedList<String> specs = new LinkedList<>();
			String spec = in.readLine();
			while(!spec.equals("stop")){
				specs.add(spec);
				spec = in.readLine();
			}
			System.out.println(specs);
			spec = specs.removeFirst();
			if(spec.toLowerCase().contains("level"))
				url = "http://localhost:8080/SokoDBServer/webapi/level_records/" + specs.removeFirst();
			else if(spec.toLowerCase().contains("player"))
				url = "http://localhost:8080/SokoDBServer/webapi/player_records/" + specs.removeFirst();
			url+="?order="+order;
			if(specs.size() != 0)
				url+="&search=" + specs.get(1);
			
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(url);
			Response response = webTarget.request(MediaType.APPLICATION_JSON).get(Response.class);
			if (response.getStatus() == 200) {
				List<Player_solved_level> psls = response.readEntity(new GenericType<List<Player_solved_level>>() {
				});
				for (Player_solved_level psl : psls) {
					System.out.println(psl);
					out.write(psl.toString()+"\n");
				}
				out.write("stop"+"\n");
				out.flush();
			} else {
				System.out.println(response.getHeaderString("errorResponse"));
			}
			
		}
		
		} catch (IOException e) { System.out.println(e.getMessage()); } 
		
		finally {
			disconnectUser();
		}
	}

	@Override
	public void disconnectUser() {
		if(this.client_connection != null)
			try { this.client_connection.close(); } 
			catch (IOException e) {	e.printStackTrace(); }
		System.out.println(">>client " + this.client_connection.getInetAddress()+":"+client_connection.getPort()+ " disconnected from the server");
		setChanged();
		notifyObservers(CommandType.USER_DISCONNECT);
	}	
	
	@Override
	public String toString() {
	
		return client_connection.getInetAddress().toString() + ":" + client_connection.getPort() + " " + action;
	}
}
