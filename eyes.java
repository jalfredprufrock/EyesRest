import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
public class eyes{
	/* Rest program, every twenty minutes puts up a screen wide dialog box or an image, and 
	 * disables user input for twenty seconds, should probably be able to change frequency and duration 
	 * of screen outs
	 * NEXT: 
	 * 1.  get black panel to fade in !!!!
	 * 2. style it the main frame:  colors, perhaps make it not so shitty looking, i.e. make own graphics, alter existing ones so it looks slick
	 * 3. put it in icon tray for minimization,  so I would have to create icon graphic, 
	 * 4. figure out how to get it onto other computers?
	 * 5. include longer breaks?
	 */
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EyeRest.createAndShowGUI();
			}
		});
	}
	public static class EyeRest extends JPanel{
		boolean stop;
		public EyeRest() {
			super(new BorderLayout());
			
			//Create the radio buttons.
			JFrame  black= new JFrame();
			JLabel label = new JLabel("Stare at something 20 meters away");
	        label.setPreferredSize(new Dimension(500, 500));
	        label.setForeground(Color.blue);
	        label.setFont(new Font("Serif", Font.BOLD, 28));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        black.getContentPane().add(label, BorderLayout.CENTER);
	    	black.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    	black.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    	black.setUndecorated(true);
	    	black.getContentPane().setBackground(Color.gray);
	    	
			JButton onButton = new JButton("On");
			JButton offButton =new JButton("Off");
			onButton.addActionListener(new ActionListener(){
				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			        @Override
			        public void actionPerformed( ActionEvent e ) {
			        	onButton.setEnabled(false);
				        offButton.setEnabled(true);
			            // add Action
			        	//System.out.println("ON");
			        	Runnable blackenRunnable = new Runnable() {
						    public void run() {
						    	//make a new screen wide frame that is black or dark grey
						    	//milliseconds
						    	Timer timer = new Timer(20000, new ActionListener() {
						    	    public void actionPerformed(ActionEvent a) {
						    	        black.setVisible(false);
						    	    }
						    	});
						    	if(stop){
						    		timer.stop();
						    		stop=false;
						    		try {
										executor.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
						    	}
						    	else
						    	{
							    	timer.setRepeats(false);
							    	timer.start();
							    	//could do set Visible but i'd prefer to actually destroy it
							    	//black.setExtendedState(JFrame.MAXIMIZED_BOTH); 
							    	black.setVisible(true);
							    	black.setAlwaysOnTop(true);
							    	//black.setBackground(new Color(1,1,1,115));
						    	}
						    }
						};
						//calls something every number of seconds
						//condition didn't seem to do anything
						if(!stop){
							executor.scheduleAtFixedRate(blackenRunnable, 0, 1200, TimeUnit.SECONDS);
						}
			        }
			    });
			offButton.addActionListener(new ActionListener(){
			        public void actionPerformed( ActionEvent e ) {
			        	onButton.setEnabled(true);
				        offButton.setEnabled(false);
				        stop=true;
			        	black.dispose();
			        }
			    });
			//Group the radio buttons.
			ButtonGroup group = new ButtonGroup();
			group.add(onButton);
			group.add(offButton);
			
			//Put the radio buttons in a column in a panel.
			JPanel radioPanel = new JPanel(new GridLayout(10, 1));
			radioPanel.add(onButton);
			radioPanel.add(offButton);
			
			add(radioPanel, BorderLayout.LINE_START);
			//add(picture, BorderLayout.CENTER);
			setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		}
	
		/**
		* Create the GUI and show it.  For thread safety,
		* this method should be invoked from the
		* event-dispatching thread.
		*/
		private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("EyeRest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create and set up the content pane.
		JComponent newContentPane = new EyeRest();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);
		Dimension d = new Dimension(300, 90);
		//IS THIS THE CORRECT WAY TO SET SIZE UPON OPENING??
		frame.setMinimumSize(d);
		frame.setLocation(500, 200);
		//size the frame
		frame.pack();
		//Display the window.
		frame.setVisible(true);
		}
	}
}