package sma.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.introspection.ACLMessage;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import sma.ConsommateurContainer;

public class ConsommateurAgent extends GuiAgent {
	private ConsommateurContainer gui;

	// redefinir la methode setup appeler juste apres instanciation
	@Override
	protected void setup() {
		// lie l'agent a son interface graphique
		gui = (ConsommateurContainer) getArguments()[0];
		// et l'interface est lié a son agent
		gui.setConsommateurAgent(this);
		System.out.println("Initialisation de l'agent " + this.getAID().getName());
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				MessageTemplate messageTemplate=MessageTemplate.or(
						MessageTemplate.MatchPerformative(jade.lang.acl.ACLMessage.REQUEST),
						MessageTemplate.MatchPerformative(jade.lang.acl.ACLMessage.REFUSE));
				jade.lang.acl.ACLMessage message = receive(messageTemplate);
				if (message != null) {
					System.out.println("Réception d'un message " + message.getContent());
					GuiEvent guiEvent = new GuiEvent(this, 1);
					guiEvent.addParameter(message.getContent());
					gui.viewMessage(guiEvent);
				}

			}
		});
	}

//juste avant la distruction de l'agent
	@Override
	protected void takeDown() {
		System.out.println("Destruction de l'agent ");
	}

	@Override
	protected void beforeMove() {
		try {
			System.out.println(
					"Avant migration... vers le container " + this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	@Override
	protected void afterMove() {
		try {
			System.out.println("Apres migration...du container " + this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

//car l'agent possede une interface graphique qd il ya un evt qe produit dans l'interface graphique
	// car c'est l'agent qui doit envoyer le msg pas linterface grpahique
	@Override
	public void onGuiEvent(GuiEvent guiEvent) {
		if (guiEvent.getType() == 1) {
			jade.lang.acl.ACLMessage aclMessage = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.REQUEST);
			String livre = guiEvent.getParameter(0).toString();
			aclMessage.setContent(livre);
			aclMessage.addReceiver(new AID("Acheteur", AID.ISLOCALNAME));
			send(aclMessage);
		}

	}
}
