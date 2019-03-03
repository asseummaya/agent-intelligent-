package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainContainer {
	public static void main(String[] args) {
		try {
			// pour demarrer un main contaner creer un objet de type Runtime de jade
			Runtime runtime = Runtime.instance();
			Properties properties = new ExtendedProperties();
			// specifier klks propertises comme afficher l'interface graphique
			properties.setProperty(Profile.GUI, "true");
			// creer un objet profile comme parametres properties
			Profile profile = new ProfileImpl(properties);
			// creer le container
			AgentContainer mainContainer = runtime.createMainContainer(profile);
			mainContainer.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
