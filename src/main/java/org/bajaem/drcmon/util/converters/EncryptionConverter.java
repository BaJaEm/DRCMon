package org.bajaem.drcmon.util.converters;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.persistence.AttributeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCStartupException;
import org.bajaem.drcmon.util.EncryptionDecryptionAES;

public class EncryptionConverter implements AttributeConverter<String, String>
{
    private static final Logger LOG = LogManager.getLogger(); // Encrypt the
                                                              // data

    @Override
    public String convertToDatabaseColumn(final String data)
    {
        if (data == null)
        {
            throw new DRCStartupException("Invalid Key - NULL not allowed");
        }

        try
        {
            final SecretKey key = EncryptionDecryptionAES.createKey();
            final EncryptionDecryptionAES e = new EncryptionDecryptionAES(key, data);
            return e.getEncodedString();
        }
        catch (final BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException
                | IOException | NoSuchPaddingException e1)
        {
            LOG.error(e1);
            throw new DRCStartupException(e1);
        }

    }

    // Decrypt the data
    @Override
    public String convertToEntityAttribute(final String data)
    {
        if (data == null)
        {
            throw new DRCStartupException("Invalid Key - NULL not allowed");
        }

        try
        {
            final EncryptionDecryptionAES e = EncryptionDecryptionAES.deSerialize(data);
            return e.decrypt();
        }
        catch (final ClassNotFoundException | IOException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e1)
        {
            LOG.error(e1);
            throw new DRCStartupException(e1);
        }
    }

}
