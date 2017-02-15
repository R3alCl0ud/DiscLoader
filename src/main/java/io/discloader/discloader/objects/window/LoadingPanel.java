package io.discloader.discloader.objects.window;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Perry Berman
 *
 */
public class LoadingPanel extends JPanel {

	
	private static final long serialVersionUID = 704573790954978688L;
	public static JProgressBar phase;
	public static JLabel phaseLabel;
	public static JProgressBar step;
	public static JLabel stepLabel;
	public static JProgressBar progress;
	public static JLabel progressLabel;
	
	public LoadingPanel() {
		super(new GridLayout(0, 1));
		this.add(phase = new JProgressBar());
		this.add(step = new JProgressBar());
		this.add(progress = new JProgressBar());
		phase.add(phaseLabel = new JLabel("0: starting")); 
		phase.setMinimum(0);
		phase.setMaximum(3);
		phase.setValue(1);
		phase.setSize(200, 10);
		phase.setString("TEST");
		step.setSize(200, 10);
		progress.setSize(200, 10);
		this.revalidate();
		this.repaint();
	}
	
	public static void setPhase(int phaseNum, String text) {
		
	}

	public static void setProgess(int progressNum, String text) {
		
	}
	
	public static void setStep(int stepNum, String text) {
		
	}
	
}
