//This Code Connects the code from Javascript Page functionalities to the backend UDP+Database
package com.gamemaster.controller;
import org.springframework.web.bind.annotation.RequestParam;
import com.gamemaster.database.Database;
import com.gamemaster.udp.UdpManager;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")

public class ControllerMaster{

private Database database;
private UdpManager Udp;

public ControllerMaster(Database database, UdpManager Udp)
{

    this.database = database;
    this.Udp = Udp;

}


@PostMapping("/addPlayers")
public String addPlayers(@RequestBody List<String> players) 
{
    boolean added;

    if (players.size() % 2 != 0)
    throw new IllegalArgumentException("Players list must contain pairs of id and name");

    for (int i = 0; i < players.size(); i = i + 2)
        {
        added = this.database.addPlayer(Integer.parseInt(players.get(i)),players.get(i+1));
        try
        {
        Udp.broadcastInt(Integer.parseInt(players.get(i)));
        }
        catch
        (IOException e)
        {
        e.printStackTrace();
        return "Error Transmittinhg ID";
        }
        }
    return "Players Added Succesfully";

}

@PostMapping("/removePlayers")
public int removePlayers(@RequestBody List<String> players)
{

    int remover;
    for(int i = 0; i < players.size(); i++)
    remover = this.database.removePlayers(players.get(i));

    return 0;

}

@PostMapping("/PrintPlayers")
public void PrintPlayers()
{

    this.database.printPlayers();

}

@PostMapping("/setNetworkAddress")
public void setNetworkAddress(String address)
{
    try
    {
    Udp.setNetworkAddress(address);
    System.out.println("Current network address: " + Udp.getNetworkAddress().getHostAddress());

    }
    catch(IOException e)
    {
    e.printStackTrace();
    }

}


}