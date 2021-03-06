/*
 * MIT License
 *
 * Copyright (c) 2020 ALİ GÜNGÖR
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author Ali Güngör (aligng1620@gmail.com)
 */

package tr.havelsan.ueransim;

import fr.marben.asnsdk.japi.spe.ExtSequenceValue;
import fr.marben.asnsdk.japi.spe.SequenceValue;
import tr.havelsan.ueransim.nas.core.messages.NasMessage;
import tr.havelsan.ueransim.ngap.ngap_pdu_descriptions.NGAP_PDU;

public class IncomingMessage {
    public final NGAP_PDU ngapPdu;
    public final SequenceValue ngapMessage;
    public final NasMessage nasMessage;

    public IncomingMessage(NGAP_PDU ngapPdu, SequenceValue ngapMessage, NasMessage nasMessage) {
        this.ngapPdu = ngapPdu;
        this.ngapMessage = ngapMessage;
        this.nasMessage = nasMessage;
    }

    public <T extends NasMessage> T getNasMessage(Class<T> messageType) {
        if (nasMessage == null) {
            return null;
        }
        if (messageType.isAssignableFrom(nasMessage.getClass())) {
            return (T) nasMessage;
        }
        return null;
    }

    public <T extends ExtSequenceValue> T getNgapMessage(Class<T> messageType) {
        if (ngapMessage == null) {
            return null;
        }
        if (messageType.isAssignableFrom(ngapMessage.getClass())) {
            return (T) ngapMessage;
        }
        return null;
    }
}
