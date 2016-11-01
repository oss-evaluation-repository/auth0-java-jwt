package com.auth0.jwtdecodejava.algorithms;

import com.auth0.jwtdecodejava.exceptions.SignatureVerificationException;
import org.apache.commons.codec.binary.Base64;

import java.security.*;

class RSAAlgorithm extends Algorithm {

    private final PublicKey publicKey;

    RSAAlgorithm(String id, String algorithm, PublicKey publicKey) {
        super(id, algorithm);
        if (publicKey == null) {
            throw new IllegalArgumentException("The PublicKey cannot be null");
        }
        this.publicKey = publicKey;
    }

    PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public void verify(String[] jwtParts) throws SignatureVerificationException {
        try {
            String content = String.format("%s.%s", jwtParts[0], jwtParts[1]);
            Signature s = Signature.getInstance(getDescription());
            s.initVerify(publicKey);
            s.update(content.getBytes());
            boolean valid = s.verify(Base64.decodeBase64(jwtParts[2]));
            if (!valid) {
                throw new SignatureVerificationException(this);
            }
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new SignatureVerificationException(this, e);
        }
    }
}
