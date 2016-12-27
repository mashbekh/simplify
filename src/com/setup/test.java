package com.setup;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

@Path("/hello")
public class test {
	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public String test()
	{
		String abc="Hello World!";
		return abc;
		
	}
	
	
	@GET
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON)
	public String generate_token() throws JoseException
	{
		Long userid= (long) 4;
		RsaJsonWebKey rsaJsonWebKey =  RsaJwkGenerator.generateJwk(2048);
	    String key_id   = "Key" + userid;
	    // Give the JWK a Key ID (kid), which is just the polite thing to do
	    rsaJsonWebKey.setKeyId(key_id);

	    // Create the Claims, which will be the content of the JWT
	    JwtClaims claims = new JwtClaims();
	    //claims.setIssuer("Issuer");  // who creates the token and signs it
	    //claims.setAudience("Audience"); // to whom the token is intended to be sent
	    claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
	    claims.setGeneratedJwtId(); // a unique identifier for the token
	    claims.setIssuedAtToNow();  // when the token was issued/created (now)
	   
	    claims.setSubject("subject"); // the subject/principal is whom the token is about
	    
	    
	    JsonWebSignature jws = new JsonWebSignature();
	    jws.setPayload(claims.toJson());
	    jws.setKey(rsaJsonWebKey.getPrivateKey());
	    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
	    String jwt = jws.getCompactSerialization();
	    //System.out.println(rsaJsonWebKey.toJson());
	    //System.out.println("JWT: " + jwt);
		
	    return jwt;
	}

}
