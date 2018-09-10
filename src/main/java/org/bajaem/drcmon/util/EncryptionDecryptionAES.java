package org.bajaem.drcmon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class EncryptionDecryptionAES implements Serializable
{

    private static final long serialVersionUID = 3861384385569368884L;

    private final static Base64.Encoder encoder = Base64.getEncoder();

    private final static Base64.Decoder decoder = Base64.getDecoder();

    private final SecretKey secretKey;

    private final byte[] encrypted;

    private final static String KEY_TYPE = "AES";

    public EncryptionDecryptionAES(final SecretKey key, final String value) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        final Cipher cipher = Cipher.getInstance(KEY_TYPE);
        secretKey = key;
        final byte[] plainTextByte = value.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        encrypted = cipher.doFinal(plainTextByte);
    }

    public void serialize(final String f) throws IOException
    {
        try (final BufferedWriter w = new BufferedWriter(new FileWriter(new File(f))))
        {
            w.write(getEncodedString());
        }
    }

    public String getEncodedString() throws IOException
    {
        try (final ByteArrayOutputStream ba = new ByteArrayOutputStream())
        {
            try (final ObjectOutputStream out = new ObjectOutputStream(ba))
            {
                out.writeObject(this);
                return encoder.encodeToString(ba.toByteArray());
            }
        }

    }

    public static EncryptionDecryptionAES deSerialize(final InputStream is) throws IOException, ClassNotFoundException
    {
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(is)))
        {
            final String enc;
            if ((enc = r.readLine()) == null)
            {
                r.close();
                throw new IOException("not a valid key file");
            }

            try (final ByteArrayInputStream fileIn = new ByteArrayInputStream(decoder.decode(enc)))
            {
                try (final ObjectInputStream in = new ObjectInputStream(fileIn))
                {
                    final EncryptionDecryptionAES ob = (EncryptionDecryptionAES) in.readObject();
                    return ob;
                }
            }
        }
    }

    public static EncryptionDecryptionAES deSerialize(final String value) throws IOException, ClassNotFoundException
    {
        if (value == null)
        {
            throw new IOException("not a valid key");
        }
        try (final InputStream bais = new ByteArrayInputStream(value.getBytes()))
        {
            return deSerialize(bais);
        }
    }

    public static EncryptionDecryptionAES deSerialize(final File f) throws IOException, ClassNotFoundException
    {
        try (final FileInputStream fis = new FileInputStream(f))
        {
            return deSerialize(fis);
        }
    }

    public static SecretKey createKey() throws NoSuchAlgorithmException
    {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        final SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static class Encrypt
    {
        public static void main(final String[] args) throws Exception
        {

            final Options options = new Options();
            options.addRequiredOption(null, "keyFile", true, "Name of the file storing the key");
            options.addRequiredOption(null, "value", true, "Value to ecrypt");

            final CommandLineParser parser = new DefaultParser();
            final CommandLine cmd = parser.parse(options, args);

            final String file = cmd.getOptionValue("keyFile");
            final String value = cmd.getOptionValue("value");

            final EncryptionDecryptionAES eda = new EncryptionDecryptionAES(EncryptionDecryptionAES.createKey(), value);
            eda.serialize(file);
        }
    }

    public static class Decrypt
    {
        public static void main(final String[] args) throws Exception
        {

            final Options options = new Options();
            options.addRequiredOption(null, "keyFile", true, "Name of the file storing the key");

            final CommandLineParser parser = new DefaultParser();
            final CommandLine cmd = parser.parse(options, args);

            final String file = cmd.getOptionValue("keyFile");

            System.out.println(EncryptionDecryptionAES.decrypt(new File(file)));
        }
    }

    public static String decrypt(final InputStream is) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IOException

    {
        return EncryptionDecryptionAES.deSerialize(is).decrypt();
    }

    public static String decrypt(final File file) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, IOException

    {
        return EncryptionDecryptionAES.deSerialize(file).decrypt();
    }

    public String decrypt() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {
        final Cipher cipher = Cipher.getInstance(KEY_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final byte[] decryptedByte = cipher.doFinal(encrypted);
        final String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}