package org.bajaem.drcmon.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Key
{
    private static final Logger LOG = LogManager.getLogger(Key.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(final String[] args) throws Exception
    {
        final Options options = new Options();
        options.addRequiredOption(null, "keyFile", true, "Name of the file storing the key");
        options.addRequiredOption(null, "id", true, "id to ecrypt");
        options.addRequiredOption(null, "secret", true, "secret to ecrypt");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);

        final String file = cmd.getOptionValue("keyFile");
        final String id = cmd.getOptionValue("id");
        final String secret = cmd.getOptionValue("secret");
        encryptKey(file, id, secret);
    }

    public static void encryptKey(final String file, final String id, final String secret)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException
    {
        final Key key = new Key(id, secret);
        final String json = mapper.writeValueAsString(key);
        final EncryptionDecryptionAES eda = new EncryptionDecryptionAES(EncryptionDecryptionAES.createKey(), json);
        eda.serialize(file);
    }

    public static Key decryptKey(final String file)
    {
        try
        {
            final InputStream is = Key.class.getClassLoader().getResourceAsStream(file);
            final String data = EncryptionDecryptionAES.decrypt(is);
            is.close();
            return mapper.readValue(data, Key.class);
        }
        catch (final Exception e)
        {
            LOG.error(e);
            throw new RuntimeException(e);
        }
    }

    final String id;

    final String secret;

    @JsonCreator
    public Key(@JsonProperty("id") final String _id, @JsonProperty("secret") final String _secret)
    {
        id = _id;
        secret = _secret;

    }

    public String getId()
    {
        return id;
    }

    public String getSecret()
    {
        return secret;
    }
}