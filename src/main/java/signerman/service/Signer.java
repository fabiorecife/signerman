package signerman.service;

import org.demoiselle.signer.core.keystore.loader.factory.KeyStoreLoaderFactory;
import org.demoiselle.signer.core.keystore.loader.implementation.FileSystemKeyStoreLoader;
import org.demoiselle.signer.policy.engine.factory.PolicyFactory;
import org.demoiselle.signer.policy.impl.cades.SignerAlgorithmEnum;
import org.demoiselle.signer.policy.impl.cades.factory.PKCS7Factory;
import org.demoiselle.signer.policy.impl.cades.pkcs7.PKCS7Signer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class Signer {
    private Path contentFile;
    private String keyStorePath;
    private String keyStorePassword;
    private KeyStore keyStore;
    FileSystemKeyStoreLoader keyStoreLoader;

    public void setContentFile(Path contentfile) {
        this.contentFile = contentfile;
    }

    public void setKeyStoreParams(String keystorePath, String keyStorePassword) {
        this.keyStorePath = keystorePath;
        this.keyStorePassword = keyStorePassword;
    }

    public Path getContentFile() {
        return this.contentFile;
    }

    public String getKeyStorePath() {
        return this.keyStorePath;
    }

    public String getKeyStorePassword() {
        return this.keyStorePassword;
    }

    public KeyStore getKeyStore() {
        return this.keyStore;
    }

    public void loadKeyStore() {

        keyStoreLoader = (FileSystemKeyStoreLoader) KeyStoreLoaderFactory
                .factoryKeyStoreLoader(Paths.get(keyStorePath).toFile());
        keyStore = keyStoreLoader.getKeyStore(keyStorePassword);

    }

    public  String alias;
    public X509Certificate certificate;
    public PrivateKey privateKey;
    // public  PinHandler pinHandler;
    public  String password;
    public  Certificate[] certificateChain;
    public boolean signsucess;

    public void sign() {
        signsucess = false;
        if (keyStore == null) {
            loadKeyStore();
        }
        try {
            PKCS7Signer signer = PKCS7Factory.getInstance().factoryDefault();
            alias = keyStore.aliases().nextElement();
            certificate = (X509Certificate) keyStore.getCertificate(alias);
            privateKey = (PrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());
            certificateChain = keyStore.getCertificateChain(alias);

            signer.setCertificates(certificateChain);

            signer.setPrivateKey(privateKey);

            signer.setSignaturePolicy(PolicyFactory.Policies.AD_RB_CADES_2_2);
            signer.setAlgorithm(SignerAlgorithmEnum.SHA512withRSA);
            byte[] content = Files.readAllBytes(contentFile);
            byte[] sign = signer.doDetachedSign(content);


            ByteArrayInputStream bis = new ByteArrayInputStream(sign);
            Path pathP7s = buildPathP7s();
            Files.copy(bis, pathP7s, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            signsucess = true;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Path buildPathP7s() {
        return Paths.get(contentFile.toFile().getAbsolutePath()+".p7s");
    }
}
