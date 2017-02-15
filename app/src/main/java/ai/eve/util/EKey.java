package ai.eve.util;

import ai.eve.EApplication;

public class EKey {
	public String rsaPublicKey = "";
    
	public String rsaPrivateKey="";
	
	private volatile static EKey rsakey;
	
	private EKey(){
		rsaPublicKey = EApplication.rsaPublicKey;
		rsaPrivateKey = EApplication.rsaPrivateKey;
	}
	
	public static EKey getInstance(){
		if(rsakey==null){
			synchronized(EKey.class){
				if(rsakey==null){
					rsakey = new EKey();
				}
			}
		}
		return rsakey;
	}
	

	
}
