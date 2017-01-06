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

package jchess.gui.secondary;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import jchess.util.TypedResourceBundle;

public class JChessAboutBox extends JDialog
{
	private static final long serialVersionUID = 8209938830184516667L;
	
	private static final TypedResourceBundle ABOUT_PROPERTIES = new TypedResourceBundle(
	        "jchess.resources.JChessAboutBox");
	
	private AboutPanel aboutPanel;
	
	public JChessAboutBox(Frame parent)
	{
		super(parent);
		setDialogProperties();
		
		aboutPanel = new AboutPanel();
		this.add(aboutPanel);
	}
	
	private void setDialogProperties()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ABOUT_PROPERTIES.getString("title"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		
		setName("aboutBox");
		setResizable(false);
	}
}
