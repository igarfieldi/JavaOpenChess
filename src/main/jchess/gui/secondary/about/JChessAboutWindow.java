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

package jchess.gui.secondary.about;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class JChessAboutWindow extends JDialog implements IResources
{
	private static final long serialVersionUID = 8209938830184516667L;
	
	private AboutPanel aboutPanel;
	
	public JChessAboutWindow(Frame parent)
	{
		super(parent);
		setWindowProperties();
		
		aboutPanel = new AboutPanel();
		this.add(aboutPanel);
	}
	
	private void setWindowProperties()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ABOUT_PROPERTIES.getString("title"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		
		setName("aboutBox");
		setResizable(false);
	}
}
