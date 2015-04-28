package org.springframework.boot.firehose.integration;

import events.EnvelopeOuterClass.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

/**
 * Created by vcarvalho on 4/27/15.
 */
public class EnvelopeTransformer extends AbstractTransformer {

    private final Logger logger = LoggerFactory.getLogger(EnvelopeTransformer.class);
    public EnvelopeTransformer() {
    }

    protected Object doTransform(Message<?> message) throws Exception {

        byte[] bytes = (byte[])((byte[])message.getPayload());
        Envelope envelope = Envelope.parseFrom(bytes);
        return envelope;
    }
}
