package signerman.example;

import org.demoiselle.signer.core.CertificateManager;
import org.demoiselle.signer.core.extension.BasicCertificate;
import org.demoiselle.signer.core.keystore.loader.factory.KeyStoreLoaderFactory;
import org.demoiselle.signer.core.keystore.loader.implementation.FileSystemKeyStoreLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class CertificateLoad {

    private static final Logger logger = LoggerFactory.getLogger(CertificateLoad.class);

    public static FileSystemKeyStoreLoader keyStoreLoader;
    public static KeyStore keyStore;
    public static X509Certificate certificate;
    public static PrivateKey privateKey;
    public static char[] password = "caro84867944".toCharArray();
    public static Certificate[] certificateChain;

    public static void main(String[] args) {

        password = Config.getConfig().getValue("pfx.password").toCharArray();

        Path path = Paths.get(Config.getConfig().getValue("pfx.path"));
        if (!path.toFile().exists()) {
            System.out.println("não existe");
            return;
        }
        try {
            // Carrega a keystore (TOKEN)
            keyStoreLoader = (FileSystemKeyStoreLoader) KeyStoreLoaderFactory.factoryKeyStoreLoader(path.toFile());

            keyStore = keyStoreLoader.getKeyStore(new String(password));

            Enumeration<String> aliases = keyStore.aliases();

            while (aliases.hasMoreElements()) {

                String alias = aliases.nextElement();

                System.out.println("Alias: " + alias);

                certificate = (X509Certificate) keyStore.getCertificate(alias);
                privateKey = (PrivateKey) keyStore.getKey(alias, password);
                certificateChain = keyStore.getCertificateChain(alias);

                try {

                    CertificateManager cm = new CertificateManager(certificate);
                    CertICPBrasil cert = cm.load(CertICPBrasil.class);
                    logger.info("CPF: {}",new String[] {cert.getCpf()});

                     BasicCertificate bc = new BasicCertificate(certificate);
                     logger.info("Name....................[{}]",
                     new String[]       {bc.getNome()});
                     logger.info("E-mail..................[{}]",
                             new String[]       {bc.getEmail()});
                     logger.info("Serial Number.........[{}]",
                             new String[]       {bc.getSerialNumber()});
                } catch (Exception e) {
                    logger.error("Error loading certificate (ICP Brasil) with alias [" + alias + "]", e);
                }

            }

        } catch (Throwable e) {

            e.printStackTrace();

        }

    }
}