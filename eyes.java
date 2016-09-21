import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
public class eyes{
	/* Rest program, every twenty minutes puts up a screen wide window for twenty seconds' duration and 
	 * disables user input for twenty seconds.
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
		//responds to stop button, turns timer off
		boolean stop;
		public EyeRest() {
			super(new BorderLayout());
			
		//Create fullscreen window.
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
	    	//create buttons to start and stop the program
		JButton onButton = new JButton("On");
		JButton offButton =new JButton("Off");
		//add action listener to on button
			onButton.addActionListener(new ActionListener(){
				ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			        @Override
			        public void actionPerformed( ActionEvent e ) {
			        	//if on button is pressed, disable addtional presses
			        	//enable off button
			        	onButton.setEnabled(false);
				        offButton.setEnabled(true);
			            // add Action
			        	Runnable blackenRunnable = new Runnable() {
						    public void run() {
						    	//make a new screen wide frame that is black
						    	//set timer to twenty seconds 
						    	Timer timer = new Timer(20000, new ActionListener() {
						    	    public void actionPerformed(ActionEvent a) {
						    	        black.setVisible(false);
						    	    }
						    	});
						    	if(stop){
						    		//stop timer and make the executor service wait
						    		timer.stop();
						    		stop=false;
						    		try {
										executor.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
						    	}
						    	else
						    	{	//timer should only run only per scheduled service
							    	timer.setRepeats(false);
							    	timer.start();
							    	black.setVisible(true);
							    	black.setAlwaysOnTop(true);
						    	}
						    }
						};
						//calls runnable every twenty minutes
						if(!stop){
							executor.scheduleAtFixedRate(blackenRunnable, 0, 1200, TimeUnit.SECONDS);
						}
			        }
			    });
			    //add action listener to off button
			offButton.addActionListener(new ActionListener(){
			        public void actionPerformed( ActionEvent e ) {
			        	//if off button is clicked, disable further clicks and 
			        	//enable off button
			        	onButton.setEnabled(true);
				        offButton.setEnabled(false);
				        stop=true;
				        //get rid of black window 
			        	black.dispose();
			        }
			    });
			//Group the buttons.
			ButtonGroup group = new ButtonGroup();
			group.add(onButton);
			group.add(offButton);
			
			//Put the buttons in a column in a panel.
			JPanel panel = new JPanel(new GridLayout(10, 1));
			panel.add(onButton);
			panel.add(offButton);
			
			add(panel, BorderLayout.LINE_START);
			setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		}
	
		/**
		* Create the GUI and show it.
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
		frame.setSize(d);
		//set in middle of screen
		frame.setLocation(500, 200);
		//no resizing, only two buttons, don't need much space
		frame.setResizable(false);
		//size the frame
		frame.pack();
		//Display the window.
		frame.setVisible(true);
		}
	}
}
