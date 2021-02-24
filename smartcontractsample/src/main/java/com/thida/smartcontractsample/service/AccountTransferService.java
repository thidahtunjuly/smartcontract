package com.thida.smartcontractsample.service;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.Base64;
import java.util.List;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.owlike.genson.Genson;
import com.thida.smartcontractsample.dto.Account;
import com.thida.smartcontractsample.utility.Asymmetric;

@Service("AccountTransferService")
public class AccountTransferService {
	private static final Logger logger = LoggerFactory.getLogger(AccountTransferService.class);
	private static final String error="ERROR";
	private static final String success="SUCCESS";
	
	/*
	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}
	*/

	// helper function for getting connected to the gateway
	public Gateway connect() throws Exception{
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
		
		public String initAccount() {
			// connect to the network and invoke the smart contract
			try (Gateway gateway = connect()) {

				// get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("accounttransfer");

				contract.submitTransaction("initAccount");
				return success;
			}
			catch(Exception e){
				e.printStackTrace();
				return error;
			}
		}

		public String getAllAccounts() {
				// connect to the network and invoke the smart contract
				try (Gateway gateway = connect()) {

					// get the network and contract
					Network network = gateway.getNetwork("mychannel");
					Contract contract = network.getContract("accounttransfer");

					byte[] result;

					result = contract.evaluateTransaction("getAllAccounts");
					System.out.println("getAllAccounts, result: " + new String(result));
					logger.info("getAllAccounts, result: " + new String(result));
					
					
					return new String(result);
				}
				catch(Exception e){
					e.printStackTrace();
					return error;
				}
		}
	
		public String getAccount(String accountID,String id,long timeStamp) {
			// connect to the network and invoke the smart contract
			try (Gateway gateway = connect()) {
				
				// id+timestamp+param1+val1+param2+val2+...+paramN+valN+key
				StringBuilder builder=new StringBuilder();
				builder.append(id);
				builder.append(String.valueOf(timeStamp));
				builder.append("accountID"+accountID);
				String publicKey=Asymmetric.getPublicKey();
				builder.append(publicKey);
				
				
				String plainText=builder.toString();
				KeyPair pair = Asymmetric.generateKeyPair();
				String signature=Asymmetric.sign(plainText, pair.getPrivate());
				
				// get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("accounttransfer");

				byte[] result;

				result = contract.evaluateTransaction("getAccount", accountID,signature);
				System.out.println("getAccount, result: " + new String(result));
				logger.info("getAccount, result: " + new String(result));
				
				return new String(result);
			}
			catch(Exception e){
				
				e.printStackTrace();
				return error;
			}
	}
		
		public String send(String fromAcctID,String toAcctID,double transferAmount,String id,long timeStamp) {
			
			// connect to the network and invoke the smart contract
			try (Gateway gateway = connect()) {
				
				// id+timestamp+param1+val1+param2+val2+...+paramN+valN+key
				StringBuilder builder=new StringBuilder();
				builder.append(id);
				builder.append(String.valueOf(timeStamp));
				builder.append("fromAcctID"+fromAcctID);
				builder.append("toAcctID"+toAcctID);
				builder.append("transferAmount"+transferAmount);
				String publicKey=Asymmetric.getPublicKey();
				builder.append(publicKey);
				
				
				String plainText=builder.toString();
				KeyPair pair = Asymmetric.generateKeyPair();
				String signature=Asymmetric.sign(plainText, pair.getPrivate());

				// get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("accounttransfer");

				byte[] result;

				contract.submitTransaction("send", fromAcctID, toAcctID,String.valueOf(transferAmount),signature);
				
				result = contract.evaluateTransaction("getAllAccounts");
				
				System.out.println("After calling Send->getAllAccounts, result: " + new String(result));
				logger.info("After calling Send->getAllAccounts, result: " + new String(result));
				
				return new String(result);
			}
			catch(Exception e){
				e.printStackTrace();
				return error;
			}
		}
		
}
