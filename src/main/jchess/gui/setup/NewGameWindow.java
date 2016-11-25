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

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import jchess.Localization;

/**
 *
 * @author donmateo
 */
public class NewGameWindow extends JDialog
{
	private static final long serialVersionUID = -6260320901046879928L;
	private JTabbedPane windowPane;
	
	/** Creates new form NewGameWindow */
	public NewGameWindow()
	{
		initializeComponents();
		createWindow();
	}
	
	/** This method is called from within the constructor to */
	private void initializeComponents()
	{
		initializeWindowPane();
		initializeGroupLayout();
		pack();
	}
	
	private void initializeWindowPane()
	{
		windowPane = new JTabbedPane();
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setName("Form"); // NOI18N
		
		windowPane.setName("jTabbedPane1"); // NOI18N
	}
	
	private void initializeGroupLayout()
	{
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		setHorizontalLayoutGroup(layout);
		setVerticalLayoutGroup(layout);
	}
	
	private void setVerticalLayoutGroup(GroupLayout layout)
	{
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addGap(20, 20, 20)
		                .addComponent(windowPane, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE).addContainerGap()));
	}
	
	private void setHorizontalLayoutGroup(GroupLayout layout)
	{
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addContainerGap()
		                .addComponent(windowPane, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addContainerGap()));
	}
	
	private void createWindow()
	{
		this.setSize(400, 700);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.windowPane.addTab(Localization.getMessage("local_game"), new LocalSettingsPanel(this));
	}
}
