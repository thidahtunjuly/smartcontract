package com.thida.smartcontractsample.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.thida.smartcontractsample.dto.Account;
import com.thida.smartcontractsample.service.AccountTransferService;


@Controller
@RequestMapping
public class AccountTransferController {
	// private static final Logger logger = LoggerFactory.getLogger(AccountTransferController.class);
	 private static final String error="ERROR";
	 private Genson genson = new Genson();
	
	@Autowired
	AccountTransferService accountTransferService;
	
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String showWelcome() {
        
        return "welcome";
    }
	
	/*
	 // changed to be able to return view
	@GetMapping("/init")
	public ResponseEntity<String> initAccount() {
		String result=accountTransferService.initAccount();
		if(result.equals("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.OK).body(result);
		}
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
		}
			
	}
	
	@GetMapping("/getAllAccounts")
	public ResponseEntity<String> getAllAccounts() {
		String result=accountTransferService.getAllAccounts();
		if(result!=null && !result.equals(error)) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);
		}
		else
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
		
	}
	
	@PostMapping("/getAccount")
	public ResponseEntity<String> getAccount(@RequestParam String accountID,@RequestHeader Map<String,String> headers) {
		
		String requestId=headers.get("X-Request-ID");
		long timeStamp=System.currentTimeMillis();
		
		String result=accountTransferService.getAccount(accountID,requestId,timeStamp);
		if(result!=null && !result.equals(error)) {
			
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);
			
		}
		else
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
	}
	
	@PostMapping("/send")
	public ResponseEntity<String> send(@RequestParam String fromAcctID,@RequestParam String toAcctID,@RequestParam double transferAmount,@RequestHeader Map<String,String> headers) {
		
		String requestId=headers.get("X-Request-ID");
		long timeStamp=System.currentTimeMillis();
		
		String result=accountTransferService.send(fromAcctID, toAcctID, transferAmount,requestId,timeStamp);
		if(result!=null && !result.equals(error)) {
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(result);
		}
		else
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
	}
	
	 */
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String initAccount(ModelMap model) {
		String result=accountTransferService.initAccount();
		if(result.equals("SUCCESS")) {
			String data=accountTransferService.getAllAccounts();
			if(result!=null && !result.equals(error)) {
				List<Account> accounts=genson.deserialize(data, List.class);
				model.put("accounts", accounts);
				return "transfer";
			}
			else {
				model.put("error", "Something is wrong with getAllAccounts");
				return "transfer";
			}
			
		}
		else {
			model.put("error", "Something is wrong with initAccount");
			return "transfer";
		}
			
	}
	
	@SuppressWarnings("unchecked")
	
	@RequestMapping(value = "/getAllAccounts", method = RequestMethod.GET)
	public String getAllAccounts(ModelMap model) {
		String result=accountTransferService.getAllAccounts();
		if(result!=null && !result.equals(error)) {
			List<Account> accounts=genson.deserialize(result, List.class);
			model.put("accounts", accounts);
			return "transfer";
		}
		else {
			model.put("error", "Something is wrong with getAllAccounts");
			return "transfer";
		}
		
	}
	

	@RequestMapping(value = "/getAccountForm", method = RequestMethod.GET)
	public String getAccountForm(@RequestHeader Map<String,String> headers,ModelMap model) {
		
		return "getbalance";
		
	}
	 
	
	@RequestMapping(value = "/getAccount", method = RequestMethod.POST)
	public String getAccount(@RequestParam String accountID,@RequestHeader Map<String,String> headers,ModelMap model) {
		
		String requestId=headers.get("X-Request-ID");
		long timeStamp=System.currentTimeMillis();
		
		String result=accountTransferService.getAccount(accountID,requestId,timeStamp);
		if(result!=null && !result.equals(error)) {
			Account account=genson.deserialize(result, Account.class);
			model.put("account", account);
			return "getbalance";
		}
		else {
			model.put("error", "Something is wrong with getAccount");
			return "getbalance";
		}
			
	}
	
	@RequestMapping(value = "/getTransferForm", method = RequestMethod.GET)
	public String getTransferForm(@RequestHeader Map<String,String> headers,ModelMap model) {
		
		return "transfer";
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(@RequestParam String fromAcctID,@RequestParam String toAcctID,@RequestParam double transferAmount,@RequestHeader Map<String,String> headers,ModelMap model) {
		
		String requestId=headers.get("X-Request-ID");
		long timeStamp=System.currentTimeMillis();
		
		String result=accountTransferService.send(fromAcctID, toAcctID, transferAmount,requestId,timeStamp);
		if(result!=null && !result.equals(error)) {
			
			List<Account> accounts=genson.deserialize(result, List.class);
			model.put("accounts", accounts);
			return "transfer";
		}
		else{
			model.put("error", "Something is wrong with getAllAccounts");
			return "transfer";
		}
			
	}
	
}
	
	

