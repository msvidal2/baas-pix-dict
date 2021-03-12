package com.picpay.banking.pix.core.domain.reconciliation;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.function.BinaryOperator;

public class VsyncBitwiseXOR implements BinaryOperator<String> {

    @Override
    public String apply(final String cid1, final String cid2) {
        try {
            byte[] result = Hex.decodeHex(cid1);
            result = xor(result, Hex.decodeHex(cid2));

            return String.valueOf(Hex.encodeHex(result));
        } catch (DecoderException e) {
            throw new IllegalArgumentException("The Cid must be a String that represents a hexadecimal with 64 characters.", e);
        }
    }

    private static byte[] xor(byte[] a, byte[] b) {
        int length = Math.min(a.length, b.length);
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

}
