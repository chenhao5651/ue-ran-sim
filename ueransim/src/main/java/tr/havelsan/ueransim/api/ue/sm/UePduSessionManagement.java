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

package tr.havelsan.ueransim.api.ue.sm;

import tr.havelsan.ueransim.core.SimulationContext;
import tr.havelsan.ueransim.nas.impl.enums.EPduSessionIdentity;
import tr.havelsan.ueransim.nas.impl.enums.EProcedureTransactionIdentity;
import tr.havelsan.ueransim.utils.Logging;
import tr.havelsan.ueransim.utils.Tag;

public class UePduSessionManagement {

    // todo
    public static EPduSessionIdentity allocatePduSessionId(SimulationContext ctx) {
        return EPduSessionIdentity.VAL_8;
    }

    public static EProcedureTransactionIdentity allocateProcedureTransactionId(SimulationContext ctx) {
        var arr = ctx.smCtx.procedureTransactions;

        int id = -1;
        for (int i = ProcedureTransaction.MIN_ID; i <= ProcedureTransaction.MAX_ID; i++) {
            if (arr[i] == null) {
                id = i;
                break;
            }
        }

        if (id == -1) {
            Logging.error(Tag.PROC, "PTI allocation failed");
            return null;
        }

        arr[id] = new ProcedureTransaction();

        var val = EProcedureTransactionIdentity.fromValue(id);
        Logging.debug(Tag.PROC, "PTI allocated: %s", val);
        return val;
    }

    public static void releaseProcedureTransactionId(SimulationContext ctx, EProcedureTransactionIdentity pti) {
        ctx.smCtx.procedureTransactions[pti.intValue()] = null;
        Logging.debug(Tag.PROC, "PTI released: %s", pti);
    }
}
