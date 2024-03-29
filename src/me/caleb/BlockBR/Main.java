package me.caleb.BlockBR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;

import me.caleb.BlockBR.command.PlayerCommands;
import me.caleb.BlockBR.listener.BlockMeta;
import me.caleb.BlockBR.utils.Chat;
import me.caleb.BlockBR.utils.GetInfo;
import me.caleb.BlockBR.utils.Rewards;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	private int port;
	public Connection connection;
	private String host, database, username, password;
	public static Economy economy = null;
	
	@Override
	public void onEnable() {
		
		setupEconomy();
		
		mysqlSetup();
		loadConfig();
		
		new BlockMeta(this);
		new BlockBR(this);
		new PlayerCommands(this);
		new GetInfo(this);
	}
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
    public static Economy getEconomy() {
        return economy;
    }
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void mysqlSetup() {
		
		host = this.getConfig().getString("Host");
		port = this.getConfig().getInt("Port");
		password = this.getConfig().getString("Password");
		database = this.getConfig().getString("Database");
		username = this.getConfig().getString("Username");

		try {     
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		
		if(p instanceof WorldGuardPlugin) return (WorldGuardPlugin) p;
		else return null;
		
	}
	
	@Override
	public void onDisable() {
		
		try {
			if(connection != null && !connection.isClosed()) {
				connection.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	public void openConnection() throws SQLException, ClassNotFoundException {
		
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }
	 
	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        } 
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
	        Bukkit.getConsoleSender().sendMessage(Chat.blockBrChat(("MYSQL CONNECTED!")));
	    }
	    
	}

	
}
