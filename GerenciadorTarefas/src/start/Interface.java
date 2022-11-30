package start;
//Fernando Butzke

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.CardLayout;
import javax.swing.JPanel;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Interface {

	private JFrame frame;
	private JTable table;
	private JTextField textField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface window = new Interface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 968, 604);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(10, 89, 932, 417);
		frame.getContentPane().add(layeredPane);
		layeredPane.setLayout(new CardLayout(0, 0));
		
		JPanel panelProcessos = new JPanel();
		layeredPane.add(panelProcessos, "name_1130472235970600");
		panelProcessos.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 932, 417);
		panelProcessos.add(scrollPane);
		
		table = new JTable();
		table.setColumnSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		DefaultTableModel dfltTableModel = new DefaultTableModel(
				new Object[][] {},
				new String[] {"PID", "Usu\u00E1rio", "Comando", "Linha"});
		
		getProcesses(dfltTableModel);
		
		table.setModel(dfltTableModel);
		
		JButton btnPanelProcessos = new JButton("Processos");
		btnPanelProcessos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				layeredPane.removeAll();
				layeredPane.add(panelProcessos);
				layeredPane.repaint();
				layeredPane.revalidate();	
			}
		});
		btnPanelProcessos.setBounds(10, 11, 219, 23);
		frame.getContentPane().add(btnPanelProcessos);
		
		JButton btnPanelFinalizarTarefa = new JButton("Finalizar tarefa");
		btnPanelFinalizarTarefa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				System.out.println(dfltTableModel.getValueAt(table.getSelectedRow(), 0)); 
				
				ProcessHandle.allProcesses().forEach(proc -> {
					if (dfltTableModel.getValueAt(table.getSelectedRow(), 0).equals(proc.pid())) {
						proc.destroy();
					}
				});
				
				dfltTableModel.removeRow(table.getSelectedRow());

				getProcesses(new DefaultTableModel(
						new Object[][] {},
						new String[] {"PID", "Usu\u00E1rio", "Comando", "Linha"})
				);
			}
		});
		btnPanelFinalizarTarefa.setBounds(723, 531, 219, 23);
		frame.getContentPane().add(btnPanelFinalizarTarefa);
		
		textField = new JTextField();
		textField.setBounds(430, 12, 192, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Filtrar processo");
		lblNewLabel.setBounds(351, 15, 86, 14);
		frame.getContentPane().add(lblNewLabel);
	}
	
	public void getProcesses(DefaultTableModel dfltTableModel) {
		ProcessHandle.allProcesses().forEach(proc -> {
			if (proc != null && proc.info().user().isPresent()) {
				dfltTableModel.addRow(new Object[] {proc.pid(), proc.info().user().orElse(""), proc.info().commandLine().orElse(""), proc.info().command().orElse("")});
			}

			System.out.println("PID: " + proc.pid() + " User: " + proc.info().user().orElse("") 
					+ " Comando: " + proc.info().commandLine().orElse("") + " Linha:" + proc.info().command().orElse(""));
		});
		
		table.setModel(dfltTableModel);
	}
}
