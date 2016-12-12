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
 * NewGameWindow.java
 *
 * Created on 2009-10-20, 15:11:49
 */
package jchess.gui.setup;

import javax.swing.JDialog;
import javax.swing.JFrame;

import jchess.gamelogic.game.IGameBuilderFactory;

/**
 *
 * @author donmateo
 */
public class NewGameWindow extends JDialog
{
	private static final long serialVersionUID = -6260320901046879928L;
	private LocalSettingsPanel localSettingsPanel;
	
	public NewGameWindow(JFrame parent, IGameBuilderFactory factory)
	{
		super(parent);
		setDialogProperties();
		
		this.localSettingsPanel = new LocalSettingsPanel(this, factory);
		this.add(localSettingsPanel);
	}
	
	private void setDialogProperties()
	{
		this.setTitle("New Game");
		this.setAlwaysOnTop(true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setSize(400, 700);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
}
