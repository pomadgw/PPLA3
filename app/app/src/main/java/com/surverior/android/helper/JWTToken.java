package com.surverior.android.helper;

import android.util.Base64;
import android.util.Log;

import com.surverior.android.app.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * Created by rahadian.yusuf on 28/03/16.
 */
public class JWTToken {
    private String type;
    private String alg;
    private String issuer;
    private String subject;
    private int issueAt;
    private int expire;
    private int notBefore;
    private String jwtID;
    private String token;

    private static Map<String, String> algorithms;

    static {
        algorithms = new HashMap<String,String>();
        algorithms.put("HS256","HmacSHA256");
        algorithms.put("HS384","HmacSHA384");
        algorithms.put("HS512", "HmacSHA512");
    }

    public JWTToken(String token) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.token = token;

        init();
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getAlg() {
        return alg;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getSubject() {
        return subject;
    }

    public int getIssueAt() {
        return issueAt;
    }

    public int getExpire() {
        return expire;
    }

    public int getNotBefore() {
        return notBefore;
    }

    public String getJwtID() {
        return jwtID;
    }

    private void init() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String[] clips = token.split("\\.");

        String jsonHeader = new String(Base64.decode(clips[0], Base64.URL_SAFE));
        String jsonContent = new String(Base64.decode(clips[1], Base64.URL_SAFE));
        byte[] signature = Base64.decode(clips[2], Base64.URL_SAFE);

        try {
            JSONObject jObjHeader = new JSONObject(jsonHeader);
            JSONObject jObjContent = new JSONObject(jsonContent);

            System.out.println(jObjContent);
            type = (String)jObjHeader.get("typ");
            alg = (String)jObjHeader.get("alg");

            issuer = (String) jObjContent.get("iss");
            subject = (String) jObjContent.get("sub").toString();
            expire = jObjContent.has("exp") ? (int) jObjContent.get("exp") : 0;
            issueAt = (int) jObjContent.get("iat");
            notBefore = (int) jObjContent.get("nbf");
            jwtID = (String) jObjContent.get("jti");

            Mac verifier = Mac.getInstance(algorithms.get(alg));
            SecretKeySpec signingKey = new SecretKeySpec(AppConfig.JWT_SECRET.getBytes(), algorithms.get(alg));
            verifier.init(signingKey);

            String dataToBeSigned = clips[0] + "." + clips[1];
            byte[] data = verifier.doFinal(dataToBeSigned.getBytes());

            if (!MessageDigest.isEqual(signature, data)) {
                throw new SignatureException("Signature failed");
            }

        } catch(JSONException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

}