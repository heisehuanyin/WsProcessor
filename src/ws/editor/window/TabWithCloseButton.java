package ws.editor.window;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import ws.editor.WsProcessor;
import ws.editor.p.contentview.ContentView;
import ws.editor.p.window.FrontWindow;

import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent; Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class TabWithCloseButton extends JPanel {
	private final ContentView view;
	private final FrontWindow win;

	public TabWithCloseButton(final FrontWindow win,final JTabbedPane pane, final ContentView view) {
		// unset default FlowLayout' gaps
		super(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		
		this.view = view;
		this.win = win;
		
		if (pane == null) {
			throw new NullPointerException("TabbedPane is null");
		}
		setOpaque(false);

		// make JLabel read titles from JTabbedPane
		JLabel label = new JLabel() {
			public String getText() {
				int i = pane.indexOfTabComponent(TabWithCloseButton.this);
				if (i != -1) {
					return pane.getTitleAt(i);
				}
				return null;
			}
		};
		label.setVerticalAlignment(SwingConstants.CENTER);

		add(label);
		// add more space between the label and the button
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		// tab button
		JButton button = new TabButton();
		add(button);
		// add more space to the top of the component
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private class TabButton extends JButton implements ActionListener {
		public TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			// Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			// Make it transparent
			setContentAreaFilled(false);
			// No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			// Making nice rollover effect
			// we use the same listener for all buttons
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			// Close the proper tab by clicking the button
			addActionListener(this);
		}

		// we don't want to update UI for this button
		public void updateUI() {
		}

		// paint the cross
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// shift the image for pressed buttons
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.MAGENTA);
			}
			
			int vint = 4;
			g2.drawLine(vint, vint, getWidth()-vint, getHeight()-vint);
			g2.drawLine(vint, getHeight()-vint, getWidth()-vint, vint);
			
			//int delta = 6;
			//g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			//g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
		
		public void actionPerformed(ActionEvent e) {
			win.closeView(view);
		}
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}