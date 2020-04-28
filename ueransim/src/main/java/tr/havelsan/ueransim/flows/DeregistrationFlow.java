package tr.havelsan.ueransim.flows;

import tr.havelsan.ueransim.BaseFlow;
import tr.havelsan.ueransim.URSimUtils;
import tr.havelsan.ueransim.contexts.SimulationContext;
import tr.havelsan.ueransim.flowinputs.DeregistrationInput;
import tr.havelsan.ueransim.nas.impl.enums.ETypeOfSecurityContext;
import tr.havelsan.ueransim.nas.impl.ies.IENasKeySetIdentifier;
import tr.havelsan.ueransim.nas.impl.messages.DeRegistrationAcceptUeOriginating;
import tr.havelsan.ueransim.nas.impl.messages.DeRegistrationRequestUeOriginating;
import tr.havelsan.ueransim.ngap.ngap_pdu_contents.DownlinkNASTransport;
import tr.havelsan.ueransim.ngap.ngap_pdu_contents.UEContextReleaseCommand;
import tr.havelsan.ueransim.ngap.ngap_pdu_descriptions.InitiatingMessage;
import tr.havelsan.ueransim.ngap.ngap_pdu_descriptions.NGAP_PDU;
import tr.havelsan.ueransim.ngap2.NgapBuilder;
import tr.havelsan.ueransim.ngap2.NgapCriticality;
import tr.havelsan.ueransim.ngap2.NgapPduDescription;
import tr.havelsan.ueransim.ngap2.NgapProcedure;
import tr.havelsan.ueransim.utils.Color;
import tr.havelsan.ueransim.utils.Console;

public class DeregistrationFlow extends BaseFlow {

    private final DeregistrationInput input;

    public DeregistrationFlow(SimulationContext simContext, DeregistrationInput input) {
        super(simContext);
        this.input = input;
    }

    @Override
    public State main(NGAP_PDU ngapIn) throws Exception {
        var request = new DeRegistrationRequestUeOriginating();
        request.deRegistrationType = input.deregistrationType;
        request.ngKSI = new IENasKeySetIdentifier(ETypeOfSecurityContext.NATIVE_SECURITY_CONTEXT, input.ngKSI);
        request.mobileIdentity = input.guti;

        sendNgap(new NgapBuilder()
                .withDescription(NgapPduDescription.INITIATING_MESSAGE)
                .withProcedure(NgapProcedure.UplinkNASTransport, NgapCriticality.IGNORE)
                .addRanUeNgapId(input.ranUeNgapId, NgapCriticality.REJECT)
                .addAmfUeNgapId(input.amfUeNgapId, NgapCriticality.REJECT)
                .addNasPdu(request, NgapCriticality.REJECT)
                .addUserLocationInformationNR(input.userLocationInformationNr, NgapCriticality.IGNORE)
                .build());

        return this::waitDeregistrationAccept;
    }

    private State waitDeregistrationAccept(NGAP_PDU ngapIn) {
        if (!(ngapIn.getValue() instanceof InitiatingMessage)) {
            Console.println(Color.YELLOW, "bad message, InitiatingMessage is expected. message ignored");
            return this::waitDeregistrationAccept;
        }

        var initiatingMessage = ((InitiatingMessage) ngapIn.getValue()).value.getDecodedValue();
        if (!(initiatingMessage instanceof DownlinkNASTransport)) {
            Console.println(Color.YELLOW, "bad message, DownlinkNASTransport is expected. message ignored");
            return this::waitDeregistrationAccept;
        }

        var downlinkNASTransport = (DownlinkNASTransport) initiatingMessage;
        var nasMessage = URSimUtils.getNasMessage(downlinkNASTransport);
        if (nasMessage == null) {
            Console.println(Color.YELLOW, "bad message, nas pdu is missing. message ignored");
            return this::waitDeregistrationAccept;
        }

        if (!(nasMessage instanceof DeRegistrationAcceptUeOriginating)) {
            Console.println(Color.YELLOW, "bad message, DeRegistrationAcceptUeOriginating is expected. message ignored");
            return this::waitDeregistrationAccept;
        }

        return this::waitUeContextReleaseCommand;
    }

    private State waitUeContextReleaseCommand(NGAP_PDU ngapIn) {
        if (!(ngapIn.getValue() instanceof InitiatingMessage)) {
            Console.println(Color.YELLOW, "bad message, InitiatingMessage is expected. message ignored");
            return this::waitDeregistrationAccept;
        }

        var initiatingMessage = ((InitiatingMessage) ngapIn.getValue()).value.getDecodedValue();
        if (!(initiatingMessage instanceof UEContextReleaseCommand)) {
            Console.println(Color.YELLOW, "bad message, UEContextReleaseCommand is expected. message ignored");
            return this::waitDeregistrationAccept;
        }

        var command = (UEContextReleaseCommand) initiatingMessage;
        // do something with command

        sendNgap(new NgapBuilder()
                .withDescription(NgapPduDescription.SUCCESSFUL_OUTCOME)
                .withProcedure(NgapProcedure.UEContextReleaseComplete, NgapCriticality.REJECT)
                .addRanUeNgapId(input.ranUeNgapId, NgapCriticality.IGNORE)
                .addAmfUeNgapId(input.amfUeNgapId, NgapCriticality.IGNORE)
                .build());

        logFlowComplete();
        return abortReceiver();
    }
}