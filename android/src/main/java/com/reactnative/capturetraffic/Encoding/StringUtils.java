package com.reactnative.capturetraffic.Encoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.regex.Pattern;

public class StringUtils
{
    private static final Pattern DOUBLE_QUOTED =
            Pattern.compile("^[\\s]*\"(.*)\"[\\s]*$");
    private static final Pattern SINGLE_QUOTED =
            Pattern.compile("^[\\s]*\'(.*)\'[\\s]*$");

    /**
     * Using a CharsetDecoder, translate the ByteBuffer into a stream, updating the buffer's position as we go.
     */
    public static String bufferToString(ByteBuffer buf, Charset cs)
    {
        if (buf.hasArray()) {
            // For common character sets like ASCII and UTF-8, this is actually much more efficient
            String s = new String(buf.array(),
                    buf.arrayOffset() + buf.position(),
                    buf.remaining(),
                    cs);
            buf.position(buf.limit());
            return s;
        }

        CharsetDecoder decoder = Charsets.get().getDecoder(cs);
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        int bufLen = (int)(buf.limit() * decoder.averageCharsPerByte());
        CharBuffer cBuf = CharBuffer.allocate(bufLen);
        CoderResult result;
        do {
            result = decoder.decode(buf, cBuf, true);
            if (result.isOverflow()) {
                cBuf = BufferUtils.doubleBuffer(cBuf);
            }
        } while (result.isOverflow());
        do {
            result = decoder.flush(cBuf);
            if (result.isOverflow()) {
                cBuf = BufferUtils.doubleBuffer(cBuf);
            }
        } while (result.isOverflow());

        cBuf.flip();
        return cBuf.toString();
    }

    /**
     * Like bufferToString, but read multiple buffers.
     */
    public static String bufferToString(ByteBuffer[] bufs, Charset cs)
    {
        CharsetDecoder decoder = Charsets.get().getDecoder(cs);
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

        int totalBytes = 0;
        for (int i = 0; i < bufs.length; i++) {
            totalBytes += (bufs[i] == null ? 0 : bufs[i].remaining());
        }
        int bufLen = (int)(totalBytes * decoder.averageCharsPerByte());
        CharBuffer cBuf = CharBuffer.allocate(bufLen);
        CoderResult result;
        for (int i = 0; i < bufs.length; i++) {
            do {
                result = decoder.decode(bufs[i], cBuf, (i == (bufs.length - 1)));
                if (result.isOverflow()) {
                    cBuf = BufferUtils.doubleBuffer(cBuf);
                }
            } while (result.isOverflow());
        }
        do {
            result = decoder.flush(cBuf);
            if (result.isOverflow()) {
                cBuf = BufferUtils.doubleBuffer(cBuf);
            }
        } while (result.isOverflow());

        cBuf.flip();
        return cBuf.toString();
    }

    /**
     * Using a CharsetEncoder, translate a string to a ByteBuffer, allocating a new buffer
     * as necessary.
     */
    public static ByteBuffer stringToBuffer(String str, Charset cs)
    {
        if (Charsets.BASE64.equals(cs)) {
            // Special handling for Base64 -- ignore unmappable characters
            CharsetEncoder enc = Charsets.get().getEncoder(cs);
            enc.onMalformedInput(CodingErrorAction.REPORT);
            enc.onUnmappableCharacter(CodingErrorAction.IGNORE);

            CharBuffer chars = CharBuffer.wrap(str);
            int bufLen = (int)(chars.remaining() * enc.averageBytesPerChar());
            ByteBuffer writeBuf =  ByteBuffer.allocate(bufLen);

            CoderResult result;
            do {
                result = enc.encode(chars, writeBuf, true);
                if (result.isOverflow()) {
                    writeBuf = BufferUtils.doubleBuffer(writeBuf);
                }
            } while (result.isOverflow());
            do {
                result = enc.flush(writeBuf);
                if (result.isOverflow()) {
                    writeBuf = BufferUtils.doubleBuffer(writeBuf);
                }
            } while (result.isOverflow());

            writeBuf.flip();
            return writeBuf;
        }

        // Use default decoding options, and this is optimized for common charsets as well
        byte[] enc = str.getBytes(cs);
        return ByteBuffer.wrap(enc);
    }
}