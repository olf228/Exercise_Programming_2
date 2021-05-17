import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

@SuppressWarnings("serial")
class Raten extends JFrame implements ActionListener {
		
	private static final String DIE_IST_ES = "Die ist es!";
	private static final String ZU_KLEIN = "Zu klein";
	private static final String ZU_GROSS = "Zu gross";
	
	private JLabel tippLabel = new JLabel();
	private int lower = 0;
	private int upper = 1001;
	private int tipp;
	private int tippNumber = 1;
	
	
	private int getTipp() {
		tippNumber++;
		if (lower == 999)
			tipp = 1000;
		else
			tipp = (lower + upper) / 2;
		return tipp;
	}
	
	Raten() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("Zahlenraten");
		JLabel explain = new JLabel("<html>Sie denken sich eine Zahl " +
				"zwischen 1 und 1000 aus.<p>" +
				"Ich werde sie erraten.<p>" +
				"Geben Sie mir nur Tipps, ob ich zu " +
				"niedrig oder zu hoch geraten habe.<hr></html>");
		add(explain, BorderLayout.NORTH);
		tippLabel.setText("Mein " + tippNumber + ". Tipp: " + getTipp());
		add(tippLabel);
		JPanel buttons = new JPanel();
		JButton zuGross = new JButton(ZU_GROSS);
		zuGross.addActionListener(this);
		buttons.add(zuGross);
		JButton zuKlein = new JButton(ZU_KLEIN);
		zuKlein.addActionListener(this);
		buttons.add(zuKlein);
		JButton gefunden = new JButton(DIE_IST_ES);
		gefunden.addActionListener(this);
		buttons.add(gefunden);
		add(buttons, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(ZU_GROSS)) {
			upper = tipp;
		}
		else if (cmd.equals(ZU_KLEIN)) {
			lower = tipp;
		}
		else if (cmd.equals(DIE_IST_ES)) {
			tippLabel.setText("Na endlich!");
			return;
		}
		if (lower >= upper - 1) {
			tippLabel.setText("Da ist was faul!");
			return;
		}
		tippLabel.setText("Mein " + tippNumber + ". Tipp: " + getTipp());
	}

	public static void main(String[] args) {
		new Raten();
	}


}
