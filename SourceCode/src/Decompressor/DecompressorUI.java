package Decompressor;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MergerCompressor.MRGCompressor;

/**
 * @author Kayhan Dehghani Mohammadi 
 * CMPT 365 Spring 2017
 * Assignment 2 Image Compressor
 **/

public class DecompressorUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// UI Variables
	private JFrame mainFrame;
	private JPanel editButtonsPanel, txtPanel;
	private JFileChooser fc;
	private static String imageOneFilePath, imageTwoFilePath;
	JMenuBar menuBar;
	JMenu menu, fileMenu;
	JMenuItem selectItem, exitItem;
	JLabel label;

	// Image Variables
	private BufferedImage originalImgOne, originalImgTwo;

	public static void main(String[] args) {
		new DecompressorUI();
	}

	public DecompressorUI() {
		// Frame
		mainFrame = new JFrame("CMPT365 Image Decompressor");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setSize(600, 400);
		mainFrame.setLocationRelativeTo(null);

		// Panels
		editButtonsPanel = new JPanel(new GridLayout(4, 3));
		txtPanel = new JPanel(new GridLayout(4, 3));
		mainFrame.add(editButtonsPanel, BorderLayout.EAST);
		mainFrame.add(txtPanel, BorderLayout.WEST);

		// Menu
		menuBar = new JMenuBar();
		menu = new JMenu("Window");
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		menuBar.add(menu);
		selectItem = new JMenuItem("Open File...");
		selectItem.addActionListener(this);
		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		fileMenu.add(selectItem);
		menu.add(exitItem);
		mainFrame.setJMenuBar(menuBar);

		// TextArea
		label = new JLabel("<html><br>Welcome!</html>");
		txtPanel.add(label);

		editButtonsPanel.setVisible(true);
		txtPanel.setVisible(true);
		mainFrame.setVisible(true);
	}

	private void selectFile() {
		JDialog dialog = new JDialog(mainFrame, "Select Image", ModalityType.APPLICATION_MODAL);
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fc.setMultiSelectionEnabled(true);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setVisible(true);
		int result = fc.showOpenDialog(dialog);

		if (result == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			imageOneFilePath = files[0].getAbsolutePath();
			// show image attributes
			String fileOneName = Paths.get(imageOneFilePath).getFileName().toString();
			if(!fileOneName.equals("compressed.mrg")){

				label.setText("<html>Please select the correct file. This program can only open compressed.mrg file! <br>");
			}
			else {
				label.setText("Loading...");
				List<double[][]> pixels = MRGDecompressor.decompressFile();
				label.setText("<html><br>First Image : compressed.mrg<br>" + "Dimensions (Width x Height): "
						+ pixels.get(0).length + " x " + pixels.get(0)[0].length + "<br>");
				BufferedImage mrgImage = MRGDecompressor.getImageFromYuv(pixels, 0);
				viewImage(mrgImage, "Decompressed Images");
			}
		}
	}

	private void viewImage(BufferedImage image, String header) {
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
		label.setAutoscrolls(true);
		JOptionPane.showMessageDialog(null, label, header, JOptionPane.PLAIN_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitItem) {
			System.exit(0);
		} else if (e.getSource() == selectItem) {
			selectFile();
		}
	}
}
