package com.xytx.utils;



import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;


public class Pkipair {


    /*生成签名*/
    public String signMsg(String signMsg) {
        String base64 = "";
        try {
            // 1. 加载PKCS12证书
            KeyStore ks = KeyStore.getInstance("PKCS12");
            String file = Pkipair.class.getResource("10012140356.pfx").getPath().replaceAll("%20", " ");
            System.out.println(file);

            try (FileInputStream ksfis = new FileInputStream(file);
                 BufferedInputStream ksbufin = new BufferedInputStream(ksfis)) {

                // 2. 加载密钥库
                char[] keyPwd = "123456".toCharArray();
                ks.load(ksbufin, keyPwd);

                // 3. 获取私钥并签名
                PrivateKey priK = (PrivateKey) ks.getKey("test-alias", keyPwd);
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initSign(priK);
                signature.update(signMsg.getBytes(StandardCharsets.UTF_8));

                // 4. 使用java.util.Base64进行编码（替代sun.misc.BASE64Encoder）
                byte[] signatureBytes = signature.sign();
                base64 = Base64.getEncoder().encodeToString(signatureBytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return base64;
    }

    public boolean enCodeByCer(String val, String msg) {
        boolean flag = false;
        try {
            // 1. 加载证书文件
            String file = Pkipair.class.getResource("99bill[1].cert.rsa.20140803.cer").toURI().getPath();
            System.out.println(file);

            try (FileInputStream inStream = new FileInputStream(file)) {
                // 2. 获取证书和公钥
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
                PublicKey pk = cert.getPublicKey();

                // 3. 初始化签名验证
                Signature signature = Signature.getInstance("SHA256withRSA");
                signature.initVerify(pk);
                signature.update(val.getBytes(StandardCharsets.UTF_8));

                // 4. 使用java.util.Base64进行解码（替代sun.misc.BASE64Decoder）
                byte[] signatureBytes = Base64.getDecoder().decode(msg);
                System.out.println(new String(signatureBytes, StandardCharsets.UTF_8));

                // 5. 验证签名
                flag = signature.verify(signatureBytes);
                System.out.println(flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("no");
        }
        return flag;
    }

}
