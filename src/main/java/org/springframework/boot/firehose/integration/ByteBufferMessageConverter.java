package org.springframework.boot.firehose.integration;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeTypeUtils;

import java.nio.ByteBuffer;

/**
 * Created by vcarvalho on 4/27/15.
 */
public class ByteBufferMessageConverter extends AbstractMessageConverter {
    protected ByteBufferMessageConverter() {
        super(MimeTypeUtils.APPLICATION_OCTET_STREAM);
    }

    protected boolean supports(Class<?> clazz) {
        return ByteBuffer.class.isAssignableFrom(clazz);
    }

    public Object convertFromInternal(Message<?> message, Class<?> targetClass) {
        ByteBuffer buffer = (ByteBuffer)message.getPayload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, bytes.length);
        return bytes;
    }

    public Object convertToInternal(Object payload, MessageHeaders headers) {
        return payload;
    }
}
