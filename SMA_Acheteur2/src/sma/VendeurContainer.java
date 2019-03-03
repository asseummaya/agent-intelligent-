package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sma.agents.ConsommateurAgent;
import sma.agents.VendeurAgent;

/**
 * @author Asseum Maya
 *
 */
public class VendeurContainer extends Application {
	private VendeurAgent VendeurAgent;
	private ObservableList<String> observableList;
	private AgentContainer agentContainer;
	private VendeurContainer vendeurContainer;

	public static void main(String[] args) {
		// pour demaarrer l'interface graphique qui va instancier la classe qui par elle
		// mm va demarrer la methode start
		launch(VendeurContainer.class);
	}

	public void startContainer() {
		try {
			Runtime runtime = Runtime.instance();
			// false veut dire que ce nest pas un main container
			Profile profile = new ProfileImpl(false);
			// man_host pour specifier dans quelle machine se trouve le maincontainer
			profile.setParameter(profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		startContainer();
		vendeurContainer=this;
		// interface graphique de l'agent
		// definir un titre de fenetre
		primaryStage.setTitle("Vendeur");
		// scene utilise un composant principal comme conteneur qui devise la scene en 5
		// zones
		BorderPane borderPane = new BorderPane();

		// creer l'interface avec javafix
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 10, 10, 10));
		// creer des espaces
		hBox.setSpacing(10);

		Label labelVendeur = new Label("Nom de vendeur: ");
		TextField textFieldVendeur = new TextField();
		Button buttonVendeur = new Button("Déployer l'agent vendeur");
		hBox.getChildren().add(labelVendeur);
		hBox.getChildren().add(textFieldVendeur);
		hBox.getChildren().add(buttonVendeur);
		borderPane.setTop(hBox);

		// ajouter une liste
		VBox vBox = new VBox();
		// deviser le paneau sous forme d'une grille avec des lignes et des colonnes
		GridPane gridPane = new GridPane();
		observableList = FXCollections.observableArrayList();
		ListView<String> listViewMessages = new ListView<String>(observableList);
		gridPane.add(listViewMessages, 0, 0);
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);

		vBox.getChildren().add(gridPane);
		borderPane.setCenter(vBox);

		Scene scene = new Scene(borderPane, 400, 500);
		primaryStage.setScene(scene);
		primaryStage.show();

		// ajouter l'evennment sur le bouton
		buttonVendeur.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String nomVendeur = textFieldVendeur.getText();
					AgentController agentController = agentContainer.createNewAgent(nomVendeur,
							"sma.agents.VendeurAgent", new Object[] {vendeurContainer});
					agentController.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	

	public VendeurAgent getVendeurAgent() {
		return VendeurAgent;
	}

	public void setVendeurAgent(VendeurAgent vendeurAgent) {
		VendeurAgent = vendeurAgent;
	}

	public void viewMessage(GuiEvent guiEvent) {
		String message = guiEvent.getParameter(0).toString();
		observableList.add(message);
	}

}
