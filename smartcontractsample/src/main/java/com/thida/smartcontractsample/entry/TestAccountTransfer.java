package com.thida.smartcontractsample.entry;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;


public class TestAccountTransfer {

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	// helper function for getting connected to the gateway
	public static Gateway connect() throws Exception{
		// Load a file system based wallet for managing identities.
		/*
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		//Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
		Path networkConfigPath = Paths.get("test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
		return builder.connect();
		*/
		
		// Load an existing wallet holding identities used to access the network.
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("connection.json");

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "user1")
                .networkConfig(networkConfigFile);
        
        return builder.connect();
		
	}

	public static void main(String[] args) throws Exception {
		/* enrolls the admin and registers the user
		try {
			EnrollAdmin.main(null);
			RegisterUser.main(null);
		} catch (Exception e) {
			System.err.println(e);
		}
		*/
		
		
		// connect to the network and invoke the smart contract
		try (Gateway gateway = connect()) {

			// get the network and contract
			Network network = gateway.getNetwork("mychannel");
			Contract contract = network.getContract("accounttransfer");

			byte[] result;

			contract.submitTransaction("initAccount");

			System.out.println("\n");
			result = contract.evaluateTransaction("getAllAccounts");
			System.out.println("Evaluate Transaction: GetAllAssets, result: " + new String(result));

			System.out.println("\n");
			result = contract.evaluateTransaction("getAccount", "111");
			System.out.println("calling getAccount: " + new String(result));

			System.out.println("\n");
			contract.submitTransaction("send", "111", "112","5000");
			
			System.out.println("\n");
			result = contract.evaluateTransaction("getAllAccounts");
			System.out.println("After calling Send, result: " + new String(result));

		}
		catch(Exception e){
			System.err.println(e);
		}

	}
}
