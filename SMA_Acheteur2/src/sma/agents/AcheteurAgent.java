package sma.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.ACLMessage;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import sma.AcheteurContainer;

public class AcheteurAgent extends GuiAgent {
	private AcheteurContainer gui;
	private AID[] listVendeurs;

	// redefinir la methode setup appeler juste apres instanciation
	@Override
	protected void setup() {
		// lie l'agent a son interface graphique
		gui = (AcheteurContainer) getArguments()[0];
		// et l'interface est lié a son agent
		gui.setAcheteurAgent(this);
		System.out.println("Initialisation de l'agent " + this.getAID().getName());
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new TickerBehaviour(this, 6000) {

			@Override
			protected void onTick() {
				try {
					DFAgentDescription description = new DFAgentDescription();
					ServiceDescription serviceDescription = new ServiceDescription();
					serviceDescription.setType("Vente");
					serviceDescription.setName("Vente-Livre");
					description.addServices(serviceDescription);
					DFAgentDescription[] agentdescriptions = DFService.search(myAgent, description);
					listVendeurs = new AID[agentdescriptions.length];
					for (int i = 0; i < agentdescriptions.length; i++) {
						listVendeurs[i] = agentdescriptions[i].getName();
					}
				} catch (FIPAException e) {
					e.printStackTrace();
				}

			}
		});

		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(jade.lang.acl.ACLMessage.REQUEST);
				jade.lang.acl.ACLMessage message = receive(messageTemplate);
				if (message != null) {
					System.out.println("Réception d'un message " + message.getContent());
					GuiEvent guiEvent = new GuiEvent(this, 1);
					String nomLivre = message.getContent();
					guiEvent.addParameter(nomLivre);
					gui.viewMessage(guiEvent);
					/*
					 * Opération d'achat de livre
					 */
					jade.lang.acl.ACLMessage aclMessage = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.CFP);
					aclMessage.setContent(nomLivre);
					for (AID aid : listVendeurs) {
						aclMessage.addReceiver(aid);
					}
					send(aclMessage);
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

	}
}
