package sma.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
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
import sma.VendeurContainer;

public class VendeurAgent extends GuiAgent {
	private VendeurContainer gui;

	// redefinir la methode setup appeler juste apres instanciation
	@Override
	protected void setup() {
		// lie l'agent a son interface graphique
		gui = (VendeurContainer) getArguments()[0];
		// et l'interface est lié a son agent
		gui.setVendeurAgent(this);
		System.out.println("Initialisation de l'agent " + this.getAID().getName());
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				DFAgentDescription dfa = new DFAgentDescription();
				dfa.setName(getAID());
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Vente");
				sd.setName("Vente-livres");
				dfa.addServices(sd);
				try {
					DFService.register(myAgent, dfa);
				} catch (FIPAException e) {
					e.printStackTrace();
				}

			}
		});
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				jade.lang.acl.ACLMessage aclMessage = receive();
				if (aclMessage != null) {
					switch (aclMessage.getPerformative()) {
					case jade.lang.acl.ACLMessage.CFP:
						GuiEvent guiEvent=new GuiEvent(this,1);
						guiEvent.addParameter(aclMessage.getContent());
						gui.viewMessage(guiEvent);

						break;
					case jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL:
						break;

					default:
						break;
					}
				} else {
					block();
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
