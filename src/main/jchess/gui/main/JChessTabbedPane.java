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
#    along with this program.  If not, see <http://www.gnu.org/licenses/>..
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gui.main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import jchess.JChessApp;
import jchess.gui.ThemeImageLoader;
import jchess.gui.setup.NewGameWindow;

public class JChessTabbedPane extends JTabbedPane implements MouseListener, ImageObserver
{
	private static final long serialVersionUID = -7046648284513652282L;
	private static Logger log = Logger.getLogger(JChessTabbedPane.class.getName());
	
	private Image addIcon;
	private Rectangle addIconRectangle = null;
	
	public JChessTabbedPane()
	{
		super();
		this.addIcon = ThemeImageLoader.getInstance().loadThemeImage("add-tab-icon.png");
		
		this.setDoubleBuffered(true);
		super.addMouseListener(this);
	}
	
	public void mouseReleased(MouseEvent event)
	{
	}
	
	public void mousePressed(MouseEvent event)
	{
	}
	
	@Override
	public void mouseClicked(MouseEvent event)
	{
		int tabIndex = getUI().tabForCoordinate(this, event.getX(), event.getY());
		if(tabIndex >= 0)
		{
			closeGameTab(event, tabIndex);
			if(this.getTabCount() == 0)
				this.showNewGameWindow();
		}
		else if(this.addIconRectangle != null && this.addIconRectangle.contains(event.getX(), event.getY()))
		{
			log.log(Level.FINE, "newGame by + button");
			this.showNewGameWindow();
		}
	}

	private void closeGameTab(MouseEvent event, int tabIndex)
	{
		Rectangle closeIconRectangle = ((CloseIcon) getIconAt(tabIndex)).getBounds();
		if(closeIconRectangle.contains(event.getX(), event.getY()))
		{
			log.log(Level.FINE, "Removing tab with " + tabIndex + " number!...");
			this.removeTabAt(tabIndex);
			this.updateAddIconRectangle();
		}
	}

	private void updateAddIconRectangle()
	{
		if(this.getTabCount() > 0)
		{
			Rectangle newRectangleBounds = this.getBoundsAt(this.getTabCount() - 1);
			this.addIconRectangle = new Rectangle(newRectangleBounds.x + newRectangleBounds.width + 5,
			        newRectangleBounds.y, this.addIcon.getWidth(this), this.addIcon.getHeight(this));
		}
		else
			this.addIconRectangle = null;
	}

	private void showNewGameWindow()
	{
		if(JChessApp.view.newGameFrame == null)
			JChessApp.view.newGameFrame = new NewGameWindow(JChessApp.view.getFrame());
		
		JChessApp.getApplication().show(JChessApp.view.newGameFrame);
	}
	
	public void mouseEntered(MouseEvent event)
	{
	}
	
	public void mouseExited(MouseEvent event)
	{
	}
	
	@Override
	public void addTab(String title, Component component)
	{
		super.addTab(title, new CloseIcon(null), component);
		log.info("Present number of tabs: " + this.getTabCount());
		this.updateAddIconRectangle();
	}

	@Override
	public boolean imageUpdate(Image image, int infoflags, int x, int y, int width, int height)
	{
		super.imageUpdate(image, infoflags, x, y, width, height);
		this.updateAddIconRectangle();
		return true;
	}
	
	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);
		if(addIconRectangle != null)
			graphics.drawImage(this.addIcon, addIconRectangle.x, addIconRectangle.y, null);
	}
	
	@Override
	public void update(Graphics graphics)
	{
		this.repaint();
	}
	
	private class CloseIcon implements Icon
	{
		private int xPosition;
		private int yPosition;
		
		private Image iconImage = ThemeImageLoader.getInstance().loadThemeImage("close-tab-icon.png");
		private Icon fileIcon;
		
		public CloseIcon(Icon fileIcon)
		{
			this.fileIcon = new ImageIcon(iconImage);
		}
		
		@Override
		public void paintIcon(Component component, Graphics graphics, int x, int y)
		{
			this.xPosition = x;
			this.yPosition = y;
			
			if(fileIcon != null)
				fileIcon.paintIcon(component, graphics, x, y);
		}
		
		public int getIconWidth()
		{
			return fileIcon.getIconWidth();
		}
		
		public int getIconHeight()
		{
			return fileIcon.getIconHeight();
		}
		
		public Rectangle getBounds()
		{
			return new Rectangle(xPosition, yPosition, fileIcon.getIconWidth(), fileIcon.getIconHeight());
		}
	}
}