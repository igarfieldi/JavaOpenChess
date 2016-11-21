/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 */
package jchess.gui.setup;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Game;
import jchess.network.Client;
import jchess.network.MD5;
import jchess.server.Server;

/**
 * Class responible for drawing Network Settings, when player want to start a
 * game on a network
 * 
 * @param parent
 *            Where are saved default settings
 */
public class DrawNetworkSettings extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 7197800490735491654L;
	private static Logger log = Logger.getLogger(DrawNetworkSettings.class.getName());
	
	private JDialog parent;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;
	
	private ButtonGroup serverORclientButtonGroup;
	private JRadioButton serverRadioButton;
	private JRadioButton clientRadioButton;
	
	private JLabel nicknameLabel;
	private JLabel passwordLabel;
	private JLabel gameIdLabel;
	private JLabel optionsLabel;
	private JPanel optionsPanel;
	
	private JTextField nicknameTextField;
	private JPasswordField passwordField;
	private JTextField gameIdTextField;
	
	private JButton startButton;
	private ServerOptionsPanel serverOptionsPanel;
	private ClientOptionsPanel clientOptionsPanel;
	
	DrawNetworkSettings(JDialog parent)
	{
		super();
		this.parent = parent;
		
		initializeGuiElements();
		createGridBagLayout();
		placeGuiElementsOnWindowWithPositionConstraints();
	}
	
	private void initializeGuiElements()
	{
		initializeRadioButtons();
		initializeLabels();
		initializeTextFields();
		initializePanels();
		
		this.startButton = new JButton(Localization.getMessage("start"));
		this.startButton.addActionListener(this);
	}
	
	private void initializeRadioButtons()
	{
		this.serverRadioButton = new JRadioButton(Localization.getMessage("create_server"), true);
		this.clientRadioButton = new JRadioButton(Localization.getMessage("connect_2_server"), false);
		this.serverORclientButtonGroup = new ButtonGroup();
		this.serverORclientButtonGroup.add(this.serverRadioButton);
		this.serverORclientButtonGroup.add(this.clientRadioButton);
		this.serverRadioButton.addActionListener(this);
		this.clientRadioButton.addActionListener(this);
	}

	private void initializeLabels()
	{
		this.nicknameLabel = new JLabel(Localization.getMessage("nickname"));
		this.passwordLabel = new JLabel(Localization.getMessage("password"));
		this.gameIdLabel = new JLabel(Localization.getMessage("game_id"));
		this.optionsLabel = new JLabel(Localization.getMessage("server_options"));
	}

	private void initializeTextFields()
	{
		this.nicknameTextField = new JTextField();
		this.passwordField = new JPasswordField();
		this.gameIdTextField = new JTextField();
	}

	private void initializePanels()
	{
		this.optionsPanel = new JPanel();
		this.clientOptionsPanel = new ClientOptionsPanel();
		this.serverOptionsPanel = new ServerOptionsPanel();
	}
	
	private void createGridBagLayout()
	{
		this.gridBagLayout = new GridBagLayout();
		this.gridBagConstraints = new GridBagConstraints();
		this.gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.setLayout(gridBagLayout);
	}

	private void placeGuiElementsOnWindowWithPositionConstraints()
	{
		setGridBagConstraints(0, 0, 1, serverRadioButton);
		setGridBagConstraints(1, 0, 1, clientRadioButton);
		setGridBagConstraints(0, 1, 2, gameIdLabel);
		setGridBagConstraints(0, 2, 2, gameIdTextField);
		setGridBagConstraints(0, 3, 2, nicknameLabel);
		setGridBagConstraints(0, 4, 2, nicknameTextField);
		setGridBagConstraints(0, 5, 2, passwordLabel);
		setGridBagConstraints(0, 6, 2, passwordField);
		setGridBagConstraints(0, 7, 2, optionsLabel);
		setGridBagConstraints(0, 8, 2, optionsPanel);
		setGridBagConstraints(0, 9, 2, startButton);
		this.optionsPanel.add(serverOptionsPanel);
	}
	
	private void setGridBagConstraints(int gridx, int gridy, int gridwidth, JComponent uiElement)
	{
		this.gridBagConstraints.gridx = gridx;
		this.gridBagConstraints.gridy = gridy;
		this.gridBagConstraints.gridwidth = gridwidth;
		this.gridBagLayout.setConstraints(uiElement, gridBagConstraints);
		this.add(uiElement);
	}
	
	/*
	 * Method for showing settings which the player is intrested with
	 */
	public void actionPerformed(ActionEvent uiElement)
	{
		if(uiElement.getSource() == this.serverRadioButton)
			showOptionsFor(serverOptionsPanel);
		else if(uiElement.getSource() == this.clientRadioButton)
			showOptionsFor(clientOptionsPanel);
		else if(uiElement.getSource() == this.startButton)
		{
			String error = checkForNamingErrors();
			if(error.length() > 0)
			{
				JOptionPane.showMessageDialog(this, error);
				return;
			}
			
			String password = this.passwordField.getText().toString();
			if(this.serverRadioButton.isSelected())
			{
				setUpServer(password);
				testForThreadInterruption();
			}
			
			try
			{
				connectToServer();	
			}
			catch(Error connectionError)
			{
				log.log(Level.SEVERE, "Client connection: failure", connectionError);
				JOptionPane.showMessageDialog(this, connectionError);
			}
		}
	}

	private void showOptionsFor(JPanel panel)
	{
		this.optionsPanel.removeAll();
		this.optionsPanel.add(panel);
		this.optionsPanel.revalidate();
		this.optionsPanel.requestFocus();
		this.optionsPanel.repaint();
	}

	private String checkForNamingErrors()
	{
		String error = "";
		
		if(this.gameIdTextField.getText().isEmpty())
			error = Localization.getMessage("fill_game_id") + "\n";
		if(this.nicknameTextField.getText().length() == 0)
			error += Localization.getMessage("fill_name") + "\n";
		if(this.passwordField.getText().length() <= 4)
			error += Localization.getMessage("fill_pass_with_more_than_4_signs") + "\n";
		if(this.clientRadioButton.isSelected() && this.clientOptionsPanel.serverIpTextField.getText().length() == 0)
			error += Localization.getMessage("please_fill_field") + " IP \n";
		else if(this.clientRadioButton.isSelected())
		{
			Pattern ipPattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
			if(!ipPattern.matcher(this.clientOptionsPanel.serverIpTextField.getText()).matches())
				error += Localization.getMessage("bad_ip_format") + "\n";
		}
		
		return error;
	}

	private void setUpServer(String password)
	{
		Server server = new Server(); // create server
		server.newTable(Integer.parseInt(gameIdTextField.getText()), password,
		        !serverOptionsPanel.withoutObserversCheckBox.isSelected(),
		        !serverOptionsPanel.disableChatCheckBox.isSelected()); // create new table
		// set client options
		clientOptionsPanel.serverIpTextField.setText("127.0.0.1");
	}

	private void testForThreadInterruption()
	{
		try
		{
			Thread.sleep(100); // wait 100 ms
		}
		catch(InterruptedException exception)
		{
			log.log(Level.SEVERE, null, exception);
		}
	}

	private void connectToServer() throws Error
	{
		Client client = new Client(clientOptionsPanel.serverIpTextField.getText(), Server.port);
		boolean isJoining = client.join(Integer.parseInt(gameIdTextField.getText()),
		        !clientOptionsPanel.onlyObserveCheckBox.isSelected(), nicknameTextField.getText(),
		        MD5.encrypt(passwordField.getText()));
		
		if(isJoining) // Client connection: succesful
		{
			log.info("Client connection: succesfull");
			startGame(client);
		}
		else
			JOptionPane.showMessageDialog(this, Localization.getMessage("error_connecting_to_server"));
	}

	private void startGame(Client client)
	{
		Game newGUI = JChessApp.jcv.addNewTab("Network game, table: " + gameIdTextField.getText());
		client.game = newGUI;
		newGUI.add(newGUI.getChat());
		newGUI.getChessboard().draw();
		
		Thread thread = new Thread(client);
		thread.start(); // client listening
		
		this.parent.setVisible(false);// hide parent
	}
	
	/*
	 * Method witch is saving the latest network settings
	 */
	private class ServerOptionsPanel extends JPanel // options for server
	{
		private static final long serialVersionUID = -2128136033331821722L;
		
		private GridBagLayout gridBagLayout;
		private GridBagConstraints gridBagConstraints;
		
		private JLabel gameTimeLabel;
		private JTextField gameTimeTextField;
		public JCheckBox withoutObserversCheckBox;
		public JCheckBox disableChatCheckBox;
		
		ServerOptionsPanel()
		{
			super();
			
			initializeGuiElements();
			createGridBagLayout();
			placeGuiElementsOnWindowWithPositionConstraints();
		}

		private void initializeGuiElements()
		{
			gameTimeLabel = new JLabel(Localization.getMessage("time_game_min"));
			gameTimeTextField = new JTextField();
			withoutObserversCheckBox = new JCheckBox(Localization.getMessage("without_observers"));
			disableChatCheckBox = new JCheckBox(Localization.getMessage("without_chat"));
			
			// temporary disabled options
			this.gameTimeLabel.setEnabled(false);
			this.gameTimeTextField.setEnabled(false);
			this.disableChatCheckBox.setEnabled(false);
			// ------------------------
		}

		private void createGridBagLayout()
		{
			this.gridBagLayout = new GridBagLayout();
			this.gridBagConstraints = new GridBagConstraints();
			this.gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.setLayout(gridBagLayout);
		}

		private void placeGuiElementsOnWindowWithPositionConstraints()
		{
			setGridBagConstraints(0, 0, 2, gameTimeLabel);
			setGridBagConstraints(0, 1, 2, gameTimeTextField);
			setGridBagConstraints(0, 2, 1, withoutObserversCheckBox);
			setGridBagConstraints(1, 2, 1, disableChatCheckBox);
		}

		private void setGridBagConstraints(int gridx, int gridy, int gridwidth, JComponent uiElement)
		{
			this.gridBagConstraints.gridx = gridx;
			this.gridBagConstraints.gridy = gridy;
			this.gridBagConstraints.gridwidth = gridwidth;
			this.gridBagLayout.setConstraints(uiElement, gridBagConstraints);
			this.add(uiElement);
		}
	}
	
	/*
	 * Method responible for drawing clients panel
	 */
	private class ClientOptionsPanel extends JPanel // options for client
	{
		private static final long serialVersionUID = 730398615613122251L;
		
		private GridBagLayout gridBagLayout;
		private GridBagConstraints gridBagConstraints;
		private JLabel serverIpLabel;
		public JTextField serverIpTextField;
		private JCheckBox onlyObserveCheckBox;
		
		ClientOptionsPanel()
		{
			super();
			
			this.serverIpLabel = new JLabel(Localization.getMessage("server_ip"));
			this.serverIpTextField = new JTextField();
			this.onlyObserveCheckBox = new JCheckBox(Localization.getMessage("only_observe"));
			
			createGridBagLayout();
			placeGuiElementsOnWindowWithPositionConstraints();
		}

		private void createGridBagLayout()
		{
			this.gridBagLayout = new GridBagLayout();
			this.gridBagConstraints = new GridBagConstraints();
			this.gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.setLayout(gridBagLayout);
		}

		private void placeGuiElementsOnWindowWithPositionConstraints()
		{
			setGridBagConstraints(0, 0, 1, serverIpLabel);
			setGridBagConstraints(0, 1, 1, serverIpTextField);
			setGridBagConstraints(0, 2, 1, onlyObserveCheckBox);
		}
		
		private void setGridBagConstraints(int gridx, int gridy, int gridwidth, JComponent uiElement)
		{
			this.gridBagConstraints.gridx = gridx;
			this.gridBagConstraints.gridy = gridy;
			this.gridBagConstraints.gridwidth = gridwidth;
			this.gridBagLayout.setConstraints(uiElement, gridBagConstraints);
			this.add(uiElement);
		}
	}
}
