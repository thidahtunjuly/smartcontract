package com.thida.smartcontractsample.chanincode;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;

import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.owlike.genson.Genson;
import com.thida.smartcontractsample.dto.Account;

@Contract(
        name = "accounttransfer",
        info = @Info(
                title = "Account Transfer Contract",
                description = "Hyperledger Fabric Account transfer contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "tdh.transfer@example.com",
                        name = "Thida Transfer",
                        url = "https://hyperledger.example.com")))

@Default
public class AccountTransferContract implements ContractInterface{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountTransferContract.class);
	private Genson genson = new Genson();
	
	private enum AccountTransferErrors {
	        ACCOUNT_NOT_FOUND,
	        ACCOUNT_ALREADY_EXISTS,
	        INSUFFICIENT_AMOUNT
	}
	
		// initialize sample account
	 	@Transaction(intent = Transaction.TYPE.SUBMIT)
	 	public void initAccount(Context ctx) {
	 		
	 		createAccount(ctx,"111",30000);
	 		createAccount(ctx,"112",20000);
	 		createAccount(ctx,"113",50000);
	 		
	    }
	 	
	 	 @Transaction(intent = Transaction.TYPE.SUBMIT)
	     public Account createAccount(Context ctx,String accountID, double amount) {
	         ChaincodeStub stub = ctx.getStub();
	         
	         if (accountExists(ctx, accountID)) {
	             String errorMessage = String.format("Account %s already exists", accountID);
	             logger.error(errorMessage);
	             throw new ChaincodeException(errorMessage,AccountTransferErrors.ACCOUNT_ALREADY_EXISTS.toString());
	            
	         }

	         Account acct=new Account(accountID,amount);
	         String acctJSON=genson.serialize(acct);
	         stub.putStringState(accountID, acctJSON);
	         
	         return acct;
	     }

	 	// retrieve account with accountID
	 	@Transaction(intent = Transaction.TYPE.SUBMIT)
	 	public Account getAccount(Context ctx,String accountID) {
	 		ChaincodeStub stub=ctx.getStub();
	 		String acctState = stub.getStringState(accountID);
	 		if(acctState==null || acctState.isEmpty()) {
	 			String errorMessage = String.format("Account %s does not exists", accountID);
	            logger.error(errorMessage);
	            throw new ChaincodeException(errorMessage,AccountTransferErrors.ACCOUNT_NOT_FOUND.toString());
	        }
	 		
	 		Account account=genson.deserialize(acctState, Account.class);
	 		return account;
		}
	 	
	 	 @Transaction(intent = Transaction.TYPE.EVALUATE)
	     private boolean accountExists(Context ctx,String accountID) {
	         ChaincodeStub stub = ctx.getStub();
	         String acctState = stub.getStringState(accountID);

	         return (acctState != null && !acctState.isEmpty());
	     }
	 	 
	 	 @Transaction(intent = Transaction.TYPE.SUBMIT)
	 	 public void send(Context ctx,String fromAcctID,String toAcctID,double transferAmount) {
	 		 ChaincodeStub stub=ctx.getStub();
	 		 String fromAcctJSON=stub.getStringState(fromAcctID);
	 		 String toAcctJSON=stub.getStringState(toAcctID);
	 		 
	 		 if (!accountExists(ctx, fromAcctID)) {
	             String errorMessage = String.format("From Account %s does not exists", fromAcctID);
	             logger.error(errorMessage);
	             throw new ChaincodeException(errorMessage,AccountTransferErrors.ACCOUNT_NOT_FOUND.toString());
	         }
	 		 if (!accountExists(ctx, toAcctID)) {
	             String errorMessage = String.format("To Account %s does not exists", toAcctID);
	             logger.error(errorMessage);
	             throw new ChaincodeException(errorMessage,AccountTransferErrors.ACCOUNT_NOT_FOUND.toString());
	         }
	 		 
	 		 Account sendAccount=genson.deserialize(fromAcctJSON, Account.class);
	 		 Account receiveAccount=genson.deserialize(toAcctJSON, Account.class);
	 		 
	 		 if(sendAccount.getAmount()<transferAmount) {
	 			 String errorMessage = String.format("Account %s has insufficient amount", fromAcctID);
	             logger.error(errorMessage);
	             throw new ChaincodeException(errorMessage,AccountTransferErrors.INSUFFICIENT_AMOUNT.toString());
	 		 }
	 		 else {
	 			 double newAmtForSender=sendAccount.getAmount()-transferAmount;
	 			 double newAmtForReceiver=receiveAccount.getAmount()+transferAmount;
	 			 
	 			 // update fromAccount
	 			 sendAccount.setAmount(newAmtForSender);
	 			 String newSendAccount = genson.serialize(sendAccount);
	 			 stub.putStringState(fromAcctID, newSendAccount);
	 			 // update toAccount
	 			 receiveAccount.setAmount(newAmtForReceiver);
	 			 String newReceiveAccount=genson.serialize(receiveAccount);
	 			 stub.putStringState(toAcctID, newReceiveAccount);
	 		 }
	 		 
	 	 }
	 	 
	 	// retrieve all accounts for testing
	 	@Transaction(intent = Transaction.TYPE.EVALUATE)
	    public String getAllAccounts(Context ctx) {
	        ChaincodeStub stub = ctx.getStub();

	        List<Account> queryResults = new ArrayList<Account>();
	        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

	        for (KeyValue result: results) {
	            Account account = genson.deserialize(result.getStringValue(), Account.class);
	            queryResults.add(account);
	            System.out.println(account.toString());
	        }

	        final String response = genson.serialize(queryResults);

	        return response;
	    }

	 	 
}
