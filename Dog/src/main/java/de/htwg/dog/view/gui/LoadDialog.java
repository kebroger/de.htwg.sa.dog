package de.htwg.dog.view.gui;

import de.htwg.dog.controller.IController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	private String[] columnNames = { "Name" };
	private IController controller;
	private String[][] rowData;
	

	public LoadDialog(Frame aFrame, final IController controller) {
        //super(aFrame, true);

	    this.controller = controller;

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel tablePanel = new JPanel();
		table = new JTable();
		table.getColumnModel().setSelectionModel(
				new DefaultListSelectionModel() {
					private static final long serialVersionUID = 1L;

					@Override
					public int getLeadSelectionIndex() {
						return -1;
					}
				});

		tablePanel.setBorder(BorderFactory.createTitledBorder("Games"));

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(200,200));
		tablePanel.add(scroll);
		mainPanel.add(tablePanel, BorderLayout.PAGE_START);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,
				10));
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int rowIdx = LoadDialog.this.table.getSelectedRow();
				if (rowIdx == -1) {
					JOptionPane.showMessageDialog(LoadDialog.this,
							"Please choose an entry first.");
					return;
				}
				String id = (String) LoadDialog.this.rowData[rowIdx][1];

				controller.loadFromDB(id);
				LoadDialog.this.setVisible(false);
			}
		});
		buttonPanel.add(loadButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				LoadDialog.this.setVisible(false);
			}
		});
		buttonPanel.add(exitButton);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

		updateTable();

		this.setContentPane(mainPanel);
		this.setResizable(false);
		this.setTitle("Load a Game from Database");
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(aFrame);
	}

	protected void updateTable() {

		rowData = controller.getAllNames();
		table.setModel(new DefaultTableModel(rowData, columnNames) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
}
